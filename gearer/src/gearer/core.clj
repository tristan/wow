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

(defn watch-requested-items []
  (process-stream
   "http://localhost:5984/wow/_changes?feed=continuous&filter=items/requested&heartbeat=5000"
   (fn [i] ; todo: this should append to a list and handled by an agent
     (when (contains? i :id)
       (println "processing: " (i :id))
       (let [i (get-json (str "http://localhost:5984/wow/" (i :id)))
	     i (merge i (bn/item (i :id)))
	     icon-file-name (str (i :icon) ".jpg")]
	 (put-json ; save document
	  (str "http://localhost:5984/wow/item-" (i :id))
	  i)
	 (doseq [s ["18" "56"]] ; store the icons for this item
	   (let [icons (get-json (str "http://localhost:5984/wow/icons%2F" s))
		 rev (if (contains? icons :_rev) ; if no :_rev, then we create the doc
		       (icons :_rev)
		       ((post-json "http://localhost:5984/wow/"
				  {:_id (str "icons/" s)}) :rev))]
	     (when
		 (not
		  (and (contains? icons :_attachments)
		       (contains? (icons :_attachments) (keyword icon-file-name))))
	       (let [f (get-file
			(str "http://us.battle.net/wow-assets/static/images/icons/"
			     s "/" icon-file-name))]
		 
		 (c/put (str "http://localhost:5984/wow/icons%2F" s "/" icon-file-name
			     (str "?rev=" rev))
		    {:headers {"Content-Type" "image/jpeg"}
		     :body f})))))
       )))))



(defn watch-requested []
  (process-stream
   "http://tk:qwertyui@localhost:5984/wow/_changes?feed=continuous&filter=wow/requested&heartbeat=5000"
   (fn [i]
     (println "processing: " (i :id))
     (let [i (get-json (str "http://localhost:5984/wow/" (i :id)))]
       (cond (= (i :type) "item") ; item
	     nil ; TODO: implement
	     (= (i :type) "character") ; otherwise character
	     (let [server (.replaceAll (.toLowerCase (i :realm)) "'" "") 
		   domain (i :domain)
		   character (i :character)]
	       (try
		(put-json
		 (str "http://localhost:5984/wow/" (i :_id))
		 (assoc (dissoc i :requested) 
		   :inventory (bn/character domain server character)))
		(catch Exception e (.printStackTrace e))))
	     :else
	     nil)))))