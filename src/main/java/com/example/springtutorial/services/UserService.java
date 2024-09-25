package com.example.springtutorial.services;

import com.example.springtutorial.entity.JournalEntry;
import com.example.springtutorial.entity.User;
import com.example.springtutorial.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

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

    public User addJournalEntryToRespectiveUser(String username, JournalEntry je){
        User uInDb = getUserByUsername(username);

        if(uInDb!=null){
            List<JournalEntry> journalEntries = uInDb.getJournalEntries();
            journalEntries.add(je);
            uInDb.setJournalEntries(journalEntries);
            createUser(uInDb);

            return uInDb;
        }
        return uInDb;
    }

}
