import groovy.json.JsonBuilder

zk {
    host = "localhost:2181"
}

kafka {
    topic = "datasift"
}

solr{
    baseURL = "http://localhost:8983/solr"
    collection = "c4"
}
threadNumber = 10

def neo4jWorker = new JsonBuilder()

//neo4jWorker {
//    name("neo4j-consumer")
//    topic("datasift")
//    consumerGroup("neo4j-group")
//    messageHandlerName("com.brandtology.kafka.Neo4jConsumer")
//    zkConnect("localhost:2181")
//    maxWorkers(1)
//    active(true)
//}

neo4jWorker {
    name("neo4j-consumer")
    topic("datasift")
    consumerGroup("neo4j-group")
    messageHandlerName("com.jointhegrid.ironcount.eventtofile.MessageToFileHandler")
    zkConnect("localhost:2181")
    maxWorkers(4)
    active(true)
}

def solrWorker = new JsonBuilder()

solrWorker {
    name("solr-consumer")
    topic("datasift")
    consumerGroup("solr-group")
    messageHandlerName("com.brandtology.kafka.SolrConsumer")
    zkConnect("localhost:2181")
    maxWorkers(4)
    active(true)
}

workers = [
        solrWorker.toString(),
        neo4jWorker.toString()
]