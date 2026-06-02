package com.portail.etudiant.repository;

import com.portail.etudiant.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findFirstByEmail(String email);

    @Query("""
        SELECT e FROM Etudiant e
        WHERE UPPER(COALESCE(e.role, '')) <> 'ADMIN'
          AND (
              :search IS NULL OR :search = '' OR
              LOWER(COALESCE(e.nom, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(COALESCE(e.prenom, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(COALESCE(e.matricule, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(COALESCE(e.numeroNational, '')) LIKE LOWER(CONCAT('%', :search, '%'))
          )
          AND (
              :specialite IS NULL OR :specialite = '' OR
              LOWER(COALESCE(e.specialite, '')) = LOWER(:specialite)
          )
    """)
    List<Etudiant> findForAdminDashboard(@Param("search") String search, @Param("specialite") String specialite);
}