package com.brandtology

import com.jointhegrid.ironcount.manager.WorkloadManager

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-10-16
 * Time: 下午4:20
 * To change this template use File | Settings | File Templates.
 */
class IronCountLauncher {
    public static void main(String[] args) {
        String host = Config.config.zk.host
        String threadNumber = Config.config.threadNumber

        def props = new Properties();
        props."${WorkloadManager.ZK_SERVER_LIST}" = host;
        props."${WorkloadManager.IC_THREAD_POOL_SIZE}" = threadNumber;

        def workloadManager = new WorkloadManager(props);
        workloadManager.init();
    }
}
