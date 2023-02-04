(ns thats-life.logic)

(def init-players
  {:players []})

(def fields
  (concat
   (range -1 -9 -1)
   (repeat 6 0)
   (range 1 9)
   (range -1 -11 -1)))

(def init-path
  (vec fields))

(defn is-valid? [game-state]
  (contains? game-state :players))

(def guard -1)
(def is-guard? (partial = guard))

(defn roll-dice
  (inc (rand-int 6)))

(defn initial-pawns [num-of-players]
  (nth [3 3 3 3 2 2] (- num-of-players 2)))

;;maybe it can be more meaningful
(defn setup-fields [path]
  (mapv
    #(vec (remove nil? (conj %1 %2)))
   (repeat (count path) nil)
   (concat 
    (repeat 8 nil)
    (repeat 8 guard)
    (repeat 16 nil))))

(defn join-game [game-state player-name]
  (update game-state :players #(conj % player-name)))

(defn start-game [game-state]
  (let [num-of-players (count (get game-state :players))]
    (-> game-state
        (assoc :start (vec (mapcat #(repeat (initial-pawns num-of-players) %) (range num-of-players))))
        (assoc :path init-path)
        (assoc :dice (roll-dice))
        (assoc :idx (vec (map-indexed (fn [idx] idx) init-path)))
        (assoc :fields (setup-fields init-path))
        (assoc :collect (vec (repeat num-of-players [])))
        (assoc :up (rand-int num-of-players)))))

(defn restart [game-state]
  (start {:players (get game-state :players)}))

(defn calculate-score [collect]
 (let [pos (filter pos? collect)
       neg (filter neg? collect)
       zero (filter zero? collect)]
    (reduce + 0
      (concat pos 
        (map * 
           (sort (filter neg? collect))
           (concat (repeat (count zero) -1) (repeat 1)))))))

(defn scores [game-state]
  (mapv calculate-score (get game-state :collect)))

(defn moved-pawns [game-state]
  (remove is-guard? (apply concat (get game-state :pawns))))

(defn unmoved-pawns [game-state]
  (get game-state :start))

(defn move [game-state from pawn])

(defn pawns-in-play [game-state]
   (concat 
     (unmoved-pawns state)
     (moved-pawns game-state)))

(defn card-kind [n]
  (cond
    (pos? n) "potion"
    (neg? n) "toxin"
    (zero? n) "book"))

(def robots 
  [(partial re-find (re-pattern "(^Robot-.+)")) random-move])

(defn )