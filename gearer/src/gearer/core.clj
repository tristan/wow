(ns gearer.core
  (:require
  ;[http.async.client :as c]
   [clj-http.client :as c]
   [clojure.contrib.str-utils :as re]))


(defn check-conds [tree-node path-node]
;  (println "CHECK:" (= (tree-node :tag) (path-node :tag))
;	   (tree-node :tag) (tree-node :attrs) path-node)
  (and (= (tree-node :tag) (path-node :tag))
       (if (contains? path-node :class)
	 (if (contains? (tree-node :attrs) :class)
	   (not (nil? (re-find (re-pattern (path-node :class))
			       ((tree-node :attrs) :class))))
	   false)
	 true)
       (if (contains? path-node :id)
	 (if (contains? (tree-node :attrs) :id)
	   (= (path-node :id)
	      ((tree-node :attrs) :id))
	   false)
	 true)))

(defn- tree-walker1 [tree path]
;  (println "TW1: " (tree :tag) (tree :attrs) path)
  (cond (empty? path)
	tree
	(not (contains? tree :content))
	(throw (Exception. "failed to walk tree due to invalid tree"))
	:else
	(tree-walker1
	 (let [f (filter #(check-conds % (first path)) (tree :content))]
	   (if (empty? f)
	     (throw (Exception. (apply str "failed to follow tree at point:"
				       (first path))))
	     (first f)))
	 (rest path))))

(defn tree-walker [tree path]
  (cond (map? tree)
	(if (check-conds tree (first path)) ; make sure the root of the tree is
	  (tree-walker1 tree (rest path)) ; the first path-node and start the sub
	  (throw (Exception. (apply str "failed to follow tree at point:"
				    (first path)))))
	:else
	(throw (Exception. "failed to walk tree due to invalid tree"))))

(defn get-xml-from-url [url]
  (let [response ;(c/GET url)]
	(c/get url)]
    ;(c/await response)
    (let [xmlstream (java.io.ByteArrayInputStream. (.getBytes (response :body)))
	  xmlmap (clojure.xml/parse xmlstream)]
      xmlmap)))

(def items-search-table-body [{:tag :html}
			      {:tag :body}
			      {:tag :div :id "wrapper"}
			      {:tag :div :id "content"}
			      {:tag :div :class "content-top"}
			      {:tag :div :class "content-bot"}
			      {:tag :div :id "search-results"}
			      {:tag :div :id "results-interior"}
			      {:tag :table :class "table"}
			      {:tag :tbody}
			      ])

(def item-link-path [{:tag :tr}
		     {:tag :td :class "table-link"}
		     {:tag :a}])

(def page-nav-path [{:tag :html}
		     {:tag :body}
		     {:tag :div :id "wrapper"}
		     {:tag :div :id "content"}
		     {:tag :div :class "content-top"}
		     {:tag :div :class "content-bot"}
		     {:tag :div :id "search-results"}
		     {:tag :div :id "results-interior"}
		     {:tag :div :class "search-paging-container"}
		     {:tag :div :class "page-nav"}
		     {:tag :div :class "pageNav"}])

(defn str-to-int [s]
  (try (Integer/parseInt s)
       (catch Exception e nil)))

(defn get-all-items-from-page 
  ([xmlmap]
     (let [table-body (tree-walker xmlmap items-search-table-body)]
       (map #(last (re/re-split #"item:" ((% :attrs) :rel)))
	    (map #(tree-walker % item-link-path) 
		 (table-body :content)))))
  ([classId page]
     (println "getting classId:" classId " page: " page)
     (let [xmlmap (get-xml-from-url 
		   (str "http://us.battle.net/wow/en/search?f=wowitem&q=classId:" classId
			"&page=" page))]
       (get-all-items-from-page xmlmap))))

; classIds:
; 1 containers
; 2 weapons
; 3 gems
; 4 Armor
; 5 consumables
(defn get-all-items [classId]
  (let [xmlmap (get-xml-from-url 
		(str "http://us.battle.net/wow/en/search?f=wowitem&q=classId:" classId))]
    (let [page-nav-div (tree-walker xmlmap page-nav-path)
	  pages (apply max
		       (remove nil?
			       (map #(str-to-int (first (% :content)))
				    (filter #(= (:tag %) :a) (page-nav-div :content)))))
	  pages 3] ; just to make debugging nicer!
      (reduce
       (fn [results pageNo]
	 (concat results (get-all-items-from-page classId pageNo)))
       (get-all-items-from-page xmlmap)
       (range 2 (inc pages))))))
		 
; http://us.battle.net/wow/en/item/<itemID>
(defn get-item [itemId]
  (let [xmlmap (get-xml-from-url 
		(str "http://us.battle.net/wow/en/item/" itemId))]

(defn get-toon-data [region server name]
  (let [response (c/get ;(c/GET 
		  (str "http://" region 
			     ".battle.net/wow/en/character/"
			     (.toLowerCase server) "/"
			     (.toLowerCase name) "/advanced"))]
    ;(c/await response)
    (let [xmlstream (java.io.ByteArrayInputStream. (.getBytes (response :body))) ; (c/string response)))
	  xmlmap (clojure.xml/parse xmlstream)
	  body (first (filter #(= (% :tag) :body) (xmlmap :content)))
	  div-wrapper (first (filter #(= (% :tag) :div) (body :content)))
	  div-content (first (filter #(and (= (% :tag) :div) 
					   (= ((% :attrs) :id) "content"))
				     (div-wrapper :content)))
	  div-content-top (first (filter #(and (= (% :tag) :div) 
					       (= ((% :attrs) :class) "content-top"))
				     (div-content :content)))
	  div-content-bot (first (filter #(and (= (% :tag) :div) 
					       (= ((% :attrs) :class) "content-bot"))
				     (div-content-top :content)))
	  div-profile-wrapper (first (filter #(and (= (% :tag) :div) 
					   (= ((% :attrs) :id) "profile-wrapper"))
				     (div-content-bot :content)))
	  ; for char dets
	  div-p-s-a (first (filter #(and (= (% :tag) :div) 
					   (= ((% :attrs) :class) 
					      "profile-sidebar-anchor"))
				     (div-profile-wrapper :content)))
	  div-p-s-o (first (filter #(and (= (% :tag) :div) 
					   (= ((% :attrs) :class) 
					      "profile-sidebar-outer"))
				     (div-p-s-a :content)))
	  div-p-s-i (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class) 
					    "profile-sidebar-inner"))
				     (div-p-s-o :content)))
	  div-p-s-c (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class) 
					    "profile-sidebar-contents"))
				     (div-p-s-i :content)))
	  div-p-i-a (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class) 
					    "profile-info-anchor"))
				     (div-p-s-c :content)))
	  div-p-i (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class)
					    "profile-info"))
				     (div-p-i-a :content)))
	  div-name ((first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class)
					    "name"))
				   (div-p-i :content))) :content)
	  char-name (first ((first (filter #(= (% :tag) :a) div-name)) :content))
	  div-title-guild (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class)
					    "title-guild"))
				     (div-p-i :content)))
	  div-guild (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class)
					    "guild"))
				     (div-title-guild :content)))
	  div-title (first (filter #(and (= (% :tag) :div) 
					 (= ((% :attrs) :class)
					    "title"))
				     (div-title-guild :content)))
	  char-title (first (div-title :content))
	  char-guild (first ((first (filter #(= (% :tag) :a) 
					    (div-guild :content))) :content))
	  div-level-race-spec-class (first (filter #(re-find #"level-race-spec-class" 
							     ((% :attrs) :class))
						   (div-p-i :content)))
	  span-level (first (filter #(and (= (% :tag) :span)
					  (= ((% :attrs) :class) "level"))
				    (div-level-race-spec-class :content)))
	  char-level (first (span-level :content))
	  span-race (first (filter #(and (= (% :tag) :span)
					  (= ((% :attrs) :class) "race"))
				    (div-level-race-spec-class :content)))
	  char-race (first (span-race :content))
	  a-spec (first (filter #(and (= (% :tag) :a)
				      (= ((% :attrs) :class) "spec tip"))
				(div-level-race-spec-class :content)))
	  char-spec (first (a-spec :content))
	  span-class (first (filter #(and (= (% :tag) :span)
					  (= ((% :attrs) :class) "class"))
				    (div-level-race-spec-class :content)))
	  char-class (first (span-class :content))
	  span-realm (first (filter #(and (= (% :tag) :span)
					  (= ((% :attrs) :class) "realm tip"))
				    (div-level-race-spec-class :content)))
	  char-realm (first (span-realm :content))
	  ; for invent
	  div-profile-contents (first (filter #(and (= (% :tag) :div) 
					       (= ((% :attrs) :class) "profile-contents"))
				     (div-profile-wrapper :content)))
	  div-summary-top (first (filter #(and (= (% :tag) :div) 
					       (= ((% :attrs) :class) "summary-top"))
				     (div-profile-contents :content)))
	  div-summary-top-inv (first (filter #(and (= (% :tag) :div) 
					       (= ((% :attrs) :class) 
						  "summary-top-inventory"))
				     (div-summary-top :content)))
	  div-summary-inv (first (filter #(and (= (% :tag) :div) 
					   (= ((% :attrs) :id) "summary-inventory"))
				     (div-summary-top-inv :content)))
	  inventory (reduce ; get item ids
		     (fn [m div]
		       (assoc m
			 (keyword ((div :attrs) :data-id))
			 (let [slot-inner (first (filter #(and (= (% :tag) :div) 
							       (= ((% :attrs) :class) 
								  "slot-inner"))
							 (div :content)))
			       slot-contents (first (filter #(and (= (% :tag) :div) 
								  (= ((% :attrs) :class) 
								     "slot-contents"))
							    (slot-inner :content)))
			       a (first (filter #(and (= (% :tag) :a) 
						      (= ((% :attrs) :class) 
							 "item"))
						(slot-contents :content)))
			       item-data ((a :attrs) :data-item)
			       item-data (re/re-split #"&|=" item-data)]
			   (apply hash-map item-data))))
		     {}
		     (div-summary-inv :content))]
      (println div-level-race-spec-class)
      {:profile {:name char-name
		 :title char-title
		 :guild char-guild
		 :level char-level
		 :race char-race
		 :spec char-spec
		 :realm (.trim char-realm)
		 :class char-class}
       :inventory inventory})))
