package net.arcoflexdroid.panels.filebrowser;

public class ArcoFlexFileItem  implements Comparable<ArcoFlexFileItem>{
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;
    private String inlay=null;
    private boolean isRemoteZip = false;

    public ArcoFlexFileItem(String n,String d, String dt, String p, String img)
    {
        name = n;
        data = d;
        date = dt;
        path = p;
        image = img;
    }
    public void setRemoteZip(boolean b) {isRemoteZip=b;}
    public boolean isRemoteZip() {return isRemoteZip;}
    public void setInlay(String _inlay) {
        inlay = _inlay;
    }
    public String getInlay() { return inlay; }
    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getDate()
    {
        return date;
    }
    public String getPath()
    {
        return path;
    }
    public String getImage() {
        return image;
    }
    public int compareTo(ArcoFlexFileItem o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}

