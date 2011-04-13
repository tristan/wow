(ns gearer.gui
  (:use gearer.gui.layouts gearer.gui.convenience gearer.gui.swing)
  (:require [gearer.utils :as utils]
	    [gearer.battlenet :as bnet]
	    [gearer.gui.mig :as mig])
  (:import (javax.swing JFrame
			SwingUtilities)))

(defn slot-panel [slot-id]
  (let [icon (utils/crop-image 
	      (utils/get-icon "images/item-empty-bg.png")
	      0 (* slot-id 47) 47 47)]
    (mig/new-panel
     "insets 0"
     (label icon)
     (mig/new-panel
      "insets 0"
      (mig/new-panel 
       "insets 0"
       (label "Elementium Gutslicer")
       (doto (label "Heroic")
	 (set-fg-color "#1EFF00")
	 (set-font-style :ITALIC))
       (label "346")
       (label "GEMS GO HERE"))
      "wrap"
      (mig/new-panel
       "insets 0"
       (label "+194 Stamina")
       (label "+129 Agility")
       (doto (label "+86 Mastery Rating")
	 (set-fg-color "#1EFF00"))
       (doto (label "+52 Hit Rating")
	 (set-fg-color "#1EFF00"))
       (doto (label "+34")
	 (set-fg-color "#1EFF00"))
       (combobox ["Expertise Rating"
		  "Crit Rating"
		  "Haste Rating"
		  "Spirit"]))))))

(defn inventory-panel []
  (mig/new-panel
   "wrap 1, insets 0, gap 2"
   (slot-panel 0)
   (slot-panel 1)
   (slot-panel 2)))

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