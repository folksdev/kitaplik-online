package com.kitaplik.libraryservice.controller;

import com.kitaplik.libraryservice.config.VaultConfig;
import com.kitaplik.libraryservice.dto.AddBookRequest;
import com.kitaplik.libraryservice.dto.LibraryDto;
import com.kitaplik.libraryservice.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/v1/library")
public class LibraryController {

    Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryService libraryService;
    private final Environment environment;

    private final VaultConfig vaultConfig;

    @Value("${library-service.book.count}")
    private Integer bookCount;

    public LibraryController(LibraryService libraryService, Environment environment, VaultConfig vaultConfig) {
        this.libraryService = libraryService;
        this.environment = environment;
        this.vaultConfig = vaultConfig;
    }

    @GetMapping("{id}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable String id) {
        return ResponseEntity.ok(libraryService.getAllBooksInLibraryById(id));
    }

    @PostMapping
    public ResponseEntity<LibraryDto> createLibrary() {
        logger.info("Library created on port number " + environment.getProperty("local.server.port"));

        return ResponseEntity.ok(libraryService.createLibrary());
    }

    @PutMapping
    public ResponseEntity<Void> addBookToLibrary(@RequestBody AddBookRequest request) {
        libraryService.addBookToLibrary(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllLibraries() {
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }

    @GetMapping("/count")
    public ResponseEntity<String> getCount() {
        String s = "Library count is from VaultConfig is " + vaultConfig.getCount() + "\n" +
                "Library count is from application properties is " + bookCount;
        return ResponseEntity.ok(s);
    }

}
