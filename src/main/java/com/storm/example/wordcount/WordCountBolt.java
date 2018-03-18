package com.storm.example.wordcount;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WordCountBolt extends BaseBasicBolt {
    Map<String, Integer> wordCount;
    private String outputfile;

    public void prepare(Map stormConf, TopologyContext context) {
        this.outputfile = stormConf.get("outputfile").toString();
        this.wordCount = new HashMap<String, Integer>();
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        String s = input.getString(0);

        if (wordCount.containsKey(s)) {
            wordCount.put(s, wordCount.get(s) + 1);
        } else {
            wordCount.put(s, 1);
        }

        collector.emit(new Values(input.getString(0) + " After Bolt"));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public void cleanup() {
        try {
            PrintWriter printWriter = new PrintWriter(outputfile, "UTF-8");
            for (Map.Entry<String, Integer> instance : wordCount.entrySet()) {
                printWriter.println(instance.getKey() + ":" + instance.getValue());
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
