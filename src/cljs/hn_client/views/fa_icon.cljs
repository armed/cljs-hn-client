(ns hn-client.views.fa-icon)

(defn fa-icon
  [name]
  [:i {:class (str "fa fa-" name)}])
