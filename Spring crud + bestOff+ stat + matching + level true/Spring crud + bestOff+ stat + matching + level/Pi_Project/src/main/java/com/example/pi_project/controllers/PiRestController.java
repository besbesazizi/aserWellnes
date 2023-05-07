package com.example.pi_project.controllers;

import com.example.pi_project.entities.*;
import com.example.pi_project.services.IPiService;
import com.example.pi_project.repositories.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@AllArgsConstructor
public class PiRestController {
    @Autowired
    IPiService piService;
    @Autowired
    OfferRepository OfferRepository;


    @GetMapping("/AllOffers")
    @ResponseBody
    public List<Offer> getAllOffers(){
        return  OfferRepository.findAll();
    }
    @GetMapping("/AllOffersArchived")
    @ResponseBody
    public List<Offer> getAllOffersArchived(){
        List<Offer> archived = new ArrayList<>();
        List<Offer> offers = new ArrayList<>();
        offers =  OfferRepository.findAll();
        for (Offer offer :offers){
            if(offer.isArchived()==true){
                archived.add(offer);
            }
        }
        return archived;
    }

    @GetMapping("/AllOffersMsg")
    @ResponseBody
    public List<String> getAllOffersMsg() {
        List<String> offers = new ArrayList<>();
        for (Offer offer : piService.getAllOffers()) {
            if (offer.getNb_dislike_offer() > 100) {
                offers.add("Cette offre est archivée");
            } else {
                offers.add("Numéro de l'offre : " + offer.getNumOffer() + ", Description : " + offer.getDescription() + ", Nombre d'offres : " + offer.getNbr_offer() + ", Prix de l'offre : " + offer.getOrderPrice() + ", Date de création : " + offer.getCreation_date() + ", Date d'expiration : " + offer.getExpiration_date() + ", Nombre de mentions j'aime : " + offer.getNbLikeOffer() + ", Nombre de mentions je n'aime pas : " + offer.getNb_dislike_offer()
                );
            }
        }
        return offers;
    }

    @GetMapping("/AllRequests")
    @ResponseBody
    public List<Request> getAllRequests() {
        return piService.getAllRequests();
    }

    @GetMapping("/getOffre/id")
    @ResponseBody
    public Offer getOffre(@PathVariable("id") int id) {
        return piService.getOffer(id);
    }

    @PostMapping("/addRequest")
    @ResponseBody
    public Request addDelivery(@RequestBody Request request) {
        return piService.addRequest(request);
    }

    @PostMapping("/addOffer")
    public Offer addContract(@RequestBody Offer offer) {
        return piService.addOffer(offer);
    }

    @PutMapping("/updateRequest/{id}")
    @ResponseBody
    public Request updateRequest(@RequestBody Request request,@PathVariable("id") int id) {
        return piService.updateRequest(request,id);
    }

    @PutMapping("/updateOffer/{id}")
    @ResponseBody
    public Offer updateOffer(@RequestBody Offer offer,@PathVariable("id") int id) {
        return piService.updateOffer(offer,id);
    }

    @DeleteMapping("/deleteRequest/{id}")
    @ResponseBody
    public void deleteRequest(@PathVariable("id") int id) {
        piService.deleteRequest(id);
    }


    @DeleteMapping("/deleteOffer/{id}")
    @ResponseBody
    public void deleteOffer(@PathVariable("id") int id) {
        piService.deleteOffer(id);
    }


    //--------------------------------------best-off-----------------------------------------------------
    @GetMapping("/best-off")
    public ResponseEntity<List<Offer>> getBestOff() {
        List<Offer> offers = piService.bestOff();
        if (offers.size() == 3) {
            StringBuilder message = new StringBuilder();
            message.append("Remise de 20% sur l'offre ").append(offers.get(0).getId());
            offers.get(0).setDiscount_details(message.toString());
            message.setLength(0);
            message.append("Remise de 15% sur l'offre ").append(offers.get(1).getId());
            offers.get(1).setDiscount_details(message.toString());
            message.setLength(0);
            message.append("Remise de 10% sur l'offre ").append(offers.get(2).getId());
            offers.get(2).setDiscount_details(message.toString());
            OfferRepository.saveAll(offers);
        }
        return ResponseEntity.ok(offers);
    }
    //---------------------------------------------------------------------------------------------
    @GetMapping("/statistics")
    public double getStatistics() {
        return piService.statistics();

    }
    //----------------------------------------------------------------------------------------------
    @GetMapping("/cosine-similarity/{offerDescription}/{requestDescription}")
    public double cosineSimilarity(@PathVariable("offerDescription") String offerDescription, @PathVariable("requestDescription") String requestDescription) {
        return piService.cosineSimilarity(offerDescription, requestDescription);
    }

    //---------------------------------------------------------------------------------------------------


    @GetMapping("/requests/matching-offers/{id}")
    public ResponseEntity<List<Offer>> getMatchingOffers(@PathVariable("id") int requestId) {
        List<Offer> matchingOffers = piService.findMatchingOffers(requestId);
        if (matchingOffers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(matchingOffers);
        }
    }

    @GetMapping("/{offerId}/like")
    public void likeOffer(@PathVariable int offerId) {
        piService.likeOffer(offerId);
    }

    @GetMapping("/{offerId}/dislike")
    public void dislikeOffer(@PathVariable int offerId) {
        piService.dislikeOffer(offerId);
    }

//--------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------

    @GetMapping("/AllContracts")
    @ResponseBody
    public List<Contract> getAllContracts() {
        return piService.getAllContracts();
    }

    @GetMapping("/getContractById/{id}")
    @ResponseBody
    public Contract getContractById(@PathVariable("id") int id) {
        return piService.getContractById(id);
    }

    @GetMapping("/AllDeliveries")
    @ResponseBody
    public List<Delivery> getAllDeliveries() {
        return piService.getAllDeliveries();
    }

    @PostMapping("/addDelivery")
    @ResponseBody
    public Delivery addDelivery(@RequestBody Delivery delivery) {
        return piService.addDelivery(delivery);
    }

    @PostMapping("/addContract")
    public Contract addContract(@RequestBody Contract contract) {
        return piService.addContract(contract);
    }

    @PutMapping("/updateDelivery")
    @ResponseBody
    public Delivery updateDelivery(@RequestBody Delivery delivery) {
        return piService.updateDelivery(delivery);
    }

    @PutMapping("/updateContract")
    @ResponseBody
    public Contract updateContract(@RequestBody Contract contract) {
        return piService.updateContract(contract);
    }

    @DeleteMapping("/deleteDelivery/{id}")
    @ResponseBody
    public void deleteDelivery(@PathVariable("id") int id) {
        piService.deleteDelivery(id);
    }


    @DeleteMapping("/deleteContract/{id}")
    @ResponseBody
    public void deleteContract(@PathVariable("id") int id) {
        piService.deleteContract(id);
    }

    @PostMapping("/addOrder")
    @ResponseBody
    public Ordeer addOrder(@RequestBody Ordeer order) {
        return piService.addOrder(order);
    }

    @PutMapping("/updateOrder")
    @ResponseBody
    public Ordeer updateOrder(@RequestBody Ordeer order) {
        return piService.updateOrder(order);
    }

    @DeleteMapping("/deleteOrder/{id}")
    @ResponseBody
    public void deleteOrder(@PathVariable("id") int id) {
        piService.deleteOrder(id);
    }

    @GetMapping("/AllOrder")
    @ResponseBody
    public List<Ordeer> getAllOrder() {
        return piService.getAllOrder();
    }

    @PostMapping("/addCartShopping")
    @ResponseBody
    public CartShopping addCart(@RequestBody CartShopping cartShopping) {
        return piService.addOCart(cartShopping);
    }

    @PutMapping("/updateCart")
    @ResponseBody
    public CartShopping updateCartShopping(@RequestBody CartShopping cartShopping) {
        return piService.updateCartShopping(cartShopping);
    }

    @DeleteMapping("/deleteCartShopping/{id}")
    @ResponseBody
    public void deleteCartShopping(@PathVariable("id") int id) {
        piService.deleteCartShopping(id);
    }

    @GetMapping("/AllCartShopping")
    @ResponseBody
    public List<CartShopping> getAllCartShopping() {
        return piService.getAllCartShopping();
    }


}

