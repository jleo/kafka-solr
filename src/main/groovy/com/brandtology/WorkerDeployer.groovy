package com.brandtology

import com.jointhegrid.ironcount.manager.Workload
import org.apache.zookeeper.ZooKeeper
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.type.TypeFactory
import org.codehaus.jackson.type.JavaType
import org.apache.zookeeper.data.Stat
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.WatchedEvent

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-10-16
 * Time: 下午4:30
 * To change this template use File | Settings | File Templates.
 */
class WorkerDeployer implements Watcher{
    public static void main(String[] args) {
        Watcher watcher = new WorkerDeployer()
        def workersInJSON = Config.config.workers

        String host = Config.config.zk.host

        workersInJSON.each {
            ObjectMapper m = new ObjectMapper();
            JavaType t = TypeFactory.type(Workload.class);
            Workload w = null;
            try {
                w = (Workload) m.readValue(it, t);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            if (w != null) {
                try {
                    def zk = new ZooKeeper(host, 1000000, watcher);

                    if (zk.exists("/ironcount", true) == null) {
                        zk.create("/ironcount", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    if (zk.exists("/ironcount/workers", false) == null) {
                        zk.create("/ironcount/workers", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    if (zk.exists("/ironcount/workloads", watcher) == null) {
                        zk.create("/ironcount/workloads", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }

                    try {
                        Stat s = zk.exists("/ironcount/workloads/" + w.name, false);
                        if (s != null) {
                            zk.setData("/ironcount/workloads/" + w.name, it.bytes, s.getVersion());
                        } else {
                            zk.create("/ironcount/workloads/" + w.name, it.bytes,
                                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                        }
                    } catch (KeeperException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    void process(WatchedEvent event) {
        println event
    }
}
