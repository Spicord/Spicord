package org.spicord.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.spicord.Spicord;

public class JDALoggerFactory extends HashMap<String, Logger> {

    private static final long serialVersionUID = 1L;

    private final Spicord s;

    public JDALoggerFactory(Spicord s) {
        this.s = s;
    }

    @Override
    public boolean containsKey(Object key) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Logger get(Object key) {
        if (super.containsKey(key)) {
            return super.get(key);
        }

        Logger val = new JDALoggerImpl((String)key, s.getLogger());
        super.put((String)key, val);
        return val;
    }
}
