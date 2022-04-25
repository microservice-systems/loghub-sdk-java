/**
 * Package that contains abstractions needed to support optional
 * non-blocking decoding (parsing) functionality.
 * Although parsers are constructed normally via
 * {@link systems.microservice.loghub.format.jackson.core.JsonFactory}
 * (and are, in fact, sub-types of {@link systems.microservice.loghub.format.jackson.core.JsonParser}),
 * the way input is provided differs.
 *
 * @since 2.9
 */

package systems.microservice.loghub.format.jackson.core.async;
