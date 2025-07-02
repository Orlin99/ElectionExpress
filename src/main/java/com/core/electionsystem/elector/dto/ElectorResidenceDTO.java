package com.core.electionsystem.elector.dto;

import java.util.Objects;

public class ElectorResidenceDTO {

  private String region;
  private String municipality;
  private String locality;
  private String residentialArea;
  private String street;
  private Integer locationNumber;
  private Character entrance;
  private Integer floor;
  private Integer apartment;

  public ElectorResidenceDTO() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public ElectorResidenceDTO(String region, String municipality, String locality, String residentialArea, String street, Integer locationNumber,
      Character entrance, Integer floor, Integer apartment) {
    this.region = region;
    this.municipality = municipality;
    this.locality = locality;
    this.residentialArea = residentialArea;
    this.street = street;
    this.locationNumber = locationNumber;
    this.entrance = entrance;
    this.floor = floor;
    this.apartment = apartment;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getMunicipality() {
    return municipality;
  }

  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getResidentialArea() {
    return residentialArea;
  }

  public void setResidentialArea(String residentialArea) {
    this.residentialArea = residentialArea;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Integer getLocationNumber() {
    return locationNumber;
  }

  public void setLocationNumber(Integer locationNumber) {
    this.locationNumber = locationNumber;
  }

  public Character getEntrance() {
    return entrance;
  }

  public void setEntrance(Character entrance) {
    this.entrance = entrance;
  }

  public Integer getFloor() {
    return floor;
  }

  public void setFloor(Integer floor) {
    this.floor = floor;
  }

  public Integer getApartment() {
    return apartment;
  }

  public void setApartment(Integer apartment) {
    this.apartment = apartment;
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, municipality, locality, residentialArea, street, locationNumber, entrance, floor, apartment);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (getClass() != object.getClass()) {
      return false;
    }
    ElectorResidenceDTO other = (ElectorResidenceDTO) object;
    return Objects.equals(region, other.region) && Objects.equals(municipality, other.municipality) && Objects.equals(locality, other.locality)
        && Objects.equals(residentialArea, other.residentialArea) && Objects.equals(street, other.street)
        && Objects.equals(locationNumber, other.locationNumber) && Objects.equals(entrance, other.entrance) && Objects.equals(floor, other.floor)
        && Objects.equals(apartment, other.apartment);
  }

  @Override
  public String toString() {
    return "ElectorResidenceDTO [region=" + region + ", municipality=" + municipality + ", locality=" + locality + ", residentialArea="
        + residentialArea + ", street=" + street + ", locationNumber=" + locationNumber + ", entrance=" + entrance + ", floor=" + floor
        + ", apartment=" + apartment + "]";
  }
}
