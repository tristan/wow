(ns gearer.couchdb
  (:use gearer.http)
  (:require
   [gearer.battlenet :as bn]
   [clj-http.client :as c]
   [clojure.contrib.str-utils :as re]))

(def database "http://localhost:5984/wow/")

(defn load-item [id]
  (let [i (get-json (str "http://localhost:5984/wow/item-" id))]
    (if (and (not (nil? i)) 
	     (contains? i :name) 
	     (contains? i :type) 
	     (and (contains? i :invType) 
		  (not (nil? (i :invType)))
		  (not (= (i :invType) "Shield"))
		  ))
      (println "skipping")
      (let [bnet (try
		  (bn/item id)
		  (catch Exception e
		    (println "error")
		    {:error true}))
	    i (merge (dissoc i :invType) {:type "item"} bnet)
	    icon-file-name (str (i :icon) ".jpg")]
	(put-json ; save document
	 (str "http://localhost:5984/wow/item-" (i :id))
	 i)
	(when (not (bnet :error))
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
	  (println (str "done (" (i :slot) ")")))))))

(defn populate-database []
  (let [items (lazy-cat ;(bn/search {:classId 2}) ; weapons
			;(bn/search {:classId 4 :page 1252}) ; armor
	                 (bn/search {:classId 3}) ; gems
			)]
    (doseq [item items]
      (print (str "classId: " (item :classId) ", page: " (item :page)
		  ", id: " (item :id) " ... "))
       (load-item (item :id)))))


(defn fix-weapon-data []
  (let [results (get-json "http://localhost:5984/wow/_design/wow/_view/items-by-X/?startkey=[%222%22]&endkey=[%223%22]")]
    (doseq [item (map #(get % :value) (results :rows))]
      (if (re-find #"^[\d]+$" (or (item :invType) ""))
	nil
	(put-json
	 (str "http://localhost:5984/wow/item-" (item :id))
	 (cond (nil? (item :invType)) ; if the invType is nil
	       (do
		 (println "recollecting data for:" (item :id))
		 (merge item
			(bn/item (item :id))))
	       :else
	       (assoc item 
		 :invType
		 (cond (= (.toLowerCase (or (item :invType) "")) "main-hand")
		       "21"
		       (= (.toLowerCase (or (item :invType) "")) "off-hand")
		       "22"
		       (= (.toLowerCase (or (item :invType) "")) "one-hand")
		       "13"
		       (= (.toLowerCase (or (item :invType) "")) "two-hand")
		       "17"
		       (= (.toLowerCase (or (item :invType) "")) "ranged")
		       (cond (= (item :subclassId) "19") ;wands
			     "26" ; crossbows are listed as 26 on wowhead, ignoring this
			     :else ; default to other ranged weapons
			     "15")
		       (= (.toLowerCase (or (item :invType) "")) "thrown")
		       "25"
		       :else
		       (throw (Exception. 
			       (str "UNKNOWN invType: " (item :invType)
				    " on item-" (item :id))))
		       ))))))))

(defn fix-armor-data []
  (let [results (get-json "http://localhost:5984/wow/_design/wow/_view/items-by-X/?startkey=[%224%22]&endkey=[%225%22]")]
    (doseq [item (map #(get % :value) (results :rows))]
      (if (contains? item :slot)
	(let [slot (.toLowerCase (item :slot))
	      item (dissoc item :slot)
	      item (if (contains? item :invType)
		     item
		     (assoc item
		       :invType
		       (cond (= slot "shield")
			     "14"
			     (= slot "relic")
			     "28"
			     (= slot "consumable")
			     "0"
			     (= slot "held in off-hand")
			     "23"
			     :else
			     (throw (Exception. 
			       (str "UNKNOWN invType: " (item :invType)
				    " on item-" (item :id)))))))]
	  (put-json (str "http://localhost:5984/wow/item-" (item :id))
		    item))
	nil))))

(defn test-search []
  (let [r (post-json 
	   "http://localhost:5984/wow/_design/wow/_view/items-by-slot"
	   {:keys [["16",333,"22","2"],
		   ["16",333,"22","4"],
		   ["16",333,"13","2"],
		   ["16",333,"13","4"],
		   ["16",333,"14","2"],
		   ["16",333,"14","4"],
		   ["16",333,"23","2"],
		   ["16",333,"23","4"]]})]
    r))