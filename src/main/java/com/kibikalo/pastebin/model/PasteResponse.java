package com.kibikalo.pastebin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasteResponse {
    private String content;

    public PasteResponse(String content) {
        this.content = content;
    }
}
