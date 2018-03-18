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
import java.util.List;
import java.util.Map;

public class DirectGrouping extends BaseRichSpout {


    private FileReader fileReader;
    private SpoutOutputCollector collector;
    private boolean completed;
    private BufferedReader reader;
    private String string;
    private List<Integer> boltIds;


    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

        try {
            this.fileReader = new FileReader(conf.get("fileToRead").toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.collector = collector;
        this.reader = new BufferedReader(fileReader);
        this.boltIds = context.getComponentTasks("ProcessLineBolt");
    }

    public void nextTuple() {

        if (!completed) {
            try {
                this.string = reader.readLine();
                if (this.string != null) {
                    //Direct emit
                    Object bucketId = new Object();
                    this.collector.emitDirect(boltIds.get(getBoltId(bucketId)), new Values(string));
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

    private int getBoltId(Object bucketId) {
        //intbucket % boltids.size();
        return 0;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
//        declarer.declare(new Fields("time", "userid", "event", "eventType", "sku", "price", "searchString", "orderId"));
    }
}
