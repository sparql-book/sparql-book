WITH <http://sparqlbook.jp/graph>
DELETE { ?old_iri a <http://dbpedia.org/ontology/City> . }
INSERT { ?new_iri a <http://dbpedia.org/ontology/City> . }
WHERE {
  ?old_iri a <http://dbpedia.org/ontology/City> .
  BIND(STR(?old_iri) AS ?old_iri_string)
  BIND("http://ja.dbpedia.org/resource/" AS ?old_prefix)
  BIND("http://sparqlbook.jp/city/" AS ?new_prefix)
  BIND(IRI(REPLACE(?old_iri_string, ?old_prefix, ?new_prefix)) AS ?new_iri)
}
