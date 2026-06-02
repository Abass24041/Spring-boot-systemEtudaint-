package com.portail.etudiant.controller;

import com.portail.etudiant.model.Etudiant;
import com.portail.etudiant.model.Note;
import com.portail.etudiant.service.EtudiantService;
import com.portail.etudiant.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private NoteService noteService;

    public static class ModuleDto {
        private String nomModule;
        private int totalCredits;
        private double moyenneModule;
        private boolean valide;
        private List<Note> elements = new ArrayList<>();

        public String getNomModule() { return nomModule; }
        public void setNomModule(String nomModule) { this.nomModule = nomModule; }
        public int getTotalCredits() { return totalCredits; }
        public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }
        public double getMoyenneModule() { return moyenneModule; }
        public void setMoyenneModule(double moyenneModule) { this.moyenneModule = moyenneModule; }
        public boolean isValide() { return valide; }
        public void setValide(boolean valide) { this.valide = valide; }
        public List<Note> getElements() { return elements; }
        public void setElements(List<Note> elements) { this.elements = elements; }
    }

    private Optional<Etudiant> currentStudent(Authentication authentication) {
        if (authentication == null) {
            return Optional.empty();
        }
        return etudiantService.findByEmail(authentication.getName());
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Optional<Etudiant> etudiant = currentStudent(authentication);
        if (etudiant.isPresent()) {
            model.addAttribute("etudiant", etudiant.get());
            return "dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/notes")
    public String voirNotes(Authentication authentication,
                            @RequestParam(required = false) String semestre,
                            Model model) {
        Optional<Etudiant> etudiantOpt = currentStudent(authentication);
        if (etudiantOpt.isEmpty()) {
            return "redirect:/login";
        }

        Etudiant etudiant = etudiantOpt.get();
        Long id = etudiant.getId();
        List<Note> allNotes = noteService.findByEtudiantId(id);
        List<Note> notesSemestre = new ArrayList<>();

        if (semestre != null && !semestre.isEmpty()) {
            for (Note n : allNotes) {
                if (semestre.equals(n.getSemestre())) {
                    notesSemestre.add(n);
                }
            }
        }

        if (notesSemestre.isEmpty() && semestre != null) {
            model.addAttribute("errorMessage",
                "Aucun résultat disponible pour le semestre " + semestre + ".");
        }

        Map<String, ModuleDto> modulesMap = new LinkedHashMap<>();
        for (Note n : notesSemestre) {
            String modName = n.getModule() != null ? n.getModule() : "Module Indéfini";
            ModuleDto dto = modulesMap.computeIfAbsent(modName, k -> {
                ModuleDto m = new ModuleDto();
                m.setNomModule(k);
                return m;
            });
            dto.getElements().add(n);
        }

        double sommeMoyennesPonderees = 0;
        int totalCreditsSemestre = 0;

        for (ModuleDto dto : modulesMap.values()) {
            int modCredits = 0;
            double modSomme = 0;
            for (Note n : dto.getElements()) {
                double val = n.getMaxNote();
                int c = n.getCredit() != null ? n.getCredit() : 1;
                modSomme += (val * c);
                modCredits += c;
            }

            dto.setTotalCredits(modCredits);
            if (modCredits > 0) {
                dto.setMoyenneModule(modSomme / modCredits);
            }
            dto.setValide(dto.getMoyenneModule() >= 10);

            sommeMoyennesPonderees += (dto.getMoyenneModule() * modCredits);
            totalCreditsSemestre += modCredits;
        }

        double moyenneGenerale = 0;
        if (totalCreditsSemestre > 0) {
            moyenneGenerale = sommeMoyennesPonderees / totalCreditsSemestre;
        }

        model.addAttribute("etudiant", etudiant);
        model.addAttribute("modules", modulesMap.values());
        model.addAttribute("semestre", semestre);
        model.addAttribute("moyenne", moyenneGenerale);
        model.addAttribute("totalCredits", totalCreditsSemestre);

        return "notes";
    }
}
