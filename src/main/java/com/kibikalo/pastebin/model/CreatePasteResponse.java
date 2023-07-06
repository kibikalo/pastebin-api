package com.kibikalo.pastebin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePasteResponse {
    private String hash;

    public CreatePasteResponse(String hash) {
        this.hash = hash;
    }
}
