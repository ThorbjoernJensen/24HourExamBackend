package dtos;

import entities.Conference;
import entities.Speaker;
import entities.Talk;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TalkDTO {
    private Integer id;
    private String topic;
    private Integer duration;
    private String propsList;
    private ConferenceDTO conference;
    private Set<SpeakerDTO> speakers;

    public TalkDTO(Talk talk) {
        if (talk.getId() != null) {
            this.id = talk.getId();
        }
        this.topic = talk.getTopic();
        this.duration = talk.getDuration();
        this.propsList = talk.getPropsList();
        if (talk.getConference() != null) {
            this.conference = new ConferenceDTO(talk.getConference());
        }
//        if (!talk.getSpeakers().size()==0) {
//            this.speakers = new ConferenceDTO(talk.getConference());
//        }
    }

    public Integer getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getPropsList() {
        return propsList;
    }

    public ConferenceDTO getConference() {
        return conference;
    }

    public Set<SpeakerDTO> getSpeakers() {
        return speakers;
    }

    public static Set<TalkDTO> makeDTOSet(Set<Talk> talks) {
        Set<TalkDTO> talkDTOSet = new HashSet<>();
        talks.forEach(talk -> talkDTOSet.add(new TalkDTO(talk)));
        return talkDTOSet;
    }

    public static Set<TalkDTO> makeDTOSet(List<Talk> talks) {
        Set<TalkDTO> talkDTOSet = new HashSet<>();
        talks.forEach(talk -> talkDTOSet.add(new TalkDTO(talk)));
        return talkDTOSet;
    }



    public static class SpeakerDTO {
        private Integer id;
        private String name;
        private String profession;
        private String gender;

        private Set<TalkDTO> talks = new HashSet<>();

        public SpeakerDTO(Speaker speaker) {
            if (speaker.getId() != null) {
                this.id = id;
            }
            this.name = speaker.getName();
            this.profession = speaker.getProfession();
            this.gender = speaker.getGender();
            this.talks = TalkDTO.makeDTOSet(speaker.getTalks());
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getProfession() {
            return profession;
        }

        public String getGender() {
            return gender;
        }

        public Set<TalkDTO> getTalks() {
            return talks;
        }

        public static Set<SpeakerDTO> makeDTOSet(Set<Speaker> speakers) {
            Set<SpeakerDTO> speakerDTOSet = new HashSet<>();
            speakers.forEach(speaker -> speakerDTOSet.add(new SpeakerDTO(speaker)));
            return speakerDTOSet;
        }
    }

    public static class ConferenceDTO {

        private Integer id;
        private String name;
        private String location;
        private Integer capacity;
        private String date;
        private String time;
//        private Set<TalkDTO> talks;


        public ConferenceDTO(Conference conference) {
            if (conference.getId() != null) {
                this.id = conference.getId();
            }
            this.name = conference.getName();
            this.location = conference.getLocation();
            this.capacity = conference.getCapacity();
            this.date = conference.getDate();
            this.time = conference.getTime();
//            if (conference.getTalks() != null) {
//                this.talks = TalkDTO.makeDTOSet(conference.getTalks());
//            }
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

        public static Set<ConferenceDTO> makeDTOSet(List<Conference> conferences) {
            Set<ConferenceDTO> conferenceDTOSet = new HashSet<>();
            conferences.forEach(conference -> conferenceDTOSet.add(new ConferenceDTO(conference)));
            return conferenceDTOSet;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkDTO talkDTO = (TalkDTO) o;
        return Objects.equals(id, talkDTO.id) && Objects.equals(topic, talkDTO.topic) && Objects.equals(duration, talkDTO.duration) && Objects.equals(propsList, talkDTO.propsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic, duration, propsList);
    }
}
