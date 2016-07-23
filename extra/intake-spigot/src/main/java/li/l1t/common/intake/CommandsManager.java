/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.intake;

import com.sk89q.intake.Intake;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.binder.BindingBuilder;
import com.sk89q.intake.parametric.provider.PrimitivesModule;
import com.sk89q.intake.util.auth.Authorizer;
import li.l1t.common.CommandRegistrationManager;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages commands registered with Intake.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandsManager {
    final CommandRegistrationManager commandRegistrationManager = new CommandRegistrationManager();
    private final Plugin plugin;
    private final Map<Object, Object> namespaceTemplate = new HashMap<>();
    private final Map<String, CommandBuilder> commandBuilders = new HashMap<>();
    private final ParametricBuilder builder = new ParametricBuilder(Intake.createInjector());
    private final CommonInjectorModule injectorModule;
    private final CommandGraph commandGraph = new CommandGraph().builder(builder);
    private final Authorizer authorizer = new CommandSenderAuthorizer();

    public CommandsManager(Plugin plugin) {
        this.plugin = plugin;
        builder.getInjector().install(new PrimitivesModule());
        builder.getInjector().install(injectorModule = new CommonInjectorModule());
        injectorModule.bind(Plugin.class).toInstance(plugin);
        injectorModule.bind(CommandsManager.class).toInstance(this);
    }

    public void putIntoNamespace(Object key, Object value) {
        namespaceTemplate.put(key, value);
        commandBuilders.values().forEach(builder -> builder.putIntoNamespaceIfAvailable(key, value));
    }

    public <T> BindingBuilder<T> bind(Class<T> clazz) {
        return injectorModule.bind(clazz);
    }

    public void applyNamespaceTemplateTo(Namespace namespace) {
        for (Map.Entry<Object, Object> entry : namespaceTemplate.entrySet()) {
            namespace.put(entry.getKey(), entry.getValue());
        }
    }

    public void registerCommand(Object handler, String name, String... aliases) {
        getBuilderFor(name)
                .withAliases(aliases)
                .withDispatcherFor(handler)
                .create()
                .register();
    }

    public CommandBuilder getBuilderFor(String commandName) {
        return commandBuilders.computeIfAbsent(
                commandName,
                name -> new CommandBuilder(this).withName(commandName)
        );
    }

    public IntakeCommand getCommand(String commandName) {
        CommandBuilder builder = commandBuilders.get(commandName);
        return builder == null ? null : builder.getCommand();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public ParametricBuilder getBuilder() {
        return builder;
    }

    public CommonInjectorModule getInjectorModule() {
        return injectorModule;
    }

    public CommandGraph getCommandGraph() {
        return commandGraph;
    }
}
