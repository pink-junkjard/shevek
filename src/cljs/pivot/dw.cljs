(ns pivot.dw
  (:require-macros [reflow.macros :refer [defevh]])
  (:require [reflow.core :refer [dispatch]]
            [cuerdas.core :as str]
            [pivot.rpc :as rpc]
            [pivot.lib.collections :refer [reverse-merge detect]]
            [pivot.lib.dates :refer [parse-time now yesterday to-iso8601 beginning-of-day end-of-day beginning-of-month end-of-month round-to-next-second]]
            [pivot.lib.collections :refer [detect]]
            [cljs-time.core :as t]
            [reflow.db :as db]))

(defn- set-default-title [{:keys [name title] :or {title (str/title name)} :as record}]
  (assoc record :title title))

(defn set-cube-defaults [{:keys [dimensions measures time-boundary] :as cube}]
  (-> cube
      set-default-title
      (assoc :dimensions (map set-default-title dimensions))
      (assoc :measures (map set-default-title measures))
      (assoc-in [:time-boundary :max-time] (parse-time (:max-time time-boundary)))))

(defn- set-defaults [cubes]
  (map set-cube-defaults cubes))

(defn- to-map-with-name-as-key [cubes]
  (zipmap (map :name cubes) cubes))

; We need to update instead of assoc because if we reload the cube page the cube metadata could arrive before the cubes list.
; And it has to be a reverse-merge because the cube metadata contains more information that the cubes list.
(defevh :cubes-arrived [db cubes]
  (-> (update db :cubes reverse-merge (to-map-with-name-as-key (set-defaults cubes)))
      (rpc/loaded :cubes)))

(defevh :cubes-requested [db]
  (rpc/call "dw/cubes" :handler #(dispatch :cubes-arrived %))
  (rpc/loading db :cubes))

(defn fetch-cubes []
  (dispatch :cubes-requested))

(defn cubes-list []
  (vals (db/get :cubes)))

(defn time-dimension? [{:keys [name]}]
  (= name "__time"))

; TODO esto fallaría si no hay una dimension __time
(defn time-dimension [dimensions]
  (some #(when (time-dimension? %) %) dimensions))

(defn dim=? [dim1 dim2]
  (= (:name dim1) (:name dim2)))

; TODO algunos de estos metodos no corresponderian en el shared?
(defn find-dimension [name dimensions]
  (detect #(= (:name %) name) dimensions))

(defn- to-interval [{:keys [selected-period max-time]}]
  (let [now (now)
        max-time (round-to-next-second (or max-time now))]
    (condp = selected-period
      :latest-day [(t/minus max-time (t/days 1)) max-time]
      :latest-month [(t/minus max-time (t/months 1)) max-time]
      :latest-week [(t/minus max-time (t/weeks 1)) max-time]
      :current-day [(beginning-of-day now) (end-of-day now)]
      :current-month [(beginning-of-month now) (end-of-month now)])))

(defn- only-dw-query-keys [dim]
  (select-keys dim [:name :type :granularity :limit :descending]))

; Convierto manualmente los goog.dates en el intervalo a iso8601 strings porque sino explota transit xq no los reconoce. Alternativamente se podría hacer un handler de transit pero tendría que manejarme con dates en el server y por ahora usa los strings que devuelve Druid nomas.
(defn to-dw-query [{:keys [filter split measures] :as q}]
  (-> (select-keys q [:cube])
      (assoc :interval (mapv to-iso8601 (to-interval (time-dimension filter)))
             :filter (mapv only-dw-query-keys filter)
             :split (mapv only-dw-query-keys split)
             :measures (mapv only-dw-query-keys measures))))
