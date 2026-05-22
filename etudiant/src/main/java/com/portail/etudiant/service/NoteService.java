package com.portail.etudiant.service;

import com.portail.etudiant.model.Note;
import com.portail.etudiant.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> findByEtudiantId(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }
}