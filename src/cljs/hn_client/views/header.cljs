(ns hn-client.views.header
  (:require [hn-client.views.fa-icon :refer [fa-icon]]))

(defn- github-link
  []
  [:a.github-link
   {:href "https://github.com/armed/cljs-hn-client"
    :title "Open Github repo"
    :target "_blank"}
   (fa-icon "github")])

(defn header
  [{:keys [opened-story stories on-close]}]
  (let [clz        (str "header" (when opened-story " story-opened"))
        title-text (get opened-story "title")]
    [:div {:class clz}
     (github-link)
     [:span.logo]
     [:span.back-btn {:on-click on-close}
      (fa-icon "arrow-left")]
     [:div.title title-text]]))
