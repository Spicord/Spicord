package org.spicord.api.bot;

import lombok.Getter;

public abstract class SimpleBot {

    @Getter
    protected String name;

    protected String token;

    public SimpleBot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    protected abstract boolean start();
}
