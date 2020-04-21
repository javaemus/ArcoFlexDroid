package net.arcoflexdroid.panels.devices;


public class ArcoFlexDeviceItem  implements Comparable<ArcoFlexDeviceItem>{
    private String name;
    private int posi;
    private int type;
    private String extensions;

    public ArcoFlexDeviceItem(String name,int position, int type, String extensions)
    {
        this.name = name;
        this.posi = position;
        this.type = type;
        this.extensions = extensions;
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


