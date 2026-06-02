package com.portail.etudiant.service;

import com.portail.etudiant.model.Etudiant;
import com.portail.etudiant.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> findById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Optional<Etudiant> findByEmail(String email) {
        return etudiantRepository.findFirstByEmail(email);
    }

    public List<Etudiant> findForAdminDashboard(String search, String specialite) {
        String normalizedSearch = search == null ? "" : search.trim();
        String normalizedSpecialite = specialite == null ? "" : specialite.trim();
        return etudiantRepository.findForAdminDashboard(normalizedSearch, normalizedSpecialite);
    }

    public Etudiant save(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public void deleteById(Long id) {
        etudiantRepository.deleteById(id);
    }
}