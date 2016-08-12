(ns crucible.aws.lambda
  (:require [crucible.resources :refer [resource-factory spec-or-ref]]
            [clojure.spec :as s]))

(s/def ::subnet-id (spec-or-ref string?))

(s/def ::security-group-ids (spec-or-ref string?))

(s/def ::vpc-config (s/keys :req [::security-group-ids]
                            :opt [::subnet-ids]))

(s/def ::timeout (spec-or-ref (s/and pos-int? #(<= % (* 5 60)))))

(s/def ::runtime #{"nodejs" "nodejs4.3" "java8" "python2.7"})

(s/def ::role (spec-or-ref string?))

(s/def ::memory-size (spec-or-ref (s/and #(-> % (mod 64) (= 0)) #(<= 128 % 1536))))

(s/def ::handler (spec-or-ref string?))

(s/def ::function-name (spec-or-ref string?))

(s/def ::description (spec-or-ref string?))

(s/def ::s3-bucket (spec-or-ref string?))
(s/def ::s3-key (spec-or-ref string?))
(s/def ::s3-object-version (spec-or-ref string?))
(s/def ::zip-file (spec-or-ref string?))

(s/def ::code (s/keys ::opt [::s3-bucket
                             ::s3-key
                             ::s3-object-version
                             ::zip-file]))

(s/def ::function (s/keys :req [::code
                                ::handler
                                ::role
                                ::runtime]
                          :opt [::function-name
                                ::description
                                ::memory-size
                                ::runtime
                                ::vpc-config]))

(def function (resource-factory "AWS::Lambda::Function" ::function))

(s/def ::batch-size (spec-or-ref (s/and pos-int? #(< % 10000))))

(s/def ::enabled (spec-or-ref boolean?))

(s/def ::event-source-arn (spec-or-ref string?))

(s/def ::starting-postition (spec-or-ref #{"TRIM_HORIZON" "LATEST"}))

(s/def ::event-source-mapping (s/keys :req [::event-source-arn
                                            ::function-name
                                            ::starting-position]
                                      :opt [::batch-size
                                            ::enabled]))

(def event-source-mapping (resource-factory "AWS::Lambda::EventSourceMapping" ::event-source-mapping))

