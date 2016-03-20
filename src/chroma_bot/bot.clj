(ns chroma-bot.bot
  (:require [clojure.java.io :as io]
            [telegram.api :as api]
            [chroma-bot.images :as img]))

(defn handler
  "Handles update object that the bot received from the Telegram API"
  [update]
  (when-let [message (:message update)]
    (api/send-message (-> message :chat :id)
                      (str "Hi there! 😊"))
    (api/send-image (-> message :chat :id)
                    (img/generate-file {:width 200 :heigth 200 :rgb [220 50 47]}))))

