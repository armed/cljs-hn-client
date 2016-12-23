(ns hn-client.views.comment
  (:require [hn-client.views.time-label :refer [relative-time-label]]
            [hn-client.data :refer [state ids-to-path]]
            [reagent.core :as r]))

(declare hn-comment)

(defn- placeholder
  [id]
  [:div.comment {:key id}])

(defn- full-comment
  [{:strs [id by time text kids]} ids]
  (let [cb-id (str "cb" id)]
    [:div.comment {:key id}
     [:input {:id cb-id :type "checkbox"}]
     [:label.comment-header {:for cb-id}
      [:div.arrow]
      [:div.nickname by]
      [:div.time (relative-time-label time)]]
     [:div.comment-body {:dangerouslySetInnerHTML {:__html text}}]
     [:div.comment-kids
      (doall
       (for [k kids]
         (hn-comment (conj ids k))))]]))

(defn hn-comment
  [ids]
  (let [state-path   (ids-to-path ids)
        comment-data (r/cursor state state-path)]
    (if (get @comment-data "id")
      (full-comment @comment-data ids)
      (placeholder (last ids)))))
