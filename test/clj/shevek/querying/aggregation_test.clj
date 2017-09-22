(ns shevek.querying.aggregation-test
  (:require [clojure.test :refer :all]
            [shevek.asserts :refer [submaps?]]
            [shevek.querying.aggregation :as agg]
            [shevek.lib.druid-driver :as druid]))

(defn query [q]
  (agg/query :dw q))

(deftest query-test
  (testing "totals and one row split"
    (let [queries-sent (atom [])]
      (with-redefs [druid/send-query (fn [_ dq] (swap! queries-sent conj dq) [])]
        (query {:cube "wikiticker"
                :splits [{:name "page"}]
                :measures [{:name "count" :expression "(sum $count)"}]
                :filters [{:interval ["2015" "2016"]}]
                :totals true})
        (is (submaps? [{:queryType "timeseries" :granularity "all"}
                       {:queryType "topN" :dimension "page"}]
                      @queries-sent)))))

  (testing "two row splits"
    (let [queries-sent (atom [])]
      (with-redefs
        [druid/send-query
         (fn [_ dq]
           (swap! queries-sent conj dq)
           (cond
             (= "country" (dq :dimension))
             [{:result [{:country "Argentina" :count 1} {:country "Brasil" :count 2}]}]
             (= "Argentina" (get-in dq [:filter :value]))
             [{:result [{:city "Cordoba" :count 3} {:city "Rafaela" :count 4}]}]
             (= "Brasil" (get-in dq [:filter :value]))
             [{:result [{:city "Rio de Janerio" :count 5}]}]))]
        (is (submaps? [{:country "Argentina" :count 1 :child-rows [{:city "Cordoba" :count 3}
                                                                   {:city "Rafaela" :count 4}]}
                       {:country "Brasil" :count 2 :child-rows [{:city "Rio de Janerio" :count 5}]}]
                      (query {:cube "wikiticker"
                              :splits [{:name "country"} {:name "city"}]
                              :measures [{:name "count" :expression "(sum $count)"}]
                              :filters [{:interval ["2015-09-12" "2015-09-13"]}]})))
        (is (submaps? [{:queryType "topN" :dimension "country"}
                       {:queryType "topN" :dimension "city"
                        :filter {:dimension "country" :type "selector" :value "Argentina"}}
                       {:queryType "topN" :dimension "city"
                        :filter {:dimension "country" :type "selector" :value "Brasil"}}]
                      (sort-by (juxt (comp not nil? :filter) (comp :value :filter)) @queries-sent))))))

  (testing "one time and one normal dimension on rows"
    (let [queries-sent (atom [])]
      (with-redefs
        [druid/send-query
         (fn [_ dq]
           (swap! queries-sent conj dq)
           (cond
             (= "timeseries" (dq :queryType))
             [{:result {:count 1} :timestamp "2015-09-01T00:00:00.000Z"}
              {:result {:count 2} :timestamp "2015-09-01T12:00:00.000Z"}]
             :else []))]
        (query {:cube "wikiticker"
                :splits [{:name "__time" :granularity "PT12H"} {:name "country"}]
                :measures [{:name "count" :expression "(sum $count)"}]
                :filters [{:interval ["2015-09-01" "2015-09-01"]}
                          {:name "country" :operator "is" :value "Argentina"}]})
        (is (submaps? [{:queryType "timeseries"}
                       {:queryType "topN" :dimension "country"
                        :intervals "2015-09-01T00:00:00.000Z/2015-09-01T12:00:00.000Z"
                        :filter {:dimension "country" :type "selector" :value "Argentina"}}
                       {:queryType "topN" :dimension "country"
                        :intervals "2015-09-01T12:00:00.000Z/2015-09-02T00:00:00.000Z"
                        :filter {:dimension "country" :type "selector" :value "Argentina"}}]
                      (sort-by :intervals @queries-sent))))))

  (testing "no row splits, one column split and one measure"
    (with-redefs
      [druid/send-query
       (fn [_ {:keys [queryType dimension filter]}]
         (cond
           (= queryType "timeseries")
           [{:result {:count 7}}]

           (and (= queryType "topN") (= dimension "dimA") (nil? filter))
           [{:result [{:dimA "A1" :count 3} {:dimA "A2" :count 4}]}]))]
      (is (= [{:count 7 :child-cols [{:dimA "A1" :count 3}
                                     {:dimA "A2" :count 4}]}]
             (query {:cube "wikiticker"
                     :splits [{:name "dimA" :on "columns"}]
                     :measures [{:name "count" :expression "(sum $count)"}]
                     :filters [{:interval ["2015" "2016"]}]
                     :totals true})))))

  (testing "one row split, one column split and one measure"
    (with-redefs
      [druid/send-query
       (fn [_ {:keys [queryType dimension filter] :as dq}]
         (cond
           (= queryType "timeseries")
           [{:result {:count 100}}]

           (and (= queryType "topN") (= dimension "dimB") (nil? filter))
           [{:result [{:dimB "B1" :count 35} {:dimB "B2" :count 65}]}]

           (and (= queryType "topN") (= dimension "dimA") (nil? filter))
           [{:result [{:dimA "A1" :count 60} {:dimA "A2" :count 40}]}]

           (and (= queryType "topN") (= dimension "dimB") (= (filter :value) "A1"))
           [{:result [{:dimB "B1" :count 20} {:dimB "B2" :count 40}]}]

           (and (= queryType "topN") (= dimension "dimB") (= (filter :value) "A2"))
           [{:result [{:dimB "B1" :count 15} {:dimB "B2" :count 25}]}]

           :else (throw (Exception. (str "Unexpected query " dq)))))]

      (is (= [{:count 100
               :child-cols [{:dimB "B1" :count 35} {:dimB "B2" :count 65}]}
              {:count 60 :dimA "A1"
               :child-cols [{:dimB "B1" :count 20} {:dimB "B2" :count 40}]}
              {:count 40 :dimA "A2"
               :child-cols [{:dimB "B1" :count 15} {:dimB "B2" :count 25}]}]
             (query {:cube "wikiticker"
                     :splits [{:name "dimA" :on "rows"} {:name "dimB" :on "columns"}]
                     :measures [{:name "count" :expression "(sum $count)"}]
                     :filters [{:interval ["2015" "2016"]}]
                     :totals true})))))

  (testing "two column splits and one measure"
    (with-redefs
      [druid/send-query
       (fn [_ {:keys [queryType dimension filter] :as dq}]
         (cond
           (= queryType "timeseries")
           [{:result {:count 100}}]

           (and (= queryType "topN") (= dimension "dimA") (nil? filter))
           [{:result [{:dimA "A1" :count 60} {:dimA "A2" :count 40}]}]

           (and (= queryType "topN") (= dimension "dimB") (= (filter :value) "A1"))
           [{:result [{:dimB "B1" :count 20} {:dimB "B2" :count 40}]}]

           (and (= queryType "topN") (= dimension "dimB") (= (filter :value) "A2"))
           [{:result [{:dimB "B1" :count 15} {:dimB "B2" :count 25}]}]

           :else (throw (Exception. (str "Unexpected query " dq)))))]

      (is (= [{:count 100
               :child-cols [{:dimA "A1" :count 60
                             :child-cols [{:dimB "B1" :count 20} {:dimB "B2" :count 40}]}
                            {:dimA "A2" :count 40
                             :child-cols [{:dimB "B1" :count 15} {:dimB "B2" :count 25}]}]}]
             (query {:cube "wikiticker"
                     :splits [{:name "dimA" :on "columns"} {:name "dimB" :on "columns"}]
                     :measures [{:name "count" :expression "(sum $count)"}]
                     :filters [{:interval ["2015" "2016"]}]
                     :totals true})))))

  (testing "two row splits, one column split and one measure"
    (with-redefs
      [druid/send-query
       (fn [_ {:keys [queryType dimension filter] :as dq}]
         (cond
           (= queryType "timeseries")
           [{:result {:count 100}}]

           (and (= queryType "topN") (= dimension "dimC") (nil? filter))
           [{:result [{:dimC "C1" :count 35} {:dimC "C2" :count 65}]}]

           (and (= queryType "topN") (= dimension "dimA") (nil? filter))
           [{:result [{:dimA "A1" :count 60} {:dimA "A2" :count 40}]}]

           (and (= queryType "topN") (= dimension "dimB") (= (filter :value) "A1"))
           [{:result [{:dimB "B1" :count 10} {:dimB "B2" :count 50}]}]

           (and (= queryType "topN") (= dimension "dimC") (= (filter :value) "A1"))
           [{:result [{:dimC "C1" :count 20} {:dimC "C2" :count 40}]}]

           (and (= queryType "topN") (= dimension "dimB") (= (filter :value) "A2"))
           [{:result []}]

           (and (= queryType "topN") (= dimension "dimC") (= (filter :value) "A2"))
           [{:result [{:dimC "C1" :count 15} {:dimC "C2" :count 25}]}]

           (and (= queryType "topN") (= dimension "dimC") (= ["A1" "B1"] (->> filter :fields (map :value))))
           [{:result [{:dimC "C1" :count 5} {:dimC "C2" :count 5}]}]

           (and (= queryType "topN") (= dimension "dimC") (= ["A1" "B2"] (->> filter :fields (map :value))))
           [{:result [{:dimC "C1" :count 15} {:dimC "C2" :count 35}]}]

           :else (throw (Exception. (str "Unexpected query " dq)))))]

      (is (= [{:count 100
               :child-cols [{:dimC "C1" :count 35} {:dimC "C2" :count 65}]}
              {:count 60 :dimA "A1"
               :child-cols [{:dimC "C1" :count 20} {:dimC "C2" :count 40}]
               :child-rows [{:dimB "B1" :count 10 :child-cols [{:dimC "C1" :count 5} {:dimC "C2" :count 5}]}
                            {:dimB "B2" :count 50 :child-cols [{:dimC "C1" :count 15} {:dimC "C2" :count 35}]}]}
              {:count 40 :dimA "A2"
               :child-cols [{:dimC "C1" :count 15} {:dimC "C2" :count 25}]}]
             (query {:cube "wikiticker"
                     :splits [{:name "dimA" :on "rows"} {:name "dimB" :on "rows"} {:name "dimC" :on "columns"}]
                     :measures [{:name "count" :expression "(sum $count)"}]
                     :filters [{:interval ["2015" "2016"]}]
                     :totals true}))))))
