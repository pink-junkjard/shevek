(ns shevek.schema.manager-test
  (:require [clojure.test :refer [is]]
            [shevek.test-helper :refer [spec]]
            [shevek.makers :refer [make!]]
            [shevek.asserts :refer [submaps?]]
            [shevek.schema.manager :refer [discover! update-cubes]]
            [shevek.schema.metadata :refer [cubes dimensions-and-measures]]
            [shevek.schema.repository :refer [find-cubes]]
            [shevek.schemas.cube :refer [Cube]]
            [shevek.db :refer [db]]
            [com.rpl.specter :refer [select ALL]]))

; TODO Todos estos test se podrian simplificar si testeamos directamente el update-cubes. Dejar uno solo que pase x el discover y listo
(spec "initial descovery should save all cubes with their dimensions and metrics"
  (with-redefs [cubes (constantly ["wikiticker" "vtol_stats"])
                dimensions-and-measures
                (fn [_ cube]
                  (case cube
                    "wikiticker" [[{:name "region" :type "STRING"}]
                                  [{:name "added" :type "longSum"}]]
                    "vtol_stats" [[{:name "path" :type "LONG"}]
                                  [{:name "requests" :type "hyperUnique"}]]))]
    (let [cubes (discover! nil db)]
      (is (submaps? [{:name "wikiticker"} {:name "vtol_stats"}] cubes))
      (is (submaps? [{:name "region" :type "STRING"}
                     {:name "path" :type "LONG"}]
                    (mapcat :dimensions cubes)))
      (is (submaps? [{:name "added" :expression "(sum $added)"}
                     {:name "requests" :expression "(count-distinct $requests)"}]
                    (mapcat :measures cubes)))
      (is (= 2 (->> cubes (map :_id) (filter identity) count))))))

(spec "discovery of a new cube"
  (let [c1 (make! Cube {:name "c1"
                        :dimensions [{:name "d1" :type "STRING"}]
                        :measures [{:name "m1" :type "count"}]})]
    (with-redefs
      [cubes (constantly ["c1" "c2"])
       dimensions-and-measures (fn [_ cube]
                                 (case cube
                                   "c1" ((juxt :dimensions :measures) c1)
                                   "c2" [[{:name "d2" :type "STRING"}] [{:name "m2" :type "count"}]]))]
      (discover! nil db)
      (let [cubes (find-cubes db)]
        (is (= ["c1" "c2"] (map :name cubes)))
        (is (= (:_id c1) (:_id (first cubes))))
        (is (= ["d1" "d2"] (select [ALL :dimensions ALL :name] cubes)))
        (is (= ["m1" "m2"] (select [ALL :measures ALL :name] cubes)))))))

(spec "existing cube with a new dimension (d2), a deleted one (d1) and a changed measure type"
  (let [c1 (make! Cube {:name "c1" :title "C1"
                        :dimensions [{:name "d1" :type "STRING" :title "D1"}]
                        :measures [{:name "m1" :type "count" :title "M1"}]})]
    (with-redefs
      [cubes (constantly ["c1"])
       dimensions-and-measures (fn [_ cube]
                                 "c1" [[{:name "d2" :type "STRING"}]
                                       [{:name "m1" :type "longSum"}]])]
      (discover! nil db)
      (let [cubes (find-cubes db)]
        (is (= [["c1" "C1"]] (map (juxt :name :title) cubes)))
        (is (= (:_id c1) (:_id (first cubes))))
        (is (= [["d1" "D1"] ["d2" "D2"]] (map (juxt :name :title) (select [ALL :dimensions ALL] cubes))))
        (is (= [["m1" "M1" "longSum"]] (map (juxt :name :title :type) (select [ALL :measures ALL] cubes))))))))

; Seeds

(spec "updating cube title"
  (make! Cube {:name "stats"})
  (update-cubes db [{:name "stats" :title "Statistics"}])
  (is (= "Statistics" (-> (find-cubes db) first :title)))
  (update-cubes db [{:name "stats" :title "Statistics 2"}])
  (is (= "Statistics 2" (-> (find-cubes db) first :title))))

(spec "changing the default measure expression and format"
  (make! Cube {:name "sales" :measures [{:name "amount" :expression "(sum $amount)"}]})
  (update-cubes db [{:name "sales" :measures [{:name "amount" :expression "(/ (sum $amount) 100)" :format "$0.00"}]}])
  (is (submaps? [{:expression "(/ (sum $amount) 100)" :format "$0.00"}] (-> (find-cubes db) first :measures))))

(spec "adding new derived measure"
  (make! Cube {:name "sales" :measures []})
  (update-cubes db [{:name "sales" :measures [{:name "amount" :expression "(/ (sum $amount) 100)"}]}])
  (is (submaps? [{:name "amount" :expression "(/ (sum $amount) 100)"}] (-> (find-cubes db) first :measures))))
