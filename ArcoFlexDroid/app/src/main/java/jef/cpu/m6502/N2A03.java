package jef.cpu.m6502;

public class N2A03 extends M6502 {
   int ABX_P() {
      this.eaABX();
      return this.ram.read8(this.ea);
   }

   int ABY_P() {
      this.eaABX();
      return this.ram.read8(this.ea);
   }

   void ADC_NES(int var1) {
      int var3 = this.P;
      int var2 = F_C;
      var2 = this.A + var1 + (var3 & var2);
      this.P &= ~(F_V | F_C);
      if((~(this.A ^ var1) & (this.A ^ var2) & F_N) != 0) {
         this.P |= F_V;
      }

      if(('\uff00' & var2) != 0) {
         this.P |= F_C;
      }

      this.A = var2 & 255;
      this.setNZ(this.A);
   }

   int ARR_NES(int var1) {
      var1 = this.ROR(var1 & this.A);
      this.P &= ~(F_V | F_C);
      if((var1 & 64) != 0) {
         this.P |= F_C;
      }

      if((var1 & 96) == 32 || (var1 & 96) == 64) {
         this.P |= F_V;
      }

      return var1;
   }

   int IDY_NP() {
      this.eaIDY_NP();
      return this.ram.read8(this.ea);
   }

   void IDY_NP(int var1) {
      this.eaIDY_NP();
      this.WB_EA(var1);
   }

   void SBC_NES(int var1) {
      int var2 = this.P;
      int var4 = F_C;
      int var3 = F_C;
      var2 = this.A - var1 - (var2 & var4 ^ var3);
      this.P &= ~(F_V | F_C);
      if(((this.A ^ var1) & (this.A ^ var2) & F_N) != 0) {
         this.P |= F_V;
      }

      if(('\uff00' & var2) == 0) {
         this.P |= F_C;
      }

      this.A = var2 & 255;
      this.setNZ(this.A);
   }

   void TOP_P() {
      this.eaABS();
      if(this.ea + this.X > 255) {
         --this.cycles;
      }

   }

   void eaABX_P() {
      this.eaABS();
      if(this.ea + this.X > 255) {
         --this.cycles;
      }

      this.ea = (char)(this.ea + this.X);
   }

   void eaABY_P() {
      this.eaABS();
      if(this.ea + this.Y > 255) {
         --this.cycles;
      }

      this.ea = (char)(this.ea + this.Y);
   }

   void eaIDY_NP() {
      this.zpl(this.rdArg());
      this.eal(this.ram.read8(this.zp));
      this.zpl(this.zpl() + 1);
      this.eah(this.ram.read8(this.zp));
      this.ea = (char)(this.ea + this.Y);
   }

   protected void initOpcodes() {
      super.initOpcodes();
      super.setDasm(new N2A03Disasm(this.ram));
      this.setOpc(97, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.ADC_NES(N2A03.this.IDX());
         }
      });
      this.setOpc(225, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.SBC_NES(N2A03.this.IDX());
         }
      });
      this.setOpc(113, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.ADC_NES(N2A03.this.IDY());
         }
      });
      this.setOpc(145, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.IDY_NP(N2A03.this.A);
         }
      });
      this.setOpc(241, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.SBC_NES(N2A03.this.IDY());
         }
      });
      this.setOpc(3, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.IDX()));
         }
      });
      this.setOpc(35, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.IDX()));
         }
      });
      this.setOpc(67, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.IDX()));
         }
      });
      this.setOpc(99, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.IDX()));
         }
      });
      this.setOpc(131, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.IDX(N2A03.this.SAX());
         }
      });
      this.setOpc(163, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.LAX(N2A03.this.IDX());
         }
      });
      this.setOpc(195, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.IDX()));
         }
      });
      this.setOpc(227, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.IDX()));
         }
      });
      this.setOpc(19, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.IDY_NP()));
         }
      });
      this.setOpc(51, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.IDY_NP()));
         }
      });
      this.setOpc(83, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.IDY_NP()));
         }
      });
      this.setOpc(115, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.IDY_NP()));
         }
      });
      this.setOpc(147, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.eaIDY_NP();
            N2A03.this.WB_EA(N2A03.this.SAH());
         }
      });
      this.setOpc(179, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.LAX(N2A03.this.IDY_NP());
         }
      });
      this.setOpc(211, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.IDY_NP()));
         }
      });
      this.setOpc(243, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 8;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.IDY_NP()));
         }
      });
      this.setOpc(101, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 3;
            N2A03.this.ADC_NES(N2A03.this.ZPG());
         }
      });
      this.setOpc(229, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 3;
            N2A03.this.SBC_NES(N2A03.this.ZPG());
         }
      });
      this.setOpc(117, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ADC_NES(N2A03.this.ZPX());
         }
      });
      this.setOpc(245, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.SBC_NES(N2A03.this.ZPX());
         }
      });
      this.setOpc(7, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.ZPG()));
         }
      });
      this.setOpc(39, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.ZPG()));
         }
      });
      this.setOpc(71, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.ZPG()));
         }
      });
      this.setOpc(103, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.ZPG()));
         }
      });
      this.setOpc(135, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 3;
            N2A03.this.ZPG(N2A03.this.SAX());
         }
      });
      this.setOpc(167, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 3;
            N2A03.this.LAX(N2A03.this.ZPG());
         }
      });
      this.setOpc(199, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.ZPG()));
         }
      });
      this.setOpc(231, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.ZPG()));
         }
      });
      this.setOpc(23, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.ZPX()));
         }
      });
      this.setOpc(55, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.ZPX()));
         }
      });
      this.setOpc(87, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.ZPX()));
         }
      });
      this.setOpc(119, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.ZPX()));
         }
      });
      this.setOpc(151, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ZPY(N2A03.this.SAX());
         }
      });
      this.setOpc(183, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LAX(N2A03.this.ZPY());
         }
      });
      this.setOpc(215, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.ZPX()));
         }
      });
      this.setOpc(247, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.ZPX()));
         }
      });
      this.setOpc(105, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.ADC_NES(N2A03.this.IMM());
         }
      });
      this.setOpc(233, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.SBC_NES(N2A03.this.IMM());
         }
      });
      this.setOpc(25, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ORA(N2A03.this.ABY_P());
         }
      });
      this.setOpc(57, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.AND(N2A03.this.ABY_P());
         }
      });
      this.setOpc(89, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.EOR(N2A03.this.ABY_P());
         }
      });
      this.setOpc(121, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ADC_NES(N2A03.this.ABY_P());
         }
      });
      this.setOpc(185, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LDA(N2A03.this.ABY_P());
         }
      });
      this.setOpc(217, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.CMP(N2A03.this.ABY_P());
         }
      });
      this.setOpc(249, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.SBC_NES(N2A03.this.ABY_P());
         }
      });
      this.setOpc(11, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.ANC(N2A03.this.IMM());
         }
      });
      this.setOpc(43, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.ANC(N2A03.this.IMM());
         }
      });
      this.setOpc(75, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.A = N2A03.this.ASR(N2A03.this.IMM());
         }
      });
      this.setOpc(107, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.A = N2A03.this.ARR_NES(N2A03.this.IMM());
         }
      });
      this.setOpc(139, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.AXA(N2A03.this.IMM());
         }
      });
      this.setOpc(171, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.OAL(N2A03.this.IMM());
         }
      });
      this.setOpc(203, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.ASX(N2A03.this.IMM());
         }
      });
      this.setOpc(235, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.SBC_NES(N2A03.this.IMM());
         }
      });
      this.setOpc(27, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.ABY()));
         }
      });
      this.setOpc(59, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.ABY()));
         }
      });
      this.setOpc(91, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.ABY()));
         }
      });
      this.setOpc(123, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.ABY()));
         }
      });
      this.setOpc(155, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.eaABY();
            N2A03.this.WB_EA(N2A03.this.SSH());
         }
      });
      this.setOpc(187, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.AST(N2A03.this.ABY_P());
         }
      });
      this.setOpc(219, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.ABY()));
         }
      });
      this.setOpc(251, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.ABY()));
         }
      });
      this.setOpc(12, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.TOP();
         }
      });
      this.setOpc(156, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.eaABX();
            N2A03.this.WB_EA(N2A03.this.SYH());
         }
      });
      this.setOpc(188, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LDY(N2A03.this.ABX_P());
         }
      });
      this.setOpc(109, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ADC_NES(N2A03.this.ABS());
         }
      });
      this.setOpc(237, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.SBC_NES(N2A03.this.ABS());
         }
      });
      this.setOpc(29, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ORA(N2A03.this.ABX_P());
         }
      });
      this.setOpc(61, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.AND(N2A03.this.ABX_P());
         }
      });
      this.setOpc(93, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.EOR(N2A03.this.ABX_P());
         }
      });
      this.setOpc(125, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ADC_NES(N2A03.this.ABY_P());
         }
      });
      this.setOpc(189, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LDA(N2A03.this.ABX_P());
         }
      });
      this.setOpc(221, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.CMP(N2A03.this.ABX_P());
         }
      });
      this.setOpc(253, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.SBC_NES(N2A03.this.ABX_P());
         }
      });
      this.setOpc(158, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.eaABY();
            N2A03.this.WB_EA(N2A03.this.SXH());
         }
      });
      this.setOpc(190, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LDX(N2A03.this.ABY_P());
         }
      });
      this.setOpc(15, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.ABS()));
         }
      });
      this.setOpc(47, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.ABS()));
         }
      });
      this.setOpc(79, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.ABS()));
         }
      });
      this.setOpc(111, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.ABS()));
         }
      });
      this.setOpc(143, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.ABS(N2A03.this.SAX());
         }
      });
      this.setOpc(175, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LAX(N2A03.this.ABS());
         }
      });
      this.setOpc(207, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.ABS()));
         }
      });
      this.setOpc(239, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 6;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.ABS()));
         }
      });
      this.setOpc(31, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.SLO(N2A03.this.ABX()));
         }
      });
      this.setOpc(63, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.RLA(N2A03.this.ABX()));
         }
      });
      this.setOpc(95, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.SRE(N2A03.this.ABX()));
         }
      });
      this.setOpc(127, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.RRA(N2A03.this.ABX()));
         }
      });
      this.setOpc(159, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 5;
            N2A03.this.eaABY();
            N2A03.this.WB_EA(N2A03.this.SAH());
         }
      });
      this.setOpc(191, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.LAX(N2A03.this.ABY_P());
         }
      });
      this.setOpc(223, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.DCP(N2A03.this.ABX()));
         }
      });
      this.setOpc(255, new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 7;
            N2A03.this.WB_EA(N2A03.this.ISB(N2A03.this.ABX()));
         }
      });
      Opcode var1 = new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.DOP();
         }
      };
      this.setOpc(new int[]{128, 130, 194, 226, 137}, var1);
      var1 = new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 3;
            N2A03.this.DOP();
         }
      };
      this.setOpc(new int[]{4, 68, 100}, var1);
      var1 = new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.DOP();
         }
      };
      this.setOpc(new int[]{20, 52, 84, 116, 212, 244}, var1);
      var1 = new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.KIL();
         }
      };
      this.setOpc(new int[]{2, 34, 66, 98, 18, 50, 82, 114, 146, 178, 210, 242}, var1);
      var1 = new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 2;
            N2A03.this.NOP();
         }
      };
      this.setOpc(new int[]{26, 58, 90, 122, 218, 250}, var1);
      var1 = new Opcode() {
         public void exec() {
            N2A03 var1 = N2A03.this;
            var1.cycles -= 4;
            N2A03.this.TOP_P();
         }
      };
      this.setOpc(new int[]{28, 60, 92, 124, 220, 252}, var1);
   }
}
