package ca.hitkumar.journalApp.controller;

import ca.hitkumar.journalApp.entity.JournalEntry;
import ca.hitkumar.journalApp.entity.UserEntry;
import ca.hitkumar.journalApp.services.JournalEntryService;
import ca.hitkumar.journalApp.services.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/journal")
public class JournalEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserEntryService userEntryService;

    @PostMapping
    public ResponseEntity<JournalEntry> saveEntry(@RequestBody JournalEntry journalEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
              journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<JournalEntry>(journalEntry, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<JournalEntry>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserEntry user = userEntryService.findByUsername(userName);
        List<JournalEntry> journalEntriesOfUser = user.getJournalEntries();

        if(journalEntriesOfUser != null && !journalEntriesOfUser.isEmpty()){
            return new ResponseEntity<>(journalEntriesOfUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId id){
        Optional<JournalEntry> journalEntry = journalEntryService.getById(id);

//        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId Id, @RequestBody JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserEntry userEntry = userEntryService.findByUsername(userName);
        Optional<JournalEntry> getJournalEntriesByUser = userEntry.getJournalEntries().stream().filter(x -> x.getId().equals(Id)).findFirst();

        if(getJournalEntriesByUser.isPresent()){
            JournalEntry oldEntry = getJournalEntriesByUser.get();

            oldEntry.setTitle((newEntry.getTitle() == null || newEntry.getTitle().isEmpty())
                    ? oldEntry.getTitle()
                    : newEntry.getTitle());

            oldEntry.setContent((newEntry.getContent() == null || newEntry.getContent().isEmpty())
                    ? oldEntry.getContent()
                    : newEntry.getContent());

            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean deleted = journalEntryService.deleteById(id, username);

        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }
    }

}
