package com.kibikalo.pastebin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasteRequest {
    private String content;
    private int expirationMinutes;
}
