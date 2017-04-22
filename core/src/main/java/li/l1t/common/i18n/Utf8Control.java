/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * A Resource Bundle Control which loads bundles in UTF-8 instead of the default encoding. This allows for maximum
 * portability and ensures that a future-proof encoding is used. Further, it allows to specify the time-to-live value
 * for the bundle, which specifies caching behaviour. See {@link #getTimeToLive(String, Locale)} for details.
 */
public class Utf8Control extends ResourceBundle.Control {
    /**
     * A reusable control instance constructed using the {@link #Utf8Control() default constructor}.
     */
    public static final Utf8Control INSTANCE = new Utf8Control();

    /**
     * A reusable control instance with a {@link #getTimeToLive(String, Locale) time to live} of
     * {@link #TTL_DONT_CACHE}, which disables the bundle cache.
     *
     * @see #Utf8Control(long)
     */
    public static final Utf8Control NO_CACHE = new Utf8Control();

    private final long timeToLive;

    /**
     * Creates a new UTF-8 control with the default time to live of {@link #TTL_NO_EXPIRATION_CONTROL}.
     */
    public Utf8Control() {
        this(TTL_NO_EXPIRATION_CONTROL);
    }

    /**
     * Creates a new UTF-8 control.
     *
     * @param timeToLive the {@link #getTimeToLive(String, Locale) time to live} to use for all created bundles
     */
    public Utf8Control(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * {@inheritDoc}
     * <p>{@link Utf8Control} returns the time to live passed to its constructor.</p>
     */
    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        return timeToLive;
    }

    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        // below is basically a copy of the default implementation that just uses UTF-8
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try (InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
                return new PropertyResourceBundle(reader);
            }
        }
        return null;
    }
}
