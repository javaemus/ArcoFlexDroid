/*
 * Created on Dec 20, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.movegaga.jemu2.driver.spectrum;

import jef.util.ZipLoader;

public class SNALoader {
    
    Spectrum spec;

    public SNALoader(Spectrum spec) {
        this.spec = spec;
    }

    public void load(String name, int crc) {
        System.out.println("LoadSNA " + name);
        char[] c = new char[49179];
        ZipLoader zip = new ZipLoader(name);
        zip.queue(c, crc, 0, 49179);
        zip.load();
        
        spec.z80.m_i8 = c[0];

        spec.z80.m_h8 = c[2];
        spec.z80.m_l8 = c[1];
        spec.z80.m_d8 = c[4];
        spec.z80.m_e8 = c[3];
        spec.z80.m_b8 = c[6];
        spec.z80.m_c8 = c[5];
        spec.z80.m_a8 = c[8];
        spec.z80.m_f8 = c[7];
        
        spec.z80.exx();
        spec.z80.ex_af_af();
        
        spec.z80.m_h8 = c[10];
        spec.z80.m_l8 = c[9];
        spec.z80.m_d8 = c[12];
        spec.z80.m_e8 = c[11];
        spec.z80.m_b8 = c[14];
        spec.z80.m_c8 = c[13];
        spec.z80.m_iy16 = ( c[15]  | (c[16]<<8) );
        spec.z80.m_ix16 = ( c[17]  | (c[18]<<8) );
        
        spec.z80.m_iff1b = ( (c[19] & 0x04)!= 0 ) ? 1 : 0;
        
        spec.z80.m_r8 = c[20];
        spec.z80.m_a8 = c[22];
        spec.z80.m_f8 = c[11];
        spec.z80.m_sp16 = ( c[23]  | (c[24]<<8) );

        spec.z80.m_im2 = c[25] & 0x03;

        spec.z80.m_iff1a = spec.z80.m_iff1b;
        
        System.out.println("sp=" + Integer.toHexString(spec.z80.m_sp16));
        System.out.println("pc=" + Integer.toHexString(spec.z80.m_pc16));
        
        int p = 0x4000;
        for (int i = 27; i < c.length; i++) {
            spec.mem[p++] = c[i];
        }
        spec.z80.m_pc16 = spec.z80.pop16();

    }

}
