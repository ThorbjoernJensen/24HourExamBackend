package entities;

import dtos.SpeakerDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "speaker")
public class Speaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;
    private String profession;

    //    TODO implement with enum
    private String gender;

    @ManyToMany
    Set<Talk> talks = new HashSet<>();

    public Speaker() {
    }

    public Speaker(Integer id, String name, String profession, String gender) {
        this.id = id;
        this.name = name;
        this.profession = profession;
        this.gender = gender;
    }

    public Speaker(String name, String profession, String gender) {
        this.name = name;
        this.profession = profession;
        this.gender = gender;
    }
    public Speaker(SpeakerDTO speakerDTO) {
        this.name = speakerDTO.getName();
        this.profession = speakerDTO.getProfession();
        this.gender = speakerDTO.getGender();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<Talk> getTalks() {
        return talks;
    }

    public void addTalk(Talk talk) {
        talk.addSpeaker(this);
        this.talks.add(talk);
    }



}