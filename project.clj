(defproject chroma-bot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [com.taoensso/timbre "4.1.4"]
                 [com.grammarly/omniconf "0.2.2"]

                 [ring "1.4.0"]
                 [ring-server "0.4.0"]
                 [ring/ring-defaults "0.1.5"]

                 [hiccup "1.0.5"]
                 [compojure "1.4.0"]
                 [clj-http "2.0.0"]
                 [cheshire "5.5.0"]]

  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler chroma-bot.handler/app
         :init chroma-bot.main/ring-init}

  :profiles {:dev {:source-paths ["dev-resources" "src" "test"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}

             :uberjar {:aot :all
                       :omit-source true
                       :main chroma-bot.main
                       :uberjar-name "chroma-bot.jar"}})
