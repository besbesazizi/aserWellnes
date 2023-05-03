package com.example.pi_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String quantity;
    private float totalHT;
    private float totalTTC;
    private String address;
    private float unitPrice;
    private float tva;
    @Temporal(TemporalType.DATE)
    private Date deliveryDate;
    @Enumerated(EnumType.STRING)
    private State deliveryState;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "delivery")
    @JsonIgnore
    private Set<Ordeer> orders;
@OneToOne
    private  Contract contract;

    @ManyToMany(  cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Provider> providers;
}





