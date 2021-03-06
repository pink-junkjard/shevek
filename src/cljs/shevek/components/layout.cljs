(ns shevek.components.layout)

(defn page-with-header [{:keys [title subtitle icon id image]} & children]
  [:div {:id id}
   [:div.ui.inverted.basic.segment.page-title
    [:div.ui.container
     [:h1.ui.inverted.header
      (when icon [:i.icon {:class icon}])
      image
      [:div.content title
       [:div.sub.header subtitle]]]]]
   (into [:div.ui.container] children)
   [:div.page-footer]])

(defn page-loader []
  [:div.ui.active.large.loader])

(defn topbar [{:keys [left right]}]
  [:div.topbar
   [:div.left left]
   [:div.right right]])

(defn panel [{:keys [title actions id]} & content]
  (let [panel-content (into [:div.panel-content] content)]
    [:div.ui.segment.clearing.panel {:id id}
     [:div.ui.top.attached.label.panel-header
      title
      (when (seq actions)
        (into [:div.panel-actions] actions))]
     panel-content]))
