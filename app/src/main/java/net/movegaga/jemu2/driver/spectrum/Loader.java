/*
 * Created on Dec 20, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.movegaga.jemu2.driver.spectrum;

public class Loader {
    private Spectrum spec;

    public Loader(Spectrum spec) {
        this.spec = spec;
    }
    
    public void load(String name, int crc, int size) {
        if (size == 49179) {
            new SNALoader(spec).load(name, crc);
        } else {
            new Z80Loader(spec).load(name, crc, size);
        }
        spec.video.refreshFrame();
    }

}
