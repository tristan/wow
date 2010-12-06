(ns gearer.battlenet
  (:require
   [clj-http.client :as c]
   [gearer.xml :as x]
   [clojure.contrib.str-utils :as re]))

(defn get-xml [url]
  (let [response (c/get url)]
    ;(spit "output.html" (response :body))
    (let [xmlstream (java.io.ByteArrayInputStream. (.getBytes (response :body)))
	  xmlmap (clojure.xml/parse xmlstream)]
      xmlmap)))

; http://us.battle.net/wow/en/item/itemId
(defn item [itemId]
  (let [b (get-xml (str "http://us.battle.net/wow/en/item/" itemId))
	content-node (x/select-node ; class="content-bot"
		      b
		      "html/body/div/div[1]/div/div[1]")
	trail-node (x/select-node ;
		    b
		    "html/body/div/div[1]/div/div/ol")
	item-specs-node (x/select-node
			 content-node
			 "div/div/div[1]/ul")
	item {:id itemId}
	item (assoc item
	       :name
	       (x/select-node-content
		content-node
		"div/div/div[1]/h3"))
	item (assoc item
	       :quality ; get from color of item name
	       (let [n (x/select-node
			content-node
			"div/div/div[1]/h3")]
		 (second (first (remove nil?
					(map #(re-find #"color-q([\d])" %) 
					     (re/re-split #" " ((n :attrs) :class))))))))
	item (assoc item
	       :icon
	       (let [n (x/select-node
			content-node
			"div/div/div[1]/span")
		     s ((n :attrs) :style)]
		 (last (re-find #"images/icons/[\d]+/([\w]+).jpg" s))))
	item-class-stats (((x/select-node
			    trail-node
			    "ol/li[4]/a") :attrs) :href)
	item (assoc item
	       :classId ; class id is 4 for armor, 2 for weapons
	       (last (re-find #"classId\=([\d]+)" item-class-stats)))
	item (assoc item
	       :subclassId ; class id is 4 for armor, 2 for weapons
	       (last (re-find #"subClassId\=([\d]+)" item-class-stats)))
	item (if (< (Integer/parseInt (item :subclassId)) 5)
	       (assoc item
		 :invType ; TODO: perhaps this should be the slot name?
		 (last (re-find #"invType\=([\d]+)"
				(((x/select-node
				   trail-node
				   "ol/li[5]/a") :attrs) :href))))
	       item)
	item (reduce (fn [m n]
		       (cond (nil? (n :attrs))
			     (cond (reduce #(and (string? %2) %1) true (n :content))
				   (let [s (apply str (n :content))]
				     (cond (re-find #"[\d]+ Armor" s)
					   (assoc m :armor 
						  (last (re-find #"([\d]+) Armor" s)))
					   (re-find #"Item Level [\d]+" s)
					   (assoc m :itemLevel
						  (last (re-find #"Item Level ([\d]+)" s)))
					   (re-find #"Requires Level [\d]+" s)
					   (assoc m :requiredLevel
						  (last (re-find #"Requires Level ([\d]+)" 
								 s)))
					   (re-find #"Binds" s)
					   (assoc m :bonding s)
					   (re-find #"\([\d\.]+ damage per second\)" s)
					   (assoc m :damageData
						  (assoc (or (m :damageData) {})
						    :dps
						    (last 
						     (re-find 
						      #"\(([\d\.]+) damage per second\)"
						      s))))
					   :else
					   m)) ; something unknown, skip it
				   (let [lispan (x/select-node-content
						 n "li/span")]
				     (if (string? lispan)
				       (re-find #"Speed" lispan)
				       false))
				   (let [damageData (or (m :damageData) {})
					 dmginfo (apply str (filter string? (n :content)))
					 dmginfo (rest 
						  (re-find 
						   #"^[\s]+([\d]+) - ([\d]+)[\s]+([\w ]+)[\s]+$" dmginfo))
					 dd {:speed
					     (last (re-find #"^Speed (.+)$"
							    (x/select-node-content
							     n "li/span")))
					     :maxDamage (second dmginfo)
					     :minDamage (first dmginfo)
					     :type (cond (= (last dmginfo) "Damage")
							 0 ; TODO: real values below
							 (= (last dmginfo) "Arcane Damage")
							 "Arcane"
							 (= (last dmginfo) "Fire Damage")
							 "Fire"
							 (= (last dmginfo) "Nature Damage")
							 "Nature"
							 (= (last dmginfo) "Frost Damage")
							 "Frost"
							 (= (last dmginfo) "Shadow Damage")
							 "Shadow"
							 (= (last dmginfo) "Holy Damage")
							 "Holy"
							 :else
							 0 ; TODO: unknown...
							 )}]
				     (assoc m :damageData
					    (conj damageData dd)))
				   (and (string? (first (n :content)))
					(re-find #"Classes:" (first (n :content))))
				   (assoc m 
				     :allowableClasses 
				     (vec (map #(first (% :content))
					       (remove string? (n :content)))))
				   (x/select-node n "li/ul")
				   (assoc m
				     :setData
				     {:name 
				      (last
				       (re-find #"([\w ]+) \("
						(x/select-node-content n "li/ul/li")))
				      :items
				      (vec
				       (map 
					#(re-find #"[\d]+$"
						  (((first (% :content)) :attrs) :href))
					(filter
					 #(= ((% :attrs) :class) "indent")
					 ((x/select-node n "li/ul") :content))))
				      :bonuses
				      (vec
				       (map
					#(let [s (re-find 
						  #"\(([\d]+)\) Set:  (.+)" 
						  (apply str (% :content)))]
					   {:threshold (second s)
					    :desc (.trim (last s))})
					(filter
					 #(= ((% :attrs) :class) "color-d4")
					 ((x/select-node n "li/ul") :content))))
				       })
				   :else
				   m) ; nothing else with no attrs is interesting
			     (not (nil? ((n :attrs) :class)))
			     (cond (= "color-tooltip-green" ((n :attrs) :class))
				   (assoc m :heroic "1")
				   (= "color-q2" ((n :attrs) :class))
				   (let [spellData (or (m :spellData) [])
					 s (apply str
						  ((x/select-node
						    n "li/span") :content))
					 s (apply str (map #(.trim %) 
							   (re/re-split #"\n" s)))
					 s (re-find #"^([\w]+):(.+)$" s)]
				     (assoc m :spellData ; TODO: fix results from 30318
					    (conj spellData {:trigger
							     (if (= (second s) "Equip")
							       1 0)
							     :desc (last s)})))
				   (re-find #"Socket" (apply str (n :content)))
				   (let [cnt (apply str (n :content))
					 sockets (or (m :sockets) [])]
				     (if (re-find #"Socket Bonus" cnt)
				       (let [bonus (re-find #"\+([\d]+) ([\w ]+)" cnt)]
					 (assoc m 
					   :socketBonus
					   {:stat (last bonus)
					    :value (second bonus)}))
				       (let [colour (re-find #"([\w]+) Socket" cnt)]
					 (assoc m
					   :sockets (conj sockets (last colour))))))
				   :else
				   m)
			     (and (not (nil? ((n :attrs) :id)))
				  (re-find #"stat-[\d]+" ((n :attrs) :id)))
			     (let [id ((n :attrs) :id)
				   v (x/select-node-content
				      n "li/span")]
			       (cond (= id "stat-49")
				     (assoc m :bonusMasteryRating v)
				     (= id "stat-45")
				     (assoc m :bonusSpellPower v)
				     (= id "stat-37")
				     (assoc m :bonusExpretiseRating v)
				     (= id "stat-36")
				     (assoc m :bonusHasteRating v)
				     (= id "stat-35")
				     (assoc m :bonusResilienceRating v)
				     (= id "stat-32")
				     (assoc m :bonusCritRating v)
				     (= id "stat-31")
				     (assoc m :bonusHitRating v)
				     (= id "stat-14")
				     (assoc m :bonusParryRating v)
				     (= id "stat-13")
				     (assoc m :bonusDodgeRating v)
				     (= id "stat-7")
				     (assoc m :bonusStamina v)
				     (= id "stat-6")
				     (assoc m :bonusSpirit v)
				     (= id "stat-5")
				     (assoc m :bonusIntellect v)
				     (= id "stat-4")
				     (assoc m :bonusStrength v)
				     (= id "stat-3")
				     (assoc m :bonusAgility v)
				     :else ; unknown stat, skip it
				     m))
			     :else
			     m))
		     item (item-specs-node :content))
	;TODO: :itemSource
	item
	(if (= (((x/select-node content-node "div/div[1]/div[1]") :attrs) :id)
	       "location-dropCreatures")
	  (let [drop-table (x/select-node
			    content-node
			    "div/div[1]/div[1]/table/tbody")
		drop-locations
		(vec (distinct (map #(((x/select-node
					%
					"tr/td[3]/a") :attrs) :data-fansite)
				    (drop-table :content))))
		drop-creatures
		(vec (distinct (map #(((x/select-node
					%
					"tr/td[0]/a") :attrs) :data-fansite)
				    (drop-table :content))))]
	    (assoc item 
	      :dropLocations drop-locations
	      :dropCreatures drop-creatures))
	  item)
      	]
    item))
   