/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.log;

import com.google.common.base.Preconditions;
import li.l1t.common.util.FileHelper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.LogManager;

/**
 * <p>This class helps with registering and managing custom Log4J loggers,
 * as well as initialising a custom Log4J context with a mixture of
 * pre-defined and user-defined behaviour. Plugins can use this to
 * utilise the power of Log4J and its XML configuration even though
 * Minecraft has their own configuration set. This works by creating
 * a custom context that loads a custom configuration. To allow
 * administrators to customise logging, this class also provides
 * methods to merge custom and preset configurations.</p>
 * <p>
 * Please note that initialising a new Log4J context takes a lot
 * of time (seconds!) for some reason, so this might dramatically
 * increase your plugin's initialisation time.
 * </p>
 * <p>
 * Configuration is loaded from two files, {@code log4j2-}{@link #id %id%}{@code .xml} and
 * {@code log4j2-custom.xml}, which are default configuration and custom additions,
 * respectively. Templates for both files must be present in the plugin JAR. Both
 * files are copied to the plugin's data folder and merged to the configuration file.
 * The id-specific file will be overwritten and merged with the custom file every time the
 * context is initialised, while the custom file will remain untouched.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-20
 */
public class Log4JContextInitialiser {
    private final String id;
    private final String dirName;
    private LoggerContext context;
    private JavaPlugin plugin;

    /**
     * Creates a new context initialiser. Note that a plugin must be
     * {@link #setPlugin(JavaPlugin) set} in order for logs to be placed
     * in the correct directory.
     *
     * @param id      a short unique id for the managed context, must be persistent
     * @param dirName the name of the directory below the plugin's data folder where logs will be
     *                saved
     */
    public Log4JContextInitialiser(String id, String dirName) {
        this.id = id;
        this.dirName = dirName;
    }

    private static void saveXML(Document doc, File file) throws TransformerException, IOException {
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        doc.normalizeDocument();

        TransformerFactory.newInstance().newTransformer()
                .transform(new DOMSource(doc), new StreamResult(file));
    }

    private static Document mergeLog4j2Configs(File from, InputStream to)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {

        /*
        Don't fucking touch any of this. It works now. Change one line, it breaks. I can speak out of hour-long
        experience with this shitty XML 'API'. Seriously. Go away right now.
         */

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document fromDoc = builder.parse(from);
        Element fromRoot = fromDoc.getDocumentElement();
        Document toDoc = builder.parse(to);
        Element toRoot = toDoc.getDocumentElement();

        Node node = fromRoot.getFirstChild();
        Node nextNode;
        do {
            nextNode = node.getNextSibling();
            NodeList childNodes = node.getChildNodes();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList toNodes = toRoot.getElementsByTagName(node.getNodeName());
                if (toNodes.getLength() != 0) {
                    Node toNode = toNodes.item(0);
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node child = childNodes.item(i);
                        Node nextChild;
                        do {
                            nextChild = child.getNextSibling();
                            toNode.appendChild(toDoc.importNode(child, true));
                        } while ((child = nextChild) != null);
                    }
                    continue;
                }
            }
            toDoc.adoptNode(node);
            toRoot.appendChild(node);
        } while ((node = nextNode) != null);

        return toDoc;
    }

    /**
     * Obtains a logger in the managed context for a specific class. If the context has not
     * yet been initialised, initialises the context.
     *
     * @param clazz the class to use
     * @return the obtained logger
     */
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Obtains a logger in the managed context for a specific id. If the context has not
     * yet been initialised, initialises the context.
     *
     * @param id the id to use
     * @return the obtained logger
     */
    public Logger getLogger(String id) {
        if (context == null) {
            initialiseContext();
        }

        return context.getLogger(id);
    }

    /**
     * <p>Initialises the managed {@link LoggerContext logger context} for Log4J.
     * This works by merging the pre-defined static configuration (log4j2-mtc.xml)
     * and the customisable configuration (log4j2-custom.xml) from the plugin data
     * folder. Initialising logger contexts can take quite some time.</p>
     * <p>You shouldn't need to call this manually since {@link #getLogger(Class)}
     * calls this method on demand.</p>
     * <p>
     * If the system property {@code xyc.unittest} is set, {@link #useDefaultContext() the
     * default context is used} instead of creating a new context. This behaviour is
     * intended for use with Unit Tests, where initialising a context is unnecessary overhead.
     * </p>
     *
     * @throws IllegalStateException if any of the {@link Log4JContextInitialiser required files}
     *                               are missing from the plugin JAR
     */
    public void initialiseContext() {
        if (System.getProperty("xyc.unittest") != null) { //Unit Tests
            useDefaultContext();
            return;
        }

        Preconditions.checkState(context == null, "Already initialised!");
        if (plugin == null) {
            Bukkit.getLogger().warning("[XYC] Initialising log4j2 context " + id +
                    " before plugin has been set!");
            Bukkit.getLogger().warning("[XYC] Called from: " +
                    Thread.currentThread().getStackTrace()[2].toString());
            return;
        }
        System.setProperty(
                "Log4jContextSelector",
                "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
        ); //Enable async loggers with the LMAX Disruptor
        System.setProperty(
                id + ".datadir",
                plugin.getDataFolder().getAbsolutePath()
        );
        System.setProperty(
                id + ".logsdir",
                plugin.getDataFolder().getAbsolutePath() + "/" + dirName + "/"
        );

        File configFile = new File(plugin.getDataFolder(), "log4j2-" + id + ".xml");
        prepareLog4jConfig(configFile);

        context = org.apache.logging.log4j.LogManager.getContext(
                LogManager.class.getClassLoader(), false, configFile.toURI()
        );
    }

    /**
     * Forces the initialiser to use the Log4J2 default context instead of creating a custom one.
     * Overrides already-created contexts.
     */
    public void useDefaultContext() {
        context = org.apache.logging.log4j.LogManager.getContext();
    }

    /**
     * @return the unique persistent id of this context
     */
    public String getId() {
        return id;
    }

    /**
     * @return the logs directory below the plugin's data folder
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * @return the managed context, or null if none
     */
    public LoggerContext getContext() {
        return context;
    }

    //     the rest of this class is extracting and merging Log4J configuration files

    /**
     * @return the plugin associated with this initialiser, or null if not set
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Sets the plugin used to compute the data directory.
     *
     * @param newPlugin the plugin to use
     */
    public void setPlugin(JavaPlugin newPlugin) {
        this.plugin = newPlugin;
    }

    private boolean prepareLog4jConfig(File configFile) {
        try (InputStream configIn = plugin.getResource("log4j2-" + id + ".xml")) {

            saveXML(
                    mergeLog4j2Configs(extractResource("log4j2-custom.xml"), configIn),
                    configFile
            );

        } catch (IOException | TransformerException e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE,
                    "Error copying log4j2 config " + id, e);
        } catch (ParserConfigurationException | SAXException e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE,
                    "Could not parse log4j2 configuration " + id + "!", e);
        }

        return configFile.exists();
    }

    private File extractResource(String filename) throws IOException {
        File file = new File(plugin.getDataFolder(), filename);
        Path filePath = file.toPath();

        if (!file.exists()) {
            FileHelper.createWithParents(file);
            InputStream resource = plugin.getResource(filename);
            if (resource == null) {
                throw new IllegalStateException("Missing plugin resource " + filename +
                        " for Log4J2 context " + id + "!");
            }

            try (InputStream is = resource) {
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        return file;
    }


}
