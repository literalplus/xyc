package io.github.xxyy.common.lib.com.mojang.api.profiles;

import io.github.xxyy.common.lib.net.minecraft.server.UtilUUID;

import java.util.UUID;

public class MojangProfile implements Profile {
    private String id;
    private String name;
    private boolean demo;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public UUID getUniqueId() {
        return UtilUUID.getFromString(getId());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo){
        this.demo = demo;
    }
}
