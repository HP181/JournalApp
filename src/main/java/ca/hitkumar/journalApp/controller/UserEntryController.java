package ca.hitkumar.journalApp.controller;

import ca.hitkumar.journalApp.api.response.WeatherResponse;
import ca.hitkumar.journalApp.entity.JournalEntry;
import ca.hitkumar.journalApp.entity.UserEntry;
import ca.hitkumar.journalApp.services.JournalEntryService;
import ca.hitkumar.journalApp.services.UserEntryService;
import ca.hitkumar.journalApp.services.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserEntryController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private WeatherService weatherService;



//    @GetMapping
//    public ResponseEntity<?> getAll() {
//        List<UserEntry> allEntries = userEntryService.getAllEntries();
//
//        if(allEntries != null && !allEntries.isEmpty()){
//            return new ResponseEntity<>(allEntries, HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @GetMapping
    public ResponseEntity<?> greeting(@RequestParam String city){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Username = authentication.getName();

        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String greeting = "";
        if(weatherResponse != null){
            greeting = ", Weather Feels Like " + weatherResponse.getCurrent().getFeelslike() + " With Temperature of " + weatherResponse.getCurrent().getTemperature();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntry> getById(@PathVariable ObjectId id){
        Optional<UserEntry> userEntry = userEntryService.getById(id);

//        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        if(userEntry.isPresent()){
            return new ResponseEntity<>(userEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<?> updateById( @RequestBody UserEntry user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Username = authentication.getName();
        UserEntry userInDb = userEntryService.findByUsername(Username);

            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userEntryService.saveNewUser(userInDb);
            return new ResponseEntity<UserEntry>(userInDb, HttpStatus.OK);

//        return new ResponseEntity<UserEntry>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping
    public ResponseEntity<?> DeleteById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userEntryService.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
