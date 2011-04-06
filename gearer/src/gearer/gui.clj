(ns gearer.gui
  (:use gearer.gui.layouts gearer.gui.convenience)
  (:require [gearer.utils :as utils]
	    [gearer.battlenet :as bnet])
  (:import (java.awt AlphaComposite
		     Color
		     FlowLayout
		     Graphics
		     Graphics2D
		     GridBagLayout
		     Insets)
	   (java.awt.event ActionListener)
	   (java.awt.image LookupOp
			   CropImageFilter
			   FilteredImageSource)
	   (javax.swing JFrame
			JPanel
			JLabel
			JButton
			JOptionPane
			JTextField
			ImageIcon
			SwingUtilities)))

(defn create-item-panel [slot-id]
  (let [icon (doto (JButton. 
		    (utils/crop-image 
		     (ImageIcon. (utils/get-resource "images/item-empty-bg.png"))
				      0 (* slot-id 47) 47 47))
	       (.setMargin (Insets. 1 1 1 1)))
	name (JLabel. "name")
	stats (JLabel. "stats") ; TODO: make it a panel with flow layout, 1 label per stat
					; so colors can be set
	panel (doto (JPanel. (GridBagLayout.))
		 (grid-bag-layout
		  :insets (Insets. 1 1 1 1)
		  :gridx 0 :gridy 0 :gridheight 3 icon
		  :fill :HORIZONTAL :gridheight 1
		  :gridx 1 name
		  :gridy 1 stats))]
    (doto icon
      (with-action e
	(send (agent (Integer/parseInt (JOptionPane/showInputDialog "Input Item ID")))
	      (fn [item-id]
		(println "loading item" item-id)
		(let [item (bnet/item item-id)]
		  (println item)
		  (when (not (nil? item))
		    (SwingUtilities/invokeLater 
		     (fn []
		       (.setForeground 
			name (cond (= (get item :quality) "0")
				   (Color. 157 157 157)
				   (= (get item :quality) "1")
				   (Color/white)
				   (= (get item :quality) "2")
				   (Color. 30 255 0)
				   (= (get item :quality) "3")
				   (Color. 0 112 221)
				   (= (get item :quality) "4") ; #a335ee
				   (Color. 163 53 238)
				   (= (get item :quality) "5") ; #ff8000
				   (Color. 255 128 0)
				   (or (= (get item :quality) "6") ; #e5cc80
				       (= (get item :quality) "7"))
				   (Color. 229 204 128)
				   :else
				   (Color/black)))
		       (.setText name (get item :name))))))))))
    panel))

(defn open1 []
  (let [id (JTextField. 10)
	name (JTextField. 20)
	load (doto (JButton. "Load")
	       (with-action e
		 (send (agent (Integer/parseInt (.getText id)))
		       (fn [item-id]
			 (println "loading item" item-id)
			 (let [item (bnet/item item-id)]
			   (println item)
			   (if (not (nil? item))
			     (SwingUtilities/invokeLater 
			      (fn [] 
				(.setText name (get item :name))
			      ))
			     (println "no item found")))))))
	panel (doto (JPanel. (GridBagLayout.))
		(grid-bag-layout
		 :gridx 0 :gridy 0 :anchor :LINE_END
		 :insets (Insets. 5 5 5 5)
		 (JLabel. "ID:")
		 :gridy 1
		 (JLabel. "Name:")
		 :gridx 1 :gridy 0 :fill :HORIZONTAL
		 id
		 :gridy 1 :gridwidth :REMAINDER :fill :HORIZONTAL
		 name
		 :gridx 2 :gridy 0 :gridwidth 1 :anchor :LINE_START
		 load
		 ))]
    1))

(defn open []
  (let [head-panel (create-item-panel 0)]
    (doto (JFrame. "Gearer")
      (.setContentPane
       head-panel)
      (.pack)
      (.setVisible true))))

(defn main []
  (SwingUtilities/invokeLater
   open))