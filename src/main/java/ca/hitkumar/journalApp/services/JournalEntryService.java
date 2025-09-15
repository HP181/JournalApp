package ca.hitkumar.journalApp.services;

import ca.hitkumar.journalApp.entity.JournalEntry;
import ca.hitkumar.journalApp.entity.UserEntry;
import ca.hitkumar.journalApp.repository.JournalEntryRepository;
import ca.hitkumar.journalApp.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserEntryService userEntryService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
        UserEntry user = userEntryService.findByUsername(username);
        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(savedJournalEntry);
//        user.setUsername(null);
        userEntryService.saveEntry(user);
//        return savedJournalEntry;
        } catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while saving the entry");
        }
        return;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        UserEntry user = userEntryService.findByUsername(username);
        if (user == null) return false;

        Optional<JournalEntry> entryOpt = user.getJournalEntries()
                .stream()
                .filter(entry -> entry.getId().equals(id)).findFirst();

        if (entryOpt.isEmpty()) {
            // Entry does not exist or does not belong to this user
            return false;
        }

        // Safe to delete
        journalEntryRepository.deleteById(id);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
        userEntryService.saveEntry(user);

        return true;
    }


//    @GetMapping("/{id}")
//    public Optional<JournalEntry> updateById(ObjectId id, JournalEntry){
//        Optional<JournalEntry> anc = journalEntryRepository.findById(id);
//
//    }

}
