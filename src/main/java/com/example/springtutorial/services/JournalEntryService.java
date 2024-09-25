package com.example.springtutorial.services;

import com.example.springtutorial.entity.JournalEntry;
import com.example.springtutorial.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry saveEntry(JournalEntry e){
        return journalEntryRepository.save(e);

    }

    public List<JournalEntry> getAllEntries(){
        return journalEntryRepository.findAll();
    }
    public JournalEntry getById(ObjectId id){
        return journalEntryRepository.findById(id).orElse(null);
    }

    public JournalEntry deleteById(ObjectId id){
        JournalEntry res = getById(id);
         if(res!=null){
             journalEntryRepository.deleteById(id);
         }
             return res;
    }
}
