package org.servlet.museum_management.model;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ArtObject {

  private Integer id;
  private String name;
  private String description;
  private Integer creationYear;
  private String artist;
  private Integer museumId;
  private byte[] photo;


  public ArtObject() {
  }

  public ArtObject(Integer id, String name, String description, Integer creationYear, String artist, Integer museumId, byte[] photo) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.creationYear = creationYear;
    this.artist = artist;
    this.museumId = museumId;
    this.photo = photo;
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


  public Integer getCreationYear() {
    return creationYear;
  }

  public void setCreationYear(Integer creationYear) {
    this.creationYear = creationYear;
  }


  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }


  public Integer getMuseumId() {
    return museumId;
  }

  public void setMuseumId(Integer museumId) {
    this.museumId = museumId;
  }


  public byte[] getPhoto() {
    return photo;
  }

  public String getPhotoBase64() {
    if (photo == null) {
      return null;
    } else {
      byte[] encodeBase64 = Base64.getEncoder().encode(photo);
      return new String(encodeBase64, StandardCharsets.UTF_8);
    }
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
  }

}
