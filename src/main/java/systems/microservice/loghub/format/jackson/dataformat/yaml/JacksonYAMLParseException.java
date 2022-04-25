package systems.microservice.loghub.format.jackson.dataformat.yaml;

import systems.microservice.loghub.format.jackson.core.JsonParseException;
import systems.microservice.loghub.format.jackson.core.JsonParser;

/**
 * @since 2.8
 */
public class JacksonYAMLParseException extends JsonParseException
{
    private static final long serialVersionUID = 1L;

    public JacksonYAMLParseException(JsonParser p, String msg, Exception e) {
        super(p, msg, e);
    }
}
