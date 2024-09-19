package com.code.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
  @Id
  @SequenceGenerator(name = "my_sequence_generator", sequenceName = "my_sequence", allocationSize = 1) //sequenceName corresponde al nombre secuencia  en bd
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_generator") //los sequence son mas optimos que usar identity
  private Long id;
  private String name;
  @Column(name = "last_name")
  private String lastName;
  private String address;
  private String email;
}
