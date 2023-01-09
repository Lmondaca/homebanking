package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.CoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CoordinateController {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CoordinateRepository coordinateRepository;

    @RequestMapping(path = "/clients/current/coordinates", method = RequestMethod.POST)
    public ResponseEntity<Object> nuevaCoordenadas(@RequestParam Long id,Authentication authentication){
        Client currentClient = clientRepository.findByeMail(authentication.getName()).orElse(null);
        if(currentClient == null){
            return new ResponseEntity<>("Cliente no autentificado", HttpStatus.FORBIDDEN);
        }
        Coordinate coordinate = new Coordinate(generarValores());
        coordinate.setClient(currentClient);
        currentClient.setCoordinates(coordinate);

        coordinateRepository.save(coordinate);

        clientRepository.save(currentClient);
        return new ResponseEntity<>("Coordenadas Creada.", HttpStatus.CREATED);
    }

    private HashMap<String, String> generarValores(){
        HashMap<String, String> values = new HashMap<>();
        Random random = new Random();
        int columns = 0;
        String rows = "";
        for(int i=0; i<3;i++){
            columns = random.nextInt(99);
            rows = "A"+(i+1);
            values.put(rows,String.format("%02d", columns));
        }
        for(int i=0; i<3;i++){
            columns = random.nextInt(99);
            rows = "B"+(i+1);
            values.put(rows,String.format("%02d", columns));
        }
        for(int i=0; i<3;i++){
            columns = random.nextInt(99);
            rows = "C"+(i+1);
            values.put(rows,String.format("%02d", columns));
        }

        return values;
    }
}