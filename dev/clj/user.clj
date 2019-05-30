(ns user
  (:require [figwheel-sidecar.repl-api :refer [cljs-repl]]
            [clojure.tools.namespace.repl :as tns]))

(defn reset
  "ProtoREPL calls this function when starts and when refreshing namespaces"
  []
  ; So the tns/refresh doesn't try to load tests files
  (tns/set-refresh-dirs "src/clj"))

#_(shevek.app/start)
#_(shevek.app/stop)
#_(shevek.app/restart)

#_(tns/refresh)

; To start a ClojureScript REPL connect to the figwheel nREPL (port 4002) and then eval this
#_(cljs-repl)

; Restores the database to its initial state
#_(shevek.schema.seed/db-reset! shevek.db/db)
