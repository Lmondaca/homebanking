package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ClienDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String eMail;

    private Set<AccountDTO> accounts;

    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;

    private Set<String> coordinates;

    public ClienDTO(Client client) {
        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.eMail = client.geteMail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.coordinates = valoresCoordenadas(client.getCoordinates().getValuesCard());
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

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoanDTO> loans) {
        this.loans = loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }

    public Set<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Set<String> coordinates) {
        this.coordinates = coordinates;
    }

    public Set<String> valoresCoordenadas(HashMap<String, String> valuesCard){
        Set<String> valores = new TreeSet<>();
        int i=1;
        for(i=1; i<4;i++){
            valores.add(valuesCard.get("A"+i));
            valores.add(valuesCard.get("B"+i));
            valores.add(valuesCard.get("C"+i));
        }
        return valores;
    }

}
