package com.kibikalo.pastebin.service;


import com.kibikalo.pastebin.exceptions.InternalServerErrorException;
import com.kibikalo.pastebin.exceptions.NotFoundException;
import com.kibikalo.pastebin.exceptions.PasteExpiredException;
import com.kibikalo.pastebin.model.Paste;
import com.kibikalo.pastebin.repository.PasteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasteService {
    private final S3Service s3Service;
    private final PasteRepository pasteRepository;
    private final PasteNameGenerator pasteNameGenerator;
    private final HashGenerator hashGenerator;
    private static final Logger logger = LoggerFactory.getLogger(PasteService.class);

    @Autowired
    public PasteService(S3Service s3PasteService,
                        PasteRepository pasteRepository,
                        PasteNameGenerator pasteNameGenerator,
                        HashGenerator hashGenerator) {
        this.s3Service = s3PasteService;
        this.pasteRepository = pasteRepository;
        this.pasteNameGenerator = pasteNameGenerator;
        this.hashGenerator = hashGenerator;
    }

    public String createPaste(String content, int expirationMinutes) {
        try {
            //Input validation
            validateInput(content, expirationMinutes);

            // 1 : save content to S3
            String objectName = pasteNameGenerator.generateRandomName();
            s3Service.savePaste(content, objectName);

            // 2 : generate hash by object name
            String hash = hashGenerator.generateHash(objectName);

            // 3 : save data in DB
            Paste paste = new Paste();

            paste.setHash(hash);
            paste.setObjectPath(s3Service.getObjectPath());
            paste.setObjectName(objectName);
            paste.setExpirationMinutes(expirationMinutes);
            paste.setCreatedAt(LocalDateTime.now());
            if(expirationMinutes == 0) {
                paste.setExpiresOn(null);
            } else {
                paste.setExpiresOn(paste.getCreatedAt().plusMinutes(expirationMinutes));
            }

            pasteRepository.save(paste);

            // 4 : return hash string
            return hash;

        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new InternalServerErrorException("Failed to create paste");
        }
    }

    private void validateInput(String content, int expirationMinutes) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        if (expirationMinutes < 0) {
            throw new IllegalArgumentException("Expiration minutes must be a positive value");
        }
    }

    public String readPaste(String hash) {

        try {
            // 1 : searches object in the database by its hash
            Paste paste = findPasteByHash(hash);
            if (paste == null) {
                throw new NotFoundException("Paste not found");
            }

            // 2 : check if the paste has expired
            if (isPasteExpired(paste)) {
                throw new PasteExpiredException("Paste has expired");
            }

            // 3 : goes to the database for a reference to the content in the cloud
            String objectPath = paste.getObjectPath();

            // 4 : return content from the cloud
            return s3Service.getPaste(objectPath);

        }  catch (IllegalArgumentException e) {
            // Handle input validation errors
            logger.error("Invalid input: {}", e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            // Handle paste not found
            logger.error("Paste not found: {}", e.getMessage());
            throw e;
        } catch (PasteExpiredException e) {
            // Handle paste expired
            logger.error("Paste has expired: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            logger.error("An error occurred while reading paste: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to read paste");
        }
    }

    private Paste findPasteByHash(String hash) {
        if (hash.length() != 8) {
            throw new IllegalArgumentException("Invalid hash length. Hash must be 8 characters long.");
        }
        return pasteRepository.findByHash(hash);
    }

    private boolean isPasteExpired(Paste paste) {
        LocalDateTime expiresOn = paste.getExpiresOn();
        return expiresOn != null && LocalDateTime.now().isAfter(expiresOn);
    }
}
