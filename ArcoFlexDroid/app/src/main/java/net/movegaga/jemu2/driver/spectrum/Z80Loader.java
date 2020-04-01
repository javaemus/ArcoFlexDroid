/*
 * Created on Dec 17, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.movegaga.jemu2.driver.spectrum;

import jef.util.ZipLoader;

public class Z80Loader {

    static final int      VERSION_1  = 0;
    static final int      VERSION_2  = 23;
    static final int      VERSION_3  = 54;

    static final String[] V2_HW_MODE = { "48k", "48k + If.1", "SamRam", "128k", "128k + If.1" };
    static final String[] V3_HW_MODE = { "48k", "48k + If.1", "SamRam", "48k + M.G.T.", "128k", "128k + If.1",
            "128k + M.G.T."         };

    private Spectrum      spec;
    char[]                c;
    int                   version;
    int                   hwMode;

    public Z80Loader(Spectrum spec) {
        this.spec = spec;
    }

    public void load(String name, int crc, int size) {
        System.out.println("LoadZ80 " + name);
        c = new char[size];
        ZipLoader zip = new ZipLoader(name);
        zip.queue(c, crc, 0, size);
        zip.load();

        version = VERSION_1;

        // header
        spec.z80.m_a8 = c[0];
        spec.z80.m_f8 = c[1];
        spec.z80.m_c8 = c[2];
        spec.z80.m_b8 = c[3];
        spec.z80.m_l8 = c[4];
        spec.z80.m_h8 = c[5];
        spec.z80.m_pc16 = c[6] | (c[7] << 8);
        spec.z80.m_sp16 = c[8] | (c[9] << 8);
        spec.z80.m_i8 = c[10];
        spec.z80.m_r8 = c[11] & 0x7f;

        int tbyte = c[12];
        if (tbyte == 255) tbyte = 1;

        if ((tbyte & 0x01) != 0) {
            spec.z80.m_r8 |= 0x80;
        }
        boolean compressed = ((tbyte & 0x20) != 0);

        spec.z80.m_e8 = c[13];
        spec.z80.m_d8 = c[14];

        spec.z80.m_bc16alt = c[15] | (c[16] << 8);
        spec.z80.m_de16alt = c[17] | (c[18] << 8);
        spec.z80.m_hl16alt = c[19] | (c[20] << 8);
        spec.z80.m_af16alt = c[21] | (c[22] << 8);

        spec.z80.m_iy16 = c[23] | (c[24] << 8);
        spec.z80.m_ix16 = c[25] | (c[26] << 8);

        spec.z80.m_iff1a = (c[27] != 0) ? 1 : 0;
        spec.z80.m_iff1b = (c[28] != 0) ? 1 : 0;

        spec.z80.m_im2 = c[29] & 0x03;

        System.out.println("Load .Z80 Snapshot:");
        System.out.println(" size       = " + size + " (0x" + Integer.toHexString(size) + ")");
        System.out.println(" PC         = 0x" + Integer.toHexString(spec.z80.m_pc16));
        System.out.println(" compressed = " + compressed);

        if (spec.z80.m_pc16 == 0) {

            version = c[30];

            if (version == VERSION_2) {
                System.out.println(".Z80 version 2");
                loadHeaderV2();
                System.out.println("Hardware mode : " + V2_HW_MODE[hwMode]);
                
                loadPages(30 + 2 + version);

            } else if (version == VERSION_3) {
                System.out.println(".Z80 version 3");
                loadHeaderV2();
                System.out.println("Hardware mode : " + V3_HW_MODE[hwMode]);
                
                loadPages(30 + 2 + version);

            } else {

                System.err.println("UNSUPPORTED .Z80 FORMAT!!! - " + (int) c[30]);
            }
        } else {

            int addrRead = 30;
            if (compressed) {
                int addrWrite = 16384;

                while ((addrWrite < 65536) && (addrRead < size)) {
                    char tb = c[addrRead++];
                    if (tb != 0xed) {
                        spec.mem[addrWrite] = tb;
                        addrWrite++;
                    } else {
                        tb = c[addrRead++];
                        if (tb != 0xed) {
                            spec.mem[addrWrite++] = 0xed;
                            addrRead--;
                        } else {
                            int count = c[addrRead++];
                            tb = c[addrRead++];
                            while ((count--) != 0) {
                                spec.mem[addrWrite] = tb;
                                addrWrite++;
                            }
                        }
                    }
                }

                System.out.println("Uncompressed to " + addrWrite);

            } else {
                int p = 0x4000;
                for (int i = addrRead; i < c.length; i++) {
                    spec.mem[p++] = c[i];
                }
            }
        }

    }

    private void loadPages(int offs) {
        System.out.println("Load pages from " + offs);
        while (offs < c.length) {
            offs = loadPage(offs);
        }
        
    }

    private int loadPage(int offs) {
        int size = c[offs + 0] | (c[offs + 1] << 8);
        int page = c[offs + 2];
        offs += 3;
        int start = offs;
        System.out.println("Load page: size=" + Integer.toHexString(size) + " page=" + page);
        
        int addrWrite;
        
        switch (page) {
            case 4: addrWrite = 0x8000; break;
            case 5: addrWrite = 0xc000; break;
            case 8: addrWrite = 0x4000; break;
            default: System.err.println("Unknown page:" + page); addrWrite = 0; break;
        }
        
        System.out.println("Start uncompressing at " + Integer.toHexString(addrWrite));
        
        while ((addrWrite < 65536) && ((offs-start) < size)) {
            char tb = c[offs++];
            if (tb != 0xed) {
                spec.mem[addrWrite] = tb;
                addrWrite++;
            } else {
                tb = c[offs++];
                if (tb != 0xed) {
                    spec.mem[addrWrite++] = 0xed;
                    offs--;
                } else {
                    int count = c[offs++];
                    tb = c[offs++];
                    while ((count--) != 0) {
                        spec.mem[addrWrite] = tb;
                        addrWrite++;
                    }
                }
            }
        }

        System.out.println("End uncompressing at " + Integer.toHexString(addrWrite));

        return offs;
    }

    private void loadHeaderV2() {
        spec.z80.m_pc16 = c[32] | (c[33] << 8);
        hwMode = c[34];
    }

}
