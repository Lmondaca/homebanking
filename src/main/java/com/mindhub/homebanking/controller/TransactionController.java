package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.List;
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
            if(origenAccount.getAccountType() == AccountType.AHORRO){
                origenAccount.setGirosPorAnno(origenAccount.getGirosPorAnno() - 1);
            }
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
        Account accountFrom = accountRepository.findByNumber(accountFromNumber).orElse(null);
        if(accountFrom == null){
            return "Cuenta origen no existe";
        }
        else if(accountFrom.getAccountType() == AccountType.AHORRO ){
            if(accountFrom.getGirosPorAnno() < 1){
                return "No puede hacer más giros por este anno.";
            }
        }
        return "";

    }
    private void debit(Account accountDebited, double amountDebit ){
            accountDebited.setBalance(accountDebited.getBalance() - amountDebit);
    }
    private void credit(Account accountCredited, double amountCredit){
            accountCredited.setBalance(accountCredited.getBalance() + amountCredit);
    }
    //@Scheduled(cron = "0 0 9 L * ? * ")
    @Scheduled(cron = "0/60 * * ? * *")
    public void ahorroAccount(){
        List<Account> listaAccountAhorro = accountRepository.findByAccountType(AccountType.AHORRO);
        System.out.println("Entro a la tarea programada");
        for (Account cuentaAhorro :
                listaAccountAhorro) {
            if (cuentaAhorro.getBalance() >= 5000.0) {
                addTransactionInDes(cuentaAhorro);
            }

        }
    }
    private void addTransactionInDes(Account cuentaAhorro){
        double interesGanado = cuentaAhorro.getBalance()*0.0169;
        Transaction trxCredit = new Transaction(TransactionType.CREDITO, interesGanado, "Credito por Interes desvengado.", LocalDateTime.now() );
        credit(cuentaAhorro, interesGanado);
        trxCredit.setAccount(cuentaAhorro);
        accountRepository.save(cuentaAhorro);
        transactionRepository.save(trxCredit);
    }
    //@Scheduled(cron = "0 0 0 1 JAN ?")
    @Scheduled(cron = "0 * * ? * *")
    public void giroPorAnnoRes(){
        List<Account> listaAccountAhorro = accountRepository.findByAccountType(AccountType.AHORRO);
        for (Account cuentaAhorro :
                listaAccountAhorro) {
            cuentaAhorro.setGirosPorAnno(3);
            accountRepository.save(cuentaAhorro);
        }
        System.out.println("Se reseteo la cantidad de giros");

    }
}
