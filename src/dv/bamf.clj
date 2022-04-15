(ns dv.bamf
  (:require
    [clojure.string :as str]
    [reaver :refer [extract-from text attrs]]))

(defn extract-locations [source]
  (extract-from
    source
    ".c-locations__content > .js-locations-content > .c-teaser.c-teaser--row"
    [:title :address :geo]
    ".c-teaser__text.c-teaser__text--column > .c-teaser__heading > span:eq(0)" text
    ".c-teaser__text.c-teaser__text--column > .address span" text
    ".c-teaser.c-teaser--row" (fn [x] (let [attrs (attrs x)]
                                        {:latitude  (:data-geo-lat attrs)
                                         :longitude (:data-geo-long attrs)}))))

(defn to-csv [list]
  (str/join "," (map #(str "\"" % "\"") list)))

(def csv-header
  (to-csv ["Title" "Address" "Longitude" "Latitude"]))

(defn location-to-csv [location]
  (to-csv [(:title location)
           (str/join " " (:address location))
           (get-in location [:geo :longitude])
           (get-in location [:geo :latitude])]))

(defn -main []
  (let [html (slurp "https://www.bamf.de/EN/Behoerde/Aufbau/Standorte/standorte-node.html")
        source (reaver/parse html)
        locations (extract-locations source)

        results (str/join "\n" (concat [csv-header] (map location-to-csv locations)))]
    (println "locations: " locations)
    (spit "./bamf-locations.csv" results)))
