package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> solicitarLoans(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication){
        Account accountDestin = accountRepository.findByNumber(loanApplication.getToAccountNumber()).orElse(null);
        Client clientAutenticado = clientRepository.findByeMail(authentication.getName()).orElse(null);
        Loan loan = loanRepository.findById(loanApplication.getLoanId()).orElse(null);
        String respuesta = isValid(loanApplication, loan, accountDestin, clientAutenticado);
        if(respuesta.isEmpty()){
            ClientLoan solicitudLoan = new ClientLoan(loanApplication.getAmount() + loanApplication.getAmount()*0.20, loanApplication.getPayments(), clientAutenticado, loan);
            clientLoanRepository.save(solicitudLoan);
            Transaction trxLoan = new Transaction(TransactionType.CREDITO, loanApplication.getAmount(), loan.getName() + ": " + "loan approved.", LocalDateTime.now());
            trxLoan.setAccount(accountDestin);
            transactionRepository.save(trxLoan);
            accountDestin.setBalance(accountDestin.getBalance() + loanApplication.getAmount());
            accountRepository.save(accountDestin);
            return new ResponseEntity<>("éxito", HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(respuesta, HttpStatus.FORBIDDEN);
        }
    }

    private String isValid(LoanApplicationDTO loanApplication, Loan loan, Account accountDestin, Client clientAutenticado ){

        if(loanApplication.getLoanId() == 0 || loanApplication.getAmount() == 0 || loanApplication.getPayments().compareTo(Integer.valueOf(0)) == 0 || loanApplication.getToAccountNumber().isEmpty()){
            return "Uno o varios de los campos están vacíos.";
        }

        if(loan == null){
            return "El prestamo no existe.";
        }
        else if(loanApplication.getAmount() > loan.getMaxAmount()){
            return "Monto solicitado es superior al límite del préstamo.";
        }
        else if(!loan.isInPayments(loanApplication.getPayments())){
            return "Cantidad de cuotas no está disponible para este préstamo";
        }

        if (accountDestin == null){
            return "Cuenta destino no existe.";
        }
        else if(clientAutenticado == null){
            return "Cliente no existe.";
        }
        else{
            int contadorCuentasCorrectas = 0;
            for (Account accountOfClient :
                    clientAutenticado.getAccounts()) {
                if (accountOfClient.getNumber().compareTo(accountDestin.getNumber()) == 0) {
                    contadorCuentasCorrectas++;
                }
            }
            if(contadorCuentasCorrectas == 0){
                return "Cuenta no pertenece a cliente autenticado.";
            }
        }
        return "";

    }
}
