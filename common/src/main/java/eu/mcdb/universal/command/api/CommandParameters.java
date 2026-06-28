package eu.mcdb.universal.command.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandParameters {

    private final Map<String, String> values;

    public CommandParameters() {
        this.values = new HashMap<>();
    }

    public String getValue(String name) {
        return values.get(name);
    }

    public Optional<String> getOptionalValue(String name) {
        return Optional.ofNullable(getValue(name));
    }
}
