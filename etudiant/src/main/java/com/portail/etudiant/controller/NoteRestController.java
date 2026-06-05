package com.portail.etudiant.controller;

import com.portail.etudiant.model.Note;
import com.portail.etudiant.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteRestController {

    @Autowired
    private NoteService noteService;

    // GET all notes for a specific student
    @GetMapping("/etudiant/{etudiantId}")
    public List<Note> getNotesByEtudiant(@PathVariable Long etudiantId) {
        return noteService.findByEtudiantId(etudiantId);
    }

    // GET one note by ID
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteService.findById(id);
        return note.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create a note
    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.save(note);
    }

    // PUT update an existing note
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        return noteService.findById(id).map(existingNote -> {
            existingNote.setCode(noteDetails.getCode());
            existingNote.setModule(noteDetails.getModule());
            existingNote.setMatiere(noteDetails.getMatiere());
            existingNote.setCredit(noteDetails.getCredit());
            existingNote.setValeur(noteDetails.getValeur());
            existingNote.setNoteRattrapage(noteDetails.getNoteRattrapage());
            existingNote.setNoteDevoir(noteDetails.getNoteDevoir());
            existingNote.setSemestre(noteDetails.getSemestre());
            return ResponseEntity.ok(noteService.save(existingNote));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE a note
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (noteService.findById(id).isPresent()) {
            noteService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
