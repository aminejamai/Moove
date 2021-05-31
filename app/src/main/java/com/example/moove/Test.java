package com.example.moove;

public class Test {

    private String name;
    private String desc;
    private String localisation;

    public Test(String name, String desc, String localisation){
        this.desc = desc;
        this.name = name;
        this.localisation = localisation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
}
