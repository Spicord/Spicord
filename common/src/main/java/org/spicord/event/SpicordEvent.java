package org.spicord.event;

import java.util.HashSet;
import java.util.Set;
import org.spicord.Spicord;

public final class SpicordEvent<T> {

    private static final Set<SpicordEvent<?>> values = new HashSet<>();

    /*==== add events below this line ====*/
    public static final SpicordEvent<Spicord> SPICORD_LOADED = new SpicordEvent<>();
    /*====================================*/

    private SpicordEvent() {
        values.add(this);
    }

    public static SpicordEvent<?>[] values() {
        return values.toArray(new SpicordEvent[values.size()]);
    }
}
