/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.games.kits.factory;

import com.google.common.collect.Lists;
import li.l1t.common.cmd.XYCCommandExecutor;
import li.l1t.common.games.kits.KitManager;
import li.l1t.common.util.CommandHelper;
import li.l1t.common.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map.Entry;

/**
 * Register this command AND TabCompleter with the following aliases: "kf","kitfabrik".
 * <p><b>Note:</b> This class is not part of the actual API and may change or be removed without
 * notice.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
//REFACTOR: This class needs to be heavily refactored, plus should this even be in a library? No
// time for that now tho =(
public class CommandKitFactory extends XYCCommandExecutor implements TabCompleter {
    private final KitManager manager;
    private final Map<String, String> objectiveDescriptions;
    private final String permission;
    private final String helpHeader;
    private final Map<UUID, KitFactory> pendingProducts = new HashMap<>();

    /**
     * Constructs a new kit factory command.
     *
     * @param permission            the permission needed to execute the command.
     * @param helpHeader            a message that is displayed above the usage message
     * @param manager               the kit manager to create kits with
     * @param objectiveDescriptions a map of objective names to their descriptions
     */
    public CommandKitFactory(String permission, String helpHeader, KitManager manager,
                             Map<String, String> objectiveDescriptions) {
        this.permission = permission;
        this.helpHeader = helpHeader;
        this.manager = manager;
        this.objectiveDescriptions = objectiveDescriptions;
    }

    @Override
    public boolean catchCommand(CommandSender sender, String senderName, Command cmd,
                                String label, String[] args) {
        if (CommandHelper.kickConsoleFromMethod(sender, label)) {
            return true;
        }
        if (!CommandHelper.checkPermAndMsg(sender, this.permission, label)) {
            return true;
        }
        if (args.length == 0) {
            return this.printHelpTo(sender, label);
        }
        Player plr = (Player) sender;
        switch (args[0]) {
            case "produce":
                KitFactory kfProduce = this.pendingProducts.get(plr.getUniqueId());
                if (kfProduce == null) {
                    plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 1 ausführen!");
                } else {
                    kfProduce.tryProduce(plr);//Error messages handled there
                    if (kfProduce.isProduced()) {
                        this.pendingProducts.remove(plr.getUniqueId());
                    }
                }
                return true;
            case "step":
                if (args.length < 2) {
                    return this.printHelpTo(sender, label);
                }
                KitFactory kfStep = this.pendingProducts.get(plr.getUniqueId());
                if (kfStep == null) {
                    if (args[1].equals("1")) {
                        if (args.length < 4) {
                            return this.printHelpTo(sender, label);
                        }
                        if (!StringUtils.isNumeric(args[3])) {
                            plr.sendMessage(
                                    String.format("Die Sortierid muss eine Zahl sein: %s!", args[3])
                            );
                            return true;
                        }
                        kfStep = new KitFactory(args[2], Integer.parseInt(args[3]), senderName, manager);
                        this.pendingProducts.put(plr.getUniqueId(), kfStep);
                    } else {
                        plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 1 ausführen!");
                        return true;
                    }
                }

                completeStep(kfStep, plr, args);
                return true;
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
                        plr.sendMessage(new String[]{"§6Schritt 4: §eSetzt die Aufgabe des Kits (optional)",
                                "§e  Diese Aufgabe muss erfüllt werden, bevor das Kit verfügbar ist.",
                                "§e  Siehe /" + label + " objectives für verfügbare Aufgaben.",
                                "§6  Parameter 1: §eAufgaben-ID;;Aufgaben-Parameter"});
                        return true;
                    default:
                        plr.sendMessage("§7[Xyg] §cUnbekannter Schritt! §7(bekannt: 1-3)");
                        return true;
                }
            case "todo":
                KitFactory kfToDo = this.pendingProducts.get(plr.getUniqueId());
                if (kfToDo == null) {
                    plr.sendMessage("§7[Xyg] §cDu musst zuerst Schritt 1 ausführen!");
                } else {
                    kfToDo.sendCompletionState(plr);
                }
                return true;
            case "objectives":
                if (this.objectiveDescriptions.isEmpty()) {
                    plr.sendMessage("§7[Xyg] §cKeine Aufgaben definiert, überspringe diesen Schritt einfach.");
                    return true;
                }
                for (Entry<String, String> entry : this.objectiveDescriptions.entrySet()) {
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
                case "shelp":
                    return CommandHelper.tabCompleteArgs(args, Lists.newArrayList("1", "2", "3", "4"));
                default:
                    return Collections.emptyList();
            }
        }
        return Lists.newArrayList("step", "shelp", "help", "produce", "todo", "objectives");
    }

    public KitManager getManager() {
        return manager;
    }

    public Map<String, String> getObjectiveDescriptions() {
        return objectiveDescriptions;
    }

    public String getPermission() {
        return permission;
    }

    public Map<UUID, KitFactory> getPendingProducts() {
        return pendingProducts;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private boolean printHelpTo(CommandSender sender, String label) {
        sender.sendMessage(new String[]{
                this.helpHeader,
                "§7/" + label + " help §eZeigt diese Hilfe an.",
                "§7/" + label + " produce §eProduziert das Kit, d.h. veröffentlicht es.",
                "§7/" + label + " step 1 <Kit-Name> <Sortierungs-ID> §eSetzt Rüstung & Inhalt.",
                "§7/" + label + " step 2 §eSetzt das Icon.",
                "§7/" + label + " step 3 §eSetzt das Fehlericon.",
                "§7/" + label + " step 4 <Aufgaben-ID>;;<Aufgaben-Argument> §eSetzt die benötigten Aufgaben.",
                "§7/" + label + " todo §eZeigt deine ToDo-Liste an.",
                "§7/" + label + " shelp <Schritt-ID> §eZeigt Anweisungen zu einem Schritt an.",
                "§7/" + label + " objectives §eZeigt alle verfügbaren Aufgaben an"
        });
        return true;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void completeStep(KitFactory factory, Player plr, String[] args) {
        switch (args[1]) {
            case "1":
                factory.setItems(plr.getInventory().getContents(), plr.getInventory().getArmorContents());
                break;
            case "2":
                ItemStack iconStack = plr.getItemInHand();
                if (iconStack == null || iconStack.getType() == Material.AIR) {
                    plr.sendMessage("§7[Xyg] §cDu hast nichts in der Hand!");
                    break;
                }
                factory.setIcon(iconStack);
                break;
            case "4":
                factory.setObjectiveNeeded(StringHelper.varArgsString(args, 2, false));
                break;
            default:
                plr.sendMessage("§7[Xyg] §cDiesen Schritt gibt es nicht.");
                break;
        }
        factory.sendCompletionState(plr);
    }
}
