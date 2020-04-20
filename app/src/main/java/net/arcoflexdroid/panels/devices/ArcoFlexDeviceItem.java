package net.arcoflexdroid.panels.devices;


public class ArcoFlexDeviceItem  implements Comparable<ArcoFlexDeviceItem>{
    private String name;
    private int posi;
    private int type;

    public ArcoFlexDeviceItem(String n,int p, int t)
    {
        name = n;
        posi = p;
        type = t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosi() {
        return posi;
    }

    public void setPosi(int posi) {
        this.posi = posi;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int compareTo(ArcoFlexDeviceItem o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }

}


