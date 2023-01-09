package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Coordinate;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

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

        nuevoClien.setCoordinates(new Coordinate(generarValores()));

        String cuenta = "VIN-"+ numeroCuntaStr;
        Account nuevaAccount = new Account(cuenta, LocalDateTime.now(), 0.0);
        accountRepository.save(nuevaAccount);
        nuevoClien.setAccounts(nuevaAccount);
        clientRepository.save(nuevoClien);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private HashMap<String, String> generarValores(){
        HashMap<String, String> values = new HashMap<>();
        Random random = new Random();
        int columns = 0;
        String rows = "";
        for(int ia=0; ia<3;ia++){
            columns = random.nextInt(99);
            rows = "A"+(ia+1);
            values.put(rows,String.format("%02d", columns));
        }
        for(int ib=0; ib<3;ib++){
            columns = random.nextInt(99);
            rows = "B"+(ib+1);
            values.put(rows,String.format("%02d", columns));
        }
        for(int ic=0; ic<3;ic++){
            columns = random.nextInt(99);
            rows = "C"+(ic+1);
            values.put(rows,String.format("%02d", columns));
        }

        return values;
    }
}
