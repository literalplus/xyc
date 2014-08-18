/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.version;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds the loaded version of a Plugin, as given in its JAR's Implementation-Title, Implementation-Version and Implementation-Build.
 *
 * @author xxyy98 (http://xxyy.github.io/)
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

    public static PluginVersion ofClass(Class<?> clazz) {
        final CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        final URL url = codeSource.getLocation();

        if (url.toExternalForm().endsWith("jar")) { //Account for .xyjar
            try (JarInputStream jis = new JarInputStream(url.openStream())) {
                final Attributes attrs = jis.getManifest().getMainAttributes();

                return new PluginVersion(
                        attrs.getValue(Name.IMPLEMENTATION_TITLE),
                        attrs.getValue(Name.IMPLEMENTATION_VERSION),
                        attrs.getValue("Implementation-Build"));
            } catch (IOException ex) {
                Logger.getLogger(PluginVersion.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return implementationTitle + " Version " + implementationVersion + " Build " + implementationBuild;
    }

    public String getImplementationTitle() {
        return this.implementationTitle;
    }

    public String getImplementationVersion() {
        return this.implementationVersion;
    }

    public String getImplementationBuild() {
        return this.implementationBuild;
    }
}
