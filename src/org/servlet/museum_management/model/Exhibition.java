package org.servlet.museum_management.model;


import java.sql.Date;

public class Exhibition {

    private Integer id;
    private String name;
    private String description;
    private Date started;
    private Date ended;
    private Integer museumId;

    public Exhibition(Integer id, String name, String description, Date started, Date ended, Integer museumId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.started = started;
        this.ended = ended;
        this.museumId = museumId;
    }

    public Exhibition() {

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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public Integer getMuseumId() {
        return museumId;
    }

    public void setMuseumId(Integer museumId) {
        this.museumId = museumId;
    }

}
