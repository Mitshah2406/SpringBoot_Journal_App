package com.example.springtutorial.services;

import com.example.springtutorial.entity.JournalEntry;
import com.example.springtutorial.entity.User;
import com.example.springtutorial.repository.JournalEntryRepository;
import com.example.springtutorial.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    JournalEntryService journalEntryService;
    public void createUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }
    public User deleteUserByUsername(String username){
        User user = getUserByUsername(username);
        if(user!=null){
            userRepository.deleteById(user.getId());
return user;
        }
        return null;
    }
    @Transactional
    public JournalEntry addJournalEntryToRespectiveUser(String username, JournalEntry je){
        User uInDb = getUserByUsername(username);

        if(uInDb!=null){
            JournalEntry saved = journalEntryService.saveEntry(je);
            List<JournalEntry> journalEntries = uInDb.getJournalEntries();
            journalEntries.add(je);
            uInDb.setJournalEntries(journalEntries);
            uInDb.setUsername(null);
            createUser(uInDb);

            return saved;
        }
        return null;
    }

}
