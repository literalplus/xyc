/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.version;

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class offers a static utility method to extract manifest version information from JAR files by a class.
 * Instances hold the retrieved immutable information.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public final class PluginVersion {

    private final String implementationTitle;
    private final String implementationVersion;
    private final String implementationBuild;

    @java.beans.ConstructorProperties({"implementationTitle", "implementationVersion", "implementationBuild"})
    private PluginVersion(String implementationTitle, String implementationVersion, String implementationBuild) {
        this.implementationTitle = implementationTitle;
        this.implementationVersion = implementationVersion;
        this.implementationBuild = implementationBuild;
    }

    /**
     * Attempts to retrieve version information for a class from its enclosing JAR archive's manifest. If the class is
     * not in such archive, or the information cannot be acquired for any other reason, a special instance with
     * {@link Class#getSimpleName()} as implementation title and "unknown" as version and build number will be
     * returned. Note that any or all of the attributes may be unspecified and therefore null.
     *
     * @param clazz the class to retrieve version information for
     * @return the version information for given class
     */
    @Nonnull
    public static PluginVersion ofClass(@Nonnull Class<?> clazz) {
        @Nullable CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();

        if (codeSource != null) {
            URL url = codeSource.getLocation();

            if (url != null && url.toExternalForm().endsWith(".jar")) {
                try (JarInputStream jis = new JarInputStream(url.openStream())) {
                    if (jis.getManifest() != null) {
                        Attributes attrs = jis.getManifest().getMainAttributes();

                        return new PluginVersion(
                                attrs.getValue(Name.IMPLEMENTATION_TITLE),
                                attrs.getValue(Name.IMPLEMENTATION_VERSION),
                                attrs.getValue("Implementation-Build")
                        );
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PluginVersion.class.getName()).log(Level.SEVERE,
                            "Error occurred while reading JAR version info for " + clazz.getName(), ex);
                }
            }
        }

        return new PluginVersion(clazz.getSimpleName(), "unknown", "unknown");
    }

    @Override
    public String toString() {
        return implementationTitle + " Version " + implementationVersion + " Build " + implementationBuild;
    }

    @Nullable
    public String getImplementationTitle() {
        return this.implementationTitle;
    }

    @Nullable
    public String getImplementationVersion() {
        return this.implementationVersion;
    }

    @Nullable
    public String getImplementationBuild() {
        return this.implementationBuild;
    }
}
