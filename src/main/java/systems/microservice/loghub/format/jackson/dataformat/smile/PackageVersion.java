package systems.microservice.loghub.format.jackson.dataformat.smile;

import systems.microservice.loghub.format.jackson.core.Version;
import systems.microservice.loghub.format.jackson.core.Versioned;
import systems.microservice.loghub.format.jackson.core.util.VersionUtil;

/**
 * Automatically generated from PackageVersion.java.in during
 * packageVersion-generate execution of maven-replacer-plugin in
 * pom.xml.
 */
public final class PackageVersion implements Versioned {
    public final static Version VERSION = VersionUtil.parseVersion(
        "2.13.2", "systems.microservice.loghub.format.jackson.dataformat", "jackson-dataformat-smile");

    @Override
    public Version version() {
        return VERSION;
    }
}
