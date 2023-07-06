package com.kibikalo.pastebin.controller;

import com.kibikalo.pastebin.exceptions.NotFoundException;
import com.kibikalo.pastebin.exceptions.PasteExpiredException;
import com.kibikalo.pastebin.model.CreatePasteResponse;
import com.kibikalo.pastebin.model.PasteRequest;
import com.kibikalo.pastebin.model.PasteResponse;
import com.kibikalo.pastebin.service.PasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PasteController {

    private PasteService pasteService;

    @Autowired
    public PasteController(PasteService pasteService) {
        this.pasteService = pasteService;
    }

    @PostMapping("/")
    public ResponseEntity<CreatePasteResponse> createPaste(@RequestBody PasteRequest request) {
        String hash = pasteService.createPaste(request.getContent(), request.getExpirationMinutes());
        CreatePasteResponse response = new CreatePasteResponse(hash);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<PasteResponse> readPaste(@PathVariable String hash) {
        try {

            String content = pasteService.readPaste(hash);
            PasteResponse response = new PasteResponse(content);
            return ResponseEntity.ok(response);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (PasteExpiredException e) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
