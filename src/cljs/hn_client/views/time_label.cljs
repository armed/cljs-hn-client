(ns hn-client.views.time-label)

(defonce minute 60)
(defonce hour (* minute 60))
(defonce day  (* hour 24))
(defonce week (* day 7))

(defn- str-int
  [a b l]
  (str (int (/ a b)) l))

(defn relative-time-label
  [time]
  (let [current (.getTime (js/Date.))
        past    (.getTime (js/Date. (* 1000 time)))
        secs (/ (- current past) 1000)]
    (cond
      (< secs minute) "now"
      (< secs hour)   (str-int secs minute "m")
      (< secs day)    (str-int secs hour "h")
      (< secs week)   (str-int secs day "d")
      :else           (str-int secs week "w"))))
