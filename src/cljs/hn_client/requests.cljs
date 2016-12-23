(ns hn-client.requests
  (:require [firebase.app]
            [firebase.database]))

(defonce fb-config
  #js {:databaseURL "https://hacker-news.firebaseio.com"})

(defonce fb-app (.initializeApp js/firebase fb-config))
(defonce db (.database js/firebase fb-app))

(defn db-top-stories
  []
  (-> db
      (.ref "/v0/topstories")
      (.limitToFirst 125)))

(defn request-item
  [item-id handler]
  (-> db
      (.ref (str "/v0/item/" item-id))
      (.once "value" #(handler (js->clj (.val %))))))

(defn request-comments
  ([ids handler]
   (request-comments ids handler []))
  ([ids handler parent-ids]
   (doseq [id ids]
     (request-item id (partial handler parent-ids)))))

(defn top-stories
  [handler]
  (.once (db-top-stories)
         "value"
         #(handler (js->clj (.val %1)))))

(defn top-stories-changes
  [handler]
  (.on (db-top-stories)
       "child_changed"
       #(handler (js->clj (.val %1)))))
