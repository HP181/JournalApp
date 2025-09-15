package ca.hitkumar.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "config_journal_app")
//@Getter
//@Setter

@Data // will do as below automatically
//@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@NoArgsConstructor
public class ConfigJournalAppEntry {

    private String key;
    private String value;

}
