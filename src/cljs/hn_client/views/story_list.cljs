(ns hn-client.views.story-list
  (:require [reagent.core :as r]
            [hn-client.views.fa-icon :refer [fa-icon]]
            [hn-client.views.time-label :refer [relative-time-label]]))

(defn story-link
  [active?
   {:strs [id title score url descendants time] :as story}
   on-open]
  (if id
    ; loaded story
    [:div {:key id :class (str "story-link" (when active? " opened"))}
     [:div.story-title
      {:on-click on-open}
      [:div.title-text title]
      [:div.url
       [:a {:href url :target "_blank"} url]]]
     [:div.story-info
      [:div.story-score (fa-icon "bar-chart") score]
      [:div.story-comments (fa-icon "comments-o") (or descendants 0)]
      [:div.story-time (relative-time-label time)]]]
    ; empty story
    [:div.story-link {:key (get story :index)} "Loading..."]))

(defn sorted-stories
  [keyed-stories]
  (let [story-list (vals keyed-stories)]
    (sort-by :index story-list)))

(defn story-list
  [{:keys [opened-story-id opened-story stories on-open]}]
  [:div.story-list
   [:div.links (for [story   (sorted-stories stories)]
                 (let [id      (get story "id")
                       active? (= opened-story-id id)]
                   (story-link active? story #(on-open id))))]])
