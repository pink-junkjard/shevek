(ns shevek.reports.repository
  (:require [shevek.lib.mongodb :as m]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [clj-time.core :as t]))

(defn- remove-report-from-non-selected-dashboards [db rid ds-ids]
  (mc/update db "dashboards"
             (cond-> {"panels.report-id" rid}
                     ds-ids (assoc :_id {$nin ds-ids}))
             {$pull {:panels {:report-id rid}}}
             {:multi true}))

(defn- add-report-to-selected-dashboards [db rid ds-ids]
  (when (seq ds-ids)
    (mc/update db "dashboards"
               {:_id {$in ds-ids} "panels.report-id" {$ne rid}}
               {$push {:panels {:report-id rid}}}
               {:multi true})))

; TODO DASHBOARD este hacia mas cosas antes, se encargaba de mantener la relacion inversa con los dashboards, revisar
(defn save-report [db report]
  (m/save db "reports" report))

(defn delete-report [db id]
  (remove-report-from-non-selected-dashboards db (m/oid id) [])
  (m/delete-by-id db "reports" id))

(defn find-reports [db user-id]
  (m/find-all db "reports" :where {:owner-id user-id} :sort {:name 1}))

(defn delete-reports [db user-id]
  (m/delete-by db "reports" {:owner-id user-id}))

(defn find-by-id [db id]
  (m/find-by-id db "reports" id))

(defn create-or-update-by-sharing-digest [db {:keys [sharing-digest] :as report}]
  {:pre [sharing-digest]}
  (m/create-or-update-by db "reports" {:sharing-digest sharing-digest} report))

(defn delete-old-shared-reports [db & [{:keys [older-than] :or {older-than (-> 30 t/days t/ago)}}]]
  (mc/remove db "reports" {:sharing-digest {$exists true}
                           :updated-at {$lt older-than}}))

#_(delete-old-shared-reports shevek.db/db {:older-than (t/now)})
