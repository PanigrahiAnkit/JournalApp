package com.myproject.journalApp.controller;

import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.service.JournalEntryService;
import com.myproject.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/journal")
@Tag(
        name = "Journal Entry APIs",
        description = "Endpoints for managing user journal entries: create, read, update, and delete operations."
)
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    @Operation(
            summary = "Get all journal entries",
            description = "Retrieves all journal entries for the authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Entries found")
    @ApiResponse(responseCode = "404", description = "No entries found")
    public ResponseEntity<?> getAllJournalEntriesOfUser() { // localhost:8080/journal GET
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(
            summary = "Create a journal entry",
            description = "Creates a new journal entry for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Entry created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) { //
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myID}")
    @Operation(
            summary = "Get journal entry by ID",
            description = "Retrieves a specific journal entry by its ID for the authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Entry found")
    @ApiResponse(responseCode = "404", description = "Entry not found")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myID) {
        ObjectId objectId = new ObjectId(myID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect =
                user.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findByID(objectId); // Introduced a variable using
            // Ctrl+Alt+V
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myID}")
    @Operation(
            summary = "Delete journal entry by ID",
            description = "Deletes a specific journal entry by its ID for the authenticated user."
    )

    @ApiResponse(responseCode = "204", description = "Entry deleted")
    @ApiResponse(responseCode = "404", description = "Entry not found")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String myID) { // "?" : Refers to wildcard entry
        ObjectId objectId = new ObjectId(myID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(objectId, userName);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{myID}")
    @Operation(
            summary = "Update journal entry by ID",
            description = "Updates a specific journal entry by its ID for the authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Entry updated")
    @ApiResponse(responseCode = "404", description = "Entry not found")
    public ResponseEntity<?> updateJournalById(
            @PathVariable String myID,
            @RequestBody JournalEntry newEntry
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        ObjectId objectId = new ObjectId(myID);
        List<JournalEntry> collect =
                user.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findByID(objectId); // Introduced a variable using
            // Ctrl+Alt+V
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());

                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }

        JournalEntry old = journalEntryService.findByID(objectId).orElse(null);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}