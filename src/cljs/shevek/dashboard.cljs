(ns shevek.dashboard
  (:require-macros [shevek.reflow.macros :refer [defevh]])
  (:require [shevek.reflow.core :refer [dispatch]]
            [shevek.reflow.db :as db]
            [shevek.i18n :refer [t]]
            [shevek.rpc :as rpc]
            [shevek.components.text :refer [page-title loader]]
            [shevek.lib.dw.cubes :refer [fetch-cubes cubes-list set-cube-defaults]]
            [shevek.menu.reports :refer [fetch-reports]]
            [shevek.schemas.conversion :refer [report->viewer viewer->query]]
            [shevek.lib.react :refer [rmap]]
            [shevek.viewer.visualization :refer [visualization warning]]
            [shevek.viewer.shared :refer [send-visualization-query cube-authorized?]]))

(defn- cube-card [{:keys [name title description] :or {description (t :cubes/no-desc)}}]
  [:a.cube.card {:on-click #(dispatch :cube-selected name)}
   [:div.content
    [:div.header [:i.cube.icon] title]
    [:div.meta description]]])

(defn- cubes-cards []
  (let [cubes (cubes-list)]
    (if (seq cubes)
      [:div.ui.cards (rmap cube-card :name cubes)]
      [:div.tip [:i.info.circle.icon] (t :cubes/missing)])))

(defn dashboard-reports []
  (filter :pin-in-dashboard (db/get :reports)))

(defevh :dashboard/cube-arrived [db cube {:keys [name] :as report}]
  (let [viewer (report->viewer report (set-cube-defaults cube))]
    (if (cube-authorized? viewer)
      (send-visualization-query db viewer [:dashboard name])
      (rpc/loaded db [:dashboard name]))))

(defevh :dashboard/cube-requested [db {:keys [name cube] :as report}]
  (rpc/call "schema.api/cube" :args [cube] :handler #(dispatch :dashboard/cube-arrived % report))
  (rpc/loading db [:dashboard name]))

(defn- report-card [{:keys [name description] :as report}]
  (dispatch :dashboard/cube-requested report)
  (fn []
    [:a.report.card {:on-click #(dispatch :report-selected report)}
     [:div.content
      [:div.header name]
      [:div.meta description]]
     [:div.content.visualization-container
      [loader [:dashboard name]]
      (let [vis (db/get-in [:dashboard name])]
        (if (cube-authorized? vis)
          [visualization vis]
          [warning (t :reports/unauthorized)]))]]))

(defn- reports-cards []
  (fetch-reports)
  (fn []
    (let [reports (dashboard-reports)]
      (if (seq reports)
        [:div.ui.two.stackable.cards (rmap report-card :name reports)]
        [:div.tip [:i.info.circle.icon] (t :reports/none)]))))

(defn page []
  (fetch-cubes)
  [:div#dashboard.ui.container
   [page-title (t :dashboard/title) (t :dashboard/subtitle) "block layout"]
   [:h2.ui.app.header (t :cubes/title)]
   [cubes-cards]
   [:h2.ui.app.header (t :reports/pinned)]
   [reports-cards]])

; TODO esto funca y queda simple pero hace demasiados requests al server. Ver de hacer solo las queries de los viewers. Aunque a favor tiene que no hace falta actualizar los max-time periodicamente.
; UPDATE: agregué despues el fetch-cubes asi se van refrescando si aparecen nuevos, si ahi traemos todo el cubo entero se podria solucionar capaz lo planteado antes
(defevh :dashboard/refresh [db]
  (fetch-cubes)
  (doseq [report (dashboard-reports)]
    (dispatch :dashboard/cube-requested report)))
