/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.games.kits;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.xxyy.common.cmd.XYCCommandExecutor;
import io.github.xxyy.common.util.CommandHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Register this command AND TabCompleter with the following aliases: "kitfactory","kf","kitfabrik"
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class CommandKitFactory extends XYCCommandExecutor implements TabCompleter {
    private KitLoaderProvider prov;
    private Map<String, KitFactory> pendingProducts = new HashMap<>(5);
    private Map<String, String> availObjectives;//and descriptions!
    private String perm = "xyg.a.kits";
    private String importantMessage;

    /**
     * Constructs a new CommandKitFactory.
     *
     * @param prov             The {@link KitLoaderProvider} that can be used to fetch a KitLoader by a player.
     * @param availObjectives  A list of available Kit Objectives, i.e. Tasks that have to be completed to unlock specific kits. If you don't have
     *                         that, just pass {@code null}.
     * @param importantMessage A message that is displayed above the help, for important information regarding multiple KitLoaders.
     */
    public CommandKitFactory(KitLoaderProvider prov,
                             Map<String, String> availObjectives,
                             String importantMessage) {
        this.prov = prov;
        this.availObjectives = availObjectives;
        this.importantMessage = importantMessage;
    }

    /**
     * Constructs a new CommandKitFactory.
     *
     * @param prov                The {@link KitLoaderProvider} that can be used to fetch a KitLoader by a player.
     * @param availObjectiveNames A list of available Kit Objectives, i.e. Tasks that have to be completed to unlock specific kits. If you don't have
     *                            that, just pass {@code null}.
     * @param importantMessage    A message that is displayed above the help, for important information regarding multiple KitLoaders.
     * @param perm                The permission needed to execute this command.
     */
    public CommandKitFactory(String perm,
                             KitLoaderProvider prov,
                             Map<String, String> availObjectiveNames,
                             String importantMessage) {
        this(prov, availObjectiveNames, importantMessage);
        this.perm = perm;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean catchCommand(CommandSender sender, String senderName, Command cmd, String label, String[] args) {
        if (CommandHelper.kickConsoleFromMethod(sender, label)) {
            return true;
        }
        if (!CommandHelper.checkPermAndMsg(sender, this.perm, label)) {
            return true;
        }
        if (args.length == 0) {
            return this.printHelpTo(sender, label);
        }
        Player plr = (Player) sender;
        switch (args[0]) {
            case "produce":
                KitFactory kfProduce = this.pendingProducts.get(senderName);
                if (kfProduce == null) {
                    plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 1 ausführen!");
                } else {
                    kfProduce.tryProduce(plr);//Msgs there
                    if (kfProduce.isProduced()) {
                        this.pendingProducts.remove(senderName);
                    }
                }
                return true;
            case "step":
                if (args.length < 2) {
                    return this.printHelpTo(sender, label);
                }
                KitFactory kfStep = this.pendingProducts.get(senderName);
                if (kfStep == null) {
                    if (args[1].equals("1")) {
                        if (args.length < 4) {
                            return this.printHelpTo(sender, label);
                        }
                        if (!StringUtils.isNumeric(args[3])) {
                            plr.sendMessage("Das ist keine Za-hal!");
                            return true;
                        }
                        KitLoader ldr = this.prov.getKitLoader(plr);
                        if (ldr == null) {
                            return true;
                        }
                        kfStep = new KitFactory(args[2], Integer.parseInt(args[3]), senderName, ldr);
                        this.pendingProducts.put(senderName, kfStep);
                    } else {
                        plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 1 ausführen!");
                        return true;
                    }
                }
                switch (args[1]) {
                    case "1":
                        //see kfStep null check!
                        CommandKitFactory.completeStep(kfStep, (byte) 1, plr, null);
                        return true;
                    case "2":
                        CommandKitFactory.completeStep(kfStep, (byte) 2, plr, null);
                        return true;
                    case "3"://I guess this is more efficient than calling Byte.parseByte()
                        CommandKitFactory.completeStep(kfStep, (byte) 3, plr, null);
                        return true;
                    case "4":
                        if (args.length < 4) {
                            return this.printHelpTo(sender, label);
                        }
                        if (!kfStep.isStep3Complete()) {
                            plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 3 ausführen!");
                            return true;
                        }
                        CommandKitFactory.completeStep(kfStep, (byte) 4, plr, null);
                        return true;
                    default:
                        return this.printHelpTo(sender, label);
                }
            case "shelp":
                if (args.length < 2) {
                    return this.printHelpTo(sender, label);
                }
                switch (args[1]) {
                    case "1":
                        plr.sendMessage(new String[]{"§6Schritt 1: §eSetzt die Rüstung und den Inhalt des Kits.",
                                "§6  Parameter 1: §eName des Kits. Muss eindeutig sein.",
                                "§6  Parameter 2: §eID des Kits; Muss eine Zahl sein, legt "
                                        + "die Position des Kits in Auswahlmenü fest ("
                                        + "Kits mit niedriger ID werden zuerst angezeigt, "
                                        + "negative Werte möglich)"});
                        return true;
                    case "2":
                        plr.sendMessage("§6Schritt 2: §eSetzt das Icon des Kits. Das Icon ist "
                                + "der Block oder das Item, das im Auswahlmenü "
                                + "angezeigt wird. Bitte benenne es um und füge eine "
                                + "Lore hinzu, um die Usability zu verbessern.");
                        return true;
                    case "3":
                        plr.sendMessage("§6Schritt 2: §eSetzt das Fehlericon des Kits. (optional!) Das ist "
                                + "der Block oder das Item, das im Auswahlmenü "
                                + "angezeigt wird, wenn das Kit nicht verfügbar ist, weil "
                                + "die Aufgabe (siehe Schritt 4) nicht erfüllt wurde. "
                                + "Bitte benenne es um und füge eine "
                                + "Lore hinzu, um die Usability zu verbessern.");
                        return true;
                    case "4":
                        plr.sendMessage(new String[]{"§6Schritt 4: §eSetzt die Aufgabe des Kits (optional, benötigt 3)",
                                "§6  Parameter 1: §eAufgaben-ID; siehe /" + label + " objectives "
                                        + "für eine Liste der verfügbaren Aufgaben.",
                                "§6  Parameter 2: §eAufgaben-Argument; siehe /" + label + " objectives "
                                        + "für eine Liste der verfügbaren Argumente."});
                        return true;
                    default:
                        plr.sendMessage("§7[Xyg] §cUnbekannter Schritt! §7(bekannt: 1-4)");
                        return true;
                }
            case "todo":
                KitFactory kfToDo = this.pendingProducts.get(senderName);
                if (kfToDo == null) {
                    plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 1 ausführen!");
                } else {
                    kfToDo.sendCompletionState(plr);
                }
                return true;
            case "objectives":
                if (this.availObjectives == null || this.availObjectives.isEmpty()) {
                    plr.sendMessage("§7[Xyg] §cKeine Aufgaben definiert, überspringe diesen Schritt einfach.");
                    return true;
                }
                for (Entry<String, String> entry : this.availObjectives.entrySet()) {
                    plr.sendMessage("§c" + entry.getKey() + ": §e" + entry.getValue());
                }
                return true;
            case "help":
            default:
                return this.printHelpTo(sender, label);
        }
    }

    @Override
    @SuppressWarnings({"fallthrough", "SpellCheckingInspection"})
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "step":
                case "s":
                    if (args.length > 1) {
                        switch (args[1]) {
                            case "1":
                                if (args.length == 3) {
                                    return Lists.newArrayList("<Kit-Name>", "§eHilfe bei /" + label + " shelp 1");
                                } else if (args.length == 4) {
                                    return Lists.newArrayList("<Sortierungs-ID>", "§eHilfe bei /" + label + " shelp 1");
                                } else {
                                    return Lists.newArrayList("<unbelegtes Argument>", "§eHilfe bei /" + label + " shelp 1");
                                }
                            case "2":
                            case "3":
                                return Lists.newArrayList("<unbelegtes Argument>", "§eHilfe bei /" + label + " shelp " + args[1]);
                            case "4":
                                if (args.length == 3) {
                                    return Lists.newArrayList("<Aufgaben-ID>", "§eHilfe bei /" + label + " shelp 4");
                                } else if (args.length == 4) {
                                    return Lists.newArrayList("<Aufgaben-Argument>", "§eHilfe bei /" + label + " shelp 4");
                                } else {
                                    return Lists.newArrayList("<unbelegtes Argument>", "§eHilfe bei /" + label + " shelp 4");
                                }
                        }
                    }//else just return available steps
                case "shelp":
                    return CommandHelper.tabCompleteArgs(args, Lists.newArrayList("1", "2", "3", "4"));
                case "help":
                case "produce":
                case "todo":
                case "objectives":
                    return Lists.newArrayList("<unbelegtes Argument>", "§eHilfe bei /" + label + " help");
            }
        }
        return Lists.newArrayList("step", "shelp", "help", "produce", "todo", "objectives");
    }

    @SuppressWarnings("SpellCheckingInspection")
    private boolean printHelpTo(CommandSender sender, String label) {
        sender.sendMessage(new String[]{
                this.importantMessage,
                "§7/" + label + " help §eZeigt diese Hilfe an.",
                "§7/" + label + " produce §eProduziert das Kit, d.h. veröffentlicht es.",
                "§7/" + label + " step 1 <Kit-Name> <Sortierungs-ID> §eSetzt Rüstung & Inhalt.",
                "§7/" + label + " step 2 §eSetzt das Icon.",
                "§7/" + label + " step 3 §eSetzt das Fehlericon.",
                "§7/" + label + " step 4 <Aufgaben-ID> <Aufgaben-Argument> §eSetzt die benötigten Aufgaben.",
                "§7/" + label + " todo §eZeigt deine ToDo-Liste an.",
                "§7/" + label + " shelp <Schritt-ID> §eZeigt Anweisungen zu einem Schritt an.",
                "§7/" + label + " objectives §eZeigt alle verfügbaren Aufgaben an",
                "§9Aliases: §7kitfactory, kf, kitfabrik"
        });
        return true;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void completeStep(KitFactory kf, byte stepId, Player plr, String[] additionalArgs) {//silently overwrite everything
        switch (stepId) {
            case 1:
                kf.setContents(plr.getInventory().getContents());
                kf.setArmor(plr.getInventory().getArmorContents());
                break;
            case 2:
                ItemStack iconStk = plr.getItemInHand();
                if (iconStk == null || iconStk.getType() == Material.AIR) {
                    plr.sendMessage("§7[Xyg] §cDu hast nichts in der Hand!");
                    break;
                }
                kf.setIcon(iconStk);
                break;
            case 3:
                ItemStack sIconStk = plr.getItemInHand();
                if (sIconStk == null || sIconStk.getType() == Material.AIR) {
                    plr.sendMessage("§7[Xyg] §cDu hast nichts in der Hand!");
                    break;
                }
                kf.setUnavailIcon(sIconStk);
                break;
            case 4:
                if (additionalArgs.length < 2) {
                    plr.sendMessage("§7[Xyg] §cDu musst 2 Parameter angeben!");
                    break;
                }
                kf.setObjectiveNeeded(additionalArgs[0]);
                kf.setObjectiveNeededAmount(additionalArgs[1]);
                break;
            default:
                plr.sendMessage("§7[Xyg] §cDiesen Schritt gibt es nicht.");
                break;
        }
        kf.sendCompletionState(plr);
    }

    /**
     * Provides {@link KitLoader}s by a {@link Player}.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public interface KitLoaderProvider {

        /**
         * Gets a {@code KitLoader} by a Player.
         *
         * @param plr the player to get a loader for
         * @return KitLoader OR null on failure !! You have to print out error messages yourself!
         */
        public abstract KitLoader getKitLoader(Player plr);
    }
}
