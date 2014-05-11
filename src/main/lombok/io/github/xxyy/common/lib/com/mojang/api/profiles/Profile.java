package io.github.xxyy.common.lib.com.mojang.api.profiles;

public class Profile {
    private String id;
    private String name;
    private boolean demo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getDemo() {
        return demo;
    }

    public void setDemo(boolean demo){
        this.demo = demo;
    }
}
