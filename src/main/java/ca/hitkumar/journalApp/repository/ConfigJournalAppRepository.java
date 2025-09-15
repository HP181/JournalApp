package ca.hitkumar.journalApp.repository;
import ca.hitkumar.journalApp.entity.ConfigJournalAppEntry;
import ca.hitkumar.journalApp.entity.UserEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

//controller ==> services ==> repository
public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntry, ObjectId> {

}
