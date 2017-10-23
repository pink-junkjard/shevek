(ns shevek.lib.collections
  (:require [com.rpl.specter :refer [transform MAP-KEYS]]))

(defn reverse-merge [m1 m2]
  (merge m2 m1))

(defn detect [pred coll]
  (first (filter pred coll)))

(defn find-by [key value coll]
  (detect #(= (get % key) value) coll))

(defn cassoc [m & entries]
   (reduce (fn [m [key val]]
             (cond-> m
                     val (assoc key val)))
           m
           (partition 2 entries)))

(defn assoc-if-seq [m & entries]
  (reduce (fn [m [key val]]
            (cond-> m
                    (seq val) (assoc key val)))
          m
          (partition 2 entries)))

(defn sequential-or-set? [x]
  (or (sequential? x) (set? x)))

(defn wrap-coll [x]
  (if (sequential-or-set? x)
    x
    (remove nil? [x])))

(defn includes? [coll x]
  (some #(= % x) coll))

(defn index-of [coll value]
  (some (fn [[idx item]] (if (= value item) idx))
        (map-indexed vector coll)))

(defn stringify-keys [m]
  (transform MAP-KEYS name m))
