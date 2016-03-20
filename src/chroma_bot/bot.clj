(ns chroma-bot.bot
  (:require [clojure.string :as s]
            [telegram.api :as api]
            [chroma-bot.images :as img]))

(def help-message (str "Text me a color in hex form, like #dc322f "
                       "and I will send you a picture of that color back"))

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

(defn handle-text [message]
  (let [text    (:text message)
        chat-id (-> message :chat :id)
        matches (re-seq hex-pattern text)]
    (if-not matches
      (api/send-message chat-id "No colors detected in the message, sorry")
      (do
        (api/send-message chat-id "Here are the chromatics:")
        (doseq [[_ match] matches]
          (api/send-image chat-id (hex->img match)))))))

(defn handle-command
  "Describes how to handle commands from a user"
  [{{chat-id :id} :chat text :text}]
  (let [parts   (s/split text #"\s")
        command (peek parts)]
    (case command
      "/start" (api/send-message chat-id (str "Hi, this is ChromaBot. "
                                              help-message))
      "/help"  (api/send-message chat-id help-message)
      (api/send-message chat-id "I don't know this command yet, sorry"))))

(defn handler
  "Handles update object that the bot received from the Telegram API"
  [update]
  (when-let [message (:message update)]
    (if-let [text (:text message)]
      (if (.startsWith text "/")
        (handle-command message)
        (handle-text message))
      (api/send-message (-> message :chat :id)
                        (str "Sorry, I work only with text for now. ☕️")))))

