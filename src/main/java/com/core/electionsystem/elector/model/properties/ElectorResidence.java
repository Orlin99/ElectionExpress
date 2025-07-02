package com.core.electionsystem.elector.model.properties;

import java.util.Objects;

import com.core.electionsystem.elector.model.Elector;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity(name = "ElectorResidence")
@Table(name = "elector_residence")
public class ElectorResidence {

  @Id
  @SequenceGenerator(name = "elector_residence_sequence", sequenceName = "elector_residence_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elector_residence_sequence")
  private Long electorResidenceId;

  @Column(name = "region", nullable = false, columnDefinition = "TEXT")
  private String region;

  @Column(name = "municipality", nullable = false, columnDefinition = "TEXT")
  private String municipality;

  @Column(name = "locality", nullable = false, columnDefinition = "TEXT")
  private String locality;

  @Column(name = "residential_area", columnDefinition = "TEXT")
  private String residentialArea;

  @Column(name = "street", columnDefinition = "TEXT")
  private String street;

  @Column(name = "location_number", nullable = false, columnDefinition = "INTEGER")
  private Integer locationNumber;

  @Column(name = "entrance", columnDefinition = "CHAR(1)")
  private Character entrance;

  @Column(name = "floor", columnDefinition = "INTEGER")
  private Integer floor;

  @Column(name = "apartment", columnDefinition = "INTEGER")
  private Integer apartment;

  @JsonBackReference
  @OneToOne(mappedBy = "electorResidence", cascade = CascadeType.ALL, orphanRemoval = true)
  private Elector elector;

  public ElectorResidence() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public ElectorResidence(Long electorResidenceId, String region, String municipality, String locality, String residentialArea, String street,
      Integer locationNumber, Character entrance, Integer floor, Integer apartment, Elector elector) {
    this.electorResidenceId = electorResidenceId;
    this.region = region;
    this.municipality = municipality;
    this.locality = locality;
    this.residentialArea = residentialArea;
    this.street = street;
    this.locationNumber = locationNumber;
    this.entrance = entrance;
    this.floor = floor;
    this.apartment = apartment;
    this.elector = elector;
  }

  @SuppressWarnings("java:S107")
  public ElectorResidence(String region, String municipality, String locality, String residentialArea, String street, Integer locationNumber,
      Character entrance, Integer floor, Integer apartment, Elector elector) {
    this.region = region;
    this.municipality = municipality;
    this.locality = locality;
    this.residentialArea = residentialArea;
    this.street = street;
    this.locationNumber = locationNumber;
    this.entrance = entrance;
    this.floor = floor;
    this.apartment = apartment;
    this.elector = elector;
  }

  public Long getElectorResidenceId() {
    return electorResidenceId;
  }

  public void setElectorResidenceId(Long electorResidenceId) {
    this.electorResidenceId = electorResidenceId;
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

  public Elector getElector() {
    return elector;
  }

  public void setElector(Elector elector) {
    this.elector = elector;
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorResidenceId, region, municipality, locality, residentialArea, street, locationNumber, entrance, floor, apartment,
        elector.getElectorId());
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
    ElectorResidence other = (ElectorResidence) object;
    return Objects.equals(electorResidenceId, other.electorResidenceId) && Objects.equals(region, other.region)
        && Objects.equals(municipality, other.municipality) && Objects.equals(locality, other.locality)
        && Objects.equals(residentialArea, other.residentialArea) && Objects.equals(street, other.street)
        && Objects.equals(locationNumber, other.locationNumber) && Objects.equals(entrance, other.entrance) && Objects.equals(floor, other.floor)
        && Objects.equals(apartment, other.apartment) && Objects.equals(elector.getElectorId(), other.elector.getElectorId());
  }

  @Override
  public String toString() {
    return "ElectorResidence [electorResidenceId=" + electorResidenceId + ", region=" + region + ", municipality=" + municipality + ", locality="
        + locality + ", residentialArea=" + residentialArea + ", street=" + street + ", locationNumber=" + locationNumber + ", entrance=" + entrance
        + ", floor=" + floor + ", apartment=" + apartment + ", elector=" + elector.getElectorId() + "]";
  }
}
