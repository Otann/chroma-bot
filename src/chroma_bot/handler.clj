(ns chroma-bot.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]

            [chroma-bot.static :as static]))


(defroutes app-routes
  (GET "/" [] static/home)
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
