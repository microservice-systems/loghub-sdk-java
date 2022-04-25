package systems.microservice.loghub.format.jackson.dataformat.yaml.snakeyaml.error;

import systems.microservice.loghub.format.jackson.core.JsonParser;

/**
 * Replacement for formerly shaded exception type from SnakeYAML; included
 * in 2.8 solely for backwards compatibility: new code that relies on Jackson 2.8
 * and after should NOT use this type but only base type
 * {@link systems.microservice.loghub.format.jackson.dataformat.yaml.JacksonYAMLParseException}.
 *
 * @deprecated Since 2.8
 */
@Deprecated
public class MarkedYAMLException extends YAMLException
{
    private static final long serialVersionUID = 1L;

    protected final systems.microservice.loghub.format.snakeyaml.error.MarkedYAMLException _source;

    protected MarkedYAMLException(JsonParser p,
            systems.microservice.loghub.format.snakeyaml.error.MarkedYAMLException src) {
        super(p, src);
        _source = src;
    }

    public static MarkedYAMLException from(JsonParser p,
            systems.microservice.loghub.format.snakeyaml.error.MarkedYAMLException src) {
        return new MarkedYAMLException(p, src);
    }

    public String getContext() {
        return _source.getContext();
    }

    public Mark getContextMark() {
        return Mark.from(_source.getContextMark());
    }

    public String getProblem() {
        return _source.getProblem();
    }

    public Mark getProblemMark() {
        return Mark.from(_source.getProblemMark());
    }
}
