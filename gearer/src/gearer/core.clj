(ns gearer.core
  (:require [gearer.utils :as utils])
  (:import (java.awt AlphaComposite
		     FlowLayout
		     Graphics
		     Graphics2D)
	   (java.awt.event ActionListener)
	   (java.awt.image LookupOp
			   CropImageFilter
			   FilteredImageSource)
	   (javax.swing JFrame
			JPanel
			JLabel
			JButton
			ImageIcon)))

(defn make-slot [s] ; s is the index of the slot icon
					; head neck sholders chest tabard shirt wrists hands waist legs feet ring trinket mh oh ranged relic
  (let [img (utils/crop-image (ImageIcon. (utils/get-resource "images/item-empty-bg.png"))
			      0 (* s 47) 47 47)
	alpha 1]
    (doto
	(proxy [JPanel] []
	  (paint [#^Graphics g]
		 (proxy-super paint g)
		 (doto (cast Graphics2D g)
		   (.setComposite
		    (AlphaComposite/getInstance AlphaComposite/SRC_OVER alpha))
		   (.drawImage (.getImage img) 0 0 nil))))
      (.setSize 47 47))))
   

(defn make-panel []
  (doto (JPanel. (FlowLayout.))
    (.add (make-slot 6))
    (.add (doto (JButton. "Press Me")
	    (.addActionListener
	     (proxy [ActionListener] []
	       (actionPerformed [e] (println e))))))))

(defn make-frame []
  (doto (JFrame. "Gearer")
    (.setBounds 300 300 600 400)
    (.add (make-panel))
    (.setVisible true)))



