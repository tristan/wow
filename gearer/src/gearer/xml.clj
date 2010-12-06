(ns gearer.xml
  (:require
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


(defmulti select-node (fn [t p] (string? p)))
(defmethod select-node true [tree path]
  ; this splits up a path string (e.g. "/page/itemTooltips/itemTooltip[1]")
  ; into a list of maps {:tag <tag keyword> :index index} 
  ;(e.g. ({:tag :page :index 0} {:tag :itemTooltips :index 0} {:tag :itemTooltip :index 1}))
  (let [path (map
	      (fn [node]
		{:tag (keyword (first (re/re-split #"\[([\d]+)\]" node)))
		 :index
		 (let [index (re-find #"\[([\d]+)\]" node)]
		   (if (nil? index)
		     0
		     (Integer/parseInt (last index))))})
	      (re/re-split #"/" path))]
    ; make sure the root node of the tree is the first node in the path
    (if (= ((first path) :tag) (tree :tag))
      (select-node tree (rest path))
      (throw (Exception. "root node of tree isn't the first node in the provided path")))))
	    
(defmethod select-node false [tree path]
  (if (empty? path) ; we want to return the root of the tree at this point
    tree
    (let [f (filter #(= (% :tag) ((first path) :tag)) (remove string? (tree :content)))]
      (if (empty? f)
	nil ; not found, return nil
	(if (> (count f) ((first path) :index))
	  (recur (nth f ((first path) :index))
		 (rest path))
	  nil))))) ; again, not found

(defn select-node-content [tree path]
  (let [node (select-node tree path)]
    (if (nil? node) 
      nil
      (first ((select-node tree path) :content)))))
	   