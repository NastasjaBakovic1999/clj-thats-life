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
  (contains? state :players))

(def guard -1)
(def is-guard? (partial = guard))

(defn roll-dice
  (inc (rand-int 6)))

(defn initial-pawns [num-of-players]
  (nth [3 3 3 2 2] (- num-of-players 2)))

(defn setup-fields [path]
  (mapv
    #(vec (remove nil? (conj %1 %2)))
   (repeat (count path) nil)
   (concat 
    (repeat 8 nil)
    (repeat 8 guard)
    (repeat nil))))

(defn join-game [game-state player-name]
  (update state :players #(conj % name)))