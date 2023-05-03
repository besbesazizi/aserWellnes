package com.example.pi_project.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTax;
    private float rising ;
    private float tva;
    @ManyToOne
    private Invoice invoice;
}
