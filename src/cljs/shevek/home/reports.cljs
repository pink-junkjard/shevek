(ns shevek.home.reports
  (:require [reagent.core :as r]
            [shevek.reflow.core :refer [dispatch]]
            [shevek.reflow.db :as db]
            [shevek.i18n :refer [t]]
            [shevek.lib.react :refer [rmap]]
            [shevek.lib.util :refer [trigger]]
            [shevek.lib.string :refer [present?]]
            [shevek.menu.reports :refer [fetch-reports save-report-form report-actions]]
            [shevek.components.form :refer [search-input filter-matching by]]
            [shevek.lib.dates :refer [format-time]]))

(defn- report-card []
  (let [form-data (r/atom nil)]
    (fn [{:keys [name description updated-at cube] :as report}]
      (if @form-data
        [:div.ui.fluid.card
         [:div.content
          [save-report-form form-data]]]
        [:a.ui.fluid.report.card {:on-click #(dispatch :report-selected report)}
         [:div.content
          [:div.right.floated [report-actions report form-data]]
          [:div.header [:i.line.chart.icon] name]
          (when (present? description)
            [:div.description description])]
         [:div.extra.content
          [:i.cube.icon]
          (db/get-in [:cubes cube :title])
          [:span.right.floated (format-time updated-at :day)]]]))))

(defn reports-cards []
  (fetch-reports)
  (let [search (r/atom "")]
    (fn []
      (let [reports (db/get :reports)]
        [:div.column
         [:h2.ui.app.header (t :reports/title)]
         [search-input search {:on-enter #(trigger "click" ".report.card") :input {:auto-focus false}}]
         (if reports
           (let [reports (filter-matching @search (by :name :description) reports)]
             (if (seq reports)
               (rmap report-card (comp hash (juxt :name :created-at)) reports)
               [:div.large.tip (if (seq @search) (t :errors/no-results) (t :reports/missing))]))
           [:div.ui.active.inline.centered.loader])]))))