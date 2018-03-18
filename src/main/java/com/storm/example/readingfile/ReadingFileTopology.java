package com.storm.example.readingfile;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class ReadingFileTopology {

    private static final String READ_FILE_SPOUT = "ReadFileSpout";
    private static final String PROCESS_LINE_BOLT = "ProcessLineBolt";

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(READ_FILE_SPOUT, new ReadFileSpout());


        topologyBuilder.setBolt(PROCESS_LINE_BOLT, new ProcessLineBolt()).shuffleGrouping(READ_FILE_SPOUT);

        //        topologyBuilder.setBolt(PROCESS_LINE_BOLT, new ProcessLineBolt(), 3).fieldsGrouping(READ_FILE_SPOUT, new Fields("fieldName"));

        //For performing different operations on same data.
        //        topologyBuilder.setBolt(PROCESS_LINE_BOLT, new ProcessLineBolt(), 3).allGrouping(READ_FILE_SPOUT);

        //For deciding which bolt picks which task, use custom grouping
        //topologyBuilder.setBolt(PROCESS_LINE_BOLT, new ProcessLineBolt(), 2).customGrouping(READ_FILE_SPOUT, new MyCustomGrouping());

        //For Direct grouping
        //        topologyBuilder.setSpout(READ_FILE_SPOUT, new DirectGrouping());
        //        topologyBuilder.setBolt(PROCESS_LINE_BOLT, new ProcessLineBolt()).directGrouping(READ_FILE_SPOUT);

        Config config = new Config();
        config.setDebug(true);
        config.put("fileToRead", "/home/jamal/all_projects/research/RESEARCH_PROJECTS/learningstorm/eventHack.csv");
        config.put("directoryToWrite", "/home/jamal/all_projects/research/RESEARCH_PROJECTS/learningstorm/");

        LocalCluster localCluster = new LocalCluster();

        try {
            localCluster.submitTopology("Reading-File-Topology", config, topologyBuilder.createTopology());
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            localCluster.shutdown();
        }

    }
}
