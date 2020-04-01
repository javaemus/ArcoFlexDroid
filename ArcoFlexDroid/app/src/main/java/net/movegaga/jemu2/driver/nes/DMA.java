package net.movegaga.jemu2.driver.nes;

import jef.map.ReadWriteHandler;

public class DMA implements ReadWriteHandler {
   private PPU ppu;

   public DMA(PPU var1) {
      this.ppu = var1;
   }

   public int read(int var1) {
      throw new RuntimeException("READ DMA");
   }

   public void write(int var1, int var2) {
      for(var2 = 0; var2 < 256; ++var2) {
         int var3 = this.ppu.readMem(var1 + var2);
         this.ppu.spram[var2] = (char)var3;
      }

   }
}
