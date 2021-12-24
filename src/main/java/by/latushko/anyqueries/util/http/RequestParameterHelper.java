package by.latushko.anyqueries.util.http;

import java.util.Map;
import java.util.Optional;

public final class RequestParameterHelper {
    private Map<String, String[]> parameters;

    public RequestParameterHelper(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public Optional<String> getValue(String key) {
        if(parameters.containsKey(key)) {
            String[] values = parameters.get(key);
            if(values != null && values.length > 0) {
                String value = values[0];
                if(!value.isEmpty()) {
                    return Optional.of(values[0]);
                }
            }
        }
        return Optional.empty();
    }
}
