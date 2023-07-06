package com.kibikalo.pastebin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "paste")
@Getter
@Setter
@NoArgsConstructor
public class Paste {

    @Id
    @SequenceGenerator(
            name = "paste_sequence",
            sequenceName = "paste_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "paste_sequence"
    )
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String hash;

    @Column(name = "object_path", nullable = false)
    private String objectPath;

    @Column(name = "object_name", nullable = false)
    private String objectName;

    @Column(nullable = true)
    private String url;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration_minutes", nullable = false)
    private int expirationMinutes;

    @Column(name = "expires_on",nullable = true)
    private LocalDateTime expiresOn;

    @Column(name = "user_id", nullable = true)
    private Long userId;

    public Paste(String hash,
                 String objectPath,
                 String objectName,
                 LocalDateTime createdAt,
                 int expirationMinutes,
                 LocalDateTime expiresOn) {
        this.hash = hash;
        this.objectPath = objectPath;
        this.objectName = objectName;
        this.createdAt = createdAt;
        this.expirationMinutes = expirationMinutes;
        this.expiresOn = expiresOn;
    }

    public LocalDateTime getExpiresOn() {
        if (expirationMinutes > 0) {
            return createdAt.plusMinutes(expirationMinutes);
        } else {
            return null;
        }
    }
}
