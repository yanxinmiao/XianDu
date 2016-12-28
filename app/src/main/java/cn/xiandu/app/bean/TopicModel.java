package cn.xiandu.app.bean;

/**
 * Created by jiang on 2016/12/23.
 */

public class TopicModel {
    private int topicId;
    private String name;


    public TopicModel(int topic, String name) {
        this.topicId = topic;
        this.name = name;
    }

    public int getTopic() {
        return topicId;
    }

    public void setTopic(int topic) {
        this.topicId = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "topic=" + topicId +
                ", name='" + name + '\'' +
                '}';
    }
}
