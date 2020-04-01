package net.movegaga.jemu2.driver.nes;

import jef.map.InputPort;
import jef.map.ReadWriteHandler;

public class Joystick implements ReadWriteHandler {
   private InputPort in;

   public Joystick(InputPort var1) {
      this.in = var1;
   }

   public int read(int var1) {
      return this.in.read(0);
   }

   public void write(int var1, int var2) {
   }
}
