package com.storm.example.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class WordCountToplogy {

    public static void main(String[] args) {

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("wordCountSpout", new WordCountSpout());
        topologyBuilder.setBolt("lineSplitBolt", new LineSplitBolt(), 5).shuffleGrouping("wordCountSpout");
        topologyBuilder.setBolt("wordCountBolt", new WordCountBolt()).fieldsGrouping("lineSplitBolt", new Fields("word"));


        Config config = new Config();
        config.setDebug(true);
        config.put("inputfile", "/home/jamal/all_projects/research/RESEARCH_PROJECTS/learningstorm/eventHack.csv");
        config.put("outputfile", "/home/jamal/all_projects/research/RESEARCH_PROJECTS/learningstorm/eventHackWords.csv");

        LocalCluster localCluster = new LocalCluster();

        localCluster.submitTopology("WordCountTopology", config, topologyBuilder.createTopology());

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            localCluster.shutdown();
        }


    }
}
