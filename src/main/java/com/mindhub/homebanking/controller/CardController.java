package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;


    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> nuevaTarjeta(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){
        Client currentClient = clientRepository.findByeMail(authentication.getName()).orElse(null);
        if(currentClient == null){ return new ResponseEntity<>("Cliente no encontrado.", HttpStatus.FORBIDDEN);}
        Set<Card> cardSet = currentClient.getCards();
        int contadorTipos = 0;
        for (Card card :
                cardSet) {
            if(card.getType() == cardType){
                contadorTipos+=1;
            }
        }
        if(contadorTipos >= 3){
            return new ResponseEntity<>("No puedes crear más de 3 tarjetas.", HttpStatus.FORBIDDEN);
        }
        Random random = new Random();
        int numeroCvv = random.nextInt(999);


        StringBuilder numeroCard = new StringBuilder();
        int seccionCard= 0;
        for (int i = 0; i<4; i++){
            seccionCard = random.nextInt(9999);
            if(i<3){
                numeroCard.append(String.format("%04d", seccionCard));
                numeroCard.append("-");
            } else {
                numeroCard.append(String.format("%04d", seccionCard));
            }
        }
        String numeroCardStr = numeroCard.toString();


        Card cardNueva = new Card(currentClient.getFirstName().toUpperCase() + " " + currentClient.getLastName().toUpperCase(), cardType, cardColor, numeroCardStr, numeroCvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
        currentClient.addCards(cardNueva);
        cardRepository.save(cardNueva);
        clientRepository.save(currentClient);

        return new ResponseEntity<>("Tarjeta Creada.", HttpStatus.CREATED);
    }
    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        Client client = this.clientRepository.findByeMail(authentication.getName()).orElse(null);
        if(client == null){return null;}
        return client.getCards().stream().map(CardDTO::new).collect(toList());
    }
}
