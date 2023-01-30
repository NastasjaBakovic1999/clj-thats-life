(defproject thats-life "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main thats-life.core 
  :cljsbuild {:builds [{:id "dev" 
                        :source-paths ["src"]
                        :figwheel true
                        :compiler 
                        {:optimizations :none
                         :output-to "resources/public/javascripts/dev.js"
                         :output-dir "resources/public/javascripts/cljs-dev/"
                         :pretty-print true 
                         :source-map true}}]} 
  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-figwheel "0.5.20"]]
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/clojurescript "1.11.60"]
                 [reagent "1.1.1"]]
  :repl-options {:init-ns thats-life.core})
