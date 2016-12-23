(ns hn-client.handlers
  (:require [reagent.core :as r]
            [hn-client.data :refer [state ids-to-path]]
            [hn-client.requests :refer [request-item
                                        request-comments]]))

(defn- set-initial-story-list!
  [stories]
  (swap! state #(assoc % :stories stories)))

(defn- as-index
  [kstr]
  (inc (int (or kstr -1))))

(defn- init-kids-data
  [coll]
  (assoc coll "kids-data" {}))

(defn- story-have-changes?
  "Re-render only if score or descendants changed"
  [{id "id" score "score" dsnds "descendants"}]
  (let [{old-score "score"
         old-dsnds "descendants"} (get-in @state [:stories id])]
    (or (not= old-score score) (not= old-dsnds dsnds))))

(defn comment-data-loaded!
  [parent-ids comment-data]
  (if (:opened-story @state)
    (let [{kids "kids"
           comment-id "id"} comment-data
          all-ids           (conj parent-ids comment-id)
          state-path        (ids-to-path all-ids)
          kids-data         (init-kids-data comment-data)]
      (swap! state #(assoc-in % state-path kids-data))
      (if-not (empty? kids)
        (request-comments kids comment-data-loaded!
                          (conj parent-ids comment-id))))))

(defn story-data-loaded!
  [{:strs [id kids] :as story-data}]
  (let [opened-story-id (:opened-story-id @state)]
    (when (story-have-changes? story-data)
      (swap! state (fn [s] (update-in s [:stories id] #(merge % story-data))))
      (when (= opened-story-id id)
        ;; if story is opened, we have to load it's contents
        ;; this happens when URL is passed with story token e.g.
        ;; `/#/story/13240409`
        (swap! state #(assoc % :opened-story (init-kids-data story-data)))
        (request-comments kids comment-data-loaded!)))))

(defn story-list-loaded!
  [story-ids]
  (->> story-ids
       (map-indexed (fn [idx id] [id {:index idx}]))
       (into {})
       (set-initial-story-list!))
  (doseq [story-id story-ids]
    (request-item story-id story-data-loaded!)))

(defn story-changed!
  [story-id]
  (let [keyed-stories (:stories @state)]
    (if (contains? keyed-stories story-id)
      (request-item story-id story-data-loaded!))))

(defn story-opened!
  [story-id]
  (if story-id
    (let [story     (get-in @state [:stories story-id])
          kids      (get story "kids")
          kids-data (init-kids-data story)]
      (swap! state
             #(-> %
                  (assoc :opened-story-id story-id)
                  (assoc :opened-story kids-data)))
      (request-comments kids comment-data-loaded!))
    (swap! state
           #(-> %
                (assoc :opened-story-id nil)
                (assoc :opened-story nil)))))

(defn story-closed!
  []
  (story-opened! nil))
