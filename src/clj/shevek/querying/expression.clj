(ns shevek.querying.expression
  (:require [clojure.core.match :refer [match]]
            [clojure.string :refer [starts-with?]]
            [shevek.lib.collections :refer [includes?]]))

(def aggregator-types {'count "count" 'sum "doubleSum" 'count-distinct "hyperUnique" 'max "doubleMax"})

(defn- field-ref->field [field-ref]
  (-> field-ref str (subs 1)))

(defn- build-aggregator [name agg-fn & [field-ref]]
  (cond-> {:type (aggregator-types agg-fn) :name name}
          field-ref (assoc :fieldName (field-ref->field field-ref))))

(defn- condition->filter [condition]
  (if (map? condition)
    (if (> (count condition) 1)
      {:type "and" :fields (map #(condition->filter (into {} [%])) condition)}
      (condition->filter (-> condition seq flatten (conj '=))))

    (match condition
      ([(op :guard #{'and 'or}) & args] :seq)
      {:type (str op) :fields (map condition->filter args)}

      (['= field-ref val] :seq)
      {:type "selector" :dimension (field-ref->field field-ref) :value val}

      (['not= field-ref val] :seq)
      {:type "not" :field {:type "selector" :dimension (field-ref->field field-ref) :value val}})))

(defn- build-filtered-aggregator [field condition aggregator]
  {:type "filtered"
   :filter (condition->filter condition)
   :aggregator aggregator
   :name field})

(defn- simple-aggregation? [f]
  (includes? (keys aggregator-types) f))

(defn- aggregation? [f]
  (includes? (conj (keys aggregator-types) 'where) f))

(defn- arithmetic-operator? [op]
  (includes? ['/ '* '+ '- 'quotient] op))

(defn- eval-aggregator [name expression]
  (match expression
    ([(agg-fn :guard simple-aggregation?) & args] :seq) (apply build-aggregator name agg-fn args)
    (['where condition subexp] :seq) (build-filtered-aggregator name condition (eval-aggregator name subexp))))

(declare eval-post-aggregator)

(defn- build-constant-postagg [value]
  [[] {:type "constant" :value value}])

(defn- build-field-access-postagg [expression tig]
  (let [temp-name (str "_t" (tig))
        aggregators [(eval-aggregator temp-name expression)]
        post-aggregator {:type "fieldAccess" :fieldName temp-name}]
    [aggregators post-aggregator]))

(defn- build-arithmetic-postagg [name operator args tig]
  (let [aggregators-and-post-aggs (map #(eval-post-aggregator nil % tig) args)
        aggregators (mapcat first aggregators-and-post-aggs)
        fields (map second aggregators-and-post-aggs)
        post-aggregator (cond-> {:type "arithmetic" :fn (str operator) :fields fields}
                                name (assoc :name name))]
    [aggregators post-aggregator]))

(defn eval-post-aggregator [name expression tig]
  (match expression
    ([(op :guard arithmetic-operator?) & args] :seq) (build-arithmetic-postagg name op args tig)
    (value :guard number?) (build-constant-postagg value)
    ([(_ :guard aggregation?) & _] :seq) (build-field-access-postagg expression tig)))

(defn eval-expression
  "First we need to discriminate between and expression that should return only an aggregator and one that should return a post-aggregator besides its generated aggregators"
  [name expression tig]
  (match expression
    ([(_ :guard aggregation?) & _] :seq) [[(eval-aggregator name expression)] nil]
    ([(_ :guard arithmetic-operator?) & _] :seq) (eval-post-aggregator name expression tig)))

(defn measure->druid [{:keys [name expression]} tig]
  {:pre [(string? expression)]}
  (let [[aggregators post-aggregator] (eval-expression name (read-string expression) tig)]
    {:aggregations aggregators :postAggregations (remove nil? [post-aggregator])}))
