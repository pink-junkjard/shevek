(ns shevek.lib.local-storage
  (:require [cljs.reader :refer [read-string]]))

(defn set-item! [key val]
  "For storing simple values."
  (.setItem js/localStorage key val))

(defn get-item [key]
  (.getItem js/localStorage key))

(defn remove-item! [key]
  (.removeItem js/localStorage key))

(defn store!
  "For storing maps or other compound values."
  [key value]
  (->> value pr-str (set-item! key)))

(defn retrieve [key]
  (-> key get-item str read-string))
