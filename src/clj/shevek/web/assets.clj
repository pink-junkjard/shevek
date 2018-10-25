(ns shevek.web.assets
  "This guy is in charge of concatenating, minimizing (although we don't needed it) and fingerprinting assets."
  (:require [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :as strategies]
            [shevek.config :refer [env?]]
            [com.rpl.specter :refer [transform select-one! ALL]]
            [clojure.string :as str]))

(defn- calculate-new-source-map [source-map bundle assets]
  (let [bundle-pattern (re-pattern (str "/bundles/\\w+/" bundle))
        bundle-js-path (select-one! [ALL :path #(re-matches bundle-pattern (str %))] assets)]
    (str/replace bundle-js-path bundle source-map)))

(defn- fix-source-map [assets [source-map bundle]]
  (transform [ALL #(str/ends-with? (str (:original-path %)) (str "/" source-map)) :path]
             (fn [_] (calculate-new-source-map source-map bundle assets))
             assets))

(defn- fix-sources-maps [assets sources-maps-and-bundles]
  "Without this, the *.js.map would appear with a different cache buster hash than the corresponding *.js. They must have the same for StackTrace.JS to found it"
  (reduce fix-source-map assets sources-maps-and-bundles))

#_(fix-sources-maps [{:original-path "/stacktrace-js/dist/stacktrace.min.js.map"}
                     {:path "/bundles/8d942b890803/libs.js" :bundle "libs.js"}
                     {:path "/js/5f391d6c6b04/app.js.map", :original-path "/js/app.js.map"}
                     {:path "/bundles/c08fe3ced8f5/app.js" :bundle "app.js"}]
                    {"app.js.map" "app.js"
                     "stacktrace.min.js.map" "libs.js"})

(defn- all-optimizations [assets options]
  (-> assets
      (optimizations/minify-js-assets options)
      (optimizations/minify-css-assets options)
      (optimizations/concatenate-bundles)
      (optimizations/add-cache-busted-expires-headers)
      (optimizations/add-last-modified-headers)
      (fix-sources-maps {"app.js.map" "app.js"
                         "stacktrace.min.js.map" "libs.js"})))

(defn get-assets []
  (concat
   (assets/load-bundle "node_modules"
                       "libs.css"
                       ["/semantic-ui-css/semantic.min.css"
                        "/semantic-ui-calendar/dist/calendar.min.css"])
   (assets/load-bundle "node_modules"
                       "libs.js"
                       ["/jquery/dist/jquery.min.js"
                        "/semantic-ui-css/semantic.min.js"
                        "/semantic-ui-calendar/dist/calendar.min.js"
                        "/stacktrace-js/dist/stacktrace.min.js"])
   (assets/load-assets "node_modules" ["/stacktrace-js/dist/stacktrace.min.js.map"])
   (assets/load-bundle "public" "app.css" ["/css/app.css"])
   (assets/load-bundle "public" "app.js" ["/js/app.js"])
   (assets/load-assets "public" ["/js/app.js.map"])))

(defn wrap-asset-pipeline [handler]
  (optimus/wrap handler
                get-assets
                (if false optimizations/none all-optimizations)
                (if (env? :development) strategies/serve-live-assets strategies/serve-frozen-assets)))
