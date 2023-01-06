package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.ClienDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClienDTO> getClients(){
        return clientRepository.findAll().stream().map(ClienDTO::new).collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public ClienDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(ClienDTO::new).orElse(null);
    }

    @RequestMapping("/clients/current")
    public ClienDTO getClient(Authentication authentication){
        return clientRepository.findByeMail(authentication.getName()).map(ClienDTO::new).orElse(null);
    }

    @RequestMapping(path = "/clients/update", method = RequestMethod.POST)
    public ResponseEntity<Object> updateClient(@RequestParam Long id, @RequestParam String phone, @RequestParam String address,
                                               @RequestParam String maritalStatus, @RequestParam String company){

        Client client = clientRepository.findById(id).orElse(null);

        if (phone.isEmpty() || address.isEmpty() || maritalStatus.isEmpty() || company.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (client != null) {
            client.setPhone(phone);
            client.setAddress(address);
            client.setMaritalStatus(maritalStatus);
            client.setCompany(company);
            clientRepository.save(client);
        }
        return new ResponseEntity<>("Se Modificó Correctamente El Cliente", HttpStatus.OK);
    }
}
