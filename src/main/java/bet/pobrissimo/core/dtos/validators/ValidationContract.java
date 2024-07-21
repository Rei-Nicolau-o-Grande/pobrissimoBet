package bet.pobrissimo.core.dtos.validators;

import java.util.List;
import java.util.Map;

public interface ValidationContract {

    void validate(String value, Map<String, List<String>> errorFields);
}
