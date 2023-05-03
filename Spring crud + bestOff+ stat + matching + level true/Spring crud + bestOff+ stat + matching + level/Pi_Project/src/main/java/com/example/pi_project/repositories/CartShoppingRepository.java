package com.example.pi_project.repositories;

import com.example.pi_project.entities.CartShopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartShoppingRepository extends JpaRepository<CartShopping,Integer> {
}
