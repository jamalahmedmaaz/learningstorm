package com.storm.example.readingfile;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ReadFileSpout extends BaseRichSpout {

    private FileReader fileReader;
    private SpoutOutputCollector collector;
    private boolean completed;
    private BufferedReader reader;
    private String string;


    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

        try {
            this.fileReader = new FileReader(conf.get("fileToRead").toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.collector = collector;
        this.reader = new BufferedReader(fileReader);
    }

    public void nextTuple() {

        if (!completed) {
            try {
                this.string = reader.readLine();
                if (this.string != null) {
                    this.collector.emit(new Values(string));
//                    this.collector.emit(new Values(string.split(",")));
                } else {
                    completed = true;
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
//        declarer.declare(new Fields("time", "userid", "event", "eventType", "sku", "price", "searchString", "orderId"));
    }
}
