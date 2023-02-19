(ns thatslife.logic)

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

(def roll-dice
  #(inc (rand-int 6)))

(defn initial-pawns [num-of-players]
  (nth [3 3 3 2 2] (- num-of-players 2)))

(defn setup-pawns [path]
  (mapv
   #(vec (remove nil? (conj %1 %2)))
   (repeat (count path) nil)
   (concat
    (repeat 8 nil)
    (repeat 8 guard)
    (repeat nil))))

(defn join-game [game-state player-name]
  (update game-state :players #(conj % player-name)))

(defn calculate-score [collect]
  (let [pos (filter pos? collect)
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
  (get game-state :start-pawns))

(defn game-started? [game-state]
  (contains? game-state :start-pawns))

(defn pawns-in-play [game-state]
  (concat
   (unmoved-pawns game-state)
   (moved-pawns game-state)))

(defn game-over? [game-state]
  (and (game-started? game-state) (empty? (pawns-in-play game-state))))

(defn start-game [game-state]
  (let [num-of-players (count (get game-state :players))]
    (-> game-state
        (assoc :start-pawns (vec (mapcat #(repeat (initial-pawns num-of-players) %) (range num-of-players))))
        (assoc :path init-path)
        (assoc :dice (roll-dice))
        (assoc :ids (vec (map-indexed (fn [idx] idx) init-path)))
        (assoc :pawns (setup-pawns init-path))
        (assoc :collect (vec (repeat num-of-players [])))
        (assoc :up (rand-int num-of-players)))))

(defn playing [game-state]
  (-> game-state
      pawns-in-play
      distinct
      sort))

(defn up [game-state]
  (get game-state :up))

(defn remove-once [predicate game-state]
  (let [[x y] (split-with (complement predicate) game-state)]
    (concat x (rest y))))

(defn unconj [game-state player]
  (vec (remove-once (partial = player) game-state)))

(defn order [game-state]
  (->>
   (map vector (scores game-state) (get game-state :players))
   (sort-by first)
   reverse
   vec))

(defn trim [game-state]
  (if (empty? (get game-state :start-pawns))
    (let [n (count (take-while empty? (map #(remove is-guard? %) (get game-state :pawns))))]
      (-> game-state
          (update :ids #(vec (drop n %)))
          (update :path #(vec (drop n %)))
          (update :pawns #(vec (drop n %)))))
    game-state))

(defn next-up [game-state]
  (let [player (get game-state :up)
        players (set (playing game-state))
        turns (take 6 (filter players (cycle (range (count (get game-state :players))))))]
    (if (game-over? game-state)
      (-> game-state
          (assoc :order (order game-state))
          (dissoc :up :dice))
      (-> game-state
          (assoc :up (second (concat (drop-while #(not= player %) turns) turns)))
          (assoc :dice (roll-dice))))))

(defn insert [game-state]
  (let [player (up game-state)]
    (if ((set (unmoved-pawns game-state)) player)
      (-> game-state
          (update :start-pawns #(unconj % player))
          (update-in [:pawns (- (get game-state :dice) 1)] #(conj % player))
          trim
          next-up)
      game-state)))

(defn drop-at [from x]
  (let [[y z] (split-at from x)]
    (vec (concat y (rest z)))))

(defn collect [game-state from]
  (let [player (up game-state)
        take #(drop-at from %)]
    (if (empty? (get-in game-state [:pawns from]))
      (-> game-state
          (update-in [:collect player] #(conj % (nth (get game-state :path) from)))
          (update :ids take)
          (update :path take)
          (update :pawns take))
      game-state)))

(defn has? [player pawns]
  (some #{player} pawns))

(defn is-exited? [game-state to]
  (not (contains? (get game-state :path) to)))

(defn take-pawn [game-state from pawn]
  (update-in game-state [:pawns from] #(unconj % pawn)))

(defn put-pawn [game-state to pawn]
  (if (is-exited? game-state to)
    game-state
    (update-in game-state [:pawns to] #(conj % pawn))))

(defn move-pawn [game-state from to pawn]
  (-> game-state
      (take-pawn from pawn)
      (put-pawn to pawn)))

(defn continue [game-state from pawn]
  (let [player (up game-state)
        pawns (get-in game-state [:pawns from])
        to (+ from (get game-state :dice))]
    (if (and (has? player pawns) (or (= player pawn) (is-guard? pawn)))
      (-> game-state
          (move-pawn from to pawn)
          (collect from)
          trim
          next-up)
      game-state)))

(defn move [game-state from pawn]
  (if (= -1 from)
    (insert game-state)
    (continue game-state from pawn)))

(defn card-kind [n]
  (cond
    (pos? n) "potion"
    (neg? n) "toxin"
    (zero? n) "cure"))

(defn use-antitoxin
  ([collect antitoxins]
   (let [worst-toxin (first (sort (filter neg? collect)))]
     (if (and worst-toxin (pos? antitoxins))
       (use-antitoxin (assoc collect (.indexOf collect worst-toxin) (* -1 worst-toxin)) (dec antitoxins))
       collect)))
  ([collect]
   (use-antitoxin collect (count (filter #(= 0 %) collect)))))

(defn winners [game-state]
  (let [best-score (apply max (sort (scores game-state)))]
    (remove nil?
            (map-indexed
             (fn [idx score]
               (when (= best-score score) idx))
             (scores game-state)))))

(defn moves
  ([game-state pawn]
   (filter #(= pawn (second %)) (moves game-state)))
  ([game-state]
   (let [player (up game-state)]
     (distinct
      (concat
       (->>
        (get game-state :start-pawns)
        (filter (partial = player))
        (map (partial vector -1)))
       (->>
        (get game-state :pawns)
        (map-indexed
         (fn [idx pawns]
           (map
            #(vector idx %)
            (filter
             (fn [pawn]
               (and (has? player pawns) (or (= player pawn) (is-guard? pawn))))
             pawns))))
        (apply concat)))))))

(defn random-move [game-state]
  (let [possible-moves (vec (moves game-state))
        picked-move (rand-int (count possible-moves))]
    (if (game-over? game-state)
      game-state
      (apply move game-state (nth possible-moves picked-move)))))

(def robots
  [[(partial re-find (re-pattern "(^Robot-.+)")) random-move]])

(defn activated-robot [game-state]
  (when (and (game-started? game-state) (not (game-over? game-state)))
    (let [{:keys [players up]} game-state
          robot-name (nth players up)]
      (->> robots
           (filter
            (fn [[match algorithm]]
              (match robot-name)))
           (map
            (fn [[_ algorithm]]
              algorithm))
           first))))

