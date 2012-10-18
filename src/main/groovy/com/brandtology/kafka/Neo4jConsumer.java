package com.brandtology.kafka;

import com.jointhegrid.ironcount.manager.MessageHandler;
import com.jointhegrid.ironcount.manager.WorkerThread;
import com.jointhegrid.ironcount.manager.Workload;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-10-16
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jConsumer implements MessageHandler {
    @Override
    public void setWorkload(Workload w) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleMessage(MessageAndMetadata<Message> m) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setWorkerThread(WorkerThread wt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
