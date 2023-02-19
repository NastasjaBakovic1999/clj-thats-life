(ns thatslife.test-runner
  (:require [cljs.test :refer-macros [run-tests]]
            [thatslife.logic_test]))

(enable-console-print!)

(defn run-all-tests
  []
  (run-tests 'thatslife.logic_test))