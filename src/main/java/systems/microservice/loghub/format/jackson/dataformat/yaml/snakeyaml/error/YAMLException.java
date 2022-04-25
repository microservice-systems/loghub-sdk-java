package systems.microservice.loghub.format.jackson.dataformat.yaml.snakeyaml.error;

import systems.microservice.loghub.format.jackson.core.JsonParser;
import systems.microservice.loghub.format.jackson.dataformat.yaml.JacksonYAMLParseException;

/**
 * Replacement for formerly shaded exception type from SnakeYAML; included
 * in 2.8 solely for backwards compatibility: new code that relies on Jackson 2.8
 * and after should NOT use this type but only base type {@link JacksonYAMLParseException}.
 *
 * @deprecated Since 2.8
 */
@Deprecated
public class YAMLException extends JacksonYAMLParseException
{
    private static final long serialVersionUID = 1L;

    public YAMLException(JsonParser p,
            systems.microservice.loghub.format.snakeyaml.error.YAMLException src) {
        super(p, src.getMessage(), src);
    }

    public static YAMLException from(JsonParser p,
            systems.microservice.loghub.format.snakeyaml.error.YAMLException src) {
        return new YAMLException(p, src);
    }
}
