package com.labawsrh.aws.introscreen;

public class Model {

    private String data;
    private String desk;
    private String time;

    public Model() {
    }

    public Model(String data, String desk, String time) {
        this.data = data;
        this.desk = desk;
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
