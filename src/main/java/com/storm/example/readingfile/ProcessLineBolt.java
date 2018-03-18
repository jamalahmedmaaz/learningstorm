package com.storm.example.readingfile;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ProcessLineBolt extends BaseBasicBolt {


    private PrintWriter writer;

    @Override
    public void prepare(Map config, TopologyContext topologyContext) {
        String directoryToWrite = config.get("directoryToWrite").toString();
        String fieldName = topologyContext.getThisTaskId() + "-" + topologyContext.getThisComponentId() + ".csv";
        try {
            this.writer = new PrintWriter(directoryToWrite + fieldName, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void execute(Tuple input, BasicOutputCollector collector) {

        writer.println(new Values(input.getString(0)));
        collector.emit(new Values(input.getString(0) + " After Bolt"));

        /*String event = input.getStringByField("event");
        String sku = input.getStringByField("sku");

        collector.emit(new Values(event,sku));*/
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//        declarer.declare(new Fields("event", "sku"));
        declarer.declare(new Fields("word"));
    }
}
