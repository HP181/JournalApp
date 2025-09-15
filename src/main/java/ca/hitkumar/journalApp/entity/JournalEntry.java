package ca.hitkumar.journalApp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "journal_entries")
//@Getter
//@Setter

@Data // will do as below automatically
//@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
public class JournalEntry {

    @Id
    private ObjectId id;
    private String title;
    private String content;
    private Date date;

    public JournalEntry(){
        this.date = new Date();
    }
}
