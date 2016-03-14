(ns telegram.core
  (:require [telegram.api :as api]
            [telegram.handlers :as h]
            [telegram.polling :as polling]))


(defn init!
  "Initializes Telegram cliend and starts all necessary routines"
  [{:keys [token handlers polling]}]
  (if token
    (reset! api/token token)
    (throw (Exception. "Can't intialize Telegram without a token")))

  (if (seq handlers) (h/reset-handlers! handlers))
  (if polling (polling/start!)))
