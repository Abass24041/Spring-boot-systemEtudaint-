package com.portail.etudiant.controller;

import com.portail.etudiant.model.Etudiant;
import com.portail.etudiant.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/etudiants")
public class StudentRestController {

    @Autowired
    private EtudiantService etudiantService;

    // GET all students: http://localhost:8080/api/etudiants
    @GetMapping
    public List<Etudiant> getAllStudents() {
        return etudiantService.findAll();
    }

    // GET one student: http://localhost:8080/api/etudiants/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getStudentById(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);
        return etudiant.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create student: http://localhost:8080/api/etudiants
    // Send JSON Body: { "nom": "Test", "prenom": "User", "email": "test@api.com", ... }
    @PostMapping
    public Etudiant createStudent(@RequestBody Etudiant etudiant) {
        return etudiantService.save(etudiant);
    }

    // PUT complete update: http://localhost:8080/api/etudiants/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateStudent(@PathVariable Long id, @RequestBody Etudiant etudiantDetails) {
        return etudiantService.findById(id).map(existing -> {
            existing.setNom(etudiantDetails.getNom());
            existing.setPrenom(etudiantDetails.getPrenom());
            existing.setEmail(etudiantDetails.getEmail());
            existing.setMatricule(etudiantDetails.getMatricule());
            existing.setNumeroNational(etudiantDetails.getNumeroNational());
            existing.setTelephone(etudiantDetails.getTelephone());
            existing.setSpecialite(etudiantDetails.getSpecialite());
            existing.setNiveau(etudiantDetails.getNiveau());
            existing.setDateNaissance(etudiantDetails.getDateNaissance());
            existing.setLieuNaissance(etudiantDetails.getLieuNaissance());
            existing.setPays(etudiantDetails.getPays());
            return ResponseEntity.ok(etudiantService.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PATCH partial update: http://localhost:8080/api/etudiants/{id}
    // Used to update only one or two fields without sending the whole object
    @PatchMapping("/{id}")
    public ResponseEntity<Etudiant> patchStudent(@PathVariable Long id, @RequestBody java.util.Map<String, Object> updates) {
        return etudiantService.findById(id).map(existing -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "nom": existing.setNom((String) value); break;
                    case "prenom": existing.setPrenom((String) value); break;
                    case "email": existing.setEmail((String) value); break;
                    case "specialite": existing.setSpecialite((String) value); break;
                    case "niveau": existing.setNiveau((String) value); break;
                }
            });
            return ResponseEntity.ok(etudiantService.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Note: Spring Boot handles HEAD and OPTIONS automatically. 
    // HEAD will return only headers for a GET request.
    // OPTIONS will return "Allow: GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD".

    // DELETE student: http://localhost:8080/api/etudiants/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (etudiantService.findById(id).isPresent()) {
            etudiantService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
