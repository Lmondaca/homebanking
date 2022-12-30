package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transference(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam double amount, @RequestParam String description, Authentication authentication){
        amount = Math.abs(amount);
        String respuesta = isValid(fromAccountNumber, toAccountNumber,  amount, description, authentication);
        if(respuesta.isEmpty()){

            Transaction trxDebit = new Transaction(TransactionType.DEBITO, amount*(-1.0), toAccountNumber + ": " + description, LocalDateTime.now());
            Transaction trxCredit = new Transaction(TransactionType.CREDITO, amount, fromAccountNumber + ": " + description, LocalDateTime.now() );

            Account origenAccount = accountRepository.findByNumber(fromAccountNumber).orElse(null);
            Account destinAccount = accountRepository.findByNumber(toAccountNumber).orElse(null);
            trxDebit.setAccount(origenAccount);
            trxCredit.setAccount(destinAccount);
            transactionRepository.save(trxDebit);
            transactionRepository.save(trxCredit);

            debit(origenAccount, amount);
            credit(destinAccount, amount);

            accountRepository.save(origenAccount);
            accountRepository.save(destinAccount);

            return new ResponseEntity<>("Operacion Exitosa", HttpStatus.CREATED);

        }
        else{
            return new ResponseEntity<>(respuesta, HttpStatus.FORBIDDEN);
        }
    }
    private String isValid( String accountFromNumber, String accountToNumber,  double amount, String description, Authentication authentication){
        if(accountToNumber.isEmpty() || accountFromNumber.isEmpty() ||  amount == 0.0 || description.isEmpty()){
            return "Favor rellene todos los campos.";
        }
        else if(accountToNumber.compareTo(accountFromNumber) == 0){
            return "Los numeros de cuentas son iguales";
        }

        Client currentClient = clientRepository.findByeMail(authentication.getName()).orElse(null);
        if(currentClient == null){
            return "Cliente no existe";
        }
        else{
            int contadorCuentasIguales = 0;
            for (Account account:
                 currentClient.getAccounts()) {
                if( account.getNumber().compareTo(accountFromNumber) == 0){
                    contadorCuentasIguales++;
                    if(account.getBalance() < amount){
                        return "Cuenta de origen no tiene monto suficiente para realizar la operación";
                    }

                }
            }
            if(contadorCuentasIguales == 0){
                return "Cuenta de origen no pertenece a cliente.";
            }
        }
        Account accountDestiny = accountRepository.findByNumber(accountToNumber).orElse(null);
        if(accountDestiny == null){
            return "Cuenta destino no existe";
        }
        return "";

    }
    private void debit(Account accountDebited, double amountDebit ){
            accountDebited.setBalance(accountDebited.getBalance() - amountDebit);
    }
    private void credit(Account accountCredited, double amountCredit){
            accountCredited.setBalance(accountCredited.getBalance() + amountCredit);
    }
}
