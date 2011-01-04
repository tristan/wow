(ns gearer.optimiser
  (:require gearer.battlenet))

(def test-data
     {:0 [56298] ; head
      :1 [56338] ; neck
      :2 [58124] ; sholders
      :3 [] ; shirt
      :4 [58121] ; chest
      :5 [57916] ; belt
      :6 [67150] ; legs
      :7 [62385] ; feet
      :8 [56340] ; wrist
      :9 [58125 56268] ; hands
      :10 [62362] ; ring1
      :11 [62348] ; ring2
      :12 [56394] ; trinket1
      :13 [56295] ; trinket2
      :14 [65177] ; back
      :15 [67602] ; main hand
      :16 [67602] ; off hand
      :17 [56316] ; ranged
      :18 [] ; tabard
      })

(def resolved-test-data
{:16 [{:requiredLevel "85", :quality "3", :icon "inv_axe_1h_bwdraid_d_01", :name "Elementium Gutslicer", :itemLevel "346", :bonusHitRating 86, :classId "2", :bonding "Binds when equipped", :bonusStamina 194, :damageData {:dps "409.4", :type 0, :minDamage "745", :maxDamage "1384", :speed "2.60"}, :invType nil, :bonusMasteryRating 86, :id 67602, :bonusAgility 129, :subclassId "0"}], :6 [{:requiredLevel "85", :quality "4", :icon "inv_pant_mail_raidhunter_i_01", :name "Arrowsinger Legguards", :sockets ["Red" "Blue"], :itemLevel "359", :bonusHitRating 165, :classId "4", :bonding "Binds when equipped", :armor "2129", :socketBonus {:bonusMasteryRating 20}, :bonusStamina 512, :invType "7", :bonusMasteryRating 241, :id 67150, :bonusAgility 301, :subclassId "3"}], :7 [{:requiredLevel "85", :quality "4", :icon "inv_boots_mail_raidhunter_i_01", :name "Treads of Malorne", :sockets ["Yellow"], :itemLevel "359", :bonusHitRating 149, :classId "4", :bonding "Binds when picked up", :armor "1673", :socketBonus {:bonusAgility 10}, :bonusStamina 380, :invType "8", :bonusMasteryRating 169, :id 62385, :bonusAgility 233, :subclassId "3"}], :4 [{:requiredLevel "85", :quality "3", :icon "inv_chest_mail_dungeonmail_c_04", :name "Vest of the True Companion", :sockets ["Red" "Yellow"], :itemLevel "346", :bonusHitRating 162, :classId "4", :bonding "Binds when picked up", :armor "2322", :socketBonus {:bonusCritRating 20}, :bonusStamina 454, :invType "5", :bonusMasteryRating 202, :id 58121, :bonusAgility 262, :subclassId "3"}], :5 [{:requiredLevel "85", :quality "3", :bonusCritRating 150, :icon "inv_belt_mail_dungeonmail_c_04", :name "Belt of the Dim Forest", :sockets ["Yellow"], :itemLevel "346", :bonusHitRating 130, :classId "4", :bonding "Binds when picked up", :armor "1306", :socketBonus {:bonusCritRating 10}, :bonusStamina 337, :invType "6", :id 57916, :bonusAgility 205, :subclassId "3"}], :1 [{:requiredLevel "85", :quality "3", :bonusCritRating 112, :heroic "1", :icon "inv_jewelry_necklace_16", :name "Pendant of the Lightless Grotto", :itemLevel "346", :classId "4", :bonding "Binds when picked up", :bonusStamina 252, :invType "2", :bonusMasteryRating 112, :id 56338, :bonusAgility 168, :subclassId "0"}], :0 [{:requiredLevel "85", :quality "3", :heroic "1", :icon "inv_helmet_mail_dungeonmail_c_03", :name "Renouncer's Cowl", :sockets ["Meta" "Blue"], :itemLevel "346", :bonusHitRating 162, :classId "4", :bonding "Binds when picked up", :armor "1887", :socketBonus {:bonusMasteryRating 30}, :bonusStamina 454, :invType "1", :bonusMasteryRating 182, :id 56298, :bonusAgility 242, :subclassId "3"}], :3 [], :2 [{:requiredLevel "85", :quality "3", :icon "inv_shoulder_mail_dungeonmail_c_04", :name "Wrap of the Valley Glades", :sockets ["Yellow"], :itemLevel "346", :bonusHitRating 130, :classId "4", :bonding "Binds when picked up", :armor "1741", :socketBonus {:bonusHitRating 10}, :bonusHasteRating 150, :bonusStamina 337, :invType "3", :id 58124, :bonusAgility 205, :subclassId "3"}], :9 [{:requiredLevel "85", :quality "3", :bonusCritRating 150, :icon "inv_gauntlets_mail_dungeonmail_c_04", :name "Gloves of the Passing Night", :sockets ["Red"], :itemLevel "346", :bonusHitRating 130, :classId "4", :bonding "Binds when picked up", :armor "1451", :socketBonus {:bonusCritRating 10}, :bonusStamina 337, :invType "10", :id 58125, :bonusAgility 205, :subclassId "3"}], :8 [{:requiredLevel "85", :quality "3", :heroic "1", :icon "inv_bracer_07", :name "Elementium Scale Bracers", :itemLevel "346", :bonusHitRating 112, :classId "4", :bonding "Binds when picked up", :armor "1016", :bonusHasteRating 112, :bonusStamina 252, :invType "9", :id 56340, :bonusAgility 168, :subclassId "3"}], :10 [{:requiredLevel "85", :quality "4", :icon "inv_misc_diamondring2", :name "Signet of the Elder Council", :itemLevel "359", :classId "4", :bonding "Binds when picked up", :bonusHasteRating 127, :bonusStamina 286, :invType "11", :bonusMasteryRating 127, :id 62362, :bonusAgility 190, :subclassId "0"}], :12 [{:spellData [{:trigger 1, :desc "Your melee and ranged attacks grant 34 Agility.  Lasts 15 sec, stacking up to 10 times."}], :requiredLevel "85", :quality "3", :heroic "1", :icon "inv_ammo_arrow_05", :name "Tia's Grace", :itemLevel "346", :classId "4", :bonding "Binds when picked up", :invType "12", :bonusMasteryRating 285, :id 56394, :subclassId "0"}], :11 [{:requiredLevel "85", :quality "3", :icon "inv_jewelry_ring_49naxxramas", :name "Terrath's Signet of Balance", :itemLevel "346", :bonusHitRating 112, :classId "4", :bonding "Binds when picked up", :bonusStamina 252, :invType "11", :bonusMasteryRating 112, :id 62348, :bonusAgility 168, :subclassId "0"}], :14 [{:requiredLevel "85", :quality "3", :heroic "1", :icon "inv_misc_cape_cataclysm_melee_c_01", :name "Cape of the Brotherhood", :itemLevel "346", :bonusHitRating 112, :classId "4", :bonding "Binds when picked up", :armor "580", :bonusHasteRating 112, :bonusStamina 252, :invType "16", :id 65177, :bonusAgility 168, :subclassId "1"}], :13 [{:spellData [{:trigger 1, :desc "Your melee and ranged attacks have a chance to grant 1710 critical strike rating for 10 sec."}], :requiredLevel "85", :quality "3", :heroic "1", :icon "inv_misc_horn_02", :name "Grace of the Herald", :itemLevel "346", :classId "4", :bonding "Binds when picked up", :invType "12", :id 56295, :bonusAgility 285, :subclassId "0"}], :17 [{:requiredLevel "85", :quality "3", :bonusCritRating 62, :heroic "1", :icon "inv_shield_56", :name "Sandshift Relic", :sockets ["Prismatic"], :itemLevel "346", :bonusHitRating 64, :classId "4", :bonding "Binds when picked up", :bonusStamina 143, :id 56316, :bonusAgility 95, :subclassId "11"}], :18 [], :15 [{:requiredLevel "85", :quality "3", :icon "inv_axe_1h_bwdraid_d_01", :name "Elementium Gutslicer", :itemLevel "346", :bonusHitRating 86, :classId "2", :bonding "Binds when equipped", :bonusStamina 194, :damageData {:dps "409.4", :type 0, :minDamage "745", :maxDamage "1384", :speed "2.60"}, :invType nil, :bonusMasteryRating 86, :id 67602, :bonusAgility 129, :subclassId "0"}]})

; for enhance
; hit rating (until 1742) > expertise rating (until 541) > agility > mastery > (crit == haste)
(def stat-weights-enhance
     [:bonusHitRating
      :bonusExpertiseRating
      :bonusAgility
      :bonusMasteryRating
      [:bonusCritRating :bonusHasteRating]])

(def caps-enhance
     {:bonusHitRating [1742 '>]
      :bonusExpertiseRating [541 '<]})

(defn resolve-data [data]
  (reduce (fn [data slot]
	    (assoc data
	      (key slot)
	      (vec (map gearer.battlenet/item (val slot)))))
	  data data))

(def enhance-gems
     [{:bonusAgility 20 :bonusHitRating 20 :colour :blue}
      {:bonusAgility 40 :colour :red}
      {:bonusAgility 20 :bonusMasteryRating 20 :colour :yellow}])

(def reforgable-stats
     [:bonusHitRating
      :bonusExpertiseRating
      :bonusSpirit
      :bonusMasteryRating
      :bonusCritRating
      :bonusHasteRating
      :bonusDodgeRating
      :bonusParryRating])

(defn optimise [data weights caps] )

; BRUTE-FORCE MODE
; build list of all reforge gem and enchant choices options for each piece of gear
; build tree using the list. compare all leaf nodes

(defn combine-stats [s1 s2]
  (reduce (fn [final stat]
	    (assoc final 
	      (key stat)
;	      (cond (number? (val stat))
		    (+ (get final (key stat) 0)
		       (val stat))
;		    (map? (val stat))
;		    (conj (get final (key stat) {})
;			  (val stat))
;		    :else
;		    (concat (get final (key stat) []) (val stat)))))
		    ))
	  s1 s2))

(defn choose-k [coll k]
  "Returns a list of all unique combinations of the elements in coll in a list of size k."
  (if (<= k 1)
    (map vector coll)
    (reduce (fn [i x] (concat i (map #(into [(first x)] %) (rest x))))
	    []
	    (map
	     #(into [(first %)] (choose-k % (dec k)))
	     (for [i (range (count coll))] (drop i coll))))))

(defn in? [coll x]
  (if (empty? coll)
    false
    (if (= (first coll) x)
      true
      (in? (rest coll) x))))

(defn build-item-options [item weights gems]
  ; only interested in the stats that are listed in the weights
  (let [base-stats (reduce (fn [i k]
			     (assoc i
			       k
			       (get item k 0)))
			   {} (flatten weights))
	gem-options (if (contains? item :sockets) ; TODO meta, prismatic
		      (let [no-gems (count (item :sockets))
			    colours (sort (map #(keyword (.toLowerCase %)) (item :sockets)))
			    combinations (choose-k gems no-gems)
			    ]
			(map (fn [gems]
			       (let [c (sort (map #(get % :colour) gems))
				     s (reduce combine-stats (map #(dissoc % :colour) gems))]
				 {:gems gems
				  :stats
				  (if (and (= c colours) (contains? item :socketBonus))
					; socket bonus matched
				    (combine-stats s (item :socketBonus))
				    s)})) combinations))
		      [{:gems [] :stats base-stats}])
	reforge-options
	(let [stats-to-reforge (filter #(in? (flatten weights) %) reforgable-stats)
	      reforgable-item-stats (filter #(in? stats-to-reforge %) (keys item))
	      options (remove nil?
			      (apply concat
				     (for [x reforgable-item-stats]
				       (for [y stats-to-reforge] 
					 (if (or (= x y) 
						 (in? reforgable-item-stats y))
					   nil (vector x y))))))]
	  (for [x options]
	    (let [to1 (last x)
		  from1 (first x)
		  value (get item (first x))
		  value (int (* 0.4 value))]
	    {:reforge {:from from1 :to to1}
	     :stats {from1 (- value)
		     to1 value}})))
	combined-options
	(apply concat
	       (for [g gem-options]
		 (for [r reforge-options]
		   {:id (item :id)
		    :gems (g :gems)
		    :reforge (r :reforge)
		    :stats (reduce combine-stats base-stats [(g :stats) (r :stats)])})))
	]
   (println (count combined-options))
   combined-options)
)

(defn add-option [options option]
  (print ".")
  (assoc options
    :items (assoc (options :items)
	     (option :slot)
	     {:id (option :id)
	      :gems (option :gems)
	      :reforge (option :reforge)})
    :stats (combine-stats (options :stats)
			  (option :stats))))
  

(defn calculate-option-leaves [options]
  "pass in all the slot options and recursivly add them together"
  (if (empty? (rest options))
    (map #(add-option {:items {} :stats {}} %) (val (first options)))
    (apply concat
    (for [i (val (first options))]
      (for [j (calculate-option-leaves (rest options))]
	(add-option i j))))))

(defn brute-force-all-options [data weights gems]
  "brute force options creater. will run forever (or until you run out of memory)"
   (let [all-options
	 (reduce (fn [m e]
		   (assoc m
		     (key e)
		     (vec 
		      (apply 
		       concat
		       (for [i (val e)]
			 (let [o (build-item-options i weights gems)]
			   (map #(assoc %
				   :slot (key e)) o)))))))
		 {}
		 data)]
     (println "generating"
	      (reduce #(* %1 (count %2)) 1 (remove #(= (count %) 0) (vals all-options)))
	      "options")
     (calculate-option-leaves (seq all-options))))

(defn test-stuff [options]
  "simplistic development function for the calculate-option-leaves function"
  (if (empty? (rest options))
    (map vector (val (first options)))
    (apply concat
    (for [i (val (first options))]
      (for [j (test-stuff (rest options))]
	(cons i j))))))

(defn run-test-stuff []
  (test-stuff (seq {:a [1 2 3 4]
		    :b [:a :b :c :d]
		    :c ['e 'f 'g 'h]
		    :d ["h" "i" "j" "k"]
		    })))

(defn run-test []
  (build-all-options resolved-test-data stat-weights-enhance enhance-gems))