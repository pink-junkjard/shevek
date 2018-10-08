(ns shevek.engine.druid
  (:require [shevek.engine.protocol :refer [Engine]]
            [shevek.engine.druid.metadata :as metadata]
            [shevek.engine.druid.planner :as planner]
            [shevek.engine.druid.raw :as raw]
            [shevek.engine.druid.driver :as driver]))

(defrecord DruidEngine [driver]
  Engine

  (cubes [_]
    (driver/datasources driver))

  (time-boundary [_ cube]
    (metadata/time-boundary driver cube))

  (cube-metadata [_ cube]
    (metadata/cube-metadata driver cube))

  (designer-query [_ query cube]
    (planner/execute-query driver query cube))

  (raw-query [_ query cube]
    (raw/execute-query driver query cube))

  (custom-query [_ query]
    (driver/send-query driver {:query query})))

(defn druid-engine [driver]
  (DruidEngine. driver))
