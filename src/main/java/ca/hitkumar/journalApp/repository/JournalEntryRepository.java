package ca.hitkumar.journalApp.repository;
import ca.hitkumar.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

//controller ==> services ==> repository
public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
}
