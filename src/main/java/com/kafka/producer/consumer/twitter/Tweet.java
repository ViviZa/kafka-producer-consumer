package com.kafka.producer.consumer.twitter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tweet {
    private String id_str;
    private String created_at;
    private String consumed_at;

    public String getId() {
        return id_str;
    }

    public void setId(String id_str) {
        this.id_str = id_str;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public String getConsumed_at() {
        return consumed_at;
    }

    public void setConsumed_at(String consumed_at) {
        this.consumed_at = consumed_at;
    }
}
