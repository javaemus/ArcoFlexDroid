package jef.machine;

import jef.map.ReadHandler;
import jef.map.WriteHandler;

public class PPI8255 {
   int groupA_mode;
   int groupB_mode;
   int ibf_a;
   int ibf_b;
   int[] in_mask = new int[3];
   int inte_a;
   int inte_b;
   int[] latch = new int[3];
   int obf_a;
   int obf_b;
   int[] out_mask = new int[3];
   int[] output = new int[3];
   int portA_dir;
   int portB_dir;
   int portCH_dir;
   int portCL_dir;
   ReadHandler[] port_read = new ReadHandler[3];
   WriteHandler[] port_write = new WriteHandler[3];
   int[] read = new int[3];

   public PPI8255(ReadHandler var1, ReadHandler var2, ReadHandler var3, WriteHandler var4, WriteHandler var5, WriteHandler var6) {
      this.port_read[0] = var1;
      this.port_read[1] = var2;
      this.port_read[2] = var3;
      this.port_write[0] = var4;
      this.port_write[1] = var5;
      this.port_write[2] = var6;
   }

   private int ppi8255_get_handshake_signals(int var1, int var2) {
      int var3 = 0;
      var1 = 0;
      byte var4;
      byte var6;
      if(this.groupA_mode == 1) {
         byte var8;
         if(this.portA_dir != 0) {
            if(this.ibf_a != 0) {
               var6 = 32;
            } else {
               var6 = 0;
            }

            if(this.ibf_a != 0 && this.inte_a != 0) {
               var8 = 8;
            } else {
               var8 = 0;
            }

            var3 = 0 | var6 | var8;
            var1 = 0 | 40;
         } else {
            short var7;
            if(this.obf_a != 0) {
               var7 = 0;
            } else {
               var7 = 128;
            }

            if(this.obf_a != 0 && this.inte_a != 0) {
               var8 = 8;
            } else {
               var8 = 0;
            }

            var3 = 0 | var7 | var8;
            var1 = 0 | 136;
         }
      } else if(this.groupA_mode == 2) {
         if(this.inte_a != 0) {
            var6 = 8;
         } else {
            var6 = 0;
         }

         short var11;
         if(this.obf_a != 0) {
            var11 = 0;
         } else {
            var11 = 128;
         }

         if(this.ibf_a != 0) {
            var4 = 32;
         } else {
            var4 = 0;
         }

         var3 = 0 | var6 | var11 | var4;
         var1 = 0 | 168;
      }

      int var9 = var3;
      int var5 = var1;
      if(this.groupB_mode == 1) {
         byte var10;
         if(this.portA_dir != 0) {
            if(this.ibf_b != 0) {
               var4 = 2;
            } else {
               var4 = 0;
            }

            if(this.ibf_b != 0 && this.inte_b != 0) {
               var10 = 1;
            } else {
               var10 = 0;
            }

            var9 = var3 | var4 | var10;
            var5 = var1 | 3;
         } else {
            if(this.obf_b != 0) {
               var4 = 0;
            } else {
               var4 = 2;
            }

            if(this.obf_b != 0 && this.inte_b != 0) {
               var10 = 1;
            } else {
               var10 = 0;
            }

            var9 = var3 | var4 | var10;
            var5 = var1 | 3;
         }
      }

      return var2 & ~var5 | var9 & var5;
   }

   private void ppi8255_input(int var1, int var2) {
      boolean var3 = false;
      if(this.read[var1] != var2) {
         this.read[var1] = var2;
         if(var1 == 2) {
            boolean var4;
            label36: {
               if(this.groupA_mode != 1 || this.portA_dir != 0) {
                  var4 = var3;
                  if(this.groupA_mode != 2) {
                     break label36;
                  }
               }

               var4 = var3;
               if((var2 & 64) == 0) {
                  this.obf_a = 0;
                  var4 = true;
               }
            }

            var3 = var4;
            if(this.groupB_mode == 1) {
               var3 = var4;
               if(this.portB_dir == 0) {
                  var3 = var4;
                  if((var2 & 4) == 0) {
                     this.obf_b = 0;
                     var3 = true;
                  }
               }
            }

            if(var3) {
               this.ppi8255_write_port(2);
            }
         }
      }

   }

   private void ppi8255_write_port(int var1) {
      int var3 = this.latch[var1] & this.out_mask[var1] | ~this.out_mask[var1] & 255;
      int var2 = var3;
      if(var1 == 2) {
         var2 = this.ppi8255_get_handshake_signals(0, var3);
      }

      this.output[var1] = var2;
      if(this.port_write[var1] != null) {
         this.port_write[var1].write(0, var2);
      }

   }

   private int readPort(int var1) {
      int var2 = 0;
      if(this.in_mask[var1] != 0) {
         if(this.port_read[var1] != null) {
            this.ppi8255_input(var1, this.port_read[var1].read(0));
         }

         var2 = 0 | this.read[var1] & this.in_mask[var1];
      }

      int var3 = var2 | this.latch[var1] & this.out_mask[var1];
      var2 = var3;
      if(var1 == 2) {
         var2 = this.ppi8255_get_handshake_signals(1, var3);
      }

      return var2;
   }

   private void set_mode(int var1, int var2) {
      this.groupA_mode = var1 >> 5 & 3;
      this.groupB_mode = var1 >> 2 & 1;
      this.portA_dir = var1 >> 4 & 1;
      this.portB_dir = var1 >> 1 & 1;
      this.portCH_dir = var1 >> 3 & 1;
      this.portCL_dir = var1 >> 0 & 1;
      if(this.groupA_mode == 3) {
         this.groupA_mode = 2;
      }

      if(this.portA_dir != 0) {
         this.in_mask[0] = 255;
         this.out_mask[0] = 0;
      } else {
         this.in_mask[0] = 0;
         this.out_mask[0] = 255;
      }

      if(this.portB_dir != 0) {
         this.in_mask[1] = 255;
         this.out_mask[1] = 0;
      } else {
         this.in_mask[1] = 0;
         this.out_mask[1] = 255;
      }

      if(this.portCH_dir != 0) {
         this.in_mask[2] = 240;
         this.out_mask[2] = 0;
      } else {
         this.in_mask[2] = 0;
         this.out_mask[2] = 240;
      }

      int[] var3;
      if(this.portCL_dir != 0) {
         var3 = this.in_mask;
         var3[2] |= 15;
      } else {
         var3 = this.out_mask;
         var3[2] |= 15;
      }

      switch(this.groupA_mode) {
      case 0:
      default:
         break;
      case 1:
         var3 = this.in_mask;
         var3[2] &= -57;
         var3 = this.out_mask;
         var3[2] &= -57;
         break;
      case 2:
         var3 = this.in_mask;
         var3[2] &= -249;
         var3 = this.out_mask;
         var3[2] &= -249;
      }

      switch(this.groupB_mode) {
      case 0:
      default:
         break;
      case 1:
         var3 = this.in_mask;
         var3[2] &= -8;
         var3 = this.out_mask;
         var3[2] &= -8;
      }

      int[] var4 = this.latch;
      var3 = this.latch;
      this.latch[2] = 0;
      var3[1] = 0;
      var4[0] = 0;
      if(var2 != 0) {
         for(var1 = 0; var1 < 3; ++var1) {
            this.ppi8255_write_port(var1);
         }
      }

   }

   public int read(int var1) {
      byte var2 = 0;
      var1 %= 4;
      switch(var1) {
      case 0:
      case 1:
      case 2:
         var1 = this.readPort(var1);
         break;
      case 3:
         var1 = 255;
         break;
      default:
         var1 = var2;
      }

      return var1;
   }

   public void write(int var1, int var2) {
      var1 %= 4;
      switch(var1) {
      case 0:
      case 1:
      case 2:
         this.latch[var1] = var2;
         this.ppi8255_write_port(var1);
         switch(var1) {
         case 0:
            if(this.portA_dir == 0 && this.groupA_mode != 0) {
               this.obf_a = 1;
               this.ppi8255_write_port(2);
            }

            return;
         case 1:
            if(this.portB_dir == 0 && this.groupB_mode != 0) {
               this.obf_b = 1;
               this.ppi8255_write_port(2);
            }

            return;
         default:
            return;
         }
      case 3:
         if((var2 & 128) != 0) {
            this.set_mode(var2 & 127, 1);
         } else {
            var1 = var2 >> 1 & 7;
            int[] var3;
            if((var2 & 1) != 0) {
               var3 = this.latch;
               var3[2] |= 1 << var1;
            } else {
               var3 = this.latch;
               var3[2] &= ~(1 << var1);
            }

            this.ppi8255_write_port(2);
         }
      }

   }
}
