package jef.cpu;

import jef.cpuboard.CpuBoard;

public class M6809 implements M6809Const, Cpu {
   public int a = 0;
   public int addrMode;
   public int b = 0;
   public int breakpoint = '\uffff';
   private CpuBoard cb;
   private int curInstr = 0;
   public int dp = 0;
   public int ea = 0;
   public boolean flagC;
   public boolean flagE;
   public boolean flagF;
   public boolean flagH;
   public boolean flagI;
   public boolean flagN;
   public boolean flagV;
   public boolean flagZ;
   public int interruptState;
   public int ir = 0;
   public int lastPc;
   public int opPc;
   public int page;
   public int pc = 0;
   public int s = 0;
   private int tCycle;
   private String tag;
   int traceinstructions = 0;
   public int u = 0;
   private boolean useEa = false;
   public int waitState = 0;
   public int x = 0;
   public int y = 0;

   private void abx() {
      this.x = this.x + this.b & '\uffff';
   }

   private void adca() {
      this.a = this.adc8BitBase(this.a);
   }

   private void adcb() {
      this.b = this.adc8BitBase(this.b);
   }

   private void adda() {
      this.a = this.add8BitBase(this.a);
   }

   private void addb() {
      this.b = this.add8BitBase(this.b);
   }

   private void addd() {
      int var1;
      if(this.useEa) {
         var1 = this.read2Bytes(this.ea);
      } else {
         var1 = this.fetch2();
      }

      int var2 = this.d();
      boolean var3;
      if(this.getBit((var2 & 32767) + (var1 & 32767), 15) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagV = var3;
      var1 = (var2 & '\uffff') + (var1 & '\uffff');
      if(this.getBit(var1, 16) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagC = var3;
      this.flagV ^= this.flagC;
      var1 &= '\uffff';
      this.flagN = this.isNeg16Bit(var1);
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
      this.setD(var1);
   }

   private void anda() {
      this.a = this.and8BitBase(this.a);
   }

   private void andb() {
      this.b = this.and8BitBase(this.b);
   }

   private void andcc() {
      this.setCC(this.cc() & this.fetch());
   }

   private void asl() {
      int var1;
      if(!this.useEa) {
         var1 = this.readByte(this.pc);
         int var2 = this.pc;
         this.pc = var2 + 1;
         this.writeByte(var2, this.asl8BitBase(var1));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.asl8BitBase(var1));
      }

   }

   private void asla() {
      this.a = this.asl8BitBase(this.a);
   }

   private void aslb() {
      this.b = this.asl8BitBase(this.b);
   }

   private void asr() {
      int var1;
      if(!this.useEa) {
         int var2 = this.readByte(this.pc);
         var1 = this.pc;
         this.pc = var1 + 1;
         this.writeByte(var1, this.asr8BitBase(var2));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.asr8BitBase(var1));
      }

   }

   private void asra() {
      this.a = this.asr8BitBase(this.a);
   }

   private void asrb() {
      this.b = this.asr8BitBase(this.b);
   }

   private void bita() {
      this.and8BitBase(this.a);
   }

   private void bitb() {
      this.and8BitBase(this.b);
   }

   private void bra() {
      this.branchBase();
   }

   private void bsr() {
      int var1 = this.fetch();
      this.pull_push8BitBase(false, true, 128);
      this.tCycle += 2;
      this.pc += this.to2C8Bit(var1);
   }

   private final int calcEA() {
      this.useEa = true;
      switch(this.addrMode) {
      case 1:
         this.ea = this.dp << 8 | this.fetch();
         break;
      case 2:
         int var2 = this.fetch();
         int var4 = (var2 & 96) >>> 5;
         int var1;
         if(var4 == 0) {
            var1 = this.x;
         } else if(var4 == 1) {
            var1 = this.y;
         } else if(var4 == 2) {
            var1 = this.u;
         } else {
            var1 = this.s;
         }

         if(this.getBit(var2, 7) == 0) {
            this.ea = this.to2C5Bit(var2 & 31) + var1;
         } else {
            int var5 = var2 & 15;
            boolean var3;
            if((var2 & 16) != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var2 = var1;
            switch(var5) {
            case 0:
               this.ea = var1;
               this.tCycle -= 2;
               var2 = var1 + 1;
               break;
            case 1:
               this.ea = var1;
               var2 = var1 + 2;
               this.tCycle -= 3;
               break;
            case 2:
               var2 = var1 - 1;
               this.ea = var2;
               this.tCycle -= 2;
               break;
            case 3:
               var2 = var1 - 2;
               this.ea = var2;
               this.tCycle -= 3;
               break;
            case 4:
               this.ea = var1;
               var2 = var1;
               break;
            case 5:
               this.ea = this.to2C8Bit(this.b) + var1;
               --this.tCycle;
               var2 = var1;
               break;
            case 6:
               this.ea = this.to2C8Bit(this.a) + var1;
               --this.tCycle;
               var2 = var1;
            case 7:
            case 10:
            case 14:
               break;
            case 8:
               this.ea = this.to2C8Bit(this.fetch()) + var1;
               --this.tCycle;
               var2 = var1;
               break;
            case 9:
               this.ea = this.to2C16Bit(this.fetch2()) + var1;
               this.tCycle -= 4;
               var2 = var1;
               break;
            case 11:
               this.ea = this.to2C16Bit(this.d()) + var1;
               this.tCycle -= 4;
               var2 = var1;
               break;
            case 12:
               this.ea = this.to2C8Bit(this.fetch()) + this.pc;
               --this.tCycle;
               var2 = var1;
               break;
            case 13:
               this.ea = this.to2C16Bit(this.fetch2()) + this.pc;
               this.tCycle -= 5;
               var2 = var1;
               break;
            case 15:
               this.ea = this.fetch2();
               this.tCycle -= 5;
               var2 = var1;
               break;
            default:
               var2 = var1;
            }

            if(var5 <= 3) {
               if(var4 == 0) {
                  this.x = var2;
               } else if(var4 == 1) {
                  this.y = var2;
               } else if(var4 == 2) {
                  this.u = var2;
               } else {
                  this.s = var2;
               }
            }

            if(var3) {
               this.ea = this.read2Bytes(this.ea);
               this.tCycle -= 3;
            }
         }
         break;
      case 3:
         this.ea = this.fetch2();
      }

      return 0;
   }

   private void clr() {
      if(!this.useEa) {
         int var1 = this.pc;
         this.pc = var1 + 1;
         this.writeByte(var1, this.clr8BitBase());
      } else {
         this.writeByte(this.ea, this.clr8BitBase());
      }

   }

   private void clra() {
      this.a = this.clr8BitBase();
   }

   private void clrb() {
      this.b = this.clr8BitBase();
   }

   private void cmpa() {
      this.cmp8BitBase(this.a);
   }

   private void cmpb() {
      this.cmp8BitBase(this.b);
   }

   private void cmpx_y_s() {
      if(this.page == 1) {
         this.cmp16BitBase(this.x);
      } else if(this.page == 2) {
         this.cmp16BitBase(this.y);
      } else {
         this.cmp16BitBase(this.s);
      }

   }

   private void com() {
      int var1;
      if(!this.useEa) {
         var1 = this.readByte(this.pc);
         int var2 = this.pc;
         this.pc = var2 + 1;
         this.writeByte(var2, this.com8BitBase(var1));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.com8BitBase(var1));
      }

   }

   private void coma() {
      this.a = this.com8BitBase(this.a);
   }

   private void comb() {
      this.b = this.com8BitBase(this.b);
   }

   private void cwai() {
      this.setCC(this.cc() & this.fetch() | E_MASK);
      this.pull_push8BitBase(false, true, 255);
      this.waitState = 8;
      this.interruptState |= 8;
      this.tCycle = 0;
   }

   private final int d() {
      return this.a << 8 | this.b;
   }

   private void daa() {
      int var2 = this.a & 15;
      int var3 = this.a & 240;
      int var1 = 0;
      if(var2 > 9 || this.flagH) {
         var1 = 0 | 6;
      }

      label43: {
         if((var3 <= 128 || var2 <= 9) && !this.flagC) {
            var2 = var1;
            if(var3 <= 144) {
               break label43;
            }
         }

         var2 = var1 | 96;
      }

      var1 = var2 + this.a;
      boolean var4;
      if((var1 & 256) == 0 && !this.flagC) {
         var4 = false;
      } else {
         var4 = true;
      }

      this.flagC = var4;
      this.a = var1 & 255;
      if(this.a == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagZ = var4;
      this.flagN = this.isNeg8Bit(this.a);
   }

   private void dec() {
      int var1;
      if(!this.useEa) {
         int var2 = this.readByte(this.pc);
         var1 = this.pc;
         this.pc = var1 + 1;
         this.writeByte(var1, this.dec8BitBase(var2));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.dec8BitBase(var1));
      }

   }

   private void deca() {
      this.a = this.dec8BitBase(this.a);
   }

   private void decb() {
      this.b = this.dec8BitBase(this.b);
   }

   private void eora() {
      this.a = this.eor8BitBase(this.a);
   }

   private void eorb() {
      this.b = this.eor8BitBase(this.b);
   }

   private void execSingleInstr(int var1) {
      switch(var1) {
      case 0:
         this.neg();
      case 1:
      case 2:
      case 5:
      case 11:
      case 16:
      case 17:
      case 20:
      case 21:
      case 24:
      case 27:
      case 56:
      case 62:
      case 65:
      case 66:
      case 69:
      case 75:
      case 78:
      case 81:
      case 82:
      case 85:
      case 91:
      case 94:
      case 97:
      case 98:
      case 101:
      case 107:
      case 113:
      case 114:
      case 117:
      case 123:
      case 135:
      case 143:
      case 199:
      case 205:
      case 207:
      default:
         break;
      case 3:
         this.com();
         break;
      case 4:
         this.lsr();
         break;
      case 6:
         this.ror();
         break;
      case 7:
         this.asr();
         break;
      case 8:
         this.asl();
         break;
      case 9:
         this.rol();
         break;
      case 10:
         this.dec();
         break;
      case 12:
         this.inc();
         break;
      case 13:
         this.tst();
         break;
      case 14:
         this.jmp();
         break;
      case 15:
         this.clr();
         break;
      case 18:
         this.nop();
         break;
      case 19:
         this.sync();
         break;
      case 22:
         this.lbra();
         break;
      case 23:
         this.lbsr();
         break;
      case 25:
         this.daa();
         break;
      case 26:
         this.orcc();
         break;
      case 28:
         this.andcc();
         break;
      case 29:
         this.sex();
         break;
      case 30:
         this.exg();
         break;
      case 31:
         this.tfr();
         break;
      case 32:
         this.bra();
         break;
      case 33:
         this.xbrn();
         break;
      case 34:
         this.xbhi();
         break;
      case 35:
         this.xbls();
         break;
      case 36:
         this.xbcc();
         break;
      case 37:
         this.xbcs();
         break;
      case 38:
         this.xbne();
         break;
      case 39:
         this.xbeq();
         break;
      case 40:
         this.xbvc();
         break;
      case 41:
         this.xbvs();
         break;
      case 42:
         this.xbpl();
         break;
      case 43:
         this.xbmi();
         break;
      case 44:
         this.xbge();
         break;
      case 45:
         this.xblt();
         break;
      case 46:
         this.xbgt();
         break;
      case 47:
         this.xble();
         break;
      case 48:
         this.leax();
         break;
      case 49:
         this.leay();
         break;
      case 50:
         this.leas();
         break;
      case 51:
         this.leau();
         break;
      case 52:
         this.pshs();
         break;
      case 53:
         this.puls();
         break;
      case 54:
         this.pshu();
         break;
      case 55:
         this.pulu();
         break;
      case 57:
         this.rts();
         break;
      case 58:
         this.abx();
         break;
      case 59:
         this.rti();
         break;
      case 60:
         this.cwai();
         break;
      case 61:
         this.mul();
         break;
      case 63:
         this.swi();
         break;
      case 64:
         this.nega();
         break;
      case 67:
         this.coma();
         break;
      case 68:
         this.lsra();
         break;
      case 70:
         this.rora();
         break;
      case 71:
         this.asra();
         break;
      case 72:
         this.asla();
         break;
      case 73:
         this.rola();
         break;
      case 74:
         this.deca();
         break;
      case 76:
         this.inca();
         break;
      case 77:
         this.tsta();
         break;
      case 79:
         this.clra();
         break;
      case 80:
         this.negb();
         break;
      case 83:
         this.comb();
         break;
      case 84:
         this.lsrb();
         break;
      case 86:
         this.rorb();
         break;
      case 87:
         this.asrb();
         break;
      case 88:
         this.aslb();
         break;
      case 89:
         this.rolb();
         break;
      case 90:
         this.decb();
         break;
      case 92:
         this.incb();
         break;
      case 93:
         this.tstb();
         break;
      case 95:
         this.clrb();
         break;
      case 96:
         this.neg();
         break;
      case 99:
         this.com();
         break;
      case 100:
         this.lsr();
         break;
      case 102:
         this.ror();
         break;
      case 103:
         this.asr();
         break;
      case 104:
         this.asl();
         break;
      case 105:
         this.rol();
         break;
      case 106:
         this.dec();
         break;
      case 108:
         this.inc();
         break;
      case 109:
         this.tst();
         break;
      case 110:
         this.jmp();
         break;
      case 111:
         this.clr();
         break;
      case 112:
         this.neg();
         break;
      case 115:
         this.com();
         break;
      case 116:
         this.lsr();
         break;
      case 118:
         this.ror();
         break;
      case 119:
         this.asr();
         break;
      case 120:
         this.asl();
         break;
      case 121:
         this.rol();
         break;
      case 122:
         this.dec();
         break;
      case 124:
         this.inc();
         break;
      case 125:
         this.tst();
         break;
      case 126:
         this.jmp();
         break;
      case 127:
         this.clr();
         break;
      case 128:
         this.suba();
         break;
      case 129:
         this.cmpa();
         break;
      case 130:
         this.sbca();
         break;
      case 131:
         this.subd_cmpd_cmpu();
         break;
      case 132:
         this.anda();
         break;
      case 133:
         this.bita();
         break;
      case 134:
         this.lda();
         break;
      case 136:
         this.eora();
         break;
      case 137:
         this.adca();
         break;
      case 138:
         this.ora();
         break;
      case 139:
         this.adda();
         break;
      case 140:
         this.cmpx_y_s();
         break;
      case 141:
         this.bsr();
         break;
      case 142:
         this.ldx_y();
         break;
      case 144:
         this.suba();
         break;
      case 145:
         this.cmpa();
         break;
      case 146:
         this.sbca();
         break;
      case 147:
         this.subd_cmpd_cmpu();
         break;
      case 148:
         this.anda();
         break;
      case 149:
         this.bita();
         break;
      case 150:
         this.lda();
         break;
      case 151:
         this.sta();
         break;
      case 152:
         this.eora();
         break;
      case 153:
         this.adca();
         break;
      case 154:
         this.ora();
         break;
      case 155:
         this.adda();
         break;
      case 156:
         this.cmpx_y_s();
         break;
      case 157:
         this.jsr();
         break;
      case 158:
         this.ldx_y();
         break;
      case 159:
         this.stx_y();
         break;
      case 160:
         this.suba();
         break;
      case 161:
         this.cmpa();
         break;
      case 162:
         this.sbca();
         break;
      case 163:
         this.subd_cmpd_cmpu();
         break;
      case 164:
         this.anda();
         break;
      case 165:
         this.bita();
         break;
      case 166:
         this.lda();
         break;
      case 167:
         this.sta();
         break;
      case 168:
         this.eora();
         break;
      case 169:
         this.adca();
         break;
      case 170:
         this.ora();
         break;
      case 171:
         this.adda();
         break;
      case 172:
         this.cmpx_y_s();
         break;
      case 173:
         this.jsr();
         break;
      case 174:
         this.ldx_y();
         break;
      case 175:
         this.stx_y();
         break;
      case 176:
         this.suba();
         break;
      case 177:
         this.cmpa();
         break;
      case 178:
         this.sbca();
         break;
      case 179:
         this.subd_cmpd_cmpu();
         break;
      case 180:
         this.anda();
         break;
      case 181:
         this.bita();
         break;
      case 182:
         this.lda();
         break;
      case 183:
         this.sta();
         break;
      case 184:
         this.eora();
         break;
      case 185:
         this.adca();
         break;
      case 186:
         this.ora();
         break;
      case 187:
         this.adda();
         break;
      case 188:
         this.cmpx_y_s();
         break;
      case 189:
         this.jsr();
         break;
      case 190:
         this.ldx_y();
         break;
      case 191:
         this.stx_y();
         break;
      case 192:
         this.subb();
         break;
      case 193:
         this.cmpb();
         break;
      case 194:
         this.sbcb();
         break;
      case 195:
         this.addd();
         break;
      case 196:
         this.andb();
         break;
      case 197:
         this.bitb();
         break;
      case 198:
         this.ldb();
         break;
      case 200:
         this.eorb();
         break;
      case 201:
         this.adcb();
         break;
      case 202:
         this.orb();
         break;
      case 203:
         this.addb();
         break;
      case 204:
         this.ldd();
         break;
      case 206:
         this.ldu_s();
         break;
      case 208:
         this.subb();
         break;
      case 209:
         this.cmpb();
         break;
      case 210:
         this.sbcb();
         break;
      case 211:
         this.addd();
         break;
      case 212:
         this.andb();
         break;
      case 213:
         this.bitb();
         break;
      case 214:
         this.ldb();
         break;
      case 215:
         this.stb();
         break;
      case 216:
         this.eorb();
         break;
      case 217:
         this.adcb();
         break;
      case 218:
         this.orb();
         break;
      case 219:
         this.addb();
         break;
      case 220:
         this.ldd();
         break;
      case 221:
         this.std();
         break;
      case 222:
         this.ldu_s();
         break;
      case 223:
         this.stu_s();
         break;
      case 224:
         this.subb();
         break;
      case 225:
         this.cmpb();
         break;
      case 226:
         this.sbcb();
         break;
      case 227:
         this.addd();
         break;
      case 228:
         this.andb();
         break;
      case 229:
         this.bitb();
         break;
      case 230:
         this.ldb();
         break;
      case 231:
         this.stb();
         break;
      case 232:
         this.eorb();
         break;
      case 233:
         this.adcb();
         break;
      case 234:
         this.orb();
         break;
      case 235:
         this.addb();
         break;
      case 236:
         this.ldd();
         break;
      case 237:
         this.std();
         break;
      case 238:
         this.ldu_s();
         break;
      case 239:
         this.stu_s();
         break;
      case 240:
         this.subb();
         break;
      case 241:
         this.cmpb();
         break;
      case 242:
         this.sbcb();
         break;
      case 243:
         this.addd();
         break;
      case 244:
         this.andb();
         break;
      case 245:
         this.bitb();
         break;
      case 246:
         this.ldb();
         break;
      case 247:
         this.stb();
         break;
      case 248:
         this.eorb();
         break;
      case 249:
         this.adcb();
         break;
      case 250:
         this.orb();
         break;
      case 251:
         this.addb();
         break;
      case 252:
         this.ldd();
         break;
      case 253:
         this.std();
         break;
      case 254:
         this.ldu_s();
         break;
      case 255:
         this.stu_s();
      }

   }

   private void exg() {
      int var1 = this.fetch();
      if(this.getBit(var1, 7) == 0) {
         this.exg_tfr16BitBase(true, var1);
      } else {
         this.exg_tfr8BitBase(true, var1);
      }

   }

   private final int fetch() {
      CpuBoard var2 = this.cb;
      int var1 = this.pc;
      this.pc = var1 + 1;
      return var2.read8(var1);
   }

   private final int fetch2() {
      CpuBoard var3 = this.cb;
      int var1 = this.pc;
      this.pc = var1 + 1;
      var1 = var3.read8(var1);
      var3 = this.cb;
      int var2 = this.pc;
      this.pc = var2 + 1;
      return var1 << 8 | var3.read8(var2);
   }

   private int getBit(int var1, int var2) {
      byte var3;
      if((BIT_MASK[var2] & var1) == 0) {
         var3 = 0;
      } else {
         var3 = 1;
      }

      return var3;
   }

   private void inc() {
      int var1;
      if(!this.useEa) {
         int var2 = this.readByte(this.pc);
         var1 = this.pc;
         this.pc = var1 + 1;
         this.writeByte(var1, this.inc8BitBase(var2));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.inc8BitBase(var1));
      }

   }

   private void inca() {
      this.a = this.inc8BitBase(this.a);
   }

   private void incb() {
      this.b = this.inc8BitBase(this.b);
   }

   private boolean isNeg16Bit(int var1) {
      boolean var2;
      if(('耀' & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean isNeg8Bit(int var1) {
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void jmp() {
      this.pc = this.ea;
   }

   private void jsr() {
      this.pull_push8BitBase(false, true, 128);
      this.tCycle += 2;
      this.pc = this.ea;
   }

   private void lbra() {
      this.page = 2;
      this.branchBase();
      ++this.tCycle;
   }

   private void lbsr() {
      this.page = 2;
      int var1 = this.fetch2();
      this.pull_push8BitBase(false, true, 128);
      this.tCycle += 2;
      this.pc += this.to2C16Bit(var1);
   }

   private void lda() {
      this.a = this.ld8BitBase();
   }

   private void ldb() {
      this.b = this.ld8BitBase();
   }

   private void ldd() {
      this.setD(this.ld16BitBase());
   }

   private void ldu_s() {
      if(this.page == 1) {
         this.u = this.ld16BitBase();
      } else {
         this.s = this.ld16BitBase();
      }

   }

   private void ldx_y() {
      if(this.page == 1) {
         this.x = this.ld16BitBase();
      } else {
         this.y = this.ld16BitBase();
      }

   }

   private void leas() {
      this.s = this.lea16BitBase(this.s);
   }

   private void leau() {
      this.u = this.lea16BitBase(this.u);
   }

   private void leax() {
      this.x = this.lea16BitBase(this.x);
      boolean var1;
      if(this.x == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.flagZ = var1;
   }

   private void leay() {
      this.y = this.lea16BitBase(this.y);
      boolean var1;
      if(this.y == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.flagZ = var1;
   }

   private void lsr() {
      int var1;
      if(!this.useEa) {
         var1 = this.readByte(this.pc);
         int var2 = this.pc;
         this.pc = var2 + 1;
         this.writeByte(var2, this.lsr8BitBase(var1));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.lsr8BitBase(var1));
      }

   }

   private void lsra() {
      this.a = this.lsr8BitBase(this.a);
   }

   private void lsrb() {
      this.b = this.lsr8BitBase(this.b);
   }

   private void mul() {
      boolean var1;
      if(this.a != 0 && this.b != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      this.flagZ = var1;
      if(((this.a & 255) * (this.b & 255) & 128) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.flagC = var1;
      this.setD((this.a & 255) * (this.b & 255) & '\uffff');
   }

   private void neg() {
      int var1;
      if(!this.useEa) {
         var1 = this.readByte(this.pc);
         int var2 = this.pc;
         this.pc = var2 + 1;
         this.writeByte(var2, this.neg8BitBase(var1));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.neg8BitBase(var1));
      }

   }

   private void nega() {
      this.a = this.neg8BitBase(this.a);
   }

   private void negb() {
      this.b = this.neg8BitBase(this.b);
   }

   private void nop() {
   }

   private void ora() {
      this.a = this.or8BitBase(this.a);
   }

   private void orb() {
      this.b = this.or8BitBase(this.b);
   }

   private void orcc() {
      this.setCC(this.cc() | this.fetch());
   }

   private void pshs() {
      this.pull_push8BitBase(false, true, this.fetch());
   }

   private void pshu() {
      this.pull_push8BitBase(false, false, this.fetch());
   }

   private void puls() {
      this.pull_push8BitBase(true, true, this.fetch());
   }

   private void pulu() {
      this.pull_push8BitBase(true, false, this.fetch());
   }

   private void rol() {
      int var1;
      if(!this.useEa) {
         int var2 = this.readByte(this.pc);
         var1 = this.pc;
         this.pc = var1 + 1;
         this.writeByte(var1, this.rol8BitBase(var2));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.rol8BitBase(var1));
      }

   }

   private void rola() {
      this.a = this.rol8BitBase(this.a);
   }

   private void rolb() {
      this.b = this.rol8BitBase(this.b);
   }

   private void ror() {
      int var1;
      if(!this.useEa) {
         var1 = this.readByte(this.pc);
         int var2 = this.pc;
         this.pc = var2 + 1;
         this.writeByte(var2, this.ror8BitBase(var1));
      } else {
         var1 = this.readByte(this.ea);
         this.writeByte(this.ea, this.ror8BitBase(var1));
      }

   }

   private void rora() {
      this.a = this.ror8BitBase(this.a);
   }

   private void rorb() {
      this.b = this.ror8BitBase(this.b);
   }

   private void rti() {
      this.pull_push8BitBase(true, true, 1);
      ++this.tCycle;
      if(this.flagE) {
         this.pull_push8BitBase(true, true, 254);
         this.tCycle -= 4;
      } else {
         this.pull_push8BitBase(true, true, 128);
         this.tCycle += 3;
      }

   }

   private void rts() {
      this.pull_push8BitBase(true, true, 128);
      this.tCycle += 2;
   }

   private void sbca() {
      this.a = this.sbc8BitBase(this.a);
   }

   private void sbcb() {
      this.b = this.sbc8BitBase(this.b);
   }

   private final void setCC(int var1) {
      boolean var2;
      if((C_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagC = var2;
      if((V_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagV = var2;
      if((Z_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      if((N_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagN = var2;
      if((I_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagI = var2;
      if((H_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagH = var2;
      if((F_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagF = var2;
      if((E_MASK & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagE = var2;
   }

   private final void setD(int var1) {
      this.a = var1 >>> 8;
      this.b = var1 & 255;
   }

   private void sex() {
      if(this.isNeg8Bit(this.b)) {
         this.a = 255;
      } else {
         this.a = 0;
      }

      this.flagV = false;
      this.flagN = this.isNeg8Bit(this.b);
      boolean var1;
      if(this.b == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.flagZ = var1;
   }

   private void sta() {
      this.st8BitBase(this.a);
   }

   private void stb() {
      this.st8BitBase(this.b);
   }

   private void std() {
      this.st16BitBase(this.d());
   }

   private void stu_s() {
      if(this.page == 1) {
         this.st16BitBase(this.u);
      } else {
         this.st16BitBase(this.s);
      }

   }

   private void stx_y() {
      if(this.page == 1) {
         this.st16BitBase(this.x);
      } else {
         this.st16BitBase(this.y);
      }

   }

   private void suba() {
      this.a = this.sub8BitBase(this.a);
   }

   private void subb() {
      this.b = this.sub8BitBase(this.b);
   }

   private void subd_cmpd_cmpu() {
      if(this.page == 1) {
         int var2 = this.d();
         int var1;
         if(!this.useEa) {
            var1 = this.fetch2();
         } else {
            var1 = this.read2Bytes(this.ea);
         }

         int var3 = var2 - var1;
         boolean var4;
         if(((var2 ^ var1 ^ var3 ^ var3 >> 1) & '耀') != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.flagV = var4;
         if((65536 & var3) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.flagC = var4;
         var1 = var3 & '\uffff';
         this.flagN = this.isNeg16Bit(var1);
         if(var1 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.flagZ = var4;
         this.setD(var1);
      } else if(this.page == 2) {
         this.cmp16BitBase(this.d());
      } else {
         this.cmp16BitBase(this.u);
      }

   }

   private void swi() {
      this.flagE = true;
      this.pull_push8BitBase(false, true, 255);
      this.tCycle += 12;
      this.flagI = true;
      this.flagF = true;
      this.pc = this.readByte('\ufffa') << 8 | this.readByte('\ufffb');
   }

   private void sync() {
      this.waitState = 16;
      this.tCycle = 0;
   }

   private void tfr() {
      int var1 = this.fetch();
      if(this.getBit(var1, 7) == 0) {
         this.exg_tfr16BitBase(false, var1);
      } else {
         this.exg_tfr8BitBase(false, var1);
      }

   }

   private int to2C16Bit(int var1) {
      if(var1 > 32767) {
         var1 |= -65536;
      }

      return var1;
   }

   private int to2C5Bit(int var1) {
      if(var1 > 15) {
         var1 |= -32;
      }

      return var1;
   }

   private int to2C8Bit(int var1) {
      if(var1 > 127) {
         var1 |= -256;
      }

      return var1;
   }

   private void tst() {
      if(!this.useEa) {
         this.tst8BitBase(this.fetch());
      } else {
         this.tst8BitBase(this.readByte(this.ea));
      }

   }

   private void tsta() {
      this.tst8BitBase(this.a);
   }

   private void tstb() {
      this.tst8BitBase(this.b);
   }

   private void xbcc() {
      if(!this.flagC) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbcs() {
      if(this.flagC) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbeq() {
      if(this.flagZ) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbge() {
      if(this.flagN ^ this.flagV) {
         if(this.page == 1) {
            ++this.pc;
         } else {
            this.pc += 2;
         }
      } else {
         this.branchBase();
      }

   }

   private void xbgt() {
      if(!this.flagZ && !(this.flagN ^ this.flagV)) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbhi() {
      if(this.flagC | this.flagZ) {
         if(this.page == 1) {
            ++this.pc;
         } else {
            this.pc += 2;
         }
      } else {
         this.branchBase();
      }

   }

   private void xble() {
      if(!this.flagZ && !(this.flagN ^ this.flagV)) {
         if(this.page == 1) {
            ++this.pc;
         } else {
            this.pc += 2;
         }
      } else {
         this.branchBase();
      }

   }

   private void xbls() {
      if(!this.flagC && !this.flagZ) {
         if(this.page == 1) {
            ++this.pc;
         } else {
            this.pc += 2;
         }
      } else {
         this.branchBase();
      }

   }

   private void xblt() {
      if(this.flagN ^ this.flagV) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbmi() {
      if(this.flagN) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbne() {
      if(!this.flagZ) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbpl() {
      if(!this.flagN) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbrn() {
      if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbvc() {
      if(!this.flagV) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   private void xbvs() {
      if(this.flagV) {
         this.branchBase();
      } else if(this.page == 1) {
         ++this.pc;
      } else {
         this.pc += 2;
      }

   }

   public int adc8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      byte var3;
      if(this.flagC) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      boolean var4;
      if(((var1 & 15) + (var2 & 15) + var3 & 16) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagH = var4;
      if(((var1 & 127) + (var2 & 127) + var3 & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagV = var4;
      var1 = (var1 & 255) + (var2 & 255) + var3;
      if((var1 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagC = var4;
      var1 &= 255;
      this.flagV ^= this.flagC;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagZ = var4;
      return var1;
   }

   public int add8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      boolean var3;
      if(((var1 & 15) + (var2 & 15) & 16) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagH = var3;
      if(((var1 & 127) + (var2 & 127) & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagV = var3;
      var1 = (var1 & 255) + (var2 & 255);
      if((var1 & 256) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagC = var3;
      var1 &= 255;
      this.flagV ^= this.flagC;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
      return var1;
   }

   public int and8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      var1 &= var2;
      this.flagV = false;
      this.flagN = this.isNeg8Bit(var1);
      boolean var3;
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
      return var1;
   }

   public int asl8BitBase(int var1) {
      boolean var2;
      if(this.getBit(var1, 7) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagC = var2;
      if(this.getBit(var1, 7) != this.getBit(var1, 6)) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagV = var2;
      var1 = var1 << 1 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int asr8BitBase(int var1) {
      boolean var3;
      if(this.getBit(var1, 0) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagC = var3;
      int var2 = var1 >> 1;
      var1 = var2;
      if(this.getBit(var2, 6) != 0) {
         var1 = var2 | 128;
      }

      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
      return var1;
   }

   public void branchBase() {
      int var1;
      if(this.page == 1) {
         var1 = this.to2C8Bit(this.fetch());
         this.pc += var1;
      } else {
         var1 = this.to2C16Bit(this.fetch2());
         this.pc += var1;
         --this.tCycle;
      }

   }

   final int cc() {
      int var2 = 0;
      if(this.flagC) {
         var2 = 0 | C_MASK;
      }

      int var1 = var2;
      if(this.flagV) {
         var1 = var2 | V_MASK;
      }

      var2 = var1;
      if(this.flagZ) {
         var2 = var1 | Z_MASK;
      }

      int var3 = var2;
      if(this.flagN) {
         var3 = var2 | N_MASK;
      }

      var1 = var3;
      if(this.flagI) {
         var1 = var3 | I_MASK;
      }

      var2 = var1;
      if(this.flagH) {
         var2 = var1 | H_MASK;
      }

      var1 = var2;
      if(this.flagF) {
         var1 = var2 | F_MASK;
      }

      var2 = var1;
      if(this.flagE) {
         var2 = var1 | E_MASK;
      }

      return var2;
   }

   public int clr8BitBase() {
      this.flagN = false;
      this.flagZ = true;
      this.flagV = false;
      this.flagC = false;
      return 0;
   }

   public void cmp16BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch2();
      } else {
         var2 = this.read2Bytes(this.ea);
      }

      int var3 = var1 - var2;
      boolean var4;
      if(((var1 ^ var2 ^ var3 ^ var3 >> 1) & '耀') != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagV = var4;
      if((65536 & var3) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagC = var4;
      var1 = var3 & '\uffff';
      this.flagN = this.isNeg16Bit(var1);
      if(var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagZ = var4;
   }

   public void cmp8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      int var3 = var1 - var2;
      boolean var4;
      if(((var1 ^ var2 ^ var3 ^ var3 >> 1) & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagV = var4;
      if((var3 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagC = var4;
      var1 = var3 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagZ = var4;
   }

   public int com8BitBase(int var1) {
      this.flagV = false;
      this.flagC = true;
      var1 = ~var1 & 255;
      this.flagN = this.isNeg8Bit(var1);
      boolean var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int dec8BitBase(int var1) {
      boolean var2;
      if(var1 == 128) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagV = var2;
      var1 = var1 - 1 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int decode() {
      int var1;
      CpuBoard var2;
      if(this.ir == 16) {
         this.page = 2;
         var2 = this.cb;
         var1 = this.pc;
         this.pc = var1 + 1;
         this.ir = var2.read8opc(var1);
      } else if(this.ir == 17) {
         this.page = 3;
         var2 = this.cb;
         var1 = this.pc;
         this.pc = var1 + 1;
         this.ir = var2.read8opc(var1);
      } else {
         this.page = 1;
      }

      var1 = baseCycles[this.page - 1][this.ir & 255];
      this.addrMode = addrModeArray[this.ir & 255];
      this.opPc = this.pc;
      if(this.addrMode != 4 && this.addrMode != 5 && this.addrMode != 0) {
         var1 += this.calcEA();
      } else {
         this.useEa = false;
      }

      return var1;
   }

   public int eor8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      var1 = (var1 ^ var2) & 255;
      this.flagV = false;
      this.flagN = this.isNeg8Bit(var1);
      boolean var3;
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
      return var1;
   }

   public void exec(int var1) {
      this.tCycle = var1;
      if((this.interruptState & 1) != 0) {
         this.runNmi();
      }

      if((this.interruptState & 2) != 0 && !this.flagI) {
         this.runIrq();
      }

      if((this.interruptState & 4) != 0 && !this.flagF) {
         this.runFirq();
      }

      this.interruptState = 0;

      while(this.tCycle > 0 && this.pc <= this.breakpoint) {
         this.execInstr();
      }

   }

   public void execInstr() {
      this.lastPc = this.pc;
      CpuBoard var2 = this.cb;
      int var1 = this.pc;
      this.pc = var1 + 1;
      this.ir = var2.read8opc(var1);
      this.tCycle -= this.decode();
      this.execSingleInstr(this.ir & 255);
   }

   public void exg_tfr16BitBase(boolean var1, int var2) {
      int var4 = (var2 & 112) >>> 4;
      int var5 = var2 & 7;
      if(var4 == 0) {
         var2 = this.d();
      } else if(var4 == 1) {
         var2 = this.x;
      } else if(var4 == 2) {
         var2 = this.y;
      } else if(var4 == 3) {
         var2 = this.u;
      } else if(var4 == 4) {
         var2 = this.s;
      } else {
         var2 = this.pc;
      }

      int var3;
      if(var5 == 0) {
         var3 = this.d();
      } else if(var5 == 1) {
         var3 = this.x;
      } else if(var5 == 2) {
         var3 = this.y;
      } else if(var5 == 3) {
         var3 = this.u;
      } else if(var5 == 4) {
         var3 = this.s;
      } else {
         var3 = this.pc;
      }

      if(!var1) {
         var3 = var2;
      }

      if(var4 == 0) {
         this.setD(var3);
      } else if(var4 == 1) {
         this.x = var3;
      } else if(var4 == 2) {
         this.y = var3;
      } else if(var4 == 3) {
         this.u = var3;
      } else if(var4 == 4) {
         this.s = var3;
      } else {
         this.pc = var3;
      }

      if(var5 == 0) {
         this.setD(var2);
      } else if(var5 == 1) {
         this.x = var2;
      } else if(var5 == 2) {
         this.y = var2;
      } else if(var5 == 3) {
         this.u = var2;
      } else if(var5 == 4) {
         this.s = var2;
      } else {
         this.pc = var2;
      }

   }

   public void exg_tfr8BitBase(boolean var1, int var2) {
      int var4 = (var2 & 112) >>> 4;
      int var5 = var2 & 7;
      if(var4 == 0) {
         var2 = this.a;
      } else if(var4 == 1) {
         var2 = this.b;
      } else if(var4 == 2) {
         var2 = this.cc();
      } else {
         var2 = this.dp;
      }

      int var3;
      if(var5 == 0) {
         var3 = this.a;
      } else if(var5 == 1) {
         var3 = this.b;
      } else if(var5 == 2) {
         var3 = this.cc();
      } else {
         var3 = this.dp;
      }

      if(var1) {
         if(var4 == 0) {
            this.a = var3;
         } else if(var4 == 1) {
            this.b = var3;
         } else if(var4 == 2) {
            this.setCC(var3);
         } else {
            this.dp = var3;
         }
      }

      if(var5 == 0) {
         this.a = var2;
      } else if(var5 == 1) {
         this.b = var2;
      } else if(var5 == 2) {
         this.setCC(var2);
      } else {
         this.dp = var2;
      }

   }

   public void firq() {
      this.interruptState |= 4;
      if(this.waitState == 8 && !this.flagF) {
         this.waitState = 0;
      }

      if(this.waitState == 16) {
         this.waitState = 0;
      }

   }

   public int getCyclesLeft() {
      return this.tCycle;
   }

   public final int getDebug() {
      return 0;
   }

   public final long getInstruction() {
      if(this.page == 2) {
         this.curInstr |= 4096;
      } else if(this.page == 3) {
         this.curInstr |= 4352;
      }

      return (long)this.curInstr;
   }

   public String getTag() {
      return this.tag;
   }

   public int inc8BitBase(int var1) {
      boolean var2;
      if(var1 == 127) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagV = var2;
      var1 = var1 + 1 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public final boolean init(CpuBoard var1, int var2) {
      this.cb = var1;
      return true;
   }

   public final void interrupt(int var1, boolean var2) {
      switch(var1) {
      case 0:
         this.irq();
         break;
      case 1:
         this.nmi();
         break;
      case 2:
         this.firq();
      }

   }

   public void irq() {
      this.interruptState |= 2;
      if(this.waitState == 8 && !this.flagI) {
         this.waitState = 0;
      }

      if(this.waitState == 16) {
         this.waitState = 0;
      }

   }

   public int ld16BitBase() {
      this.flagV = false;
      int var1;
      if(!this.useEa) {
         var1 = this.fetch2();
      } else {
         var1 = this.read2Bytes(this.ea);
      }

      this.flagN = this.isNeg16Bit(var1);
      boolean var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int ld8BitBase() {
      this.flagV = false;
      int var1;
      if(!this.useEa) {
         var1 = this.fetch();
      } else {
         var1 = this.readByte(this.ea);
      }

      this.flagN = this.isNeg8Bit(var1);
      boolean var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int lea16BitBase(int var1) {
      return this.ea & '\uffff';
   }

   public int lsr8BitBase(int var1) {
      boolean var2;
      if(this.getBit(var1, 0) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagC = var2;
      var1 >>>= 1;
      this.flagN = false;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int neg8BitBase(int var1) {
      boolean var2;
      if(var1 == 128) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagV = var2;
      if(var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagC = var2;
      var1 = -var1 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public void nmi() {
      this.interruptState |= 1;
      if(this.waitState == 8) {
         this.waitState = 0;
      }

      if(this.waitState == 16) {
         this.waitState = 0;
      }

   }

   public int or8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      var1 |= var2;
      this.flagV = false;
      this.flagN = this.isNeg8Bit(var1);
      boolean var3;
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
      return var1;
   }

   public void pull_push8BitBase(boolean var1, boolean var2, int var3) {
      int var4;
      int var6;
      if(var2) {
         var6 = this.u;
         var4 = this.s;
      } else {
         var6 = this.s;
         var4 = this.u;
      }

      int var5;
      if(var1) {
         if(this.getBit(var3, 0) != 0) {
            var5 = var4 + 1;
            this.setCC(this.readByte(var4));
            --this.tCycle;
         } else {
            var5 = var4;
         }

         var4 = var5;
         if(this.getBit(var3, 1) != 0) {
            this.a = this.readByte(var5);
            --this.tCycle;
            var4 = var5 + 1;
         }

         var5 = var4;
         if(this.getBit(var3, 2) != 0) {
            this.b = this.readByte(var4);
            --this.tCycle;
            var5 = var4 + 1;
         }

         var4 = var5;
         if(this.getBit(var3, 3) != 0) {
            this.dp = this.readByte(var5);
            --this.tCycle;
            var4 = var5 + 1;
         }

         var5 = var4;
         if(this.getBit(var3, 4) != 0) {
            this.x = this.readByte(var4 & '\uffff') << 8;
            this.x |= this.readByte((var4 & '\uffff') + 1);
            var5 = var4 + 2;
            this.tCycle -= 2;
         }

         var4 = var5;
         if(this.getBit(var3, 5) != 0) {
            this.y = this.read2Bytes(var5);
            var4 = var5 + 2;
            this.tCycle -= 2;
         }

         var5 = var4;
         if(this.getBit(var3, 6) != 0) {
            var6 = this.read2Bytes(var4);
            var5 = var4 + 2;
            if(var2) {
               this.u = var6;
            } else {
               this.s = var6;
            }

            this.tCycle -= 2;
         }

         var4 = var5;
         if(this.getBit(var3, 7) != 0) {
            this.pc = this.read2Bytes(var5);
            var4 = var5 + 2;
            this.tCycle -= 2;
         }
      } else {
         if(this.getBit(var3, 7) != 0) {
            var5 = var4 - 2;
            this.write2Bytes(var5, this.pc);
            this.tCycle -= 2;
         } else {
            var5 = var4;
         }

         var4 = var5;
         if(this.getBit(var3, 6) != 0) {
            var4 = var5 - 2;
            this.write2Bytes(var4, var6);
            this.tCycle -= 2;
         }

         var5 = var4;
         if(this.getBit(var3, 5) != 0) {
            var5 = var4 - 2;
            this.write2Bytes(var5, this.y);
            this.tCycle -= 2;
         }

         var4 = var5;
         if(this.getBit(var3, 4) != 0) {
            var4 = var5 - 2;
            this.write2Bytes(var4, this.x);
            this.tCycle -= 2;
         }

         var5 = var4;
         if(this.getBit(var3, 3) != 0) {
            var5 = var4 - 1;
            this.writeByte(var5, this.dp);
            --this.tCycle;
         }

         var4 = var5;
         if(this.getBit(var3, 2) != 0) {
            var4 = var5 - 1;
            this.writeByte(var4, this.b);
            --this.tCycle;
         }

         var5 = var4;
         if(this.getBit(var3, 1) != 0) {
            var5 = var4 - 1;
            this.writeByte(var5, this.a);
            --this.tCycle;
         }

         var4 = var5;
         if(this.getBit(var3, 0) != 0) {
            var4 = var5 - 1;
            this.writeByte(var4, this.cc());
            --this.tCycle;
         }
      }

      if(var2) {
         this.s = var4 & '\uffff';
      } else {
         this.u = var4 & '\uffff';
      }

   }

   public final int read2Bytes(int var1) {
      return this.cb.read8(var1 & '\uffff') << 8 | this.cb.read8((var1 & '\uffff') + 1);
   }

   public final int readByte(int var1) {
      return this.cb.read8('\uffff' & var1);
   }

   public final void reset() {
      this.ea = 0;
      this.ir = 0;
      this.dp = 0;
      this.flagI = true;
      this.flagF = true;
      this.pc = this.readByte('\ufffe') << 8 | this.readByte('\uffff');
      this.page = 1;
      this.useEa = false;
      this.addrMode = 4;
      this.interruptState = 0;
      this.tCycle = 0;
   }

   public int rol8BitBase(int var1) {
      boolean var2;
      if(this.getBit(var1, 6) != this.getBit(var1, 7)) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagV = var2;
      boolean var3 = this.flagC;
      if(this.getBit(var1, 7) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagC = var2;
      var1 = var1 << 1 & 255;
      if(var3) {
         var1 |= 1;
      } else {
         var1 &= 254;
      }

      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public int ror8BitBase(int var1) {
      boolean var3 = this.flagC;
      boolean var2;
      if(this.getBit(var1, 0) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagC = var2;
      var1 = var1 >>> 1 & 255;
      if(var3) {
         var1 |= 128;
      } else {
         var1 &= 127;
      }

      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
      return var1;
   }

   public void runFirq() {
      if(this.waitState == 0) {
         this.flagE = false;
      } else {
         this.pull_push8BitBase(false, true, 129);
      }

      this.flagF = true;
      this.flagI = true;
      this.pc = this.readByte('\ufff6') << 8 | this.readByte('\ufff7');
   }

   public void runIrq() {
      this.flagE = true;
      if((this.interruptState & 8) == 0) {
         this.pull_push8BitBase(false, true, 255);
      } else {
         this.interruptState &= -9;
      }

      this.flagF = true;
      this.flagI = true;
      this.pc = this.readByte('\ufff8') << 8 | this.readByte('\ufff9');
   }

   public void runNmi() {
      this.waitState = 0;
      this.flagE = true;
      if(this.waitState != 8) {
         this.pull_push8BitBase(false, true, 255);
      }

      this.flagF = true;
      this.flagI = true;
      this.pc = this.readByte('￼') << 8 | this.readByte('�');
   }

   public int sbc8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      int var3;
      if(this.flagC) {
         var3 = var1 - var2 - 1;
      } else {
         var3 = var1 - var2;
      }

      boolean var4;
      if(((var1 ^ var2 ^ var3 ^ var3 >> 1) & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagV = var4;
      if((var3 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagC = var4;
      var1 = var3 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagZ = var4;
      return var1;
   }

   public final void setDebug(int var1) {
   }

   public final void setProperty(int var1, int var2) {
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   public void st16BitBase(int var1) {
      if(this.useEa) {
         this.write2Bytes(this.ea, var1);
      } else {
         this.write2Bytes(this.pc, var1);
         this.pc += 2;
      }

      this.flagV = false;
      this.flagN = this.isNeg16Bit(var1);
      boolean var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
   }

   public void st8BitBase(int var1) {
      if(this.useEa) {
         this.writeByte(this.ea, var1);
      } else {
         int var2 = this.pc;
         this.pc = var2 + 1;
         this.writeByte(var2, var1);
      }

      this.flagV = false;
      this.flagN = this.isNeg8Bit(var1);
      boolean var3;
      if(var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.flagZ = var3;
   }

   public int sub8BitBase(int var1) {
      int var2;
      if(!this.useEa) {
         var2 = this.fetch();
      } else {
         var2 = this.readByte(this.ea);
      }

      int var3 = var1 - var2;
      boolean var4;
      if(((var1 ^ var2 ^ var3 ^ var3 >> 1) & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagV = var4;
      if((var3 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagC = var4;
      var1 = var3 & 255;
      this.flagN = this.isNeg8Bit(var1);
      if(var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.flagZ = var4;
      return var1;
   }

   public void tst8BitBase(int var1) {
      this.flagV = false;
      this.flagN = this.isNeg8Bit(var1);
      boolean var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.flagZ = var2;
   }

   public final void write2Bytes(int var1, int var2) {
      this.cb.write8(var1 & '\uffff', var2 >>> 8 & 255);
      this.cb.write8((var1 & '\uffff') + 1, var2 & 255);
   }

   public final void writeByte(int var1, int var2) {
      this.cb.write8('\uffff' & var1, var2 & 255);
   }
}
