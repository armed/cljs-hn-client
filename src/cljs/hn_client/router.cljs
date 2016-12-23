(ns hn-client.router
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History
           goog.history.EventType)
  (:require
   ; vendor
   [secretary.core :as secretary]
   [goog.events :as events]
   ; local
   [hn-client.handlers :refer [story-opened!
                               story-closed!]]))

(secretary/set-config! :prefix "#")

(defroute home-path "/" []
  (story-closed!))

(defroute story-path "/story/:id" [id]
  (story-opened! (int id)))

(defn- set-url!
  [url]
  (set! (.-location js/window) url))

(defn go-home
  []
  (set-url! (home-path)))

(defn go-to-story
  [story-id]
  (set-url! (story-path {:id story-id})))

(defonce history
  (let [h (History.)]
    (goog.events/listen h EventType.NAVIGATE
                        #(secretary/dispatch! (.-token %)))
    (doto h (.setEnabled true))))
