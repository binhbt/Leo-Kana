package com.leo.study.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by binhbt on 3/1/2017.
 */
public class SelectedTopicList implements Serializable{
    private List<Integer> selectedTopic = new ArrayList<>();

    public List<Integer> getSelectedTopic() {
        return selectedTopic;
    }

    public void setSelectedTopic(List<Integer> selectedTopic) {
        this.selectedTopic = selectedTopic;
    }

    public SelectedTopicList(List<Topic> selectedTopic) {
        for (Topic topic:selectedTopic){
            this.selectedTopic.add(topic.getId());
        }
    }
}
