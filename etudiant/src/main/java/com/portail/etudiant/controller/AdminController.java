package com.portail.etudiant.controller;

import com.portail.etudiant.model.Etudiant;
import com.portail.etudiant.model.Note;
import com.portail.etudiant.service.EtudiantService;
import com.portail.etudiant.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String search, 
                            @RequestParam(required = false) String specialite, 
                            Model model) {
        String normalizedSearch = search != null ? search.trim() : "";
        String normalizedSpecialite = specialite != null ? specialite.trim() : "";
        List<Etudiant> filtered = new ArrayList<>(etudiantService.findForAdminDashboard(normalizedSearch, normalizedSpecialite));

        // Grouping: Level -> Specialite -> List<Etudiant>
        java.util.Map<String, java.util.Map<String, List<Etudiant>>> grouped = new java.util.LinkedHashMap<>();
        
        // Define order of levels
        String[] levels = {"L1", "L2", "L3", "M1", "M2"};
        for (String lv : levels) {
            grouped.put(lv, new java.util.TreeMap<>());
        }

        for (Etudiant e : filtered) {
            String lv = e.getNiveau() != null ? e.getNiveau() : "Autres";
            String spec = e.getSpecialite() != null ? e.getSpecialite() : "Sans Spécialité";
            
            if (!grouped.containsKey(lv)) {
                grouped.put(lv, new java.util.TreeMap<>());
            }
            
            java.util.Map<String, List<Etudiant>> specMap = grouped.get(lv);
            if (!specMap.containsKey(spec)) {
                specMap.put(spec, new ArrayList<>());
            }
            specMap.get(spec).add(e);
        }

        Set<String> specialites = new LinkedHashSet<>();
        Set<String> niveaux = new LinkedHashSet<>();
        for (Etudiant e : filtered) {
            if (e.getSpecialite() != null && !e.getSpecialite().trim().isEmpty()) {
                specialites.add(e.getSpecialite().trim());
            }
            if (e.getNiveau() != null && !e.getNiveau().trim().isEmpty()) {
                niveaux.add(e.getNiveau().trim());
            }
        }

        model.addAttribute("groupedEtudiants", grouped);
        model.addAttribute("totalStudents", filtered.size());
        model.addAttribute("totalSpecialites", specialites.size());
        model.addAttribute("totalNiveaux", niveaux.size());
        model.addAttribute("newEtudiant", new Etudiant());
        model.addAttribute("search", normalizedSearch);
        model.addAttribute("specialite", normalizedSpecialite);
        return "admin_dashboard";
    }

    @PostMapping("/etudiant/add")
    public String addEtudiant(@ModelAttribute Etudiant etudiant) {
        if (etudiant.getEmail() != null) etudiant.setEmail(etudiant.getEmail().trim());
        if (etudiant.getNumeroNational() != null) etudiant.setNumeroNational(etudiant.getNumeroNational().trim());
        if (etudiant.getMatricule() != null) etudiant.setMatricule(etudiant.getMatricule().trim());

        // Prevent duplicate creation if an admin double clicks
        Optional<Etudiant> existing = etudiantService.findAll().stream()
            .filter(e -> e.getEmail().equalsIgnoreCase(etudiant.getEmail()))
            .findFirst();
            
        if (existing.isPresent()) {
            // Overwrite existing instead of duplicating
            Etudiant e = existing.get();
            e.setMatricule(etudiant.getMatricule());
            e.setNumeroNational(etudiant.getNumeroNational());
            e.setNom(etudiant.getNom());
            e.setPrenom(etudiant.getPrenom());
            e.setSpecialite(etudiant.getSpecialite());
            e.setNiveau(etudiant.getNiveau());
            e.setRole(etudiant.getRole());
            
            if (etudiant.getNumeroNational() != null && !etudiant.getNumeroNational().isEmpty()) {
                e.setPassword(passwordEncoder.encode(etudiant.getNumeroNational()));
            }
            etudiantService.save(e);
            return "redirect:/admin/dashboard";
        }

        // Enforce NNI as password for all new users created by Admin
        if (etudiant.getNumeroNational() != null && !etudiant.getNumeroNational().isEmpty()) {
            etudiant.setPassword(passwordEncoder.encode(etudiant.getNumeroNational()));
        } else {
            // Fallback just in case, though NNI is required in UI
            etudiant.setPassword(passwordEncoder.encode("123456")); 
        }

        if(etudiant.getRole() == null || etudiant.getRole().isEmpty()){
            etudiant.setRole("USER");
        }
        etudiantService.save(etudiant);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/etudiant/{id}")
    public String etudiantDetails(@PathVariable Long id, Model model) {
        Optional<Etudiant> etudiantOpt = etudiantService.findById(id);
        if (etudiantOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            List<Note> allNotes = noteService.findByEtudiantId(id);
            
            // Group by Semester
            java.util.Map<String, List<Note>> groupedNotes = new java.util.TreeMap<>();
            for (Note n : allNotes) {
                String sem = n.getSemestre() != null ? n.getSemestre() : "Inconnu";
                groupedNotes.computeIfAbsent(sem, k -> new ArrayList<>()).add(n);
            }
            
            model.addAttribute("etudiant", etudiant);
            model.addAttribute("groupedNotes", groupedNotes);
            model.addAttribute("newNote", new Note());
            return "admin_etudiant";
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/etudiant/edit/{id}")
    public String editEtudiantForm(@PathVariable Long id, Model model) {
        Optional<Etudiant> etudiantOpt = etudiantService.findById(id);
        if (etudiantOpt.isPresent()) {
            model.addAttribute("etudiant", etudiantOpt.get());
            return "admin_etudiant_edit";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/etudiant/edit/{id}")
    public String updateEtudiant(@PathVariable Long id, @ModelAttribute Etudiant etudiant) {
        Optional<Etudiant> existingOpt = etudiantService.findById(id);
        if (existingOpt.isPresent()) {
            Etudiant existing = existingOpt.get();
            existing.setMatricule(etudiant.getMatricule() != null ? etudiant.getMatricule().trim() : existing.getMatricule());
            existing.setNumeroNational(etudiant.getNumeroNational() != null ? etudiant.getNumeroNational().trim() : existing.getNumeroNational());
            existing.setNom(etudiant.getNom() != null ? etudiant.getNom().trim() : existing.getNom());
            existing.setPrenom(etudiant.getPrenom() != null ? etudiant.getPrenom().trim() : existing.getPrenom());
            existing.setEmail(etudiant.getEmail() != null ? etudiant.getEmail().trim() : existing.getEmail());
            existing.setSpecialite(etudiant.getSpecialite());
            existing.setNiveau(etudiant.getNiveau());
            existing.setRole(etudiant.getRole());
            
            if (etudiant.getNumeroNational() != null && !etudiant.getNumeroNational().trim().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(etudiant.getNumeroNational().trim()));
            }
            etudiantService.save(existing);
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/note/add/{etudiantId}")
    public String addNote(@PathVariable Long etudiantId, @ModelAttribute Note note) {
        Optional<Etudiant> etudiant = etudiantService.findById(etudiantId);
        if (etudiant.isPresent()) {
            note.setEtudiant(etudiant.get());
            noteService.save(note);
        }
        return "redirect:/admin/etudiant/" + etudiantId;
    }

    @PostMapping("/note/generate/{etudiantId}")
    public String generateNotes(@PathVariable Long etudiantId, @RequestParam(defaultValue = "3") String semestre) {
        Optional<Etudiant> etudiantOpt = etudiantService.findById(etudiantId);
        if(etudiantOpt.isPresent()){
            Etudiant etudiant = etudiantOpt.get();
            String[][] template = new String[0][0];
            
            // Templates by semester (Placeholder data for S1, S2, S4, S5, S6 - Needs to be updated with real data)
            switch (semestre) {
                case "1":
                    template = new String[][] {
                        {"ALG110", "Mathématiques et Informatique", "Algorithmique et Programmation C", "3"},
                        {"MAT110", "Mathématiques et Informatique", "Analyse Mathématique I", "3"},
                        {"PHY110", "Physique", "Physique Générale", "2"},
                        {"DPR110", "Développement personnel", "Communication I", "2"}
                    };
                    break;
                case "2":
                    template = new String[][] {
                        {"ALG210", "Mathématiques et Informatique", "Structures de données en C", "3"},
                        {"MAT210", "Mathématiques et Informatique", "Algèbre Linéaire", "3"},
                        {"WEB210", "Développement Web", "Introduction au Web (HTML/CSS)", "2"},
                        {"DPR210", "Développement personnel", "Anglais I", "2"}
                    };
                    break;
                case "3":
                    template = new String[][] {
                        {"DAS311", "Science des données", "Recherche opérationnelle", "2"},
                        {"DAS310", "Science des données", "Machine learning", "2"},
                        {"DPR310", "Développement personnel", "Communication III", "4"},
                        {"DPR311", "Développement personnel", "Anglais III", "4"},
                        {"DPR312", "Développement personnel", "Projet personnel et professionnel III", "1"},
                        {"DPR313", "Développement personnel", "Gestion d'entreprise", "2"},
                        {"PAV310", "Programmation Avancée", "Programmation Orientée Objets Java", "2"},
                        {"PAV311", "Programmation Avancée", "Structure de données et Complexité algo.", "2"},
                        {"PAV312", "Programmation Avancée", "Projet Intégrateur Avancé I", "3"},
                        {"RSS310", "Outils Réseaux et Systèmes", "Introduction aux Réseaux Mobiles", "2"},
                        {"RSS311", "Outils Réseaux et Systèmes", "Administration systèmes et réseaux", "2"},
                        {"RSS320", "Sécurité et Base de données", "Introduction à la sécurité info.", "2"},
                        {"RSS321", "Sécurité et Base de données", "Bases de données et conception des SI", "2"}
                    };
                    break;
                case "4":
                    template = new String[][] {
                        {"WEB410", "Développement Avancé", "Programmation Web Avancée (PHP)", "3"},
                        {"BDD410", "Bases de Données", "SGBD Avancés", "3"},
                        {"RES410", "Réseaux", "Réseaux Locaux", "3"},
                        {"DPR410", "Développement personnel", "Anglais IV", "2"}
                    };
                    break;
                case "5":
                    template = new String[][] {
                        {"SEC510", "Sécurité Informatique", "Cryptographie", "3"},
                        {"RES510", "Réseaux Avancés", "Routage et Commutation", "3"},
                        {"SYS510", "Systèmes d'exploitation", "Administration Linux", "3"},
                        {"PRJ510", "Projet", "Mini-Projet de fin d'année", "4"}
                    };
                    break;
                case "6":
                    template = new String[][] {
                        {"STG610", "Stage", "Stage de Fin d'Études", "15"},
                        {"MGT610", "Management", "Gestion de Projet", "3"}
                    };
                    break;
            }

            // Clear existing notes for this specific semester before generating to avoid duplicates
            List<Note> existingNotes = noteService.findByEtudiantId(etudiantId);
            for (Note oldNote : existingNotes) {
                if (semestre.equals(oldNote.getSemestre())) {
                    noteService.deleteById(oldNote.getId());
                }
            }

            for(String[] t : template) {
                Note n = new Note();
                n.setEtudiant(etudiant);
                n.setSemestre(semestre);
                n.setCode(t[0]);
                n.setModule(t[1]);
                n.setMatiere(t[2]);
                n.setCredit(Integer.parseInt(t[3]));
                noteService.save(n);
            }
        }
        return "redirect:/admin/etudiant/" + etudiantId;
    }

    @PostMapping("/note/update/{noteId}")
    public String updateNote(@PathVariable Long noteId, 
                             @RequestParam Long etudiantId,
                             @RequestParam(required = false) Double noteDevoir,
                             @RequestParam(required = false) Double valeur,
                             @RequestParam(required = false) Double noteRattrapage) {
        Optional<Note> noteOpt = noteService.findById(noteId);
        if(noteOpt.isPresent()) {
            Note n = noteOpt.get();
            n.setNoteDevoir(noteDevoir);
            n.setValeur(valeur);
            n.setNoteRattrapage(noteRattrapage);
            noteService.save(n);
        }
        return "redirect:/admin/etudiant/" + etudiantId;
    }

    @PostMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable Long noteId, @RequestParam Long etudiantId) {
        noteService.deleteById(noteId);
        return "redirect:/admin/etudiant/" + etudiantId;
    }

    @PostMapping("/etudiant/delete/{id}")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/parametres")
    public String parametres(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Etudiant> adminOpt = etudiantService.findAll().stream()
            .filter(e -> e.getEmail().equals(email))
            .findFirst();
        
        if (adminOpt.isPresent()) {
            model.addAttribute("admin", adminOpt.get());
            return "admin_parametres";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/parametres/update")
    public String updateParametres(Authentication authentication, 
                                   @RequestParam String nom,
                                   @RequestParam String prenom,
                                   @RequestParam(required = false) String newPassword,
                                   RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Etudiant> adminOpt = etudiantService.findAll().stream()
            .filter(e -> e.getEmail().equals(email))
            .findFirst();
        
        if (adminOpt.isPresent()) {
            Etudiant admin = adminOpt.get();
            admin.setNom(nom);
            admin.setPrenom(prenom);
            
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                admin.setPassword(passwordEncoder.encode(newPassword.trim()));
            }
            
            etudiantService.save(admin);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès !");
        }
        return "redirect:/admin/parametres";
    }
}
