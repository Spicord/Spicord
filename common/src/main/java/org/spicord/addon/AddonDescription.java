package org.spicord.addon;

import lombok.Getter;

@Getter
public class AddonDescription {

    private String id;
    private String name;
    private String author;
    private String version;
    //private String[] authors = new String[0];
    private String[] modules = new String[0];
    private String main = "index.js"; //default
    //private String engine = "rhino";
    private String language = "javascript"; //default
}
