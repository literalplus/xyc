/*
 * -- placeholder --
 */

package li.l1t.common.lib.com.mojang.api.profiles;

import li.l1t.common.util.UUIDHelper;

import java.util.UUID;

public class MojangProfile implements Profile {
    private String id;
    private String name;
    private boolean demo;
    private boolean legacy;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public UUID getUniqueId() {
        return UUIDHelper.getFromString(getId());
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

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }
}
