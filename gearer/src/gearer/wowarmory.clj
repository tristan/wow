(ns gearer.wowarmory
  (:require
  ;[http.async.client :as c]
   [clj-http.client :as c]
   [gearer.xml :as x]
   [clojure.contrib.str-utils :as re]))

(defn get-xml [url]
  (let [response ;(c/GET url)]
	(c/get url)]
    ;(c/await response)
    (let [xmlstream (java.io.ByteArrayInputStream. (.getBytes (response :body)))
	  xmlmap (clojure.xml/parse xmlstream)]
      xmlmap)))

(defn assoc-if-not-nil [coll k v & kvs]
  (let [coll (if (nil? v)
	       coll
	       (assoc coll k v))]
    (if kvs
      (recur coll (first kvs) (second kvs) (nnext kvs))
      coll)))

; http://us.wowarmory.com/item-tooltip.xml?i=itemId
(defn item [itemId]
  (let [b (get-xml (str "http://us.wowarmory.com/item-tooltip.xml?i=" itemId "&rhtml=n"))
	item {:id (x/select-node-content b "page/itemTooltips/itemTooltip/id")
	      :name (x/select-node-content b "page/itemTooltips/itemTooltip/name")
	      :quality (x/select-node-content 
			b "page/itemTooltips/itemTooltip/overallQualityId")
	      :icon (x/select-node-content 
			b "page/itemTooltips/itemTooltip/icon")
	      :bonding (x/select-node-content 
			b "page/itemTooltips/itemTooltip/bonding")
	      :classId (x/select-node-content
			b "page/itemTooltips/itemTooltip/classId")
	      :slot (x/select-node-content
		     b "page/itemTooltips/itemTooltip/equipData/inventoryType")
	      :itemLevel (x/select-node-content
			  b "page/itemTooltips/itemTooltip/itemLevel")
	      :requiredLevel (or (x/select-node-content
				  b "page/itemTooltips/itemTooltip/requiredLevel")
				 (x/select-node-content
				  b "page/itemTooltips/itemTooltip/requiredLevelMax"))
	      :itemSource ((x/select-node
			    b "page/itemTooltips/itemTooltip/itemSource") :attrs)
	      }
	item (assoc-if-not-nil 
	      item
	      :bonusAgility (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusAgility")
	      :bonusStamina (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusStamina")
	      :bonusStrength (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusStrength")
	      :bonusIntellect (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusIntellect")
	      :bonusSpirit (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusSpirit")
	      :bonusHitRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusHitRating")
	      :bonusCritRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusCritRating")
	      :bonusHasteRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusHasteRating")
	      :bonusDodgeRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusDodgeRating")
	      :bonusParryRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusParryRating")
	      :bonusExpertiseRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusExpertiseRating")
	      :bonusResilienceRating (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/bonusResilienceRating")
	      :bonusMasteryRating (x/select-node-content 
				   b "page/itemTooltips/itemTooltip/bonusMasteryRating")
	      :description (x/select-node-content 
			    b "page/itemTooltips/itemTooltip/desc")
	      :armor (x/select-node-content 
			     b "page/itemTooltips/itemTooltip/armor")
	      :heroic (x/select-node-content
			     b "page/itemTooltips/itemTooltip/heroic")
	      :subclassName (x/select-node-content
			     b "page/itemTooltips/itemTooltip/equipData/subclassName"))
	damageData (x/select-node
		    b "page/itemTooltips/itemTooltip/damageData")
	item (if (and (not (nil? damageData))
		      (not (empty? (damageData :content))))
	       (assoc item :damageData
		      {:damage {:type (x/select-node-content 
				       damageData "damageData/damage/type")
				:min (x/select-node-content 
				      damageData "damageData/damage/min")
				:max (x/select-node-content 
				      damageData "damageData/damage/max")}
		       :speed (x/select-node-content 
			       damageData "damageData/speed")
		       :dps (x/select-node-content 
			     damageData "damageData/dps")})
	       item)
	spellData (x/select-node
		    b "page/itemTooltips/itemTooltip/spellData")
	item (if (not (nil? spellData))
	       (assoc item :spellData
		      (vec (map (fn [s]
				  {:trigger
				   (x/select-node-content 
				    s "spell/trigger")
				   :description
				   (x/select-node-content 
				    s "spell/desc")})
				(spellData :content))))
	       item)
	socketData (x/select-node
		    b "page/itemTooltips/itemTooltip/socketData")
	item (if (not (nil? socketData))
	       (assoc item :socketData
		      (vec (map #((% :attrs) :color) (socketData :content))))
	       item)
	allowableClasses (x/select-node
			  b "page/itemTooltips/itemTooltip/allowableClasses")
	item (if (not (nil? allowableClasses))
	       (assoc item :allowableClasses
		      (vec (map #(x/select-node-content % "class")
				(allowableClasses :content))))
	       item)
	setData (x/select-node
		 b "page/itemTooltips/itemTooltip/setData")
	item (if (not (nil? setData))
	       (assoc item :setData
		      {:name (x/select-node-content setData "setData/name")
		       :items (vec (map #((% :attrs) :name) 
					(filter #(= (% :tag) :item)
						(setData :content))))
		       :bonus
		       (vec (map #(% :attrs)
				 (filter #(= (% :tag) :setBonus)
					 (setData :content))))})
	       item)
	]
    item))
