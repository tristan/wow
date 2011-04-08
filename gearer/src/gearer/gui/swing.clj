(ns gearer.gui.swing
  (:import (java.awt Color
		     Font)
	   (javax.swing DefaultComboBoxModel
			JComboBox
			JLabel)
	   ))

(defn- color-str-to-list [color]
  (map #(Integer/parseInt % 16)
       (or (rest (re-find #"^#?([a-zA-Z0-9]{2})([a-zA-Z0-9]{2})([a-zA-Z0-9]{2})$" color))
	   (map #(str % %) 
		(rest (re-find #"^#?([a-zA-Z0-9])([a-zA-Z0-9])([a-zA-Z0-9])$" color))))))

(defmacro set-fg-color [component color]
  `(doto ~component
     (.setForeground ~(cond (keyword? color)
			    `(. Color ~(symbol (name color)))
			    (string? color)
			    `(Color. ~@(color-str-to-list color))
			    :default
			    color))))

(defmacro set-font-style [component style]
  `(let [f# (.getFont ~component)]
     (doto ~component
       (.setFont (Font. (.getName f#) 
			(. Font ~(symbol (name style)))
			(.getSize f#))))))

(defmacro label [content]
  `(doto (JLabel. ~content)
     (set-font-style :PLAIN)))

(defmacro combobox [options]
  `(doto (JComboBox.)
     (.setModel 
      (DefaultComboBoxModel. (to-array ~options)))))