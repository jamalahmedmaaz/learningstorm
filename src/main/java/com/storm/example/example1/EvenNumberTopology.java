package com.storm.example.example1;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class EvenNumberTopology {

    private static final String MY_EVEN_NUMBER_SPOUT = "My-Even-Number-Spout";
    private static final String MY_EVEN_NUMBER_BOLT = "My-Even-Number-Bolt";

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(MY_EVEN_NUMBER_SPOUT, new MyEvenNumberSpout());
        topologyBuilder.setBolt(MY_EVEN_NUMBER_BOLT, new MyEvenNumberBolt()).shuffleGrouping(MY_EVEN_NUMBER_SPOUT);

        Config config = new Config();
        config.setDebug(true);


        LocalCluster localCluster = new LocalCluster();

        try {
            localCluster.submitTopology("Even-Number-Topology", config, topologyBuilder.createTopology());

            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            localCluster.shutdown();
        }

    }
}
