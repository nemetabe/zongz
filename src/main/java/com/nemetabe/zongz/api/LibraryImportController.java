package com.nemetabe.zongz.api;


import com.nemetabe.zongz.application.LibraryImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
public class LibraryImportController {

    private LibraryImportService libraryImportService;

    public LibraryImportController(LibraryImportService importService){
        this.libraryImportService = importService;
    }

    @PostMapping("/csv")
    public ResponseEntity importRekordboxLibraryCsv(){
        return ResponseEntity.ok(null);
    }
}
