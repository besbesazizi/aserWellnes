package com.example.pi_project.services;

import com.example.pi_project.entities.*;
import com.example.pi_project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class PiServiceImpl implements IPiService , UserDetailsService {


    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    OfferRepository offerRepository;


    //---------------------------archive-----------------------------------------------
    //@Override
    public void archiveOffer(Offer offer) {
        if (offer.getNb_dislike_offer() > 100) {
            offer.setArchived(true);
        }
    }
    //------------------------bestOff-------------------------------------------------
    @Override
    public List<Offer> bestOff() {
        List<Offer> offers = offerRepository.findTop3ByArchivedOrderByNbLikeOfferDesc(false);
        if (offers.size() == 3) {
            for (Offer offer : offers) {
                if (!offer.getHasDiscount()) { // Vérification de la propriété hasDiscount
                    if (offer == offers.get(0)) {
                        offer.setOrderPrice(offer.getOrderPrice() * 0.8f);
                    } else if (offer == offers.get(1)) {
                        offer.setOrderPrice(offer.getOrderPrice() * 0.85f);
                    } else if (offer == offers.get(2)) {
                        offer.setOrderPrice(offer.getOrderPrice() * 0.9f);
                    }
                    offer.setHasDiscount(true); // Mise à jour de la propriété hasDiscount
                }
            }
            offerRepository.saveAll(offers);
        }
        return offers;
    }

    //-----------------------------------statistics--------------------------------------------------
    @Override
    public String statistics() {
        List<Offer> offers = offerRepository.findAll();
        List<Request> requests = requestRepository.findAll();

        int confirmedRequests = 0;
        for (Request request : requests) {
            if (request.getStateRequest() == StateRequest.confirmed) {
                for (Offer offer : offers) {
                    if ( offer.getOrderPrice() <= request.getBudget()) {
                        confirmedRequests++;
                        break;
                    }
                }
            }
        }

        double percentage = ((double) confirmedRequests / requests.size()) * 100;
        return "Le pourcentage des demandes confirmées et dont le budget est Supérieur ou égal au prix de l'offre est de " + percentage + "%";
    }

    //---------------------------------------- Similarity -------------------------------------------------
    @Override
    public double cosineSimilarity(String offerDescription, String requestDescription) {
        String[] offerWords = offerDescription.toLowerCase().split("[^a-zA-Z]+");
        String[] requestWords = requestDescription.toLowerCase().split("[^a-zA-Z]+");

        Map<String, Integer> offerFreq = new HashMap<>();
        Map<String, Integer> requestFreq = new HashMap<>();

        // calculer les fréquences de chaque mot dans les deux descriptions
        for (String word : offerWords) {
            offerFreq.put(word, offerFreq.getOrDefault(word, 0) + 1);
        }

        for (String word : requestWords) {
            requestFreq.put(word, requestFreq.getOrDefault(word, 0) + 1);
        }

        // calculer le produit point entre les deux vecteurs
        double dotProduct = 0;
        for (String word : offerFreq.keySet()) {
            if (requestFreq.containsKey(word)) {
                dotProduct += offerFreq.get(word) * requestFreq.get(word);
            }
        }

        // calculer les normes des deux vecteurs
        double offerNorm = 0;
        for (int freq : offerFreq.values()) {
            offerNorm += freq * freq;
        }
        offerNorm = Math.sqrt(offerNorm);

        double requestNorm = 0;
        for (int freq : requestFreq.values()) {
            requestNorm += freq * freq;
        }
        requestNorm = Math.sqrt(requestNorm);

        // calculer le cosinus de l'angle entre les deux vecteurs
        double cosineSimilarity = dotProduct / (offerNorm * requestNorm);

        return cosineSimilarity;
    }


    //-------------------------------------------------------------------------------------------
    @Override
    public List<Offer> findMatchingOffers(int requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return Collections.emptyList(); // retourne une liste vide si la requête n'existe pas
        }
        // Parcourir toutes les offres non archivées
        List<Offer> offers = offerRepository.findByArchived(false);
        List<Offer> matchingOffers = new ArrayList<>();
        for (Offer offer : offers) {
            if (!offer.isArchived() && offer.getExpiration_date().compareTo(new Date()) > 0
                    && offer.getOrderPrice() <= request.getBudget()) {
                double similarity = cosineSimilarity(offer.getDescription(), request.getDescription());
                if (similarity >= 0.6) {
                    matchingOffers.add(offer);
                }
            }
        }
        // Trier les offres similaires par ordre décroissant de nombre de likes
       Collections.sort(matchingOffers, Comparator.comparing(Offer::getNbLikeOffer).reversed());

        // Appliquer la méthode BestOff pour les trois premières offres similaires
        if (matchingOffers.size() >= 3) {
            bestOff();
        }
        return matchingOffers;
    }
    //---------------------------------------------------------------------
    public Offer findOfferById(int offerId) {
        return offerRepository.findById(offerId).orElse(null);
    }
    //-----------------------------------------------------------------------------------
    @Override
    public void likeOffer(int offerId) {
        Offer offer = findOfferById(offerId);
        if (offer != null) {
            offer.like();
            offerRepository.save(offer);
        }
    }

    public void dislikeOffer(int offerId) {
        Offer offer = findOfferById(offerId);
        if (offer != null) {
            offer.dislike();
            offerRepository.save(offer);
        }
    }


    //---------------------------------------------------------------------------------------------
    @Override
    public Request addRequest(Request request) {
        return  requestRepository.save(request);

    }

    @Override
    public Offer addOffer(Offer contract) {
        return offerRepository.save(contract);
    }
    @Override
    public Offer updateOffer(Offer offer , int id) {
        offerRepository.deleteById(id);
        return offerRepository.save(offer);
    }

    @Override
    public void deleteOffer(int id) {
        offerRepository.deleteById(id);
    }
    @Override
    public Request updateRequest(Request request, int id) {
        requestRepository.deleteById(id);
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(int id) {
        requestRepository.deleteById(id);
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOffer(int id) {
        return offerRepository.findById(id).get();
    }


    //------------------------------------------------------------------------------------------------------

    @Override
    public Provider updateProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    @Override
    public void deleteProvider(int id) {
        providerRepository.deleteById(id);
    }
    @Override
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @Override
    public Provider addProvider(Provider provider) {
        return  providerRepository.save(provider);

    }
    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }
    @Override
    public Contract getContractById(int id) {
        return contractRepository.findById(id).orElse(null);
    }


    @Override
    public Delivery addDelivery(Delivery delivery) {
        return  deliveryRepository.save(delivery);

    }

    @Override
    public Contract addContract(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public void deleteDelivery(int id)  {
        deliveryRepository.deleteById(id);
    }

    @Override
    public Contract updateContract(Contract contract) {
        return contractRepository.save(contract);
    }



    @Override
    public void deleteContract(int id) {
        contractRepository.deleteById(id);
    }
    @Override
    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Role r) {
        return roleRepository.save(r);
    }

    @Override
    public void deleteRole(int id) {
        roleRepository.deleteById(id);
    }

    @Override
    public User addUser(User u) {
        return userRepository.save(u);
    }
    @Override
    public User updateUser(User u) {
        return userRepository.save(u);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), authorities);
    }
    @Autowired
    OrdeerRepository orderRepository;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    CartShoppingRepository cartShoppingRepository;
    @Autowired
    DonationRepository donationRepository;

    @Override
    public Ordeer addOrder(Ordeer order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Ordeer updateOrder(Ordeer order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Ordeer> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public CartShopping addOCart(CartShopping cartShopping) {
        return cartShoppingRepository.save(cartShopping);
    }

    @Override
    public void deleteCartShopping(int id) {
        cartShoppingRepository.deleteById(id);
    }

    @Override
    public CartShopping updateCartShopping(CartShopping cartShopping) {
        return cartShoppingRepository.save(cartShopping);
    }


    @Override
    public List<CartShopping> getAllCartShopping() {
        return cartShoppingRepository.findAll();
    }




}
