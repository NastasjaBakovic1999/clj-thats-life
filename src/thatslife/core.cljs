(ns thatslife.core
    (:require
     [reagent.core :as reagent :refer [atom]]
     [reagent.dom :as rd]
     [clojure.string :as string]
     [thatslife.logic :as logic]))

(defonce game-state
  (reagent/atom logic/init-players :validator logic/is-valid?))

(def pawn-imgs ["images/guard.png"
                "images/pawn-blue.png"
                "images/pawn-green.png"
                "images/pawn-orange.png"
                "images/pawn-black.png"
                "images/pawn-red.png"
                "images/pawn-yellow.png"])

(defn pawn-render [n]
  [:img.pawn {:src (get pawn-imgs (inc n))}])

(defn pawns-render [pawns up from robot]
  (let [guards-move (get (set pawns) up)]
    [:ol (map
          (fn [n]
            (let [mobile (and (not robot) (or (= n up) (and guards-move (logic/is-guard? n))))]
              [(symbol (str "li" (when mobile ".mobile")))
               (when mobile
                 {:on-click
                  (fn [e]
                    (swap! game-state #(logic/move % from n)))})
               [pawn-render n]]))
          pawns)]))

(defn dice-render [dice-num]
  (when dice-num [:img.dice {:src (str "images/dice-" dice-num ".png")}]))

(defn card-render [what value antitoxin]
  (let [classes (filter some? [what (when antitoxin "anti") (logic/card-kind value)])]
    [(symbol (str "div.card." (clojure.string/join "-" classes)))
     [:img.card {:src (str "images/" (clojure.string/join "." classes) ".png")}]
     [:div.value (or (when value (if antitoxin (* -1 value) value)) "")]]))

(defn space-render [what idx key value pawns up robot]
  [:div.space ^{:key key}
   {:data-key key :data-worth value}
   [:div.pawns (when (> (count pawns)  8) {:class "crowd"})  [pawns-render pawns up idx robot]]
   [card-render what value]])

(defn players-render [players up dice collect]
  [:table.players>tbody
   (map-indexed
    (fn [idx player-name]
      (let [coll  (nth collect idx)
            antitoxin (logic/use-antitoxin coll)]
        [:tr
         [:td.player-name player-name]
         [:td.pawn (pawn-render idx)]
         [:td.dice (when (= idx up) (dice-render dice))]
         [:td.scores (logic/calculate-score (get collect idx))]
         [:td.collect
          (map-indexed
           (fn [idx value]
             (card-render nil value (not= (nth antitoxin idx) value)))
           coll)]]))
    players)])

(defn player-entry-render [num-of-players]
  [:div.entry
   (when (< num-of-players 6)
     [:div
      [:input {:type "text" :placeholder "Enter player name"}]
      [:button
       {:on-click
        (fn [e]
          (let [parent-node (.-target.parentNode e)
                input (.querySelector parent-node "input")
                player-name (.-value input)]
            (do
              (aset input "value" "")
              (swap! game-state #(logic/join-game % player-name))
              (.focus input))))}
       "Entry"]])
[:button
 {:style {:visibility (if (> num-of-players 1) "visible" "hidden")}
  :on-click
  (fn [e]
    (swap! game-state logic/start-game))}
 "Start"]])

(defn game-render []
  (let [game-state @game-state
        robot   (logic/activated-robot game-state)
        {:keys [players start-pawns up dice path collect]} game-state]
    [:div
     [:h1 "That's life"]
     (when (and (not (logic/game-over? game-state)) (not up)) (player-entry-render (count players)))
     (when (logic/game-over? game-state)
       [:div.winners
        [:h2 (if (= (count (logic/winners game-state)) 1) "Winner is:" "Winners are:")]
        [:ul
         (map
          #(vector :li.player
                   (pawn-render %)
                   (vector :div.player-name (nth players %)))
          (logic/winners game-state))]])
     (apply vector :div.path
            [space-render "start" -1 -1 nil start-pawns up robot]
            (concat
             (map-indexed
              (fn [idx]
                (vector space-render
                        nil
                        idx
                        (get-in game-state [:ids idx])
                        (get-in game-state [:path idx])
                        (get-in game-state [:pawns idx])
                        up
                        robot))
              path)
             [[space-render "stop" 99 "stop" robot]]))
     [:div.summary
      [players-render players up dice collect]]
     ]))


(defn sleep [func ms]
  (js/setTimeout func ms))

(add-watch game-state :robots
           (fn [key ref old-state new-state]
             (when-let [robot (logic/activated-robot new-state)]
               (sleep #(swap! game-state robot) 3000))))

;;The onbeforeunload event occurs when a document is about to be unloaded.
(aset js/window "onbeforeunload"
      (constantly "Do you really want to leave the game unfinished? Then you must lose!"))

(rd/render [game-render]
           (. js/document (getElementById "app")))

