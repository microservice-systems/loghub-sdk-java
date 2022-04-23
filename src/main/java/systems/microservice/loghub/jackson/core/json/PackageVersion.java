package systems.microservice.loghub.jackson.core.json;

import systems.microservice.loghub.jackson.core.Version;
import systems.microservice.loghub.jackson.core.Versioned;
import systems.microservice.loghub.jackson.core.util.VersionUtil;

/**
 * Automatically generated from PackageVersion.java.in during
 * packageVersion-generate execution of maven-replacer-plugin in
 * pom.xml.
 */
public final class PackageVersion implements Versioned {
    public final static Version VERSION = VersionUtil.parseVersion(
        "2.13.2", "systems.microservice.loghub.jackson.core", "jackson-core");

    @Override
    public Version version() {
        return VERSION;
    }
}
