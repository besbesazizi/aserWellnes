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
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String delay;
    @Temporal(TemporalType.DATE)
    private Date contractDate;


@OneToOne(mappedBy = "contract" , cascade = CascadeType.ALL)
@JsonIgnore
private  Delivery delivery;
    @ManyToMany(  cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Provider> providers;








}
