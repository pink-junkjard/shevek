(ns shevek.querying.raw-test
  (:require [clojure.test :refer :all]
            [shevek.asserts :refer [submap?]]
            [shevek.querying.raw :refer [to-druid-query from-druid-results]]))

(deftest to-druid-query-test
  (testing "only time filter raw query"
    (is (submap? {:queryType "select"
                  :dataSource {:type "table" :name "wikiticker"}
                  :granularity {:type "all"}
                  :intervals "2015/2016"}
                 (to-druid-query {:cube "wikiticker"
                                  :filter [{:interval ["2015" "2016"]}]}))))

  (testing "other filters should work exactly the same as the normal query"
    (is (submap? {:filter {:dimension "isRobot" :type "selector" :value "true"}}
                 (to-druid-query {:filter [{:name "isRobot" :operator "is" :value "true"}]}))))

  (testing "should pass through the paging spec or use a default with limit 100"
    (is (submap? {:pagingSpec {:threshold 100 :fromNext true}}
                 (to-druid-query {})))
    (is (submap? {:pagingSpec {:threshold 50 :fromNext true}}
                 (to-druid-query {:paging {:threshold 50}})))
    (is (submap? {:pagingSpec {:threshold 50 :pagingIdentifiers {:a 4} :fromNext true}}
                 (to-druid-query {:paging {:threshold 50 :pagingIdentifiers {:a 4}}})))))

(deftest from-druid-results-test
  (testing "should return the new pagingIdentifiers along with the same threshold"
    (is (submap? {:paging {:threshold 3 :pagingIdentifiers {:... 4}}}
                 (from-druid-results {:paging {:threshold 3}}
                                     [{:result {:pagingIdentifiers {:... 4}}}])))
    (is (submap? {:paging {:threshold 3 :pagingIdentifiers {:... 5}}}
                 (from-druid-results {:paging {:threshold 3 :pagingIdentifiers {:... 4}}}
                                     [{:result {:pagingIdentifiers {:... 5}}}]))))

  (testing "should return the events with the timestamp as __time"
    (is (submap? {:results [{:region "Argentina" :added 1 :__time "2015"}
                            {:region "Brasil" :added 2 :__time "2016"}]}
                 (from-druid-results {}
                                     [{:result {:events [{:event {:region "Argentina" :added 1 :timestamp "2015"}}
                                                         {:event {:region "Brasil" :added 2 :timestamp "2016"}}]}}])))))