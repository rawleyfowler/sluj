;; Copyright Rawley Fowler & Contributors, under the MIT license.
;; THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT 
;; NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
;; IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
;; WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
;; OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
;; https://github.com/rawleyfowler/sluj
(ns sluj.core
  (:require [sluj.charmap :as c]
            [clojure.string :as string]
            [clojure.walk :as w]))

(defn- replace-characters
  "Replaces any characters in a string that are mapped in the given charmap"
  [s charmap]
  (string/replace s (re-pattern (string/join "|" (keys charmap))) charmap))

(defn sluj
  "'Slugifies' a given string s, to make it usable in URI's. Specifically UTF-16 strings"
  [s & {:keys [locale separator casing]
        :or {locale "none" separator "-" casing "lower"}
        :as opts}]
  (let [spec-opts [:locale :remove :separator :casing]
        casing-fn (if (= (keyword casing) :lower)
                    string/lower-case
                    string/upper-case)
        charmap (merge c/charmap
                       (w/stringify-keys (apply dissoc (get opts :charmap opts) spec-opts))
                       ((keyword locale) c/locales))]
    (-> s
        (string/replace #"[:/?#\[\]@!$&'()*+,;=]" "") ; Remove any reserved RFC 3986 characters
        (replace-characters charmap) ; Replace the characters with the charmap characters
        (->>
         (re-seq #"\w+") ; Separate the words into a list
         (string/join separator)) ; Join the words back with the separator
        casing-fn))) ; Apply the specified casing
