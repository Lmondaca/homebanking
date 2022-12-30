package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/api")
public class AppController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String eMail, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || eMail.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (clientRepository.findByeMail(eMail).orElse(null) != null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }
        Client nuevoClien = clientRepository.save(new Client(firstName, lastName, eMail, passwordEncoder.encode(password)));
        int numeroCuenta = (int) (Math.random()*99999999.0);
        String numeroCuntaStr = String.format("%08d", numeroCuenta);

        String cuenta = "VIN-"+ numeroCuntaStr;
        Account nuevaAccount = new Account(cuenta, LocalDateTime.now(), 0.0);
        accountRepository.save(nuevaAccount);
        nuevoClien.setAccounts(nuevaAccount);
        clientRepository.save(nuevoClien);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
