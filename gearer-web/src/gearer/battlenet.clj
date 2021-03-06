(ns gearer.battlenet
  (:require
   [clj-http.client :as c]
   [gearer.xml :as x]
   [clojure.contrib.string :as string]
   [clojure.contrib.str-utils :as re]))

(defn get-xml [url]
  (let [response (c/get url)]
    ;(spit "output.html" (response :body))
    (let [reader (java.io.StringReader. (response :body))
	  is (org.xml.sax.InputSource. reader)]
      (. is setEncoding "UTF-8")
      (clojure.xml/parse is))))
      ;xmlmap)))

; http://us.battle.net/wow/en/item/itemId
; TODO: trinket/ring/weapon uniqueness
; check item: 23835 (and other engineering items)
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
	item (if (and (= (item :classId) "4")
		      (= (item :subclassId) "6")) ; shields!
	       (assoc item
		 :invType "14")
	       item)
	item (if (= (item :classId) "3") ; gem!
	       (let [specs (map #(.trim (str (x/select-node-content % "li"))) (item-specs-node :content))
		     specs (first (filter #(re-find #"^\+[\d]+[ ]+.+$" %) specs))]
		 (if (not (nil? specs))
		   (reduce (fn [item stat]
			     (if (re-find #"^\+[\d]+[ ]+.+$" stat)
			       (let [[_ val key] (re-find #"^\+([\d]+)[ ]+(.+)$" stat)]
				 (assoc item
				   (keyword (str "bonus" (re/str-join "" (re/re-split #" " key))))
				   (Integer/parseInt val)))
			       (let [misc (get item :miscStats [])]
				 (assoc item
				   :miscStats
				   (conj misc stat)))))
			   (assoc item :invType "0")
			   (re/re-split #" and " specs))
		   item))
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
					   (re-find #"Requires [\w]+ \([\d]+\)" s)
					   (assoc m :requiresProfession
						  (let [[_ prof lvl] 
							(re-find #"Requires ([\w]+) \(([\d]+)\)" 
								 s)]
						    {:profession prof
						     :level lvl}))
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
				   (let [damageData (or (m :damageData) {}) ; TODO: warglave has multiple types of damage, this doesn't get stored correctly, i assume there are many similar items too.
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
							 "Physical" ; TODO: real values below
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
							 "Unknown" ; TODO: unknown...
							 )}]
				     (assoc m :damageData
					    (conj damageData dd)))
				   (and (let [lispan (x/select-node-content 
					; the "slot" "type" row
						      n "li/span")]
					  (if (string? lispan)
					    (not (= (.trim 
						     (str (x/select-node-content
							   n "li"))) "Sell Price:"))
					    false))
					(and (not (= (m :classId "4"))))) ; armor is set already
				   (let [slot-name
					 (.toLowerCase
					  (.trim 
					   (apply 
					    str
					    (rest ((x/select-node n "li") :content)))))]
				     (println slot-name)
				     (assoc m
				       :invType
				       (cond (= slot-name "main-hand")
					     "21"
					     (= slot-name "off-hand")
					     "22"
					     (= slot-name "one-hand")
					     "13"
					     (= slot-name "two-hand")
					     "17"
					     (= slot-name "ranged")
					     (cond (= (item :subclassId) "19") ; wands
						   "26" ; ignoring crossbows being 26
						   :else ; default to other ranged weapons
						   "15")
					     (= slot-name "thrown")
					     "25"
					     (= slot-name "consumable")
					     "0"
					     (= slot-name "shield")
					     "14"
					     (= slot-name "relic")
					     "28"
					     (= slot-name "held in off-hand")
					     "23"
					     :else
					     (do (println "error:" m)
						 (throw (Exception. 
						     (str "UNKNOWN invType: " slot-name
							  " on item-" itemId)))))))
				   (and (string? (first (n :content)))
					(re-find #"Classes:" (first (n :content))))
				   (assoc m 
				     :allowableClasses 
				     (vec (map #(first (% :content))
					       (remove string? (n :content)))))
				   (x/select-node n "li/ul") ; is there set info?
				   (assoc m
				     :setData
				     {:name 
				      (last  ; TODO: check needed punctuation
				       (re-find #"([\w ']+) \("
						(x/select-node-content n "li/ul/li")))
				      :items
				      (vec
				       (map 
					#(re-find #"[\d]+$"
						  (((first (% :content)) :attrs) :href))
					(filter ; TODO: ignores "Requires Tailoring" etc
					 #(and (not (nil? (% :attrs)))
					       (= ((% :attrs) :class) "indent"))
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
					 #(and (not (nil? (% :attrs)))
					       (= ((% :attrs) :class) "color-d4"))
					 ((x/select-node n "li/ul") :content))))
				       })
				   :else
				   m) ; nothing else with no attrs is interesting
			     (and (not (nil? ((n :attrs) :id)))
				  (re-find #"stat\-[\d]+" ((n :attrs) :id)))
			     (let [id ((n :attrs) :id)
				   v (x/select-node-content
				      n "li/span")
				   v (try
				      (Integer/parseInt v)
				      (catch Exception e
					v))
				   ]
			       (cond (= id "stat-49")
				     (assoc m :bonusMasteryRating v)
				     (= id "stat-45")
				     (assoc m :bonusSpellPower v)
				     (= id "stat-37")
				     (assoc m :bonusExpertiseRating v)
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
				   (and (re-find #"Socket" (apply str (n :content))) 
					(not (= (item :classId) "3"))) ; ignore when gem
				   (let [cnt (apply str (map #(.trim (str %)) (n :content)))
					 sockets (or (m :sockets) [])]
				     (if (re-find #"Socket Bonus:" cnt)
				       (let [bonus (re-find 
						    #"^Socket Bonus:[ ]+[\+]?([^ ]+) (.+)$" 
						    cnt)
					     stat (last bonus)
					     value (second bonus)
					     value (try
						    (Integer/parseInt value)
						    (catch Exception e
						      value))]
					 (assoc m 
					   :socketBonus
					   {(cond (= stat "Hit Rating")
						  :bonusHitRating
						  (= stat "Mastery Rating")
						  :bonusMasteryRating
						  (= stat "Spell Power")
						  :bonusSpellPower
						  (= stat "Expertise Rating")
						  :bonusExpertiseRating
						  (= stat "Haste Rating")
						  :bonusHasteRating
						  (= stat "Resilience Rating")
						  :bonusResilienceRating
						  (= stat "Critical Strike Rating")
						  :bonusCritRating
						  (= stat "Parry Rating")
						  :bonusParryRating
						  (= stat "Dodge Rating")
						  :bonusDodgeRating
						  (= stat "Stamina")
						  :bonusStamina
						  (= stat "Spirit")
						  :bonusSpirit
						  (= stat "Intellect")
						  :bonusIntellect
						  (= stat "Strength")
						  :bonusStrength
						  (= stat "Agility")
						  :bonusAgility
						  (= stat "mana per 5 sec")
						  :bonusMp5
						  :else
						  stat) ; no idea what it is
					    value}))
				       (let [colour (re-find #"([\w]+) Socket" cnt)]
					 (assoc m
					   :sockets (conj sockets (last colour))))))
				   :else
				   m)
			     :else
			     m))
		     item (item-specs-node :content))
	;TODO: :itemSource
	blah (comment
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
	  item))
      	]
    item))

(defn character [region server name]
  (let [b (get-xml (str "http://" region 
			".battle.net/wow/en/character/"
			(.toLowerCase server) "/"
			(.toLowerCase name) "/advanced"))
	inventory (x/select-node
		   b "html/body/div/div[1]/div/div[1]/div/div[1]/div/div[1]/div")]
    (reduce
     (fn [inv div]
       (assoc inv
	 (keyword ((div :attrs) :data-id))
	 (let [data-item (((x/select-node
			    div "div/div/div/a") :attrs) :data-item)
	       sockets (filter #(= ((% :attrs) :class) "sockets")
			       ((or 
				 (x/select-node
				  div "div/div/div/div")
				 {:content []}) :content))
	       ]
	   (if (or (nil? data-item) (empty? sockets))
	     (or data-item "")
	     (re/str-join
	      "&"
	      (concat 
	       (remove #(re-find #"^g\d=\d+$" %) (re/re-split #"&" data-item))
	       (map #(str "g" %1 "=" %2)
		    (iterate inc 0)
		    (remove 
		     nil?
		     (for [span ((first sockets) :content)]
		       (let [a (x/select-node span "span/a")]
			 (if (nil? a)
			   nil
			   (last
			    (re-find #"/wow/en/item/(\d+)" 
				     ((a :attrs) :href))))))))))))))
     {}
     (inventory :content))))


(defn search [params]
  "params include classId, subClassId, invType"
  (let [base-url "http://us.battle.net/wow/en/item/"
	url (if (string? params)
	      (str base-url params)
	      (str base-url "?"
		   (string/join 
		    "&" 
		    (map #(str (name (key %)) "=" (val %)) 
			 params))))
	xml (get-xml url)
	items (x/select-node
	       xml "html/body/div/div[1]/div/div[1]/div[1]/div[1]/table/tbody")
	next (x/select-node
	      xml "html/body/div/div[1]/div/div[1]/div[1]/div/div/ul/li[-1]/a")
	classId (or (and (map? params) (params :classId)) 
		    (last (or (re-find #"classId=([\d]+)" url) [])))
	page (or (and (map? params) (params :page))
		 (last (or (re-find #"page=([\d]+)" url) ["1"])))
	items (for [i (items :content)]
		(let [href (((x/select-node i "tr/td/a") :attrs) :href)]
		  {:id (last (re-find #"^/wow/en/item/(.+)$" href))
		   :page page
		   :classId classId}))
	]
    (lazy-seq
     (if (and next (= (string/join "" (next :content)) "Next"))
       (concat items (search ((next :attrs) :href)))
       items))
    ))
