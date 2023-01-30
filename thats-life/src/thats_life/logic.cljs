(ns thats-life.logic)

(def init-players
  {:players []})

(defn is-valid? [state]
  (contains? state :players))