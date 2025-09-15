package ca.hitkumar.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
//@Getter
//@Setter
@Data // will do as below automatically
//@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntry {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    private String email;
    private boolean sentimentAnalysis;

    @NonNull
    private String password;
    @NonNull
    private Date date = new Date();
    private List<String> roles;
    @DBRef
    private List<JournalEntry> journalEntries= new ArrayList<>();

}
