package com.example.pi_project.services;

import com.example.pi_project.entities.*;

import java.util.List;

public interface IPiService {
    public List<Delivery> getAllDeliveries();
    public List<Contract> getAllContracts();
    public Contract getContractById(int id);
    public Delivery addDelivery(Delivery delivery);
    public Contract addContract(Contract contract);
    public void deleteDelivery(int id);
    public Delivery updateDelivery(Delivery delivery);
    public void deleteContract(int id);
    public Contract updateContract(Contract contract);
    Role addRole(Role role);
    public Ordeer addOrder(Ordeer order);
    public void deleteOrder(int id);
    public Ordeer updateOrder(Ordeer order);
    public List<Ordeer> getAllOrder();


    public CartShopping addOCart(CartShopping cartShopping);
    public void deleteCartShopping(int id);
    public CartShopping updateCartShopping(CartShopping cartShopping);
    public List<CartShopping> getAllCartShopping();




    Role updateRole(Role r);

    void deleteRole(int id);

    User addUser(User u);

    User updateUser(User u);

    void deleteUser(int id);

    List<User> getAllUsers();

    Account addAccount(Account account);
    public List<Request> getAllRequests();
    public List<Offer> getAllOffers();
    public Offer getOffer(int id);
    public Request addRequest(Request request);


    public Offer addOffer(Offer offer);
    public void deleteRequest(int id);
    public Request updateRequest(Request request ,int id);
    public void deleteOffer(int id);
    public Offer updateOffer(Offer offer,int id);
    public void archiveOffer(Offer offer) ;
    public List<Offer> bestOff();
    double cosineSimilarity(String offerDescription, String requestDescription);
    public String statistics();
    public List<Offer> findMatchingOffers(int requestId);

    public void likeOffer(int offerId);
    public void dislikeOffer(int offerId);
    public void deleteProvider(int id);
    public Provider updateProvider(Provider provider);
    public List<Provider> getAllProviders();
    public Provider addProvider(Provider provider);

}
