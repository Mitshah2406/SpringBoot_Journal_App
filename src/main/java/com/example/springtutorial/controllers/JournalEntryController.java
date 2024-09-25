package com.example.springtutorial.controllers;

import com.example.springtutorial.entity.JournalEntry;
import com.example.springtutorial.entity.User;
import com.example.springtutorial.repository.JournalEntryRepository;
import com.example.springtutorial.services.JournalEntryService;
import com.example.springtutorial.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mitshah")
public class JournalEntryController {
    @Autowired
    public JournalEntryService journalEntryService;
    @Autowired
    public UserService userService;
    @PostMapping("create/{username}")
    public ResponseEntity<?> createEntry(@PathVariable String username, @RequestBody JournalEntry je){
       try{
           LocalDateTime d = LocalDateTime.now();
           je.setCreated_at(d);

           User u = userService.getUserByUsername(username);
           if(u!=null){
               JournalEntry newJE = journalEntryService.saveEntry(je);
               userService.addJournalEntryToRespectiveUser(username, newJE);
               return new ResponseEntity<>(newJE, HttpStatus.CREATED);
           }else{
               return new ResponseEntity<>("User not present", HttpStatus.NOT_FOUND);
           }

       } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }


    @GetMapping("get-all-entries/{username}")
    public ResponseEntity<?> getAllEntries(@PathVariable String username){
//        return journalEntryService.getAllEntries();
        try{


            User u = userService.getUserByUsername(username);
            if(u!=null){
               List<JournalEntry> je  =  u.getJournalEntries();

                return new ResponseEntity<>(je, HttpStatus.OK);

            }else{
                return new ResponseEntity<>("User not present", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get-by-id/{id}")
    public JournalEntry getById(@PathVariable ObjectId id){

        return journalEntryService.getById(id);
    }

    @DeleteMapping("delete-entry-of-user/{id}/{username}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id, @PathVariable String username){
    try{
        JournalEntry del = journalEntryService.deleteById(id);
        User u = userService.getUserByUsername(username);
        if(del==null || u==null){
            return new ResponseEntity<>("User/Journal Entry Doesnt Exist", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<JournalEntry> userJEs = u.getJournalEntries();
        userJEs.removeIf(x-> x.getId().equals(id));
        return new ResponseEntity<>(del, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    @PutMapping("update-by-id/{id}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId id, @RequestBody JournalEntry newJE){
        try{
            JournalEntry oldJE = journalEntryService.getById(id);
            if(oldJE!=null){
                oldJE.setTitle(newJE.getTitle());
                oldJE.setContent(newJE.getContent());
                journalEntryService.saveEntry(oldJE);
                return new ResponseEntity<>(oldJE, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Journal Entry Does Not Exist", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
