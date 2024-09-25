package com.example.springtutorial.controllers;

import com.example.springtutorial.entity.JournalEntry;
import com.example.springtutorial.entity.User;
import com.example.springtutorial.services.JournalEntryService;
import com.example.springtutorial.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JournalEntryService journalEntryService;

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            userService.createUser(user);
           return new ResponseEntity("User Created Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get-all-users")
    public ResponseEntity<?> getAllUsers(){
        try{
           List<User> users =  userService.getAllUsers();
            return new ResponseEntity(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get-by-username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        try {
            User user =  userService.getUserByUsername(username);
           if(user!=null){
               return new ResponseEntity(user, HttpStatus.OK);
           }else{
               return new ResponseEntity("No user with specified username present", HttpStatus.OK);
           }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        try{
            User user =  userService.deleteUserByUsername(username);
            List<JournalEntry> userJEs = user.getJournalEntries();
            if(!userJEs.isEmpty() || userJEs!=null){
                for(JournalEntry j: userJEs){
                    journalEntryService.deleteById(j.getId());
                }
            }
            if(user!=null){
                return new ResponseEntity(user, HttpStatus.OK);
            }else {
                return new ResponseEntity("No user with specified username present", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateUserByUsername(@PathVariable String username, @RequestBody User user){
        try{
            User userDb = userService.getUserByUsername(username);
            if(userDb!=null){
                userDb.setUsername(user.getUsername());
                userDb.setPassword(user.getPassword());
                userService.createUser(userDb);
                return new ResponseEntity<>(userDb, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("No User Present", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
