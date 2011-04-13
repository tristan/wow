(ns gearer.views
  (:use clojure.contrib.str-utils)
  (:require [clj-zpt :as zpt]
	    [gearer.battlenet :as bnet]))

(defmacro render 
  ([template] `(render ~template {}))
  ([template data]
  `((:renderer (zpt/get-template-from-cache ~template)) ~data)))

(defn index []
  (render "index.pt")
  )

(defn character [domain realm character]
  (render "character.pt" 
	  {:domain domain
	   :realm realm
	   :character character}))

(defn load-character [domain realm character]
  (let [char (bnet/character domain realm character)]
    (apply str (map #(str (val %) "<br/>")
    (reduce 
     #(assoc %1 
	(key %2)
	(bnet/item
	 (get 
	  (apply hash-map (flatten (map (fn [a] (re-split #"=" a))
					(re-split #"&" (val %2)))))
	  "i")))
	{} char)))))