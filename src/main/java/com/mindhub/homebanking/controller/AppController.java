package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String eMail,
            @RequestParam String phone, @RequestParam String address, @RequestParam String maritalStatus,
            @RequestParam String company, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || eMail.isEmpty() || phone.isEmpty() ||
                address.isEmpty() || maritalStatus.isEmpty() || company.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByeMail(eMail).orElse(null) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client nuevoClien = clientRepository.save(new Client(firstName, lastName, eMail, phone, address,
                maritalStatus, company, passwordEncoder.encode(password)));
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
