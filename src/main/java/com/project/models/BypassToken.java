package com.project.models;

import lombok.Getter;

@Getter
public class BypassToken {
    private final String path;
    private final String method;

    public BypassToken(String path, String method) {
        this.path = path;
        this.method = method;
    }

}

