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


(defmacro new-panel [constraints & components]
  `(doto (JPanel. (create-layout ~@constraints))
     ~@(for [c components]
	 `(.add ~(first c) ~(second c)))))