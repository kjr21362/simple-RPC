package com.kjr21362.register;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

@Slf4j
public class ZookeeperRegistry implements ServiceRegistry{

    private static CuratorFramework client;
    private static final String ROOT_PATH = "/rpc";

    public static CuratorFramework getClient() {
        if(client != null && client.getState() == CuratorFrameworkState.STARTED){
            return client;
        }
        String connectString = "localhost:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();
        try {
            if(!client.blockUntilConnected(10, TimeUnit.SECONDS)){
                throw new RuntimeException("Time out connecting to Zookeeper: " + connectString);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return client;
    }

    @Override
    public void registerService(String remoteServiceName, InetSocketAddress address) {
        String servicePath = ROOT_PATH + "/" + remoteServiceName + address.toString();
        log.info("Register service path: {}", servicePath);
        CuratorFramework client = getClient();

        try {
            if(client.checkExists().forPath(servicePath) != null){
                log.info("Path {} already exists.", servicePath);
                return;
            }

            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);

            log.info("Path {} created.", servicePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InetSocketAddress lookUpService(String remoteServiceName) {
        CuratorFramework client = ZookeeperRegistry.getClient();
        String servicePath = ROOT_PATH + "/" + remoteServiceName;
        try {
            log.debug("Lookup servicePath: " + servicePath);
            List<String> serviceAddresses = client.getChildren().forPath(servicePath);

            if(serviceAddresses.isEmpty()){
                throw new RuntimeException("Address for service " + remoteServiceName + " is empty!");
            }

            // load balancer here
            String selectedAddr = serviceAddresses.getFirst();
            log.debug("selectedAddr: " + selectedAddr);
            String[] addrURL = selectedAddr.split(":");
            return new InetSocketAddress(addrURL[0], Integer.parseInt(addrURL[1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
