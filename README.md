# Question Answering System-POS tagger
Analyse dataset
get each record from dataset
split it using Json into record
when we have record, take value of SPARQL query
count how many DBpedia property are in SPARQL query 
if we have one DBpedia property in SPARQL query, store it and process it and skip the SPARQL query when we have more than one property
if we have one property then take value of correct question tag usind stanford tagger
what are the verbs in NLQ and map to property in SPARQL query
