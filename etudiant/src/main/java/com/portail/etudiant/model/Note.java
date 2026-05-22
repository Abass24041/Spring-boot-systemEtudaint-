package com.portail.etudiant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String module;
    private String matiere;
    private Integer credit;
    private Double valeur;
    private Double noteRattrapage;
    private Double noteDevoir;
    private String semestre;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    @JsonBackReference
    private Etudiant etudiant;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }

    public String getMatiere() { return matiere; }
    public void setMatiere(String matiere) { this.matiere = matiere; }

    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }

    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }

    public Double getNoteRattrapage() { return noteRattrapage; }
    public void setNoteRattrapage(Double noteRattrapage) { this.noteRattrapage = noteRattrapage; }

    public Double getNoteDevoir() { return noteDevoir; }
    public void setNoteDevoir(Double noteDevoir) { this.noteDevoir = noteDevoir; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }

    public Double getMaxNote() {
        double examen = (valeur != null) ? valeur : 0.0;
        double devoir = (noteDevoir != null) ? noteDevoir : 0.0;
        double finaleNormale = (examen * 0.6) + (devoir * 0.4);
        
        if (noteRattrapage != null && noteRattrapage > 0) {
            double finaleRattrapage = (noteRattrapage * 0.6) + (devoir * 0.4);
            return Math.max(finaleNormale, finaleRattrapage);
        }
        
        return finaleNormale;
    }
}