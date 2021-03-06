(ns shevek.querying.expansion
  (:require [shevek.domain.dimension :refer [find-dimension time-dimension?]]
            [shevek.lib.period :refer [effective-interval]]
            [shevek.lib.time :as t]
            [com.rpl.specter :refer [transform must filterer ALL]]))

(defn- relative-to-absolute-time [max-time {:keys [time-zone] :as q}]
  (let [calculate-interval #(t/with-time-zone time-zone
                              (effective-interval % max-time))]
    (transform [:filters ALL time-dimension?]
               #(-> (assoc % :interval (calculate-interval %))
                    (dissoc :period))
               q)))

(def row-count {:name "rowCount" :type "rowCount"})

(defn- expand-dim [dim dimensions]
  (let [dim (if (string? dim) {:name dim} dim)]
    (-> (find-dimension (:name dim) dimensions)
        (merge dim))))

(defn- interval-intersection [[from1 to1] [from2 to2]]
  (let [from (t/latest from1 from2)
        to (t/earliest to1 to2)]
    (if (t/before-or-equal? from to)
      [from to]
      [from (t/plus from (t/millis 1))])))

(defn- merge-intervals [time-filters]
  (let [intervals (map :interval time-filters)
        merged-interval (reduce interval-intersection (first intervals) intervals)]
    [(assoc (first time-filters) :interval merged-interval)]))

(defn expand-query
  "Take a query and the corresponding cube schema and expands it with some schema information necessary to execute the query"
  [q {:keys [measures dimensions default-time-zone max-time]}]
  (let [measures (map #(assoc % :measure? true) measures)]
    (->> q
         (transform [(must :measures) ALL] #(expand-dim % (conj measures row-count)))
         (transform [:filters ALL] #(expand-dim % dimensions))
         (transform [(must :splits) ALL] #(expand-dim % dimensions))
         (transform [(must :splits) ALL (must :sort-by)] #(expand-dim % (concat dimensions measures)))
         (merge {:time-zone (or default-time-zone (t/system-time-zone))})
         (relative-to-absolute-time max-time)
         (transform [:filters (filterer time-dimension?)] merge-intervals)
         (transform [:filters ALL time-dimension? :interval ALL] t/to-iso8601))))
