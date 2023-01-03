package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    Optional<Coordinates> findById(Long id);
}
