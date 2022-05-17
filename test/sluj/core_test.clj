(ns sluj.core-test
  (:use clojure.test 
        sluj.core))

(deftest a-test
  (testing "Sluggify a given plain text string."
    (is (= (sluj "My name is mud") "my-name-is-mud"))
    (is (= (sluj "My name is definitley mud!") "my-name-is-definitley-mud"))))
