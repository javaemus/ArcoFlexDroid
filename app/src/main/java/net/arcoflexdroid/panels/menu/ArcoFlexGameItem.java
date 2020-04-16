package net.arcoflexdroid.panels.menu;

public class ArcoFlexGameItem implements Comparable {
    private String shortname = "";
    private String description = "";

    public ArcoFlexGameItem(String shortname, String description){
        this.shortname = shortname;
        this.description = description;
    }

    @Override
    public int compareTo(Object o) {
        if(this.description != null)
            return this.description.toLowerCase().compareTo(((ArcoFlexGameItem)o).getDescription().toLowerCase());
        else
            throw new IllegalArgumentException();
    }

    public String getDescription() {
        return description;
    }

    public String getShortname() {
        return shortname;
    }

}
