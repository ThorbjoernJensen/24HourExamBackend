package entities;

import dtos.SpeakerDTO;
import dtos.TalkDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "talk")
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String topic;

    private Integer duration;

    //    TODO implement as 1 to many
    private String propsList;

    @ManyToOne
    Conference conference;

    @ManyToMany(mappedBy = "talks")
    Set<Speaker> speakers = new HashSet<>();

    public Talk() {
    }

    public Talk(Integer id, String topic, Integer duration, String propsList) {
        this.id = id;
        this.topic = topic;
        this.duration = duration;
        this.propsList = propsList;

    }

    public Talk(String topic, Integer duration, String propsList) {
        this.topic = topic;
        this.duration = duration;
        this.propsList = propsList;

    }


    public Talk(TalkDTO talkDTO) {
        this.topic = talkDTO.getTopic();
        this.duration = talkDTO.getDuration();
        this.propsList = talkDTO.getPropsList();

        if(talkDTO.getConference() != null){
        this.conference = new Conference(
                talkDTO.getConference().getId(),
                talkDTO.getConference().getName(),
                talkDTO.getConference().getLocation(),
                talkDTO.getConference().getCapacity(),
                talkDTO.getConference().getDate(),
                talkDTO.getConference().getTime()
        );
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPropsList() {
        return propsList;
    }

    public void setPropsList(String propsList) {
        this.propsList = propsList;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }

    public void addSpeaker(Speaker speaker) {
        speakers.add(speaker);
    }
}