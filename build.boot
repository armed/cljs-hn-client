(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources"}

 :dependencies '[[adzerk/boot-cljs          "1.7.228-2"  :scope "test"]
                 [adzerk/boot-cljs-repl     "0.3.3"      :scope "test"]
                 [adzerk/boot-reload        "0.4.13"     :scope "test"]
                 [pandeiro/boot-http        "0.7.6"      :scope "test"]
                 [com.cemerick/piggieback   "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl   "0.2.12"     :scope "test"]
                 [weasel                    "0.7.0"      :scope "test"]
                 [http-kit                  "2.2.0"      :scope "test"]
                 [proto-repl                "0.3.1"      :scope "test"]
                 [binaryage/devtools        "0.8.3"      :scope "test"]
                 [deraen/boot-sass          "0.3.0"      :scope "test"]
                 [org.slf4j/slf4j-nop       "1.7.21"     :scope "test"]
                 [org.clojure/clojurescript "1.9.293"]
                 [reagent                   "0.6.0"]
                 [secretary                 "1.2.3"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[deraen.boot-sass      :refer [sass]])

(deftask build []
  (comp (speak)
        (sass)
        (sift :include #{#".*scss"} :invert true)
        (cljs)))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced}
                 sass {:source-map false :output-style :compressed})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none}
                 sass {:source-map true}
                 reload {:on-jsload 'hn-client.core/main})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
