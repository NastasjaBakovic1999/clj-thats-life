(ns thatslife.logic_test
  (:require [cljs.test :refer-macros [deftest is are testing run-tests]]
            [thatslife.logic :as logic]))

((deftest da-vidimo
      (testing "Context of the test assertions"
        (is (= (logic/initial-pawns 2) 3)))))
