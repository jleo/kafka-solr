package com.brandtology.kafka;


import com.brandtology.Config
import com.jointhegrid.ironcount.manager.MessageHandler
import com.jointhegrid.ironcount.manager.WorkerThread
import com.jointhegrid.ironcount.manager.Workload
import groovy.json.JsonSlurper
import kafka.message.Message
import kafka.message.MessageAndMetadata
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.SolrInputDocument

import java.nio.ByteBuffer
import org.json.simple.JSONObject

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-10-16
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
class SolrConsumer implements MessageHandler {
    DefaultHttpClient httpClient
    def collectionName
    SolrServer server

    public SolrConsumer() {
        httpClient = new DefaultHttpClient()
        collectionName = Config.config.solr.collection

        try {
            def baseURL = Config.config.solr.baseURL + "/$collectionName"
            server = new HttpSolrServer(baseURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void setWorkload(Workload w) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleMessage(MessageAndMetadata<Message> m) {
        String message = this.getMessage(m.message())
//        HttpPost post = new HttpPost("http://localhost:8983/solr/$collectionName/update/json?commit=true")
//        post.addHeader("content-type", "application/json");
//
//        StringEntity params =new StringEntity(message,"UTF-8");
//        post.setEntity(params);
//
//        HttpResponse response = httpClient.execute(post);
//        HttpEntity entity = response.getEntity();
//        if(!entity.getContent().text.contains("\"status\":0"))
//            throw new RuntimeException("solr indexing failed")
//        else
//            println "document indexed"
        try {
            SolrInputDocument inputDocument = new SolrInputDocument();
            def json = new JsonSlurper().parseText(message)

            ['interaction', 'klout', 'language', 'salience', 'twitter'].each {fieldName ->
                def jsonObject = new JSONObject()
                def fieldJson = json."$fieldName"
                if (fieldJson) {
                    jsonObject.putAll(fieldJson)
                    inputDocument.addField(fieldName, jsonObject.toString())
                }
            }

            server.add(inputDocument)
            server.commit()
        } catch (e) {
            e.printStackTrace()
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void setWorkerThread(WorkerThread wt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMessage(Message message) {
        ByteBuffer buffer = message.payload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }
}
