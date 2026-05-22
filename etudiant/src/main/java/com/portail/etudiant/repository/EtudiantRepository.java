package com.portail.etudiant.repository;

import com.portail.etudiant.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findFirstByEmail(String email);
}