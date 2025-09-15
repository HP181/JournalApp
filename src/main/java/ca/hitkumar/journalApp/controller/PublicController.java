package ca.hitkumar.journalApp.controller;

import ca.hitkumar.journalApp.entity.UserEntry;
import ca.hitkumar.journalApp.services.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private UserEntryService userEntryService;

    @GetMapping("/health-check")
    public String HealthCheck(){
        return "OK";
    }

    @PostMapping
    public ResponseEntity<Boolean> saveEntry(@RequestBody UserEntry userEntry) {
        try {
            boolean savedEntry = userEntryService.saveNewUser(userEntry);
            return new ResponseEntity<Boolean>(savedEntry, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

    }
}
