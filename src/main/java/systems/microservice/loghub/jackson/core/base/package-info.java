/**
 * Base classes used by concrete Parser and Generator implementations;
 * contain functionality that is not specific to JSON or input
 * abstraction (byte vs char).
 * Most formats extend these types, although it is also possible to
 * directly extend {@link systems.microservice.loghub.jackson.core.JsonParser} or
 * {@link systems.microservice.loghub.jackson.core.JsonGenerator}.
 */
package systems.microservice.loghub.jackson.core.base;
