package com.myproject.journalApp.entity;

import com.myproject.journalApp.enums.Sentiment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor // this is required here since without it it will not be called and it is required during
// deserialization which is JSON -> POJO
public class JournalEntry  { // POJO - Plain Old Java Object

    @Id
    private ObjectId id; // datatype of Mongodb ID
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;



    // Now using Lombok Annotation, hence removed the manual code for getters and setters
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }
//
//    public LocalDateTime getDate() {
//        return date;
//    }
//
//    public ObjectId getId() {
//        return id;
//    }
//
//    public void setId(ObjectId id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
}