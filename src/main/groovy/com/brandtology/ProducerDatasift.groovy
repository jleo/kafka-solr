package com.brandtology

import kafka.javaapi.producer.Producer
import kafka.javaapi.producer.ProducerData
import kafka.producer.ProducerConfig

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-10-16
 * Time: 下午3:12
 * To change this template use File | Settings | File Templates.
 */
class ProducerDatasift {
    public static void main(String[] args) {
        String host = Config.config.zk.host
        String topic = Config.config.kafka.topic
        def props = new Properties();
        props."zk.connect" = host;
        props."serializer.class" = "kafka.serializer.StringEncoder";

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        String userDir = System.getProperty("user.dir")
        def file = new File(userDir, "datasift_file_INTL_20120724_145001_done.json")
        file.eachLine {
            ProducerData<String, String> data = new ProducerData<String, String>(topic, it);
            producer.send(data);
        }

    }
}
