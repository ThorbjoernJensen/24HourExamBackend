package entities;

import dtos.ConferenceDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "conference")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)

    private Integer id;

    private String name;
    private String location;
    private Integer capacity;

    //    TODO implement with date format
    private String date;
    private String time;

    @OneToMany(mappedBy = "conference")
    private Set<Talk> talks = new HashSet<>();

    public Conference() {
    }


    public Conference(Integer id, String name, String location, Integer capacity, String date, String time) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.date = date;
        this.time = time;
    }

    public Conference(String name, String location, Integer capacity, String date, String time) {

        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.date = date;
        this.time = time;
    }
    public Conference(ConferenceDTO conferenceDTO) {
        if(conferenceDTO.getId()!=null){
            this.id = conferenceDTO.getId();
        }
        this.name = conferenceDTO.getName();
        this.location = conferenceDTO.getLocation();
        this.capacity = conferenceDTO.getCapacity();
        this.date = conferenceDTO.getDate();
        this.time = conferenceDTO.getTime();
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<Talk> getTalks() {
        return talks;
    }

    public void addTalk(Talk talk) {
        this.talks.add(talk);
        talk.setConference(this);
    }

    public void removeTalk(Talk talk) {
        talk.setConference(null);
        this.talks.remove(talk);
    }
}