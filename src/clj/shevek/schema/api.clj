(ns shevek.schema.api
  (:require [shevek.schema.repository :as r]
            [shevek.db :refer [db]]
            [shevek.schema.auth :as auth]))

(defn cubes [{:keys [user]}]
  (->> (r/find-cubes db)
       (auth/filter-cubes user)
       (map #(dissoc % :created-at :updated-at))
       (sort-by :title)))

(defn save-cube [_ cube]
  (r/save-cube db cube))

(defn max-time [_ cube-name]
  (:max-time (r/find-cube db cube-name)))

;; Examples

#_(cubes {:user {:permissions {:allowed-cubes "all"}}})
