package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;

@Entity
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToOne(mappedBy = "coordinates")
    private Client client;
    private HashMap<String,String> valuesCard;

    public Coordinates (){

    }

    public Coordinates (HashMap<String,String> valuesCard){
        this.valuesCard = valuesCard;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HashMap<String, String> getValuesCard() {
        return valuesCard;
    }

    public void setValuesCard(HashMap<String, String> values) {
        this.valuesCard = valuesCard;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
