package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String eMail;
    private String phone;
    private String address;
    private String maritalStatus;
    private String company;
    private String password;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinate_id", referencedColumnName = "id")
    private Coordinate coordinate;

    public Client() {
    }

    public Client(String firstName, String lastName, String eMail, String phone, String address, String maritalStatus,
                  String company, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.phone = phone;
        this.address = address;
        this.maritalStatus = maritalStatus;
        this.company = company;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    @JsonIgnore
    public List<Loan> getLoans(){
        return this.clientLoans.stream().map(loan -> loan.getLoan()).collect(Collectors.toList());
    }

    public void addLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
    public void addCards(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public String toString() {
        return firstName + " " + lastName;
    }


    public Coordinate getCoordinates() {
        return coordinate;
    }

    public void setCoordinates(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
