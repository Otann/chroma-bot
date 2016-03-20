(ns chroma-bot.bot
  (:require [telegram.api :as api]
            [chroma-bot.images :as img]))

(def hex-pattern #"#([a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9])")

(defn- hex->int [s] (Integer/parseInt s 16))

(defn- hex->rgb
  "Turns 'ab0023' to [171 0 35]"
  [value]
  [(hex->int (.substring value 0 2))
   (hex->int (.substring value 2 4))
   (hex->int (.substring value 4 6))])

(defn- hex->img [value]
  "Generates byte-array for png image filled with color"
  (let [rgb    (hex->rgb value)
        params {:width 200 :heigth 200 :rgb rgb}
        bytes  (img/generate-png params)]
    bytes))

(defn handle-text-message [message]
  (let [text    (:text message)
        chat-id (-> message :chat :id)
        matches (re-seq hex-pattern text)]
    (if-not matches
      (api/send-message chat-id "No colors detected in the message, sorry")
      (do
        (api/send-message chat-id "Here are the chromatics:")
        (doseq [[_ match] matches]
          (api/send-image chat-id (hex->img match)))))))

(defn handler
  "Handles update object that the bot received from the Telegram API"
  [update]
  (when-let [message (:message update)]
    (if (:text message)
      (handle-text-message message)
      (api/send-message (-> message :chat :id)
                        (str "Sorry, I work only with text for now. ☕️")))))

