/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.tests.implementations;

import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author xxyy98 (http://xxyy.github.io/)
 */
public class TestConsoleCommandSender implements ConsoleCommandSender {

    private final Server server;

    public TestConsoleCommandSender(Server svr) {
        this.server = svr;
    }

    @Override
    public void sendMessage(String string) {
        getServer().getLogger().log(Level.INFO, string);
    }

    @Override
    public void sendMessage(String[] strings) {
        for (String string : strings) {
            sendMessage(string);
        }
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean isPermissionSet(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean isPermissionSet(Permission prmsn) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean hasPermission(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean hasPermission(Permission prmsn) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void removeAttachment(PermissionAttachment pa) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean bln) {
        
    }

    @Override
    public boolean isConversing() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void acceptConversationInput(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean beginConversation(Conversation c) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void abandonConversation(Conversation c) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void abandonConversation(Conversation c, ConversationAbandonedEvent cae) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void sendRawMessage(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }
}
