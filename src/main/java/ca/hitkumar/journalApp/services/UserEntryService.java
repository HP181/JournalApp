package ca.hitkumar.journalApp.services;


import ca.hitkumar.journalApp.entity.JournalEntry;
import ca.hitkumar.journalApp.entity.UserEntry;
import ca.hitkumar.journalApp.repository.JournalEntryRepository;
import ca.hitkumar.journalApp.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntryService {

    @Autowired
    private UserEntryRepository userEntryRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserEntryService.class);

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntry saveEntry(UserEntry userEntry) {
        return userEntryRepository.save(userEntry);
    }

    public boolean saveNewUser(UserEntry userEntry){
        try {
            userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
            userEntry.setRoles(Arrays.asList("USER"));
            userEntryRepository.save(userEntry);
            return true;
        } catch (Exception e){
            logger.info("Hahahahaha");
            System.out.println(e);
            return false;
        }
    }

    public UserEntry saveAdmin(UserEntry user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        return userEntryRepository.save(user);
    }

    public List<UserEntry> getAllEntries() {
        return userEntryRepository.findAll();
    }

    public Optional<UserEntry> getById(ObjectId id) {
        return userEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id) {
        userEntryRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        UserEntry user = findByUsername(username);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        for (JournalEntry i: journalEntries){
            journalEntryRepository.deleteAllById(Collections.singleton(i.getId()));
        }
//        List<JournalEntry> allById = journalEntryRepository.
        userEntryRepository.deleteById(user.getId());
        return true;
    }

    public UserEntry findByUsername(String username){
        return userEntryRepository.findByUsername(username);
    }

//    @GetMapping("/{id}")
//    public Optional<JournalEntry> updateById(ObjectId id, JournalEntry){
//        Optional<JournalEntry> anc = journalEntryRepository.findById(id);
//
//    }

}
