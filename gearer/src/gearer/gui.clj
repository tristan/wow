(ns gearer.gui
  (:use gearer.gui.layouts gearer.gui.convenience)
  (:require [gearer.utils :as utils]
	    [gearer.battlenet :as bnet]
	    [gearer.gui.mig :as mig])
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
	   (javax.swing DefaultComboBoxModel
			JComboBox
			JFrame
			JPanel
			JLabel
			JButton
			JOptionPane
			JTextField
			ImageIcon
			SwingUtilities)
	   (javax.swing.border LineBorder)))

(defn create-item-panel [slot-id name]
  (let [icon (doto (JLabel. 
		    (utils/crop-image 
		     (ImageIcon. (utils/get-resource "images/item-empty-bg.png"))
				      0 (* slot-id 47) 47 47))
	       )
	name (JLabel. name)
	stats (JLabel. "") ; TODO: make it a panel with flow layout, 1 label per stat
					; so colors can be set
	panel (doto (JPanel. (GridBagLayout.))
		(.setBorder (LineBorder. Color/black))
		(grid-bag-layout
		 :insets (Insets. 1 1 1 1)
		 :gridx 0 :gridy 0 :gridheight 3 :anchor :FIRST_LINE_START icon
		 :fill :HORIZONTAL :gridheight 1
		 :gridx 1 name
		 :gridy 1 stats))]
    (doto icon
      (comment
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
		       (.setText name (get item :name)))))))))
      )
      )
    panel))

(defn slot-panel [slot-id]
  (let [icon (utils/crop-image 
	      (ImageIcon. (utils/get-resource "images/item-empty-bg.png"))
	      0 (* slot-id 47) 47 47)]
    (doto (JPanel. (mig/create-layout "insets 0"))
      (.add (JLabel. icon))
      (.add
       (doto (JPanel. (mig/create-layout 
		       "insets 0, fillx"))
	 (.add (doto (JPanel. (mig/create-layout
			       "insets 0, debug, fillx"
			       "[][]push[][]"))
		 (.add (JLabel. "Elementium Gutslicer"))
		 (.add (JLabel. "Heroic"))
		 (.add (JLabel. "346"))
		 (.add (JLabel. "GEMS GO HERE")))
	       "wrap")
	 (.add (doto (JPanel. (mig/create-layout
			       "insets 0, fillx"))
		 (.add (JLabel. "+194 Stamina"))
		 (.add (JLabel. "+129 Agility"))
		 (.add (JLabel. "+86 Mastery Rating"))
		 (.add (JLabel. "+52 Hit Rating"))
		 (.add (JLabel. "+34"))
		 (.add (doto (JComboBox.)
			 (.setModel 
			  (DefaultComboBoxModel. (to-array ["Expertise Rating"
							    "Crit Rating"
							    "Haste Rating"
							    "Spirit"])))))))
	 )))))

(defn inventory-panel []
  (doto (JPanel. (mig/create-layout 
		  "wrap 1, insets 0, gap 2"))
    (.add (slot-panel 0))
    (.add (slot-panel 1))
    (.add (slot-panel 2))))

(defn open []
  (doto (JFrame. "Gearer")
    (.setBounds 300 300 300 (* 49 12))
    (.setLayout (mig/create-layout "novisualpadding, gap 0, insets 2"))
    (.add (inventory-panel))
    (.pack)
    (.setVisible true)))

(defn main []
  (SwingUtilities/invokeLater
   open))