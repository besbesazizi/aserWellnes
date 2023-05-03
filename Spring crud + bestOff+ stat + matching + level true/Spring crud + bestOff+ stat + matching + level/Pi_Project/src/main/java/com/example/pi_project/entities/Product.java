package com.example.pi_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProduct;
    private  String description;
    private float price;
    private int  number;

    @ManyToOne
private CartShopping cartShopping;

    @ManyToOne
    private Offer offer;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<CartShopping> cartshopping;
    @ManyToOne
    private Category category;

}
