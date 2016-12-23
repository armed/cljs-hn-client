(ns hn-client.views.story
  (:require [reagent.core :as r]
            [hn-client.views.comment :refer [hn-comment]]))

(defn- story-content
  [{:strs [text kids]}]
  [:div.story-body
   [:div.story-text text]
   [:div.story-comments
    (if-not (empty? kids)
      (doall
       (for [k kids]
         (hn-comment [k]))))]])

(defn story
  "When story is not selected, this
   component renders as empty `div.story`.
   It's emptyness is used in CSS selectors
   to expand sidebar to full screen."
  [data]
  [:div.story (and data (story-content data))])
