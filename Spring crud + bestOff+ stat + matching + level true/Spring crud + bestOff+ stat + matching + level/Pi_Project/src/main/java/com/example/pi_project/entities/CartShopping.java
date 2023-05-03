package com.example.pi_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartShopping implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String description;
    private float totalPrice;
    private String quantity;
    private String codePromo ;

    @ManyToMany(mappedBy = "cartshopping")
    @JsonIgnore
    private Set<Product> products;
    @OneToMany(mappedBy = "Cart")
    @JsonIgnore
    private Set<Ordeer> orders;



}
