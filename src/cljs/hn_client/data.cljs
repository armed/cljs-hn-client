(ns hn-client.data
  (:require [reagent.core :as r]))

(defonce state (r/atom {:stories {}
                        :opened-story nil
                        :opened-story-id nil}))

(defn ids-to-path
  [ids]
  (reduce #(into %1 ["kids-data" %2]) [:opened-story] ids))
