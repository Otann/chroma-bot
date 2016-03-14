(ns chroma-bot.main
  "Responsible for starting application from command line"
  (:gen-class)
  (:require [taoensso.timbre :as log]
            [clojure.java.io :as io]
            [omniconf.core :as cfg]
            [ring.adapter.jetty :refer [run-jetty]]

            [telegram.core :as telegram]
            [telegram.api :as api]
            [chroma-bot.handler :refer [app]]))

(cfg/define {:port {:description "HTTP port"
                    :type :number
                    :default 8080}
             :telegram-token {:description "Token to connect to Telegram API"
                              :type :string
                              :required true
                              :secret true}})

(defn handler
  "Handles update object that the bot received from a Telegram API"
  [update]
  (when-let [message (:message update)]
    (api/send-message (-> update :message :chat :id)
                      (str "Hi there! 😊"))))

(defn init []
  (cfg/verify :quit-on-error true)
  (telegram/init! {:token (cfg/get :telegram-token)
                   :handlers [handler]
                   :polling true}))

(defn ring-init []
  (let [local-config "dev-config.edn"]
    (if (.exists (io/as-file local-config))
      (cfg/populate-from-file local-config)
      (log/warn "Can't find local dev configuration file" local-config))
    (init)))

(defn -main [& args]

  (cfg/populate-from-env)
  (cfg/verify :quit-on-error true)
  (log/info "Starting server")
  (run-jetty app {:port (cfg/get :port) :join? false}))
