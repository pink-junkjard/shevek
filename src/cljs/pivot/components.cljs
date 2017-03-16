(ns pivot.components
  (:require [reagent.core :as r :refer [dom-node create-class]]
            [pivot.i18n :refer [t]]
            [cuerdas.core :as str]))

(defn page-title [title subtitle icon-class]
  [:h1.ui.header
   [:i.icon {:class icon-class}]
   [:div.content title
    [:div.sub.header subtitle]]])

(defn- dropdown* [coll & [{:keys [placeholder selected class]}]]
  (let [select? (= class "selection")]
    [:div.ui.dropdown {:class class}
     (when-not select? [:div.text])
     [:input {:type "hidden" :value (or selected "")}]
     [:i.dropdown.icon]
     (when select? [:div.default.text placeholder])
     [:div.menu
      (for [[title val] coll]
        ^{:key val}
        [:div.item {:data-value val} title])]]))

(defn make-dropdown [{:keys [on-change class] :or {on-change identity}} content]
  (let [bind-events #(-> % dom-node js/$
                         (.dropdown #js {:onChange on-change}))]
    (create-class {:reagent-render content
                   :component-did-mount bind-events})))

(defn dropdown [coll opts]
  (make-dropdown opts dropdown*))

(defn select [_ opts]
  (make-dropdown opts (fn [coll opts] [dropdown* coll (assoc opts :class "selection")])))

(defn- checkbox [label & [{:keys [checked on-change name]
                           :or {on-change identity name (str/slug label)}}]]
  [:div.ui.checkbox
   [:input {:type "checkbox" :id name :checked (or checked false)
            :on-change #(on-change (not checked))}]
   [:label {:for name} label]])

(defn- popup* [activator popup-container _]
  [:div activator popup-container])

; TODO hacer solo el reposition si on = manual, asi para los demas casos sigue normal
(defn popup [_ _ opts]
  (create-class {:reagent-render popup*
                 :component-did-mount #(-> % dom-node js/$ (.find ".item")
                                           (.popup (clj->js (merge {:inline true} opts)))
                                           (.popup "reposition"))}))
