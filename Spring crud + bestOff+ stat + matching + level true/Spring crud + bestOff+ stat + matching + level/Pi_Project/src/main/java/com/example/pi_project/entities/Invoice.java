package com.example.pi_project.entities;

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
@NoArgsConstructor
@AllArgsConstructor
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @Enumerated(EnumType.STRING)
    private TypeInvoice TypeInvoice;
    private  String desciption;
    private  float total;
    @OneToMany (mappedBy = "invoice")
    private Set<Tax> taxes ;

}
