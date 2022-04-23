/**
 * Main public API classes of the core streaming JSON
 * processor: most importantly {@link systems.microservice.loghub.jackson.core.JsonFactory}
 * used for constructing
 * JSON parser ({@link systems.microservice.loghub.jackson.core.JsonParser})
 * and generator
 * ({@link systems.microservice.loghub.jackson.core.JsonGenerator})
 * instances.
 * <p>
 * Public API of the higher-level mapping interfaces ("Mapping API")
 * is found from the "jackson-databind" bundle, except for following
 * base interfaces that are defined here:
 * <ul>
 *<li>{@link systems.microservice.loghub.jackson.core.TreeNode} is included
 *within Streaming API to support integration of the Tree Model
 *(which is based on <code>JsonNode</code>) with the basic
 *parsers and generators (iff using mapping-supporting factory: which
 *is part of Mapping API, not core)
 *  </li>
 *<li>{@link systems.microservice.loghub.jackson.core.ObjectCodec} is included so that
 *  reference to the object capable of serializing/deserializing
 *  Objects to/from JSON (usually, <code>systems.microservice.loghub.jackson.databind.ObjectMapper</code>)
 *  can be exposed, without adding direct dependency to implementation.
 *  </li>
 *</ul>
 */

package systems.microservice.loghub.jackson.core;
