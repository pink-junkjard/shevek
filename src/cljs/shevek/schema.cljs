(ns shevek.schema
  (:require [schema.core :as s :include-macros true]))

(def Settings
  {:lang s/Str})

(def Dimension
  {:name s/Str
   :title s/Str
   :type s/Str})

(def Measure Dimension)

(def Split
  (assoc Dimension
         :limit (s/cond-pre s/Int s/Str)
         (s/optional-key :sort-by) (assoc Measure :descending s/Bool)
         (s/optional-key :granularity) s/Str))

(def TimeFilter
  (assoc Dimension
         :max-time s/Any ; TODO no me convence el max-time aquí xq se persistiria con los filtros, usar el del cubo directamente
         :selected-period s/Keyword))

(def NormalFilter
  (assoc Dimension
         :operator s/Str
         (s/optional-key :value) #{s/Str}))

(def Filter
  (s/if :selected-period TimeFilter NormalFilter))

(def Result {s/Keyword s/Any})

(def CubeView
  {:cube s/Str
   (s/optional-key :filter) [Filter]
   (s/optional-key :split) [Split]
   (s/optional-key :split-arrived) [Split]
   (s/optional-key :measures) [Measure]
   (s/optional-key :pinboard) {:measure Measure :dimensions [Split]}
   (s/optional-key :results) {(s/enum :main :pinboard :filter) (s/cond-pre [Result] {s/Str [Result]})}
   (s/optional-key :last-added-filter) s/Any})

(def AppDB
  {(s/optional-key :page) s/Keyword
   (s/optional-key :loading) {(s/cond-pre s/Keyword [s/Any]) s/Bool}
   (s/optional-key :cubes) {s/Str {s/Keyword s/Any}} ; TODO falta describir mejor estos cubes
   (s/optional-key :settings) Settings
   (s/optional-key :cube-view) CubeView})

(defn check-schema [db]
  (try
    (s/validate AppDB db)
    (catch js/Error e
      (console.log e)
      db)))

(defn checker [interceptor]
  (fn [db event]
    (-> (interceptor db event)
        (check-schema))))
