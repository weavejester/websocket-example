(ns websocket-example.core
  (:require [aleph.http :refer :all]
            [lamina.core :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(defn async-handler [channel handshake]
  (println "New client is here!")
  (enqueue channel "hello world")
  (receive-all channel #(println "Client has sent:" %))
  (on-closed channel #(println "Client has disconnected")))

(defroutes handler
  (GET "/" [] (io/resource "public/index.html"))
  (GET "/async" [] (wrap-aleph-handler async-handler))
  (route/not-found "Not found"))

(defn -main []
  (start-http-server
   (wrap-ring-handler handler)
   {:port 3000 :websocket true}))
