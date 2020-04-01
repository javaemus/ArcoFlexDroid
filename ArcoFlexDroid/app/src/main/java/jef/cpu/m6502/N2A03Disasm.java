package jef.cpu.m6502;

import jef.cpuboard.CpuBoard;

public class N2A03Disasm {
   final int MODE_ABS = 5;
   final int MODE_ABSX = 6;
   final int MODE_ABSY = 7;
   final int MODE_BRANCH = 10;
   final int MODE_IMD = 1;
   final int MODE_INDABS = 11;
   final int MODE_INDX = 8;
   final int MODE_INDY = 9;
   final int MODE_NON = 0;
   final int MODE_ZP = 2;
   final int MODE_ZPX = 3;
   final int MODE_ZPY = 4;
   DebugInstruction[] instructions = new DebugInstruction[]{new DebugInstruction(0, "BRK", 0), new DebugInstruction(1, "ORA", 8), new DebugInstruction(2, "KIL2", 0), new DebugInstruction(3, "", 0), new DebugInstruction(4, "", 0), new DebugInstruction(5, "ORA", 2), new DebugInstruction(6, "ASL", 2), new DebugInstruction(7, "", 0), new DebugInstruction(8, "PHP", 0), new DebugInstruction(9, "ORA", 1), new DebugInstruction(10, "ASL", 0), new DebugInstruction(11, "", 0), new DebugInstruction(12, "", 0), new DebugInstruction(13, "ORA", 5), new DebugInstruction(14, "ASL", 5), new DebugInstruction(15, "", 0), new DebugInstruction(16, "BPL", 10), new DebugInstruction(17, "ORA", 9), new DebugInstruction(18, "", 0), new DebugInstruction(19, "", 0), new DebugInstruction(20, "", 0), new DebugInstruction(21, "ORA", 3), new DebugInstruction(22, "ASL", 3), new DebugInstruction(23, "", 0), new DebugInstruction(24, "CLC", 0), new DebugInstruction(25, "ORA", 7), new DebugInstruction(26, "", 0), new DebugInstruction(27, "", 0), new DebugInstruction(28, "", 0), new DebugInstruction(29, "ORA", 6), new DebugInstruction(30, "ASL", 6), new DebugInstruction(31, "", 0), new DebugInstruction(32, "JSR", 5), new DebugInstruction(33, "AND", 8), new DebugInstruction(34, "", 0), new DebugInstruction(35, "", 0), new DebugInstruction(36, "BIT", 2), new DebugInstruction(37, "AND", 2), new DebugInstruction(38, "ROL", 2), new DebugInstruction(39, "", 0), new DebugInstruction(40, "PLP", 0), new DebugInstruction(41, "AND", 1), new DebugInstruction(42, "ROL A", 0), new DebugInstruction(43, "", 0), new DebugInstruction(44, "BIT", 5), new DebugInstruction(45, "AND", 5), new DebugInstruction(46, "ROL", 5), new DebugInstruction(47, "", 0), new DebugInstruction(48, "BMI", 10), new DebugInstruction(49, "AND", 9), new DebugInstruction(50, "", 0), new DebugInstruction(51, "", 0), new DebugInstruction(52, "", 0), new DebugInstruction(53, "AND", 3), new DebugInstruction(54, "ROL", 3), new DebugInstruction(55, "", 0), new DebugInstruction(56, "SEC", 0), new DebugInstruction(57, "AND", 7), new DebugInstruction(58, "", 0), new DebugInstruction(59, "", 0), new DebugInstruction(60, "", 0), new DebugInstruction(61, "AND", 6), new DebugInstruction(62, "ROL", 6), new DebugInstruction(63, "", 0), new DebugInstruction(64, "RTI", 0), new DebugInstruction(65, "EOR", 8), new DebugInstruction(66, "", 0), new DebugInstruction(67, "", 0), new DebugInstruction(68, "", 0), new DebugInstruction(69, "EOR", 2), new DebugInstruction(70, "LSR", 2), new DebugInstruction(71, "", 0), new DebugInstruction(72, "PHA", 0), new DebugInstruction(73, "EOR", 1), new DebugInstruction(74, "LSR", 0), new DebugInstruction(75, "", 0), new DebugInstruction(76, "JMP", 5), new DebugInstruction(77, "EOR", 5), new DebugInstruction(78, "LSR", 5), new DebugInstruction(79, "", 0), new DebugInstruction(80, "BVC", 10), new DebugInstruction(81, "EOR", 9), new DebugInstruction(82, "", 0), new DebugInstruction(83, "", 0), new DebugInstruction(84, "", 0), new DebugInstruction(85, "EOR", 3), new DebugInstruction(86, "LSR", 3), new DebugInstruction(87, "", 0), new DebugInstruction(88, "CLI", 0), new DebugInstruction(89, "EOR", 7), new DebugInstruction(90, "", 0), new DebugInstruction(91, "", 0), new DebugInstruction(92, "", 0), new DebugInstruction(93, "EOR", 6), new DebugInstruction(94, "LSR", 6), new DebugInstruction(95, "", 0), new DebugInstruction(96, "RTS", 0), new DebugInstruction(97, "ADC", 8), new DebugInstruction(98, "", 0), new DebugInstruction(99, "", 0), new DebugInstruction(100, "", 0), new DebugInstruction(101, "ADC", 2), new DebugInstruction(102, "ROR", 2), new DebugInstruction(103, "", 0), new DebugInstruction(104, "PLA", 0), new DebugInstruction(105, "ADC", 1), new DebugInstruction(106, "ROR A", 0), new DebugInstruction(107, "", 0), new DebugInstruction(108, "JMP", 11), new DebugInstruction(109, "ADC", 5), new DebugInstruction(110, "ROR", 5), new DebugInstruction(111, "", 0), new DebugInstruction(112, "BVS", 10), new DebugInstruction(113, "ADC", 9), new DebugInstruction(114, "", 0), new DebugInstruction(115, "", 0), new DebugInstruction(116, "", 0), new DebugInstruction(117, "ADC", 3), new DebugInstruction(118, "ROR", 3), new DebugInstruction(119, "", 0), new DebugInstruction(120, "SEI", 0), new DebugInstruction(121, "ADC", 7), new DebugInstruction(122, "", 0), new DebugInstruction(123, "", 0), new DebugInstruction(124, "", 0), new DebugInstruction(125, "ADC", 6), new DebugInstruction(126, "ROR", 6), new DebugInstruction(127, "", 0), new DebugInstruction(128, "", 0), new DebugInstruction(129, "STA", 8), new DebugInstruction(130, "", 0), new DebugInstruction(131, "", 0), new DebugInstruction(132, "STY", 2), new DebugInstruction(133, "STA", 2), new DebugInstruction(134, "STX", 2), new DebugInstruction(135, "", 0), new DebugInstruction(136, "DEY", 0), new DebugInstruction(137, "", 0), new DebugInstruction(138, "TXA", 0), new DebugInstruction(139, "", 0), new DebugInstruction(140, "STY", 5), new DebugInstruction(141, "STA", 5), new DebugInstruction(142, "STX", 5), new DebugInstruction(143, "", 0), new DebugInstruction(144, "BCC", 10), new DebugInstruction(145, "STA", 9), new DebugInstruction(146, "", 0), new DebugInstruction(147, "", 0), new DebugInstruction(148, "STY", 3), new DebugInstruction(149, "STA", 3), new DebugInstruction(150, "STX", 4), new DebugInstruction(151, "", 0), new DebugInstruction(152, "TYA", 0), new DebugInstruction(153, "STA", 7), new DebugInstruction(154, "TXS", 0), new DebugInstruction(155, "", 0), new DebugInstruction(156, "", 0), new DebugInstruction(157, "STA", 6), new DebugInstruction(158, "", 0), new DebugInstruction(159, "", 0), new DebugInstruction(160, "LDY", 1), new DebugInstruction(161, "LDA", 8), new DebugInstruction(162, "LDX", 1), new DebugInstruction(163, "", 0), new DebugInstruction(164, "LDY", 2), new DebugInstruction(165, "LDA", 2), new DebugInstruction(166, "LDX", 2), new DebugInstruction(167, "", 0), new DebugInstruction(168, "TAY", 0), new DebugInstruction(169, "LDA", 1), new DebugInstruction(170, "TAX", 0), new DebugInstruction(171, "", 0), new DebugInstruction(172, "LDY", 5), new DebugInstruction(173, "LDA", 5), new DebugInstruction(174, "LDX", 5), new DebugInstruction(175, "", 0), new DebugInstruction(176, "BCS", 10), new DebugInstruction(177, "LDA", 8), new DebugInstruction(178, "", 0), new DebugInstruction(179, "", 0), new DebugInstruction(180, "LDY", 3), new DebugInstruction(181, "LDA", 3), new DebugInstruction(182, "LDX", 4), new DebugInstruction(183, "", 0), new DebugInstruction(184, "CLV", 0), new DebugInstruction(185, "LDA", 7), new DebugInstruction(186, "TSX", 0), new DebugInstruction(187, "", 0), new DebugInstruction(188, "LDY", 6), new DebugInstruction(189, "LDA", 6), new DebugInstruction(190, "LDX", 7), new DebugInstruction(191, "", 0), new DebugInstruction(192, "CPY", 1), new DebugInstruction(193, "CMP", 8), new DebugInstruction(194, "", 0), new DebugInstruction(195, "", 0), new DebugInstruction(196, "CPY", 2), new DebugInstruction(197, "CMP", 2), new DebugInstruction(198, "DEC", 2), new DebugInstruction(199, "", 0), new DebugInstruction(200, "INY", 0), new DebugInstruction(201, "CMP", 1), new DebugInstruction(202, "DEX", 0), new DebugInstruction(203, "", 0), new DebugInstruction(204, "CPY", 5), new DebugInstruction(205, "CMP", 5), new DebugInstruction(206, "DEC", 5), new DebugInstruction(207, "", 0), new DebugInstruction(208, "BNE", 10), new DebugInstruction(209, "CMP", 9), new DebugInstruction(210, "", 0), new DebugInstruction(211, "", 0), new DebugInstruction(212, "", 0), new DebugInstruction(213, "CMP", 3), new DebugInstruction(214, "DEC", 3), new DebugInstruction(215, "", 0), new DebugInstruction(216, "CLD", 0), new DebugInstruction(217, "CMP", 7), new DebugInstruction(218, "", 0), new DebugInstruction(219, "", 0), new DebugInstruction(220, "", 0), new DebugInstruction(221, "CMP", 6), new DebugInstruction(222, "DEC", 6), new DebugInstruction(223, "", 0), new DebugInstruction(224, "CPX", 1), new DebugInstruction(225, "SBC", 8), new DebugInstruction(226, "", 0), new DebugInstruction(227, "", 0), new DebugInstruction(228, "CPX", 2), new DebugInstruction(229, "SBC", 2), new DebugInstruction(230, "INC", 2), new DebugInstruction(231, "", 0), new DebugInstruction(232, "INX", 0), new DebugInstruction(233, "SBC", 1), new DebugInstruction(234, "", 0), new DebugInstruction(235, "SBC", 1), new DebugInstruction(236, "CPX", 5), new DebugInstruction(237, "SBC", 5), new DebugInstruction(238, "INC", 5), new DebugInstruction(239, "", 0), new DebugInstruction(240, "BEQ", 10), new DebugInstruction(241, "SBC", 9), new DebugInstruction(242, "KIL2", 0), new DebugInstruction(243, "", 0), new DebugInstruction(244, "", 0), new DebugInstruction(245, "SBC", 3), new DebugInstruction(246, "INC", 3), new DebugInstruction(247, "", 0), new DebugInstruction(248, "SED", 0), new DebugInstruction(249, "SBC", 7), new DebugInstruction(250, "", 0), new DebugInstruction(251, "", 0), new DebugInstruction(252, "", 0), new DebugInstruction(253, "SBC", 6), new DebugInstruction(254, "INC", 6), new DebugInstruction(255, "", 0)};
   final String[] modes = new String[]{"MODE_NON", "MODE_IMD", "MODE_ZP", "MODE_ZPX", "MODE_ZPY", "MODE_ABS", "MODE_ABSX", "MODE_ABSY", "MODE_INDX", "MODE_INDY", "MODE_BRANCH", "MODE_INDABS"};
   private CpuBoard ram;

   public N2A03Disasm(CpuBoard var1) {
      this.ram = var1;
   }

   private String hex(int var1, int var2) {
      String var3;
      for(var3 = Integer.toHexString(var1); var3.length() < var2; var3 = "0" + var3) {
         ;
      }

      return var3;
   }

   private String s(String var1, int var2) {
      while(var1.length() < var2) {
         var1 = var1 + " ";
      }

      return var1;
   }

   public String decompile(CpuBoard var1, int var2) {
      this.ram = var1;
      String var5;
      if(var1 == null) {
         var5 = this.s("", 12);
      } else {
         int var3 = var1.read8(var2);
         DebugInstruction var4 = this.instructions[var3];
         if(var4.code != null && var4.code != "") {
            switch(var4.numOperands) {
            case 0:
               var5 = this.s(var4.code, 12);
               break;
            case 1:
               var2 = var1.read8(var2 + 1);
               var5 = this.s(var4.code + " #" + this.hex(var2, 2), 12);
               break;
            case 2:
               var2 = var1.read8(var2 + 1);
               var5 = this.s(var4.code + " $" + this.hex(var2, 2), 12);
               break;
            case 3:
               var2 = var1.read8(var2 + 1);
               var5 = this.s(var4.code + " $" + this.hex(var2, 2) + ",X", 12);
               break;
            case 4:
               var2 = var1.read8(var2 + 1);
               var5 = this.s(var4.code + " $" + this.hex(var2, 2) + ",Y", 12);
               break;
            case 5:
               var3 = var1.read8(var2 + 1);
               var2 = var1.read8(var2 + 2);
               var5 = this.s(var4.code + " $" + this.hex(var3 | var2 << 8, 4), 12);
               break;
            case 6:
               var3 = var1.read8(var2 + 1);
               var2 = var1.read8(var2 + 2);
               var5 = this.s(var4.code + " $" + this.hex(var3 | var2 << 8, 4) + ",X", 12);
               break;
            case 7:
               var3 = var1.read8(var2 + 1);
               var2 = var1.read8(var2 + 2);
               var5 = this.s(var4.code + " $" + this.hex(var3 | var2 << 8, 4) + ",Y", 12);
               break;
            case 8:
               var2 = var1.read8(var2 + 1);
               var5 = this.s(var4.code + " ($" + this.hex(var2, 2) + ",X)", 12);
               break;
            case 9:
               var2 = var1.read8(var2 + 1);
               var5 = this.s(var4.code + " ($" + this.hex(var2, 2) + "),Y", 12);
               break;
            case 10:
               byte var6 = (byte)var1.read8(var2 + 1);
               var5 = this.s(var4.code + " $" + this.hex(var6 + 2 + var2, 4), 12);
               break;
            case 11:
               var3 = var1.read8(var2 + 1);
               var2 = var1.read8(var2 + 2);
               var5 = this.s(var4.code + " ($" + this.hex(var3 | var2 << 8, 4) + ")", 12);
               break;
            default:
               var5 = this.s("", 12);
            }
         } else {
            var5 = this.s("", 12);
         }
      }

      return var5;
   }
}
