package jef.sound.chip;

import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.sound.SoundChip;

public class AY8910 extends SoundChip {
   static final int AY_ACOARSE = 1;
   static final int AY_AFINE = 0;
   static final int AY_AVOL = 8;
   static final int AY_BCOARSE = 3;
   static final int AY_BFINE = 2;
   static final int AY_BVOL = 9;
   static final int AY_CCOARSE = 5;
   static final int AY_CFINE = 4;
   static final int AY_CVOL = 10;
   static final int AY_ECOARSE = 12;
   static final int AY_EFINE = 11;
   static final int AY_ENABLE = 7;
   static final int AY_ESHAPE = 13;
   static final int AY_NOISEPER = 6;
   static final int AY_PORTA = 14;
   static final int AY_PORTB = 15;
   static final int MAX_8910 = 5;
   static final int MAX_OUTPUT = 32767;
   static final int STEP = 4096;
   static AY8910Context[] ay = new AY8910Context[5];
   static int ym_num = 0;
   int ay8910_index_ym;
   int baseClock;
   int num = 0;
   ReadHandler[] readA = new ReadHandler[5];
   ReadHandler[] readB = new ReadHandler[5];
   WriteHandler[] writeA = new WriteHandler[5];
   WriteHandler[] writeB = new WriteHandler[5];

   public AY8910(int var1, int var2) {
      this.num = var1;
      this.baseClock = var2;
   }

   public AY8910(int var1, int var2, ReadHandler[] var3, ReadHandler[] var4, WriteHandler[] var5, WriteHandler[] var6) {
      this.num = var1;
      this.baseClock = var2;
      this.readA = var3;
      this.readB = var4;
      this.writeA = var5;
      this.writeB = var6;
   }

   public int AY8910Read(int var1) {
      return this.AYReadReg(var1, ay[var1].register_latch);
   }

   public void AY8910Update(int var1) {
      AY8910Context var13 = ay[var1];
      int var10 = 0;
      int var11 = super.getBufferLength();
      if((var13.Regs[7] & 1) != 0) {
         if(var13.CountA <= var11 * 4096) {
            var13.CountA += var11 * 4096;
         }

         var13.OutputA = 1;
      } else if(var13.Regs[8] == 0 && var13.CountA <= var11 * 4096) {
         var13.CountA += var11 * 4096;
      }

      if((var13.Regs[7] & 2) != 0) {
         if(var13.CountB <= var11 * 4096) {
            var13.CountB += var11 * 4096;
         }

         var13.OutputB = 1;
      } else if(var13.Regs[9] == 0 && var13.CountB <= var11 * 4096) {
         var13.CountB += var11 * 4096;
      }

      if((var13.Regs[7] & 4) != 0) {
         if(var13.CountC <= var11 * 4096) {
            var13.CountC += var11 * 4096;
         }

         var13.OutputC = 1;
      } else if(var13.Regs[10] == 0 && var13.CountC <= var11 * 4096) {
         var13.CountC += var11 * 4096;
      }

      if((var13.Regs[7] & 56) == 56 && var13.CountN <= var11 * 4096) {
         var13.CountN += var11 * 4096;
      }

      int var8;
      for(int var2 = var13.OutputN | var13.Regs[7]; var11 != 0; var2 = var8) {
         int var3 = 0;
         int var5 = 0;
         int var7 = 0;
         var1 = 4096;

         int var4;
         int var6;
         int var9;
         int var12;
         do {
            if(var13.CountN < var1) {
               var12 = var13.CountN;
            } else {
               var12 = var1;
            }

            if((var2 & 8) == 0) {
               var13.CountA -= var12;

               while(true) {
                  var4 = var7;
                  if(var13.CountA > 0) {
                     break;
                  }

                  var13.CountA += var13.PeriodA;
                  if(var13.CountA > 0) {
                     var13.OutputA ^= 1;
                     var4 = var7;
                     break;
                  }

                  var13.CountA += var13.PeriodA;
               }
            } else {
               var4 = var7;
               if(var13.OutputA != 0) {
                  var4 = var7 + var13.CountA;
               }

               var13.CountA -= var12;

               while(true) {
                  if(var13.CountA > 0) {
                     var6 = var4;
                     break;
                  }

                  var13.CountA += var13.PeriodA;
                  if(var13.CountA > 0) {
                     var13.OutputA ^= 1;
                     var6 = var4;
                     if(var13.OutputA != 0) {
                        var6 = var4 + var13.PeriodA;
                     }
                     break;
                  }

                  var13.CountA += var13.PeriodA;
                  var4 += var13.PeriodA;
               }

               var4 = var6;
               if(var13.OutputA != 0) {
                  var4 = var6 - var13.CountA;
               }
            }

            if((var2 & 16) == 0) {
               var13.CountB -= var12;

               while(true) {
                  var6 = var5;
                  if(var13.CountB > 0) {
                     break;
                  }

                  var13.CountB += var13.PeriodB;
                  if(var13.CountB > 0) {
                     var13.OutputB ^= 1;
                     var6 = var5;
                     break;
                  }

                  var13.CountB += var13.PeriodB;
               }
            } else {
               var6 = var5;
               if(var13.OutputB != 0) {
                  var6 = var5 + var13.CountB;
               }

               var13.CountB -= var12;

               while(true) {
                  if(var13.CountB > 0) {
                     var5 = var6;
                     break;
                  }

                  var13.CountB += var13.PeriodB;
                  if(var13.CountB > 0) {
                     var13.OutputB ^= 1;
                     var5 = var6;
                     if(var13.OutputB != 0) {
                        var5 = var6 + var13.PeriodB;
                     }
                     break;
                  }

                  var13.CountB += var13.PeriodB;
                  var6 += var13.PeriodB;
               }

               var6 = var5;
               if(var13.OutputB != 0) {
                  var6 = var5 - var13.CountB;
               }
            }

            if((var2 & 32) == 0) {
               var13.CountC -= var12;

               while(true) {
                  var9 = var3;
                  if(var13.CountC > 0) {
                     break;
                  }

                  var13.CountC += var13.PeriodC;
                  if(var13.CountC > 0) {
                     var13.OutputC ^= 1;
                     var9 = var3;
                     break;
                  }

                  var13.CountC += var13.PeriodC;
               }
            } else {
               var5 = var3;
               if(var13.OutputC != 0) {
                  var5 = var3 + var13.CountC;
               }

               var13.CountC -= var12;

               while(true) {
                  if(var13.CountC > 0) {
                     var3 = var5;
                     break;
                  }

                  var13.CountC += var13.PeriodC;
                  if(var13.CountC > 0) {
                     var13.OutputC ^= 1;
                     var3 = var5;
                     if(var13.OutputC != 0) {
                        var3 = var5 + var13.PeriodC;
                     }
                     break;
                  }

                  var13.CountC += var13.PeriodC;
                  var5 += var13.PeriodC;
               }

               var9 = var3;
               if(var13.OutputC != 0) {
                  var9 = var3 - var13.CountC;
               }
            }

            var13.CountN -= var12;
            var8 = var2;
            if(var13.CountN <= 0) {
               if((var13.RNG + 1 & 2) != 0) {
                  var13.OutputN = ~var13.OutputN;
                  var2 = var13.OutputN | var13.Regs[7];
               }

               if((var13.RNG & 1) != 0) {
                  var13.RNG ^= 163840;
               }

               var13.RNG >>= 1;
               var13.CountN += var13.PeriodN;
               var8 = var2;
            }

            var12 = var1 - var12;
            var1 = var12;
            var2 = var8;
            var7 = var4;
            var5 = var6;
            var3 = var9;
         } while(var12 > 0);

         if(var13.Holding == 0) {
            var13.CountE -= 4096;
            if(var13.CountE <= 0) {
               do {
                  --var13.CountEnv;
                  var13.CountE += var13.PeriodE;
               } while(var13.CountE <= 0);

               if(var13.CountEnv < 0) {
                  if(var13.Hold != 0) {
                     if(var13.Alternate != 0) {
                        var13.Attack ^= 31;
                     }

                     var13.Holding = 1;
                     var13.CountEnv = 0;
                  } else {
                     if(var13.Alternate != 0 && (var13.CountEnv & 32) != 0) {
                        var13.Attack ^= 31;
                     }

                     var13.CountEnv &= 31;
                  }
               }

               var13.VolE = var13.VolTable[var13.CountEnv ^ var13.Attack];
               if(var13.EnvelopeA != 0) {
                  var13.VolA = var13.VolE;
               }

               if(var13.EnvelopeB != 0) {
                  var13.VolB = var13.VolE;
               }

               if(var13.EnvelopeC != 0) {
                  var13.VolC = var13.VolE;
               }
            }
         }

         var1 = (var13.VolA * var4 / 4096 + var13.VolB * var6 / 4096 + var13.VolC * var9 / 4096) / this.num;
         this.writeLinBuffer(var10, this.readLinBuffer(var10) + var1);
         ++var10;
         --var11;
      }

   }

   public void AY8910Write(int var1, int var2, int var3) {
      AY8910Context var4 = ay[var1];
      if((var2 & 1) != 0) {
         this.AYWriteReg(var1, var4.register_latch, var3);
      } else {
         var4.register_latch = var3 & 15;
      }

   }

   public WriteHandler AY8910_control_port_0_w() {
      return new AY8910_control_port_w(0);
   }

   public WriteHandler AY8910_control_port_1_w() {
      return new AY8910_control_port_w(1);
   }

   public WriteHandler AY8910_control_port_2_w() {
      return new AY8910_control_port_w(2);
   }

   public WriteHandler AY8910_control_port_3_w() {
      return new AY8910_control_port_w(3);
   }

   public WriteHandler AY8910_control_port_4_w() {
      return new AY8910_control_port_w(4);
   }

   public int AY8910_init(String var1, int var2, int var3, int var4, int var5, ReadHandler var6, ReadHandler var7, WriteHandler var8, WriteHandler var9) {
      System.out.println("AY8910_init " + var2);
      ay[var2] = new AY8910Context();
      AY8910Context var10 = ay[var2];
      int[] var11 = new int[3];
      var10.PortAread = var6;
      var10.PortBread = var7;
      var10.PortAwrite = var8;
      var10.PortBwrite = var9;

      for(var5 = 0; var5 < 3; ++var5) {
         var11[var5] = var4;
      }

      this.AY8910_set_clock(var2, var3);
      this.AY8910_reset(var2);
      var10.PeriodA = var10.UpdateStep;
      var10.PeriodB = var10.UpdateStep;
      var10.PeriodC = var10.UpdateStep;
      var10.PeriodE = var10.UpdateStep;
      var10.PeriodN = var10.UpdateStep;
      return 0;
   }

   public void AY8910_reset(int var1) {
      AY8910Context var3 = ay[var1];
      var3.register_latch = 0;
      var3.RNG = 1;
      var3.OutputA = 0;
      var3.OutputB = 0;
      var3.OutputC = 0;
      var3.OutputN = 255;
      var3.lastEnable = -1;

      for(int var2 = 0; var2 < 14; ++var2) {
         this._AYWriteReg(var1, var2, 0);
      }

   }

   public void AY8910_set_clock(int var1, int var2) {
      ay[var1].UpdateStep = super.getSampFreq() * 4096 * 8 / var2;
   }

   public void AY8910_sh_reset() {
      for(int var1 = 0; var1 < this.num + ym_num; ++var1) {
         this.AY8910_reset(var1);
      }

   }

   public WriteHandler AY8910_write_port_0_w() {
      return new AY8910_write_port_w(0);
   }

   public WriteHandler AY8910_write_port_1_w() {
      return new AY8910_write_port_w(1);
   }

   public WriteHandler AY8910_write_port_2_w() {
      return new AY8910_write_port_w(2);
   }

   public WriteHandler AY8910_write_port_3_w() {
      return new AY8910_write_port_w(3);
   }

   public WriteHandler AY8910_write_port_4_w() {
      return new AY8910_write_port_w(4);
   }

   public int AYReadReg(int var1, int var2) {
      AY8910Context var3 = ay[var1];
      if(var2 > 15) {
         var1 = 0;
      } else {
         switch(var2) {
         case 14:
            if((var3.Regs[7] & 64) != 0) {
               System.out.println("warning: read from 8910 #" + var1 + " Port A set as output");
            } else if(var3.PortAread != null) {
               var3.Regs[14] = var3.PortAread.read(0);
            }
            break;
         case 15:
            if((var3.Regs[7] & 128) != 0) {
               System.out.println("warning: read from 8910 #" + var1 + " Port B set as output");
            } else if(var3.PortBread != null) {
               var3.Regs[15] = var3.PortBread.read(0);
            }
         }

         var1 = var3.Regs[var2];
      }

      return var1;
   }

   public void AYWriteReg(int var1, int var2, int var3) {
      AY8910Context var5 = ay[var1];
      if(var2 <= 15) {
         if(var2 < 14 && var2 != 13) {
            int var10000 = var5.Regs[var2];
         }

         this._AYWriteReg(var1, var2, var3);
      }

   }

   public void _AYWriteReg(int var1, int var2, int var3) {
      AY8910Context var4 = ay[var1];
      var4.Regs[var2] = var3;
      int[] var5;
      switch(var2) {
      case 0:
      case 1:
         var5 = var4.Regs;
         var5[1] &= 15;
         var1 = var4.PeriodA;
         var4.PeriodA = (var4.Regs[0] + var4.Regs[1] * 256) * var4.UpdateStep;
         if(var4.PeriodA == 0) {
            var4.PeriodA = var4.UpdateStep;
         }

         var4.CountA += var4.PeriodA - var1;
         if(var4.CountA <= 0) {
            var4.CountA = 1;
         }
         break;
      case 2:
      case 3:
         var5 = var4.Regs;
         var5[3] &= 15;
         var1 = var4.PeriodB;
         var4.PeriodB = (var4.Regs[2] + var4.Regs[3] * 256) * var4.UpdateStep;
         if(var4.PeriodB == 0) {
            var4.PeriodB = var4.UpdateStep;
         }

         var4.CountB += var4.PeriodB - var1;
         if(var4.CountB <= 0) {
            var4.CountB = 1;
         }
         break;
      case 4:
      case 5:
         var5 = var4.Regs;
         var5[5] &= 15;
         var1 = var4.PeriodC;
         var4.PeriodC = (var4.Regs[4] + var4.Regs[5] * 256) * var4.UpdateStep;
         if(var4.PeriodC == 0) {
            var4.PeriodC = var4.UpdateStep;
         }

         var4.CountC += var4.PeriodC - var1;
         if(var4.CountC <= 0) {
            var4.CountC = 1;
         }
         break;
      case 6:
         var5 = var4.Regs;
         var5[6] &= 31;
         var1 = var4.PeriodN;
         var4.PeriodN = var4.Regs[6] * var4.UpdateStep;
         if(var4.PeriodN == 0) {
            var4.PeriodN = var4.UpdateStep;
         }

         var4.CountN += var4.PeriodN - var1;
         if(var4.CountN <= 0) {
            var4.CountN = 1;
         }
         break;
      case 7:
         WriteHandler var7;
         if((var4.lastEnable == -1 || (var4.lastEnable & 64) != (var4.Regs[7] & 64)) && var4.PortAwrite != null) {
            var7 = var4.PortAwrite;
            if((var4.Regs[7] & 64) != 0) {
               var1 = var4.Regs[14];
            } else {
               var1 = 255;
            }

            var7.write(0, var1);
         }

         if((var4.lastEnable == -1 || (var4.lastEnable & 128) != (var4.Regs[7] & 128)) && var4.PortBwrite != null) {
            var7 = var4.PortBwrite;
            if((var4.Regs[7] & 128) != 0) {
               var1 = var4.Regs[15];
            } else {
               var1 = 255;
            }

            var7.write(0, var1);
         }

         var4.lastEnable = var4.Regs[7];
         break;
      case 8:
         var5 = var4.Regs;
         var5[8] &= 31;
         var4.EnvelopeA = var4.Regs[8] & 16;
         if(var4.EnvelopeA != 0) {
            var1 = var4.VolE;
         } else {
            var5 = var4.VolTable;
            if(var4.Regs[8] != 0) {
               var1 = var4.Regs[8] * 2 + 1;
            } else {
               var1 = 0;
            }

            var1 = var5[var1];
         }

         var4.VolA = var1;
         break;
      case 9:
         var5 = var4.Regs;
         var5[9] &= 31;
         var4.EnvelopeB = var4.Regs[9] & 16;
         if(var4.EnvelopeB != 0) {
            var1 = var4.VolE;
         } else {
            var5 = var4.VolTable;
            if(var4.Regs[9] != 0) {
               var1 = var4.Regs[9] * 2 + 1;
            } else {
               var1 = 0;
            }

            var1 = var5[var1];
         }

         var4.VolB = var1;
         break;
      case 10:
         var5 = var4.Regs;
         var5[10] &= 31;
         var4.EnvelopeC = var4.Regs[10] & 16;
         if(var4.EnvelopeC != 0) {
            var1 = var4.VolE;
         } else {
            var5 = var4.VolTable;
            if(var4.Regs[10] != 0) {
               var1 = var4.Regs[10] * 2 + 1;
            } else {
               var1 = 0;
            }

            var1 = var5[var1];
         }

         var4.VolC = var1;
         break;
      case 11:
      case 12:
         var1 = var4.PeriodE;
         var4.PeriodE = (var4.Regs[11] + var4.Regs[12] * 256) * var4.UpdateStep;
         if(var4.PeriodE == 0) {
            var4.PeriodE = var4.UpdateStep / 2;
         }

         var4.CountE += var4.PeriodE - var1;
         if(var4.CountE <= 0) {
            var4.CountE = 1;
         }
         break;
      case 13:
         var5 = var4.Regs;
         var5[13] &= 15;
         byte var6;
         if((var4.Regs[13] & 4) != 0) {
            var6 = 31;
         } else {
            var6 = 0;
         }

         var4.Attack = var6;
         if((var4.Regs[13] & 8) == 0) {
            var4.Hold = 1;
            var4.Alternate = var4.Attack;
         } else {
            var4.Hold = var4.Regs[13] & 1;
            var4.Alternate = var4.Regs[13] & 2;
         }

         var4.CountE = var4.PeriodE;
         var4.CountEnv = 31;
         var4.Holding = 0;
         var4.VolE = var4.VolTable[var4.CountEnv ^ var4.Attack];
         if(var4.EnvelopeA != 0) {
            var4.VolA = var4.VolE;
         }

         if(var4.EnvelopeB != 0) {
            var4.VolB = var4.VolE;
         }

         if(var4.EnvelopeC != 0) {
            var4.VolC = var4.VolE;
         }
         break;
      case 14:
         if((var4.Regs[7] & 64) != 0 && var4.PortAwrite != null) {
            var4.PortAwrite.write(0, var4.Regs[14]);
         }
         break;
      case 15:
         if((var4.Regs[7] & 128) != 0 && var4.PortBwrite != null) {
            var4.PortBwrite.write(0, var4.Regs[15]);
         }
      }

   }

   public ReadHandler ay8910_read_port_0_r() {
      return new AY8910_read_port_r(0);
   }

   public ReadHandler ay8910_read_port_1_r() {
      return new AY8910_read_port_r(1);
   }

   public ReadHandler ay8910_read_port_2_r() {
      return new AY8910_read_port_r(2);
   }

   public ReadHandler ay8910_read_port_3_r() {
      return new AY8910_read_port_r(3);
   }

   public ReadHandler ay8910_read_port_4_r() {
      return new AY8910_read_port_r(4);
   }

   public void build_mixer_table(int var1) {
      AY8910Context var4 = ay[var1];
      double var2 = 10922.0D;

      for(var1 = 31; var1 > 0; --var1) {
         var4.VolTable[var1] = (int)(0.5D + var2);
         var2 /= 1.188502227D;
      }

      var4.VolTable[0] = 0;
   }

   public void init(boolean var1, int var2, int var3) {
      super.init(var1, var2, var3);

      for(var2 = 0; var2 < this.num; ++var2) {
         this.AY8910_init("AY-3-8910", var2, this.baseClock, 50, super.getSampFreq(), this.readA[var2], this.readB[var2], this.writeA[var2], this.writeB[var2]);
         this.build_mixer_table(var2);
      }

   }

   public void writeBuffer() {
      this.clearBuffer();

      for(int var1 = 0; var1 < this.num; ++var1) {
         this.AY8910Update(var1);
      }

   }

   class AY8910Context {
      int Alternate;
      int Attack;
      public int Channel;
      int CountA;
      int CountB;
      int CountC;
      int CountE;
      int CountEnv;
      int CountN;
      int EnvelopeA;
      int EnvelopeB;
      int EnvelopeC;
      int Hold;
      int Holding;
      int OutputA;
      int OutputB;
      int OutputC;
      int OutputN;
      int PeriodA;
      int PeriodB;
      int PeriodC;
      int PeriodE;
      int PeriodN;
      public ReadHandler PortAread;
      public WriteHandler PortAwrite;
      public ReadHandler PortBread;
      public WriteHandler PortBwrite;
      int RNG;
      int[] Regs = new int[16];
      public int SampleRate;
      int UpdateStep;
      int VolA;
      int VolB;
      int VolC;
      int VolE;
      int[] VolTable = new int[32];
      int lastEnable;
      int register_latch;
   }

   class AY8910_control_port_w implements WriteHandler {
      int context;

      public AY8910_control_port_w(int var2) {
         this.context = var2;
      }

      public void write(int var1, int var2) {
         AY8910.this.AY8910Write(this.context, 0, var2);
      }
   }

   class AY8910_read_port_r implements ReadHandler {
      int context;

      public AY8910_read_port_r(int var2) {
         this.context = var2;
      }

      public int read(int var1) {
         return AY8910.this.AY8910Read(this.context);
      }
   }

   class AY8910_write_port_w implements WriteHandler {
      int context;

      public AY8910_write_port_w(int var2) {
         this.context = var2;
      }

      public void write(int var1, int var2) {
         AY8910.this.AY8910Write(this.context, 1, var2);
      }
   }
}
