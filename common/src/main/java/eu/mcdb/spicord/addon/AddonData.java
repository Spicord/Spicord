package eu.mcdb.spicord.addon;

import lombok.Getter;

@Getter
public class AddonData {

    private String id;
    private String name;
    private String author;
    //private String[] authors = new String[0];
    //private String[] modules = new String[0];
    private String main = "index.js";
    //private String engine = "rhino";
}
