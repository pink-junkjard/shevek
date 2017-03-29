(ns shevek.cube-view.pinboard
  (:require-macros [reflow.macros :refer [defevh]]
                   [shevek.lib.reagent :refer [rfor]])
  (:require [reagent.core :as r]
            [reflow.core :refer [dispatch]]
            [shevek.lib.util :refer [debounce regex-escape]]
            [shevek.i18n :refer [t]]
            [shevek.rpc :refer [loading-class]]
            [shevek.dw :refer [find-dimension time-dimension? add-dimension remove-dimension replace-dimension]]
            [shevek.components :refer [dropdown]]
            [shevek.cube-view.shared :refer [current-cube panel-header cube-view send-query format-measure format-dimension filter-matching search-button search-input highlight debounce-dispatch]]))

(defn- send-pinned-dim-query [{:keys [cube-view] :as db} {:keys [name operator] :as dim}]
  (let [q (cond-> (assoc cube-view
                         :split [(assoc dim :limit 100)]
                         :measures (vector (get-in cube-view [:pinboard :measure])))
                  operator (update :filter add-dimension dim))]
    (send-query db q [:results :pinboard name])))

(defn send-pinboard-queries [db]
  (reduce #(send-pinned-dim-query %1 %2) db (cube-view :pinboard :dimensions)))

(defn init-pinned-dim [dim]
  (if (time-dimension? dim)
    (assoc dim :granularity "PT6H" :descending true) ; Default granularity
    dim))

(defevh :dimension-pinned [db dim]
  (let [dim (init-pinned-dim dim)]
    (-> (update-in db [:cube-view :pinboard :dimensions] add-dimension dim)
        (send-pinned-dim-query dim))))

(defevh :dimension-unpinned [db dim]
  (update-in db [:cube-view :pinboard :dimensions] remove-dimension dim))

; TODO al cambiar la measure se muestra temporalmente un cero en todas las filas. Ver si se puede evitar.
(defevh :pinboard-measure-selected [db measure-name]
  (-> (assoc-in db [:cube-view :pinboard :measure]
                (find-dimension measure-name (current-cube :measures)))
      (send-pinboard-queries)))

(defevh :pinned-time-granularity-changed [db dim granularity]
  (let [new-time-dim (assoc dim :granularity granularity)]
    (-> (update-in db [:cube-view :pinboard :dimensions] replace-dimension new-time-dim)
        (send-pinned-dim-query new-time-dim))))

(defevh :dimension-values-searched [db dim search]
  (send-pinned-dim-query db (assoc dim :operator "search" :value search)))

(defn- pinned-dimension-item [dim result measure search]
  (let [segment-value (format-dimension dim result)]
    [:div.item {:title segment-value}
     (highlight segment-value search)
     [:div.measure-value (format-measure measure result)]]))

(def periods {"PT1H" "1H"
              "PT6H" "6H"
              "PT12H" "12H"
              "P1D" "1D"
              "P1M" "1M"})

(defn- title-according-to-dim-type [{:keys [granularity] :as dim}]
  (when (time-dimension? dim)
    (str "(" (periods granularity) ")")))

(defn- time-granularity-button [dim]
  [dropdown (map (juxt second first) periods)
   {:class "top right pointing" :on-change #(dispatch :pinned-time-granularity-changed dim %)}
   [:i.ellipsis.horizontal.link.icon]])

(defn- pinned-dimension-panel* [{:keys [title name] :as dim}]
  (let [searching (r/atom false)
        search (r/atom "")]
    (fn []
      (let [measure (cube-view :pinboard :measure)
            results (cube-view :results :pinboard name)
            filtered-results (filter-matching @search (partial format-dimension dim) results)]
        [:div.dimension.panel.ui.basic.segment (when-not @searching (loading-class [:results :pinboard name]))
         [panel-header (str title " " (title-according-to-dim-type dim))
          (if (time-dimension? dim)
            [time-granularity-button dim]
            [search-button searching])
          [:i.close.link.link.icon {:on-click #(dispatch :dimension-unpinned dim)}]]
         (when @searching
           [search-input search {:on-change #(debounce-dispatch :dimension-values-searched dim %)
                                 :on-stop #(reset! searching false)}])
         (if results
           [:div.items
            (if (seq filtered-results)
              (rfor [result filtered-results]
                [pinned-dimension-item dim result measure @search])
              [:div.item.no-results (t :cubes/no-results)])]
           [:div.items.empty])]))))

(defn- adjust-max-height [rc]
  (let [panel (-> rc r/dom-node js/$)
        items (-> panel (.find ".header, .item, .search.input") .toArray js->clj)
        height (reduce + (map #(-> % js/$ (.outerHeight true)) items))]
    (.css panel "max-height", (max (+ height 10) 100))))

(def pinned-dimension-panel
  (with-meta pinned-dimension-panel*
    {:component-did-mount adjust-max-height
     :component-did-update adjust-max-height}))

(defn pinboard-panels []
  [:div.pinboard
   [:div.panel
    [panel-header (t :cubes/pinboard)
     [dropdown (map (juxt :title :name) (current-cube :measures))
      {:selected (cube-view :pinboard :measure :name) :class "top right pointing"
       :on-change #(dispatch :pinboard-measure-selected %)}]]]
   (if (seq (cube-view :pinboard :dimensions))
     (for [dim (cube-view :pinboard :dimensions)]
       ^{:key (dim :name)} [pinned-dimension-panel dim])
     [:div.panel.ui.basic.segment.no-pinned
      [:div.icon-hint
       [:i.pin.icon]
       [:div.text (t :cubes/no-pinned)]]])])