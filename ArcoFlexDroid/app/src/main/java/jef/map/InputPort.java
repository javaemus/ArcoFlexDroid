package jef.map;

public class InputPort implements ReadHandler {
   public static final int ACTIVE_HIGH = 0;
   public static final int ACTIVE_LOW = 255;
   public static final int ANALOG_X = 3;
   public static final int ANALOG_Y = 4;
   public static final int BUTTON1 = 12;
   public static final int BUTTON1_2 = 140;
   public static final int BUTTON2 = 13;
   public static final int BUTTON2_2 = 141;
   public static final int BUTTON3 = 14;
   public static final int BUTTON3_2 = 142;
   public static final int BUTTON4 = 15;
   public static final int BUTTON4_2 = 143;
   public static final int BUTTON5 = 16;
   public static final int BUTTON5_2 = 144;
   public static final int BUTTON6 = 17;
   public static final int BUTTON6_2 = 145;
   public static final int COIN1 = 1;
   public static final int COIN2 = 2;
   public static final int COIN3 = 2;
   public static final int COIN4 = 3;
   public static final int DIAL = 2;
   public static final int DIAL_2 = 130;
   public static final int IPF_PLAYER3 = 256;
   public static final int IPF_PLAYER4 = 384;
   public static final int JOY_0 = 30;
   public static final int JOY_1 = 21;
   public static final int JOY_2 = 22;
   public static final int JOY_3 = 23;
   public static final int JOY_4 = 24;
   public static final int JOY_5 = 25;
   public static final int JOY_6 = 26;
   public static final int JOY_7 = 27;
   public static final int JOY_8 = 28;
   public static final int JOY_9 = 29;
   public static final int JOY_A = 49;
   public static final int JOY_ALT = 74;
   public static final int JOY_B = 67;
   public static final int JOY_BACKSLASH = 47;
   public static final int JOY_BACKSPACE = 33;
   public static final int JOY_BREAK = 98;
   public static final int JOY_C = 65;
   public static final int JOY_CAPS = 48;
   public static final int JOY_COMMA = 70;
   public static final int JOY_CTRL = 73;
   public static final int JOY_D = 51;
   public static final int JOY_DEL = 90;
   public static final int JOY_DOWN = 11;
   public static final int JOY_DOWN_2 = 139;
   public static final int JOY_E = 37;
   public static final int JOY_END = 91;
   public static final int JOY_ENTER = 60;
   public static final int JOY_EQUALS = 32;
   public static final int JOY_F = 52;
   public static final int JOY_F1 = 78;
   public static final int JOY_F10 = 87;
   public static final int JOY_F11 = 88;
   public static final int JOY_F12 = 89;
   public static final int JOY_F2 = 79;
   public static final int JOY_F3 = 80;
   public static final int JOY_F4 = 81;
   public static final int JOY_F5 = 82;
   public static final int JOY_F6 = 83;
   public static final int JOY_F7 = 84;
   public static final int JOY_F8 = 85;
   public static final int JOY_F9 = 86;
   public static final int JOY_G = 53;
   public static final int JOY_H = 54;
   public static final int JOY_HOME = 93;
   public static final int JOY_I = 42;
   public static final int JOY_INS = 92;
   public static final int JOY_J = 55;
   public static final int JOY_K = 56;
   public static final int JOY_L = 57;
   public static final int JOY_LEFT = 8;
   public static final int JOY_LEFT_2 = 136;
   public static final int JOY_M = 69;
   public static final int JOY_MINUS = 31;
   public static final int JOY_N = 68;
   public static final int JOY_O = 43;
   public static final int JOY_P = 44;
   public static final int JOY_PERIOD = 71;
   public static final int JOY_PGDN = 95;
   public static final int JOY_PGUP = 94;
   public static final int JOY_PRTSC = 96;
   public static final int JOY_Q = 35;
   public static final int JOY_QUOTE = 59;
   public static final int JOY_R = 38;
   public static final int JOY_RALT = 75;
   public static final int JOY_RCTRL = 77;
   public static final int JOY_RIGHT = 9;
   public static final int JOY_RIGHT_2 = 137;
   public static final int JOY_RSHIFT = 62;
   public static final int JOY_S = 50;
   public static final int JOY_SCRLK = 97;
   public static final int JOY_SEMICOLON = 58;
   public static final int JOY_SHIFT = 61;
   public static final int JOY_SLASH = 72;
   public static final int JOY_SPACE = 76;
   public static final int JOY_SQ_BRACKET_C = 46;
   public static final int JOY_SQ_BRACKET_O = 45;
   public static final int JOY_T = 39;
   public static final int JOY_TAB = 34;
   public static final int JOY_TILDE = 20;
   public static final int JOY_U = 41;
   public static final int JOY_UP = 10;
   public static final int JOY_UP_2 = 138;
   public static final int JOY_V = 66;
   public static final int JOY_W = 36;
   public static final int JOY_X = 64;
   public static final int JOY_Y = 40;
   public static final int JOY_Z = 63;
   public static final int PADDLE = 1;
   public static final int PLAYER2 = 128;
   public static final int REVERSE = 256;
   public static final int SERVICE1 = 16;
   public static final int START1 = 4;
   public static final int START2 = 5;
   public static final int START3 = 6;
   public static final int START4 = 7;
   public static final int TILT = 15;
   public static final int UNKNOWN = 0;
   public static final int UNUSED = 0;
   private final int[] activityType = new int[512];
   private boolean analog = false;
   private final int[] bit = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
   private final int[] bitMask = new int[512];
   private int center = 127;
   private final boolean[] impulse = new boolean[8];
   private final int[] impulseActivityType = new int[8];
   private final int[] impulseCounter = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
   private final int[] impulseDelay = new int[8];
   private final int[] impulseInputType = new int[8];
   private boolean reverse = false;
   private int type = 0;
   private int value = 0;

   private int bitSet(int var1) {
      int var2 = 0;

      while(true) {
         if(var2 >= 8) {
            var2 = 0;
            break;
         }

         if(this.bit[var2] == var1) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public void keyPress(int var1) {
      System.out.println("KeyPress " + var1);
      short var2;
      switch(var1) {
      case 8:
         if(!this.analog) {
            if(this.activityType[8] == 0) {
               var1 = this.value | this.bitMask[8] & 255;
            } else {
               var1 = this.value & ~this.bitMask[8];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
            case 3:
               if(this.reverse) {
                  var2 = 255;
               } else {
                  var2 = 0;
               }

               this.value = var2;
            case 2:
            }
         }
         break;
      case 9:
         if(!this.analog) {
            if(this.activityType[9] == 0) {
               var1 = this.value | this.bitMask[9] & 255;
            } else {
               var1 = this.value & ~this.bitMask[9];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
            case 3:
               if(this.reverse) {
                  var2 = 0;
               } else {
                  var2 = 255;
               }

               this.value = var2;
            case 2:
            }
         }
         break;
      case 10:
         if(!this.analog) {
            if(this.activityType[10] == 0) {
               var1 = this.value | this.bitMask[10] & 255;
            } else {
               var1 = this.value & ~this.bitMask[10];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
            case 4:
               if(this.reverse) {
                  var2 = 255;
               } else {
                  var2 = 0;
               }

               this.value = var2;
            case 2:
            case 3:
            }
         }
         break;
      case 11:
         if(!this.analog) {
            if(this.activityType[11] == 0) {
               var1 = this.value | this.bitMask[11] & 255;
            } else {
               var1 = this.value & ~this.bitMask[11];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
            case 4:
               if(this.reverse) {
                  var2 = 0;
               } else {
                  var2 = 255;
               }

               this.value = var2;
            case 2:
            case 3:
            }
         }
         break;
      case 21:
         if(this.activityType[4] == 0) {
            var1 = this.value | this.bitMask[4] & 255;
         } else {
            var1 = this.value & ~this.bitMask[4];
         }

         this.value = var1;
         break;
      case 22:
         if(this.activityType[5] == 0) {
            var1 = this.value | this.bitMask[5] & 255;
         } else {
            var1 = this.value & ~this.bitMask[5];
         }

         this.value = var1;
         break;
      case 23:
         if(this.activityType[6] == 0) {
            var1 = this.value | this.bitMask[6] & 255;
         } else {
            var1 = this.value & ~this.bitMask[6];
         }

         this.value = var1;
         break;
      case 25:
         if(this.activityType[1] == 0) {
            var1 = this.value | this.bitMask[1] & 255;
         } else {
            var1 = this.value & ~this.bitMask[1];
         }

         this.value = var1;
         break;
      case 26:
         if(this.activityType[2] == 0) {
            var1 = this.value | this.bitMask[1] & 255;
         } else {
            var1 = this.value & ~this.bitMask[1];
         }

         this.value = var1;
         break;
      case 35:
         if(this.activityType[142] == 0) {
            var1 = this.value | this.bitMask[142] & 255;
         } else {
            var1 = this.value & ~this.bitMask[142];
         }

         this.value = var1;
         break;
      case 36:
         if(this.activityType[143] == 0) {
            var1 = this.value | this.bitMask[143] & 255;
         } else {
            var1 = this.value & ~this.bitMask[143];
         }

         this.value = var1;
         break;
      case 37:
         if(this.activityType[144] == 0) {
            var1 = this.value | this.bitMask[144] & 255;
         } else {
            var1 = this.value & ~this.bitMask[144];
         }

         this.value = var1;
         break;
      case 38:
         if(this.activityType[138] == 0) {
            var1 = this.value | this.bitMask[138] & 255;
         } else {
            var1 = this.value & ~this.bitMask[138];
         }

         this.value = var1;
         break;
      case 39:
         if(this.activityType[145] == 0) {
            var1 = this.value | this.bitMask[145] & 255;
         } else {
            var1 = this.value & ~this.bitMask[145];
         }

         this.value = var1;
         break;
      case 49:
         if(this.activityType[140] == 0) {
            var1 = this.value | this.bitMask[140] & 255;
         } else {
            var1 = this.value & ~this.bitMask[140];
         }

         this.value = var1;
         break;
      case 50:
         if(this.activityType[141] == 0) {
            var1 = this.value | this.bitMask[141] & 255;
         } else {
            var1 = this.value & ~this.bitMask[141];
         }

         this.value = var1;
         break;
      case 51:
         if(!this.analog) {
            if(this.activityType[136] == 0) {
               var1 = this.value | this.bitMask[136] & 255;
            } else {
               var1 = this.value & ~this.bitMask[136];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
               if(this.reverse) {
                  var2 = 255;
               } else {
                  var2 = 0;
               }

               this.value = var2;
            }
         }
         break;
      case 52:
         if(this.activityType[139] == 0) {
            var1 = this.value | this.bitMask[139] & 255;
         } else {
            var1 = this.value & ~this.bitMask[139];
         }

         this.value = var1;
         break;
      case 53:
         if(!this.analog) {
            if(this.activityType[137] == 0) {
               var1 = this.value | this.bitMask[137] & 255;
            } else {
               var1 = this.value & ~this.bitMask[137];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
               if(this.reverse) {
                  var2 = 0;
               } else {
                  var2 = 255;
               }

               this.value = var2;
            }
         }
         break;
      case 63:
         if(this.activityType[14] == 0) {
            var1 = this.value | this.bitMask[14] & 255;
         } else {
            var1 = this.value & ~this.bitMask[14];
         }

         this.value = var1;
         break;
      case 64:
         if(this.activityType[15] == 0) {
            var1 = this.value | this.bitMask[15] & 255;
         } else {
            var1 = this.value & ~this.bitMask[15];
         }

         this.value = var1;
         break;
      case 65:
         if(this.activityType[16] == 0) {
            var1 = this.value | this.bitMask[16] & 255;
         } else {
            var1 = this.value & ~this.bitMask[16];
         }

         this.value = var1;
         break;
      case 66:
         if(this.activityType[17] == 0) {
            var1 = this.value | this.bitMask[17] & 255;
         } else {
            var1 = this.value & ~this.bitMask[17];
         }

         this.value = var1;
         break;
      case 73:
         if(this.activityType[12] == 0) {
            var1 = this.value | this.bitMask[12] & 255;
         } else {
            var1 = this.value & ~this.bitMask[12];
         }

         this.value = var1;
         break;
      case 76:
         if(this.activityType[13] == 0) {
            var1 = this.value | this.bitMask[13] & 255;
         } else {
            var1 = this.value & ~this.bitMask[13];
         }

         this.value = var1;
      }

   }

   public void keyRelease(int var1) {
      switch(var1) {
      case 8:
         if(!this.analog) {
            if(this.activityType[8] == 255) {
               var1 = this.value | this.bitMask[8] & 255;
            } else {
               var1 = this.value & ~this.bitMask[8];
            }

            this.value = var1;
         } else {
            this.value = this.center;
         }
         break;
      case 9:
         if(!this.analog) {
            if(this.activityType[9] == 255) {
               var1 = this.value | this.bitMask[9] & 255;
            } else {
               var1 = this.value & ~this.bitMask[9];
            }

            this.value = var1;
         } else {
            this.value = this.center;
         }
         break;
      case 10:
         if(!this.analog) {
            if(this.activityType[10] == 255) {
               var1 = this.value | this.bitMask[10] & 255;
            } else {
               var1 = this.value & ~this.bitMask[10];
            }

            this.value = var1;
         } else {
            this.value = this.center;
         }
         break;
      case 11:
         if(!this.analog) {
            if(this.activityType[11] == 255) {
               var1 = this.value | this.bitMask[11] & 255;
            } else {
               var1 = this.value & ~this.bitMask[11];
            }

            this.value = var1;
         } else {
            this.value = this.center;
         }
         break;
      case 21:
         if(this.activityType[4] == 255) {
            var1 = this.value | this.bitMask[4] & 255;
         } else {
            var1 = this.value & ~this.bitMask[4];
         }

         this.value = var1;
         break;
      case 22:
         if(this.activityType[5] == 255) {
            var1 = this.value | this.bitMask[5] & 255;
         } else {
            var1 = this.value & ~this.bitMask[5];
         }

         this.value = var1;
         break;
      case 23:
         if(this.activityType[6] == 255) {
            var1 = this.value | this.bitMask[6] & 255;
         } else {
            var1 = this.value & ~this.bitMask[6];
         }

         this.value = var1;
         break;
      case 25:
         if(this.activityType[1] == 255) {
            var1 = this.value | this.bitMask[1] & 255;
         } else {
            var1 = this.value & ~this.bitMask[1];
         }

         this.value = var1;
         break;
      case 26:
         if(this.activityType[2] == 255) {
            var1 = this.value | this.bitMask[1] & 255;
         } else {
            var1 = this.value & ~this.bitMask[1];
         }

         this.value = var1;
         break;
      case 35:
         if(this.activityType[142] == 255) {
            var1 = this.value | this.bitMask[142] & 255;
         } else {
            var1 = this.value & ~this.bitMask[142];
         }

         this.value = var1;
         break;
      case 36:
         if(this.activityType[143] == 255) {
            var1 = this.value | this.bitMask[143] & 255;
         } else {
            var1 = this.value & ~this.bitMask[143];
         }

         this.value = var1;
         break;
      case 38:
         if(this.activityType[138] == 255) {
            var1 = this.value | this.bitMask[138] & 255;
         } else {
            var1 = this.value & ~this.bitMask[138];
         }

         this.value = var1;
         break;
      case 49:
         if(this.activityType[140] == 255) {
            var1 = this.value | this.bitMask[140] & 255;
         } else {
            var1 = this.value & ~this.bitMask[140];
         }

         this.value = var1;
         break;
      case 50:
         if(this.activityType[141] == 255) {
            var1 = this.value | this.bitMask[141] & 255;
         } else {
            var1 = this.value & ~this.bitMask[141];
         }

         this.value = var1;
         break;
      case 51:
         if(!this.analog) {
            if(this.activityType[136] == 255) {
               var1 = this.value | this.bitMask[136] & 255;
            } else {
               var1 = this.value & ~this.bitMask[136];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
               this.value = this.center;
            }
         }
         break;
      case 52:
         if(this.activityType[139] == 255) {
            var1 = this.value | this.bitMask[139] & 255;
         } else {
            var1 = this.value & ~this.bitMask[139];
         }

         this.value = var1;
         break;
      case 53:
         if(!this.analog) {
            if(this.activityType[137] == 255) {
               var1 = this.value | this.bitMask[137] & 255;
            } else {
               var1 = this.value & ~this.bitMask[137];
            }

            this.value = var1;
         } else {
            switch(this.type) {
            case 1:
               this.value = this.center;
            }
         }
         break;
      case 63:
         if(this.activityType[14] == 255) {
            var1 = this.value | this.bitMask[14] & 255;
         } else {
            var1 = this.value & ~this.bitMask[14];
         }

         this.value = var1;
         break;
      case 64:
         if(this.activityType[15] == 255) {
            var1 = this.value | this.bitMask[15] & 255;
         } else {
            var1 = this.value & ~this.bitMask[15];
         }

         this.value = var1;
         break;
      case 65:
         if(this.activityType[16] == 255) {
            var1 = this.value | this.bitMask[16] & 255;
         } else {
            var1 = this.value & ~this.bitMask[16];
         }

         this.value = var1;
         break;
      case 66:
         if(this.activityType[17] == 255) {
            var1 = this.value | this.bitMask[17] & 255;
         } else {
            var1 = this.value & ~this.bitMask[17];
         }

         this.value = var1;
         break;
      case 73:
         if(this.activityType[12] == 255) {
            var1 = this.value | this.bitMask[12] & 255;
         } else {
            var1 = this.value & ~this.bitMask[12];
         }

         this.value = var1;
         break;
      case 76:
         if(this.activityType[13] == 255) {
            var1 = this.value | this.bitMask[13] & 255;
         } else {
            var1 = this.value & ~this.bitMask[13];
         }

         this.value = var1;
      }

   }

   public int read(int var1) {
      if(!this.analog) {
         var1 = this.value;
      } else {
         var1 = this.value / 2 & 255;
      }

      return var1;
   }

   public void setAnalog(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.analog = true;
      this.type = var3 & 31;
      boolean var8;
      if((var3 & 256) != 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      this.reverse = var8;
      this.value = var2;
      this.center = var2;
   }

   public void setBit(int var1, int var2) {
      this.bitMask[var2] = var1;
      this.activityType[var2] = 255;
      this.value |= var1 & 255;
   }

   public void setBit(int var1, int var2, int var3) {
      this.bitMask[var3] = var1;
      this.activityType[var3] = var2;
      if(var2 == 255) {
         var1 = this.value | var1 & 255;
      } else {
         var1 = this.value & ~var1;
      }

      this.value = var1;
   }

   public void setBitImpulse(int var1, int var2, int var3) {
      this.bitMask[var2] = var1;
      this.activityType[var2] = 255;
      this.impulseActivityType[this.bitSet(var1)] = 255;
      this.impulse[this.bitSet(var1)] = true;
      this.impulseInputType[this.bitSet(var1)] = var2;
      this.impulseDelay[this.bitSet(var1)] = var3;
      if(255 == 255) {
         var1 = this.value | var1 & 255;
      } else {
         var1 = this.value & ~var1;
      }

      this.value = var1;
   }

   public void setBitImpulse(int var1, int var2, int var3, int var4) {
      this.bitMask[var3] = var1;
      this.activityType[var3] = var2;
      this.impulseActivityType[this.bitSet(var1)] = var2;
      this.impulse[this.bitSet(var1)] = true;
      this.impulseInputType[this.bitSet(var1)] = var3;
      this.impulseDelay[this.bitSet(var1)] = var4;
      if(var2 == 255) {
         var1 = this.value | var1 & 255;
      } else {
         var1 = this.value & ~var1;
      }

      this.value = var1;
   }

   public void setBits(int var1, int var2) {
      this.value = this.value & ~var1 | var2;
   }

   public void setDipName(int var1, int var2, String var3) {
      this.value = this.value & ~var1 | var2;
   }

   public void setDipSetting(int var1, String var2) {
   }

   public void setService(int var1) {
      if(255 == 255) {
         var1 = this.value | var1 & 255;
      } else {
         var1 = this.value & ~var1;
      }

      this.value = var1;
   }

   public void setService(int var1, int var2) {
      if(var2 == 255) {
         var1 = this.value | var1 & 255;
      } else {
         var1 = this.value & ~var1;
      }

      this.value = var1;
   }

   public void update() {
      for(int var1 = 0; var1 < 8; ++var1) {
         if(this.impulse[var1]) {
            if(this.impulseCounter[var1] > 0) {
               int[] var3 = this.impulseCounter;
               --var3[var1];
            } else {
               int var2 = 1 << var1;
               if(this.impulseActivityType[var1] == 255) {
                  var2 = this.value & ~var2;
               } else {
                  var2 = this.value | var2 & 255;
               }

               this.value = var2;
            }
         }
      }

   }

   public void write(int var1) {
      this.value = var1;
   }
}
