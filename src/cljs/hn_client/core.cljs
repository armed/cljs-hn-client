(ns hn-client.core
  (:require
   ; vendor
   [reagent.core   :as r]
   [reagent.ratom  :refer [reaction]]
   [secretary.core :as secretary]
   ;local
   [hn-client.data             :refer [state]]
   [hn-client.handlers         :refer [story-list-loaded!
                                       story-changed!]]
   [hn-client.requests         :refer [top-stories
                                       top-stories-changes]]
   [hn-client.views.story-list :refer [story-list]]
   [hn-client.views.header     :refer [header]]
   [hn-client.views.story      :refer [story]]
   [hn-client.router           :refer [go-home
                                       go-to-story]]))

(defn content-view
  []
  (let [opened-story-id (:opened-story-id @state)
        opened-story    (:opened-story @state)
        stories         (:stories @state)]
    [:div.content
     (header {:opened-story opened-story
              :stories stories
              :on-close go-home})
     (story opened-story)
     (story-list {:opened-story-id opened-story-id
                  :opened-story opened-story
                  :stories stories
                  :on-open go-to-story})]))

(defn ^:export main
  []
  (top-stories story-list-loaded!)
  (top-stories-changes story-changed!)
  ; (init-router)
  (r/render [content-view]
            (.getElementById js/document "app")))
