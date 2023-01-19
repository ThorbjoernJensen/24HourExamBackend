package dtos;

import entities.Conference;
import entities.Talk;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ConferenceDTO {

    private Integer id;
    private String name;
    private String location;
    private Integer capacity;
    private String date;
    private String time;

    private Set<TalkDTO> talks;


    public ConferenceDTO(Conference conference) {
        if (conference.getId() != null) {
            this.id = conference.getId();
        }
        this.name = conference.getName();
        this.location = conference.getLocation();
        this.capacity = conference.getCapacity();
        this.date = conference.getDate();
        this.time = conference.getTime();
//        talks må jf. domænemodel ikke være null...
        if(conference.getTalks() != null){
        this.talks = TalkDTO.makeDTOSet(conference.getTalks());}
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public static Set<ConferenceDTO> makeDTOSet(List<Conference> conferences){
        Set<ConferenceDTO> conferenceDTOSet= new HashSet<>();
        conferences.forEach(conference -> conferenceDTOSet.add(new ConferenceDTO(conference)));
        return conferenceDTOSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceDTO that = (ConferenceDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(capacity, that.capacity) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, capacity, date, time);
    }
}
