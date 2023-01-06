package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounDTO(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
    /*EDIT 2023-03-04:
    - Se agrega parametro a metodo para recibir Tipo de cuenta
    - Se valida cantidad de cuentas por tipo
    - Se usa nuevo constructor para crear cuentas por tipo
    */
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType){
        Client currentClient = clientRepository.findByeMail(authentication.getName()).orElse(null);
        Set<Account> accountSet = currentClient.getAccounts();
        int contadorTipos = 0;
        for (Account account : accountSet) {
            if (accountType == account.getAccountType()) {
                contadorTipos++;
            }
        }
        if(contadorTipos >= 3){
            return new ResponseEntity<>("No puede crear más de 3 cuentas del tipo: " + accountType.toString(), HttpStatus.FORBIDDEN);
        }
        int numeroCuenta = (int) (Math.random()*99999999.0);
        String numeroCuntaStr = String.format("%08d", numeroCuenta);

        String cuenta = "VIN-"+ numeroCuntaStr;
        Account nuevaAccount = new Account(cuenta, LocalDateTime.now(), 0.0, accountType);
        accountRepository.save(nuevaAccount);
        currentClient.setAccounts(nuevaAccount);
        clientRepository.save(currentClient);
        return new ResponseEntity<>("Cuenta creada.", HttpStatus.CREATED);


    }
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client client = this.clientRepository.findByeMail(authentication.getName()).orElse(null);
        if(client == null){return Collections.emptyList();}
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }
}
