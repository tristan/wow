(ns gearer.gui.mig
  (:import (net.miginfocom.swing MigLayout)
	   (javax.swing JPanel)))

(defn create-layout 
  ([] (MigLayout.))
  ([#^String layout-constraints] (MigLayout. layout-constraints))
  ([#^String layout-constraints #^String col-constraints] 
     (MigLayout. layout-constraints col-constraints))
  ([#^String layout-constraints #^String col-constraints #^String row-constraints] 
     (MigLayout. layout-constraints col-constraints row-constraints)))

(defn add [layout comp & details]
  (.add layout comp (apply str details)))

(defn- partition-things [things]
  (remove nil? (for [[a b] (map vector things (concat (rest things) [:f]))] 
		 (cond (string? a) nil (string? b) [a b] :default [a]))))

(defmacro new-panel [& stuff]
  `(doto (JPanel. (create-layout ~@(take-while string? stuff)))
     ~@(for [c (partition-things (drop-while string? stuff))]
	 (if (= (count c) 1)
	   `(.add ~(first c))
	   `(.add ~(first c) ~(second c))))))
