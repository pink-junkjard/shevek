(ns shevek.schema.api
  (:require [shevek.schema.repository :as r]
            [shevek.schema.metadata :as m]
            [shevek.db :refer [db]]
            [shevek.dw :refer [dw]]
            [shevek.schema.auth :as auth]))

; The measures are needed to configure user's permissions
(defn cubes [{:keys [user]}]
  (->> (r/find-cubes db)
       (auth/filter-cubes user)
       (map #(select-keys % [:_id :name :title :description :measures]))
       (sort-by :title)))

(defn max-time [_ cube-name]
  (:max-time (m/time-boundary dw cube-name)))

(defn cube [{:keys [user] :as req} name]
  (-> (r/find-cube db name)
      (auth/filter-cube user)
      (assoc :max-time (max-time req name))))

(defn save-cube [_ cube]
  (r/save-cube db cube))

;; Examples

#_(cubes {:user {:permissions {:allowed-cubes "all"}}})
#_(cube nil "vtol_stats")
