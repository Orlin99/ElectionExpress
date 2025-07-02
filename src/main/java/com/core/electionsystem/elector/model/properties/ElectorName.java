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

@Entity(name = "ElectorName")
@Table(name = "elector_name")
public class ElectorName {

  @Id
  @SequenceGenerator(name = "elector_name_sequence", sequenceName = "elector_name_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elector_name_sequence")
  private Long electorNameId;

  @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
  private String firstName;

  @Column(name = "middle_name", columnDefinition = "TEXT")
  private String middleName;

  @Column(name = "surname", nullable = false, columnDefinition = "TEXT")
  private String surname;

  @JsonBackReference
  @OneToOne(mappedBy = "electorName", cascade = CascadeType.ALL, orphanRemoval = true)
  private Elector elector;

  public ElectorName() {
    // Default Empty Constructor
  }

  public ElectorName(Long electorNameId, String firstName, String middleName, String surname, Elector elector) {
    this.electorNameId = electorNameId;
    this.firstName = firstName;
    this.middleName = middleName;
    this.surname = surname;
    this.elector = elector;
  }

  public ElectorName(String firstName, String middleName, String surname, Elector elector) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.surname = surname;
    this.elector = elector;
  }

  public Long getElectorNameId() {
    return electorNameId;
  }

  public void setElectorNameId(Long electorNameId) {
    this.electorNameId = electorNameId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Elector getElector() {
    return elector;
  }

  public void setElector(Elector elector) {
    this.elector = elector;
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorNameId, firstName, middleName, surname, elector.getElectorId());
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
    ElectorName other = (ElectorName) object;
    return Objects.equals(electorNameId, other.electorNameId) && Objects.equals(firstName, other.firstName)
        && Objects.equals(middleName, other.middleName) && Objects.equals(surname, other.surname)
        && Objects.equals(elector.getElectorId(), other.elector.getElectorId());
  }

  @Override
  public String toString() {
    return "ElectorName [electorNameId=" + electorNameId + ", firstName=" + firstName + ", middleName=" + middleName + ", surname=" + surname
        + ", elector=" + elector.getElectorId() + "]";
  }
}
