(ns gearer.core
  (:use gearer.http)
  (:require
   [gearer.battlenet :as bn]
   [clj-http.client :as c]
   [clojure.contrib.str-utils :as re]))

(defn load-requested-items []
  (doseq [i (map #(get % :value)
	     (get (get-json "http://localhost:5984/wow/_design/items/_view/requested") 
		 :rows []))]
    (put-json
     (str "http://localhost:5984/wow/item-" (i :id))
     (merge i (bn/item (i :id))))))