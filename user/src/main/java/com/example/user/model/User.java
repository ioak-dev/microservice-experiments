package com.example.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {


  @Id
  private String id;

  private String name;

  private String address;

  private String phoneNumber;

  @PrePersist
  public void prePersist() {
    if (id == null || id.isEmpty()) {
      id = UUID.randomUUID().toString();
    }
  }
}
