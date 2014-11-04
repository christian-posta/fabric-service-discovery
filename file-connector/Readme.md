Can invoke with this command line:

Start it up with this (for standalone):
    
    mvn camel:run
    
Find a different console, and curl it out:

    curl  -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' -d '{"name":"ceposta","creditRating":"850","ssn":"123456789"}' http://localhost:9000/credit/customers/1
    
 sample json: 
 
    {
        "creditRating": "850",
        "name": "ceposta",
        "ssn": "123456789"
    }
    
Against the fabric deployment, you wanna use this (not the url difference):

    curl  -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' -d '{"name":"ceposta","creditRating":"850","ssn":"123456789"}' http://localhost:8182/cxf/rest/credit/customers/1
