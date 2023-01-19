package dtos;

import entities.Speaker;
import entities.Talk;

import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SpeakerDTO {
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

    public static Set<SpeakerDTO> makeDTOSet(Set<Speaker> speakers){
        Set<SpeakerDTO> speakerDTOSet= new HashSet<>();
        speakers.forEach(speaker -> speakerDTOSet.add(new SpeakerDTO(speaker)));
        return speakerDTOSet;
    }
    public static Set<SpeakerDTO> makeDTOSet(List<Speaker> speakers){
        Set<SpeakerDTO> speakerDTOSet= new HashSet<>();
        speakers.forEach(speaker -> speakerDTOSet.add(new SpeakerDTO(speaker)));
        return speakerDTOSet;
    }

    public static class TalkDTO {
        private Integer id;
        private String topic;
        private Integer duration;
        private String propsList;

        public TalkDTO(Talk talk) {
            if (talk.getId() != null) {
                this.id = talk.getId();
            }
            this.topic = talk.getTopic();
            this.duration = talk.getDuration();
            this.propsList = talk.getPropsList();
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

        public static Set<TalkDTO> makeDTOSet(Set<Talk> talks){
            Set<TalkDTO> talkDTOSet= new HashSet<>();
            talks.forEach(talk -> talkDTOSet.add(new TalkDTO(talk)));
            return talkDTOSet;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeakerDTO that = (SpeakerDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(profession, that.profession) && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profession, gender);
    }
}
