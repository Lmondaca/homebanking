package com.mindhub.homebanking.models;



import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/*EDIT 2023-01-04
- Se agrega nuevo atributo ENUM
- Se crea nuevo constructor para aceptar el nuevo atributo y se mantiene el anterior con el tipo de cuenta a CORRIENTE por defecto.
* */
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    private AccountType accountType;
    private int girosPorAnno;


    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.accountType = AccountType.CORRIENTE;
        this.girosPorAnno = -1;
    }

    public Account(String number, LocalDateTime creationDate, double balance, AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.accountType = accountType;
        this.girosPorAnno = 3;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public int getGirosPorAnno() {
        return girosPorAnno;
    }

    public void setGirosPorAnno(int girosPorAnno) {
        this.girosPorAnno = girosPorAnno;
    }

    public void setTransactions(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }
}
