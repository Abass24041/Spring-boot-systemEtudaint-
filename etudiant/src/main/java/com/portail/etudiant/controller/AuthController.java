package com.portail.etudiant.controller;

import com.portail.etudiant.model.Etudiant;
import com.portail.etudiant.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Etudiant> etudiant = etudiantService.findByEmail(email);
            if (etudiant.isPresent()) {
                if ("ADMIN".equals(etudiant.get().getRole())) {
                    return "redirect:/admin/dashboard";
                }
                return "redirect:/etudiant/dashboard/" + etudiant.get().getId();
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}