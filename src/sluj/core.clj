(ns sluj.core
  (:require [sluj.charmap :as c]
            [clojure.string :as string]
            [clojure.walk :as w]))

;; How about instead we setify the map characters, and then apply replacements to any non ascii chars. This will be way more performant.
(defn- replace-characters
  "Replaces any characters in a string that are mapped in the given charmap"
  [s charmap]
  (string/replace s (re-pattern (string/join "|" (keys charmap))) charmap))

(defn- extract-words
  "Extracts words from the string"
  [s]
  (re-seq #"\w+" s))

(defn- join-on-separator
  "Joins a given array with a given separator"
  [s separator]
  (string/join separator s))

(defn sluj
  "'Slugifies' a given string s, to make it usable in URI's. Specifically UTF-16 strings"
  [s & {:as opts}]
  (let [spec-opts '(:locale
                     :remove
                     :separator
                     :casing)
        locale (get opts :locale "none")
        casing (if (= (keyword (get opts :casing :lower)) :lower)
                 string/lower-case
                 string/upper-case)
        separator (get opts :separator "-") ;; Legal separators: Any ASCII char that we don't replace, down below.
        charmap (merge c/charmap 
                       (w/stringify-keys (apply dissoc (get opts :charmap opts) spec-opts))
                       ((keyword locale) c/locales))]
    (-> s
        (string/replace #"[:/?#\[\]@!$&'()*+,;=]" "") ;; Remove any reserved RFC 3986 characters
        (replace-characters charmap) ;; Replace the characters with the charmap characters
        (extract-words) ;; Extract the words to a list of words
        (join-on-separator separator) ;; Replace spaces with the separator
        casing))) ;; Apply the specified casing
