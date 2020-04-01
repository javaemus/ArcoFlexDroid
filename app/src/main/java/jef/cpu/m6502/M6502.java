package jef.cpu.m6502;

import jef.cpu.Cpu;
import jef.cpuboard.CpuBoard;

public class M6502 implements Cpu {
   public static int F_B = 16;
   public static int F_C = 1;
   public static int F_D = 8;
   public static int F_I = 4;
   public static int F_N = 128;
   public static int F_T = 32;
   public static int F_V = 64;
   public static int F_Z = 2;
   public static char IRQ_VEC = '\ufffe';
   public static char NMI_VEC = '\ufffa';
   public static char RST_VEC = '￼';
   public static final boolean TRACE = false;
   public static final boolean TRACE_IRQ = false;
   public static int TYPE_IRQ = 1;
   public static int TYPE_NMI = 2;
   public static int TYPE_OVERFLOW = 3;
   int A;
   int P;
   int X;
   int Y;
   boolean afterCli;
   int cycles;
   private N2A03Disasm disasm;
   char ea;
   boolean irqState;
   boolean nmiState;
   Opcode[] opc = new Opcode[256];
   char pc;
   boolean pendingIrq;
   char ppc;
   CpuBoard ram;
   boolean soState;
   char sp;
   String tag;
   char zp;

   public M6502() {
      this.setTag(this.toString());
      this.initOpcodes();
   }

   private void eaIDX() {
      this.zpl(this.rdArg() + this.X);
      this.eal(this.ram.read8(this.zp));
      this.zpl(this.zpl() + 1);
      this.eah(this.ram.read8(this.zp));
   }

   private void eaIDY() {
      this.zpl(this.rdArg());
      this.eal(this.ram.read8(this.zp));
      this.zpl(this.zpl() + 1);
      this.eah(this.ram.read8(this.zp));
      this.ea = (char)(this.ea + this.Y);
   }

   private void eaZPG() {
      this.zpl(this.rdArg());
      this.ea = this.zp;
   }

   private void eaZPI() {
      this.zpl(this.rdArg());
      this.eal(this.ram.read8(this.zp));
      this.zpl(this.zpl() + 1);
      this.eah(this.ram.read8(this.zp));
   }

   private void eaZPX() {
      this.zpl(this.rdArg() + this.X);
      this.ea = this.zp;
   }

   private void eaZPY() {
      this.zpl(this.rdArg() + this.Y);
      this.ea = this.zp;
   }

   private String format(String var1, int var2) {
      while(var1.length() < var2) {
         var1 = "0" + var1;
      }

      return var1;
   }

   private void takeIrq() {
      if((this.P & F_I) == 0) {
         this.ea = IRQ_VEC;
         this.cycles -= 7;
         this.pushHL(this.pc);
         this.push(this.P & ~F_B);
         this.P |= F_I;
         this.pc = this.rdLH(this.ea);
         this.irqState = false;
      }

      this.pendingIrq = false;
   }

   int ABS() {
      this.eaABS();
      return this.ram.read8(this.ea);
   }

   void ABS(int var1) {
      this.eaABS();
      this.WB_EA(var1);
   }

   int ABX() {
      this.eaABX();
      return this.ram.read8(this.ea);
   }

   void ABX(int var1) {
      this.eaABX();
      this.WB_EA(var1);
   }

   int ABY() {
      this.eaABY();
      return this.ram.read8(this.ea);
   }

   void ABY(int var1) {
      this.eaABY();
      this.WB_EA(var1);
   }

   void ADC(int var1) {
      int var2;
      int var3;
      if((this.P & F_D) != 0) {
         var2 = this.P;
         var3 = F_C;
         int var4 = (this.A & 15) + (var1 & 15) + (var2 & var3);
         int var5 = (this.A & 240) + (var1 & 240);
         this.P &= ~(F_V | F_C | F_N | F_Z);
         if((var4 + var5 & 255) == 0) {
            this.P |= F_Z;
         }

         var2 = var5;
         var3 = var4;
         if(var4 > 9) {
            var2 = var5 + 16;
            var3 = var4 + 6;
         }

         if((var2 & 128) != 0) {
            this.P |= F_N;
         }

         if((~(this.A ^ var1) & (this.A ^ var2) & F_N) != 0) {
            this.P |= F_V;
         }

         var1 = var2;
         if(var2 > 144) {
            var1 = var2 + 96;
         }

         if((var1 & '\uff00') != 0) {
            this.P |= F_C;
         }

         this.A = (var3 & 15) + (var1 & 240);
      } else {
         var2 = this.P;
         var3 = F_C;
         var2 = this.A + var1 + (var2 & var3);
         this.P &= ~(F_V | F_C);
         if((~(this.P ^ var1) & (this.P ^ var2) & F_N) != 0) {
            this.P |= F_V;
         }

         if((var2 & '\uff00') != 0) {
            this.P |= F_C;
         }

         this.A = var2 & 255;
         this.setNZ(this.A);
      }

   }

   void ANC(int var1) {
      this.P &= ~F_C;
      this.A = this.A & var1 & 255;
      if((this.A & 128) != 0) {
         this.P |= F_C;
      }

      this.setNZ(this.A);
   }

   void AND(int var1) {
      this.A &= var1;
      this.setNZ(this.A);
   }

   int ASL(int var1) {
      this.P = this.P & ~F_C | var1 >> 7 & F_C;
      var1 = var1 << 1 & 255;
      this.setNZ(var1);
      return var1;
   }

   int ASR(int var1) {
      return this.LSR(var1 & this.A);
   }

   void AST(int var1) {
      this.S(this.S() & var1);
      var1 = this.S();
      this.X = var1;
      this.A = var1;
      this.setNZ(this.A);
   }

   void ASX(int var1) {
      this.P &= ~F_C;
      this.X &= this.A;
      if(this.X >= var1) {
         this.P |= F_C;
      }

      this.X = this.X - var1 & 255;
      this.setNZ(this.X);
   }

   void AXA(int var1) {
      this.A = (this.A | 238) & this.X & var1 & 255;
      this.setNZ(this.A);
   }

   void BCC() {
      boolean var1;
      if((this.P & F_C) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BCS() {
      boolean var1;
      if((this.P & F_C) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BEQ() {
      boolean var1;
      if((this.P & F_Z) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BIT(int var1) {
      this.P &= ~(F_N | F_V | F_Z);
      this.P |= (F_N | F_V) & var1;
      if((this.A & var1) == 0) {
         this.P |= F_Z;
      }

   }

   void BMI() {
      boolean var1;
      if((this.P & F_N) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BNE() {
      boolean var1;
      if((this.P & F_Z) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BPL() {
      boolean var1;
      if((this.P & F_N) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BRA(boolean var1) {
      if(var1) {
         this.ea = (char)(this.pc + (byte)this.rdArg() + 1);
         int var3 = this.cycles;
         byte var2;
         if(this.pc == this.ea) {
            var2 = 3;
         } else {
            var2 = 4;
         }

         this.cycles = var3 - var2;
         this.pc = this.ea;
      } else {
         ++this.pc;
         this.cycles -= 2;
      }

   }

   void BRK() {
      ++this.pc;
      this.pushHL(this.pc);
      this.push(this.P | F_B);
      this.P |= F_I;
      this.pc = this.rdLH(IRQ_VEC);
   }

   void BVC() {
      boolean var1;
      if((this.P & F_V) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void BVS() {
      boolean var1;
      if((this.P & F_V) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRA(var1);
   }

   void CLC() {
      this.P &= ~F_C;
   }

   void CLD() {
      this.P &= ~F_D;
   }

   void CLI() {
      if(this.irqState && (this.P & F_I) != 0) {
         this.afterCli = true;
      }

      this.P &= ~F_I;
   }

   void CLV() {
      this.P &= ~F_V;
   }

   void CMP(int var1) {
      this.P &= ~F_C;
      if(this.A >= var1) {
         this.P |= F_C;
      }

      this.setNZ(this.A - var1);
   }

   void CPX(int var1) {
      this.P &= ~F_C;
      if(this.X >= var1) {
         this.P |= F_C;
      }

      this.setNZ(this.X - var1);
   }

   void CPY(int var1) {
      this.P &= ~F_C;
      if(this.Y >= var1) {
         this.P |= F_C;
      }

      this.setNZ(this.Y - var1);
   }

   int DCP(int var1) {
      var1 = var1 - 1 & 255;
      this.P &= ~F_C;
      if(this.A >= var1) {
         this.P |= F_C;
      }

      this.setNZ(this.A - var1);
      return var1;
   }

   int DEC(int var1) {
      var1 = var1 - 1 & 255;
      this.setNZ(var1);
      return var1;
   }

   void DEX() {
      this.X = this.X - 1 & 255;
      this.setNZ(this.X);
   }

   void DEY() {
      this.Y = this.Y - 1 & 255;
      this.setNZ(this.Y);
   }

   void DOP() {
      ++this.pc;
   }

   void EOR(int var1) {
      this.A ^= var1;
      this.setNZ(this.A);
   }

   int IDX() {
      this.eaIDX();
      return this.ram.read8(this.ea);
   }

   void IDX(int var1) {
      this.eaIDX();
      this.WB_EA(var1 & 255);
   }

   int IDY() {
      this.eaIDY();
      return this.ram.read8(this.ea);
   }

   void IDY(int var1) {
      this.eaIDY();
      this.WB_EA(var1 & 255);
   }

   int IMM() {
      return this.rdArg();
   }

   int INC(int var1) {
      var1 = var1 + 1 & 255;
      this.setNZ(var1);
      return var1;
   }

   void INX() {
      this.X = this.X + 1 & 255;
      this.setNZ(this.X);
   }

   void INY() {
      this.Y = this.Y + 1 & 255;
      this.setNZ(this.Y);
   }

   int ISB(int var1) {
      var1 = var1 + 1 & 255;
      this.SBC(var1);
      return var1;
   }

   void JMP() {
      if(this.ea == this.ppc && !this.pendingIrq && !this.afterCli && this.cycles > 0) {
         this.cycles = 0;
      }

      this.pc = this.ea;
   }

   void JSR() {
      this.eal(this.rdArg());
      this.pushHL(this.pc);
      this.eah(this.rdArg());
      this.pc = this.ea;
   }

   void KIL() {
      --this.pc;
   }

   void LAX(int var1) {
      this.X = var1;
      this.A = var1;
      this.setNZ(this.A);
   }

   void LDA(int var1) {
      this.A = var1;
      this.setNZ(this.A);
   }

   void LDX(int var1) {
      this.X = var1;
      this.setNZ(this.X);
   }

   void LDY(int var1) {
      this.Y = var1;
      this.setNZ(this.Y);
   }

   int LSR(int var1) {
      this.P = this.P & ~F_C | F_C & var1;
      var1 = var1 >> 1 & 255;
      this.setNZ(var1);
      return var1;
   }

   void NOP() {
   }

   void OAL(int var1) {
      var1 = (this.A | 238) & var1 & 255;
      this.X = var1;
      this.A = var1;
      this.setNZ(this.A);
   }

   void ORA(int var1) {
      this.A |= var1;
      this.setNZ(this.A);
   }

   void PHA() {
      this.push(this.A);
   }

   void PHP() {
      this.push(this.P);
   }

   void PLA() {
      this.A = this.pull();
      this.setNZ(this.A);
   }

   void PLP() {
      if((this.P & F_I) != 0) {
         this.P = this.pull();
         if(this.irqState && (this.P & F_I) == 0) {
            this.afterCli = true;
         }
      } else {
         this.P = this.pull();
      }

      this.P |= F_T | F_B;
   }

   int RLA(int var1) {
      var1 = var1 << 1 | this.P & F_C;
      this.P = this.P & ~F_C | var1 >> 8 & F_C;
      var1 &= 255;
      this.A &= var1;
      this.setNZ(this.A);
      return var1;
   }

   int ROL(int var1) {
      var1 = var1 << 1 | this.P & F_C;
      this.P = this.P & ~F_C | var1 >> 8 & F_C;
      var1 &= 255;
      this.setNZ(var1);
      return var1;
   }

   int ROR(int var1) {
      var1 |= (this.P & F_C) << 8;
      this.P = this.P & ~F_C | F_C & var1;
      var1 = var1 >> 1 & 255;
      this.setNZ(var1);
      return var1;
   }

   int RRA(int var1) {
      var1 |= (this.P & F_C) << 8;
      this.P = this.P & ~F_C | F_C & var1;
      var1 = var1 >> 1 & 255;
      this.ADC(var1);
      return var1;
   }

   void RTI() {
      this.P = this.pull();
      this.pc = this.pullLH();
      this.P |= F_T | F_B;
      if(this.irqState && (this.P & F_I) == 0) {
         this.afterCli = true;
      }

   }

   void RTS() {
      this.pc = this.pullLH();
      ++this.pc;
   }

   int S() {
      return this.sp & 255;
   }

   void S(int var1) {
      this.sp = (char)(this.sp & '\uff00' | var1 & 255);
   }

   int SAH() {
      return this.A & this.X & this.eah() + 1;
   }

   int SAX() {
      return this.A & this.X;
   }

   void SBC(int var1) {
      int var2;
      int var3;
      int var4;
      if((this.P & F_D) != 0) {
         int var7 = this.P & F_C ^ F_C;
         int var6 = this.A - var1 - var7;
         int var5 = (this.A & 15) - (var1 & 15) - var7;
         var4 = (this.A & 240) - (var1 & 240);
         var2 = var4;
         var3 = var5;
         if((var5 & 16) != 0) {
            var3 = var5 - 6;
            var2 = var4 - 1;
         }

         this.P &= ~(F_V | F_C | F_Z | F_N);
         if(((this.A ^ var1) & (this.A ^ var6) & F_N) != 0) {
            this.P |= F_V;
         }

         var4 = var2;
         if((var2 & 256) != 0) {
            var4 = var2 - 96;
         }

         if((var6 & '\uff00') == 0) {
            this.P |= F_C;
         }

         if((this.A - var1 - var7 & 255) == 0) {
            this.P |= F_Z;
         }

         if((this.A - var1 - var7 & 128) != 0) {
            this.P |= F_N;
         }

         this.A = var3 & 15 | var4 & 240;
      } else {
         var3 = this.P;
         var4 = F_C;
         var2 = F_C;
         var2 = this.A - var1 - (var3 & var4 ^ var2);
         this.P &= ~(F_V | F_C);
         if(((this.A ^ var1) & (this.A ^ var2) & F_N) != 0) {
            this.P |= F_V;
         }

         if((var2 & '\uff00') == 0) {
            this.P |= F_C;
         }

         this.A = var2 & 255;
         this.setNZ(this.A);
      }

   }

   void SEC() {
      this.P |= F_C;
   }

   void SED() {
      this.P |= F_D;
   }

   void SEI() {
      this.P |= F_I;
   }

   int SLO(int var1) {
      this.P = this.P & ~F_C | var1 >> 7 & F_C;
      var1 = var1 << 1 & 255;
      this.A |= var1;
      this.setNZ(this.A);
      return var1;
   }

   int SRE(int var1) {
      this.P = this.P & ~F_C | F_C & var1;
      var1 = var1 >> 1 & 255;
      this.A ^= var1;
      this.setNZ(this.A);
      return var1;
   }

   int SSH() {
      this.S(this.A & this.X);
      return this.S() & this.eah() + 1;
   }

   int SXH() {
      return this.X & this.eah() + 1;
   }

   int SYH() {
      return this.Y & this.eah() + 1;
   }

   void TAX() {
      this.X = this.A;
      this.setNZ(this.X);
   }

   void TAY() {
      this.Y = this.A;
      this.setNZ(this.Y);
   }

   void TOP() {
      this.pc = (char)(this.pc + 2);
   }

   void TSX() {
      this.X = this.S();
      this.setNZ(this.X);
   }

   void TXA() {
      this.A = this.X;
      this.setNZ(this.A);
   }

   void TXS() {
      this.S(this.X);
   }

   void TYA() {
      this.A = this.Y;
      this.setNZ(this.A);
   }

   void WB_EA(int var1) {
      this.ram.write8(this.ea, var1);
   }

   int ZPG() {
      this.eaZPG();
      return this.ram.read8(this.ea);
   }

   void ZPG(int var1) {
      this.eaZPG();
      this.WB_EA(var1);
   }

   int ZPI() {
      this.eaZPI();
      return this.ram.read8(this.ea);
   }

   int ZPX() {
      this.eaZPX();
      return this.ram.read8(this.ea);
   }

   void ZPX(int var1) {
      this.eaZPX();
      this.WB_EA(var1);
   }

   int ZPY() {
      this.eaZPY();
      return this.ram.read8(this.ea);
   }

   void ZPY(int var1) {
      this.eaZPY();
      this.WB_EA(var1);
   }

   protected void eaABS() {
      this.eal(this.rdArg());
      this.eah(this.rdArg());
   }

   protected void eaABX() {
      this.eaABS();
      this.ea = (char)(this.ea + this.X);
   }

   void eaABY() {
      this.eaABS();
      this.ea = (char)(this.ea + this.Y);
   }

   void eaIND() {
      this.eaABS();
      int var1 = this.ram.read8(this.ea);
      this.eal(this.eal() + 1);
      this.eah(this.ram.read8(this.ea));
      this.eal(var1);
   }

   char eah() {
      return (char)(this.ea >> 8 & 255);
   }

   void eah(int var1) {
      this.ea = (char)(this.ea & 255 | (var1 & 255) << 8);
   }

   char eal() {
      return (char)(this.ea & 255);
   }

   void eal(int var1) {
      this.ea = (char)(this.ea & '\uff00' | var1 & 255);
   }

   public void exec(int var1) {
      this.cycles += var1;

      while(this.cycles > 0) {
         this.ppc = this.pc;
         if(this.pendingIrq) {
            this.takeIrq();
         }

         this.rdOpc().exec();
         if(this.afterCli) {
            this.afterCli = false;
            if(this.irqState) {
               this.pendingIrq = true;
            }
         } else if(this.pendingIrq) {
            this.takeIrq();
         }
      }

   }

   protected void genILL() {
      OPC_ILLEGAL var2 = new OPC_ILLEGAL();

      for(int var1 = 0; var1 < this.opc.length; ++var1) {
         if(this.opc[var1] == null) {
            this.opc[var1] = var2;
         }
      }

   }

   public int getCyclesLeft() {
      return this.cycles;
   }

   public int getDebug() {
      return 0;
   }

   public long getInstruction() {
      return 0L;
   }

   public String getTag() {
      return this.toString();
   }

   public boolean init(CpuBoard var1, int var2) {
      this.ram = var1;
      this.reset();
      return true;
   }

   protected void initOpcodes() {
      this.opc[0] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.BRK();
         }
      };
      this.opc[32] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.JSR();
         }
      };
      this.opc[64] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.RTI();
         }
      };
      this.opc[96] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.RTS();
         }
      };
      this.opc[160] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.LDY(M6502.this.IMM());
         }
      };
      this.opc[192] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CPY(M6502.this.IMM());
         }
      };
      this.opc[224] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CPX(M6502.this.IMM());
         }
      };
      this.opc[16] = new Opcode() {
         public void exec() {
            M6502.this.BPL();
         }
      };
      this.opc[48] = new Opcode() {
         public void exec() {
            M6502.this.BMI();
         }
      };
      this.opc[80] = new Opcode() {
         public void exec() {
            M6502.this.BVC();
         }
      };
      this.opc[112] = new Opcode() {
         public void exec() {
            M6502.this.BVS();
         }
      };
      this.opc[144] = new Opcode() {
         public void exec() {
            M6502.this.BCC();
         }
      };
      this.opc[176] = new Opcode() {
         public void exec() {
            M6502.this.BCS();
         }
      };
      this.opc[208] = new Opcode() {
         public void exec() {
            M6502.this.BNE();
         }
      };
      this.opc[240] = new Opcode() {
         public void exec() {
            M6502.this.BEQ();
         }
      };
      this.opc[1] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.ORA(M6502.this.IDX());
         }
      };
      this.opc[33] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.AND(M6502.this.IDX());
         }
      };
      this.opc[65] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.EOR(M6502.this.IDX());
         }
      };
      this.opc[97] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.ADC(M6502.this.IDX());
         }
      };
      this.opc[129] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.IDX(M6502.this.A);
         }
      };
      this.opc[161] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.LDA(M6502.this.IDX());
         }
      };
      this.opc[193] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.CMP(M6502.this.IDX());
         }
      };
      this.opc[225] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.SBC(M6502.this.IDX());
         }
      };
      this.opc[17] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.ORA(M6502.this.IDY());
         }
      };
      this.opc[49] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.AND(M6502.this.IDY());
         }
      };
      this.opc[81] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.EOR(M6502.this.IDY());
         }
      };
      this.opc[113] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.ADC(M6502.this.IDY());
         }
      };
      this.opc[145] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.IDY(M6502.this.A);
         }
      };
      this.opc[177] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.LDA(M6502.this.IDY());
         }
      };
      this.opc[209] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.CMP(M6502.this.IDY());
         }
      };
      this.opc[241] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.SBC(M6502.this.IDY());
         }
      };
      this.opc[162] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.LDX(M6502.this.IMM());
         }
      };
      this.opc[36] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.BIT(M6502.this.ZPG());
         }
      };
      this.opc[132] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.ZPG(M6502.this.Y);
         }
      };
      this.opc[164] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.LDY(M6502.this.ZPG());
         }
      };
      this.opc[196] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.CPY(M6502.this.ZPG());
         }
      };
      this.opc[228] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.CPX(M6502.this.ZPG());
         }
      };
      this.opc[148] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ZPX(M6502.this.Y);
         }
      };
      this.opc[180] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDY(M6502.this.ZPX());
         }
      };
      this.opc[5] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.ORA(M6502.this.ZPG());
         }
      };
      this.opc[37] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.AND(M6502.this.ZPG());
         }
      };
      this.opc[69] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.EOR(M6502.this.ZPG());
         }
      };
      this.opc[101] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.ADC(M6502.this.ZPG());
         }
      };
      this.opc[133] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.ZPG(M6502.this.A);
         }
      };
      this.opc[165] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.LDA(M6502.this.ZPG());
         }
      };
      this.opc[197] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.CMP(M6502.this.ZPG());
         }
      };
      this.opc[229] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.SBC(M6502.this.ZPG());
         }
      };
      this.opc[21] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ORA(M6502.this.ZPX());
         }
      };
      this.opc[53] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.AND(M6502.this.ZPX());
         }
      };
      this.opc[85] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.EOR(M6502.this.ZPX());
         }
      };
      this.opc[117] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ADC(M6502.this.ZPX());
         }
      };
      this.opc[149] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.ZPX(M6502.this.A);
         }
      };
      this.opc[181] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDA(M6502.this.ZPX());
         }
      };
      this.opc[213] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.CMP(M6502.this.ZPX());
         }
      };
      this.opc[245] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.SBC(M6502.this.ZPX());
         }
      };
      this.opc[6] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.WB_EA(M6502.this.ASL(M6502.this.ZPG()));
         }
      };
      this.opc[38] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.WB_EA(M6502.this.ROL(M6502.this.ZPG()));
         }
      };
      this.opc[70] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.WB_EA(M6502.this.LSR(M6502.this.ZPG()));
         }
      };
      this.opc[102] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.WB_EA(M6502.this.ROR(M6502.this.ZPG()));
         }
      };
      this.opc[134] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.ZPG(M6502.this.X);
         }
      };
      this.opc[166] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.LDX(M6502.this.ZPG());
         }
      };
      this.opc[198] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.WB_EA(M6502.this.DEC(M6502.this.ZPG()));
         }
      };
      this.opc[230] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.WB_EA(M6502.this.INC(M6502.this.ZPG()));
         }
      };
      this.opc[22] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.ASL(M6502.this.ZPX()));
         }
      };
      this.opc[54] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.ROL(M6502.this.ZPX()));
         }
      };
      this.opc[86] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.LSR(M6502.this.ZPX()));
         }
      };
      this.opc[118] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.ROR(M6502.this.ZPX()));
         }
      };
      this.opc[150] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ZPY(M6502.this.X);
         }
      };
      this.opc[182] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDX(M6502.this.ZPY());
         }
      };
      this.opc[214] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.DEC(M6502.this.ZPX()));
         }
      };
      this.opc[246] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.INC(M6502.this.ZPX()));
         }
      };
      this.opc[9] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.ORA(M6502.this.IMM());
         }
      };
      this.opc[41] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.AND(M6502.this.IMM());
         }
      };
      this.opc[73] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.EOR(M6502.this.IMM());
         }
      };
      this.opc[105] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.ADC(M6502.this.IMM());
         }
      };
      this.opc[169] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.LDA(M6502.this.IMM());
         }
      };
      this.opc[201] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CMP(M6502.this.IMM());
         }
      };
      this.opc[233] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.SBC(M6502.this.IMM());
         }
      };
      this.opc[8] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.PHP();
         }
      };
      this.opc[40] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.PLP();
         }
      };
      this.opc[72] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.PHA();
         }
      };
      this.opc[104] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.PLA();
         }
      };
      this.opc[136] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.DEY();
         }
      };
      this.opc[168] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.TAY();
         }
      };
      this.opc[200] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.INY();
         }
      };
      this.opc[232] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.INX();
         }
      };
      this.opc[24] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CLC();
         }
      };
      this.opc[56] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.SEC();
         }
      };
      this.opc[88] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CLI();
         }
      };
      this.opc[120] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.SEI();
         }
      };
      this.opc[152] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.TYA();
         }
      };
      this.opc[184] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CLV();
         }
      };
      this.opc[216] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.CLD();
         }
      };
      this.opc[248] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.SED();
         }
      };
      this.opc[25] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ORA(M6502.this.ABY());
         }
      };
      this.opc[57] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.AND(M6502.this.ABY());
         }
      };
      this.opc[89] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.EOR(M6502.this.ABY());
         }
      };
      this.opc[121] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ADC(M6502.this.ABY());
         }
      };
      this.opc[153] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.ABY(M6502.this.A);
         }
      };
      this.opc[185] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDA(M6502.this.ABY());
         }
      };
      this.opc[217] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.CMP(M6502.this.ABY());
         }
      };
      this.opc[249] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.SBC(M6502.this.ABY());
         }
      };
      this.opc[10] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.A = M6502.this.ASL(M6502.this.A);
         }
      };
      this.opc[42] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.A = M6502.this.ROL(M6502.this.A);
         }
      };
      this.opc[74] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.A = M6502.this.LSR(M6502.this.A);
         }
      };
      this.opc[106] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.A = M6502.this.ROR(M6502.this.A);
         }
      };
      this.opc[138] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.TXA();
         }
      };
      this.opc[170] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.TAX();
         }
      };
      this.opc[202] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.DEX();
         }
      };
      this.opc[234] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.NOP();
         }
      };
      this.opc[154] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.TXS();
         }
      };
      this.opc[186] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 2;
            M6502.this.TSX();
         }
      };
      this.opc[44] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.BIT(M6502.this.ABS());
         }
      };
      this.opc[76] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 3;
            M6502.this.eaABS();
            M6502.this.JMP();
         }
      };
      this.opc[108] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.eaIND();
            M6502.this.JMP();
         }
      };
      this.opc[140] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ABS(M6502.this.Y);
         }
      };
      this.opc[172] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDY(M6502.this.ABS());
         }
      };
      this.opc[204] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.CPY(M6502.this.ABS());
         }
      };
      this.opc[236] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.CPX(M6502.this.ABS());
         }
      };
      this.opc[188] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDY(M6502.this.ABX());
         }
      };
      this.opc[13] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ORA(M6502.this.ABS());
         }
      };
      this.opc[45] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.AND(M6502.this.ABS());
         }
      };
      this.opc[77] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.EOR(M6502.this.ABS());
         }
      };
      this.opc[109] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ADC(M6502.this.ABS());
         }
      };
      this.opc[141] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ABS(M6502.this.A);
         }
      };
      this.opc[173] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDA(M6502.this.ABS());
         }
      };
      this.opc[205] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.CMP(M6502.this.ABS());
         }
      };
      this.opc[237] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.SBC(M6502.this.ABS());
         }
      };
      this.opc[29] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ORA(M6502.this.ABX());
         }
      };
      this.opc[61] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.AND(M6502.this.ABX());
         }
      };
      this.opc[93] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.EOR(M6502.this.ABX());
         }
      };
      this.opc[125] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ADC(M6502.this.ABX());
         }
      };
      this.opc[157] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 5;
            M6502.this.ABX(M6502.this.A);
         }
      };
      this.opc[189] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDA(M6502.this.ABX());
         }
      };
      this.opc[221] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.CMP(M6502.this.ABX());
         }
      };
      this.opc[253] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.SBC(M6502.this.ABX());
         }
      };
      this.opc[14] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.ASL(M6502.this.ABS()));
         }
      };
      this.opc[46] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.ROL(M6502.this.ABS()));
         }
      };
      this.opc[78] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.LSR(M6502.this.ABS()));
         }
      };
      this.opc[110] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.ROR(M6502.this.ABS()));
         }
      };
      this.opc[142] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.ABS(M6502.this.X);
         }
      };
      this.opc[174] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDX(M6502.this.ABS());
         }
      };
      this.opc[206] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.DEC(M6502.this.ABS()));
         }
      };
      this.opc[238] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 6;
            M6502.this.WB_EA(M6502.this.INC(M6502.this.ABS()));
         }
      };
      this.opc[30] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.WB_EA(M6502.this.ASL(M6502.this.ABX()));
         }
      };
      this.opc[62] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.WB_EA(M6502.this.ROL(M6502.this.ABX()));
         }
      };
      this.opc[94] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.WB_EA(M6502.this.LSR(M6502.this.ABX()));
         }
      };
      this.opc[126] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.WB_EA(M6502.this.ROR(M6502.this.ABX()));
         }
      };
      this.opc[190] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 4;
            M6502.this.LDX(M6502.this.ABY());
         }
      };
      this.opc[222] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.WB_EA(M6502.this.DEC(M6502.this.ABX()));
         }
      };
      this.opc[254] = new Opcode() {
         public void exec() {
            M6502 var1 = M6502.this;
            var1.cycles -= 7;
            M6502.this.WB_EA(M6502.this.INC(M6502.this.ABX()));
         }
      };
      this.genILL();
   }

   public void interrupt(int var1, boolean var2) {
      if(var1 == TYPE_NMI) {
         if(this.nmiState != var2) {
            this.nmiState = var2;
            if(var2) {
               this.ea = NMI_VEC;
               this.cycles -= 7;
               this.pushHL(this.pc);
               this.push(this.P & ~F_B);
               this.P |= F_I;
               this.pcl(this.ram.read8(this.ea));
               this.pch(this.ram.read8(this.ea + 1));
            }
         }
      } else if(var1 == TYPE_OVERFLOW) {
         if(this.soState && !var2) {
            this.P |= F_V;
         }

         this.soState = var2;
      } else {
         this.irqState = var2;
         if(var2) {
            this.pendingIrq = true;
         }
      }

   }

   void pch(int var1) {
      this.pc = (char)(this.pc & 255 | (var1 & 255) << 8);
   }

   void pcl(int var1) {
      this.pc = (char)(this.pc & '\uff00' | var1 & 255);
   }

   char pull() {
      char var1 = (char)this.ram.read8(this.sp);
      this.sp = (char)(this.sp & '\uff00' | this.sp + 1 & 255);
      return var1;
   }

   char pullLH() {
      return (char)(this.pull() | this.pull() << 8);
   }

   void push(int var1) {
      this.sp = (char)(this.sp & '\uff00' | this.sp - 1 & 255);
      this.ram.write8(this.sp, var1 & 255);
   }

   void pushHL(int var1) {
      this.push(var1 >> 8);
      this.push(var1);
   }

   int rdArg() {
      CpuBoard var2 = this.ram;
      char var1 = this.pc;
      this.pc = (char)(var1 + 1);
      return var2.read8arg(var1);
   }

   char rdLH(int var1) {
      return (char)(this.ram.read8(var1) + (this.ram.read8(var1 + 1) << 8));
   }

   Opcode rdOpc() {
      Opcode[] var3 = this.opc;
      CpuBoard var2 = this.ram;
      char var1 = this.pc;
      this.pc = (char)(var1 + 1);
      return var3[var2.read8opc(var1)];
   }

   public void reset() {
      this.pc = this.rdLH(RST_VEC);
      this.sp = 511;
      this.P = F_T | F_I | F_Z | F_B | this.P & F_D;
      this.pendingIrq = false;
      this.afterCli = false;
      this.irqState = false;
      this.nmiState = false;
   }

   public void setDasm(N2A03Disasm var1) {
      this.disasm = var1;
   }

   public void setDebug(int var1) {
   }

   protected void setNZ(int var1) {
      if(var1 == 0) {
         this.P = this.P & ~F_N | F_Z;
      } else {
         this.P = this.P & ~(F_N | F_Z) | F_N & var1;
      }

   }

   protected void setOpc(int var1, Opcode var2) {
      this.opc[var1] = var2;
   }

   protected void setOpc(int[] var1, Opcode var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.opc[var1[var3]] = var2;
      }

   }

   public void setProperty(int var1, int var2) {
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   void zph(int var1) {
      this.zp = (char)(this.zp & 255 | (var1 & 255) << 8);
   }

   char zpl() {
      return (char)(this.zp & 255);
   }

   void zpl(int var1) {
      this.zp = (char)(this.zp & '\uff00' | var1 & 255);
   }

   class OPC_ILLEGAL implements Opcode {
      public void exec() {
         M6502 var1 = M6502.this;
         var1.cycles -= 2;
         System.out.println("Illegal Opcode at 0x" + Integer.toHexString(M6502.this.pc - 1) + " : 0x" + Integer.toHexString(M6502.this.ram.read8(M6502.this.pc - 1)));
      }
   }
}
