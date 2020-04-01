package jef.cpu.m68000;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jef.cpu.Cpu;
import jef.cpuboard.CpuBoard;
import jef.map.WriteHandler;

public final class jM68k implements Cpu {
   private static final int EA_ABS_L = 8;
   private static final int EA_ABS_W = 7;
   private static final int EA_AN = 1;
   private static final int EA_AN_IND = 2;
   private static final int EA_AN_INDD = 5;
   private static final int EA_AN_INDI = 6;
   private static final int EA_AN_PD = 4;
   private static final int EA_AN_PI = 3;
   private static final int EA_DN = 0;
   private static final int EA_IMM = 11;
   private static final int EA_PCD = 9;
   private static final int EA_PCI = 10;
   private static final int[] IRQ_LEVEL;
   private static final int[] IRQ_LEVEL2;
   private static final int[] SMASK;
   private static final boolean TRACE = false;
   private static final boolean TRACE_BUSY_LOOP = false;
   private static final int V_ADDRESS_ERROR = 3;
   private static final int V_BUS_ERROR = 2;
   private static final int V_CHK_EXCEPTION = 6;
   private static final int V_DIVISION_BY_ZERO = 5;
   private static final int V_ILLEGAL_INSTRUCTION = 4;
   private static final int V_INT_REQUEST_LEVEL_1 = 25;
   private static final int V_INT_REQUEST_LEVEL_2 = 26;
   private static final int V_INT_REQUEST_LEVEL_3 = 27;
   private static final int V_INT_REQUEST_LEVEL_4 = 28;
   private static final int V_INT_REQUEST_LEVEL_5 = 29;
   private static final int V_INT_REQUEST_LEVEL_6 = 30;
   private static final int V_INT_REQUEST_LEVEL_7 = 31;
   private static final int V_LINE_A_EMULATOR = 10;
   private static final int V_LINE_F_EMULATOR = 11;
   private static final int V_PRIVILEGE_VIOLATION = 8;
   private static final int V_RESERVED_00 = 12;
   private static final int V_RESERVED_01 = 13;
   private static final int V_RESERVED_02 = 14;
   private static final int V_RESERVED_03 = 15;
   private static final int V_RESERVED_04 = 16;
   private static final int V_RESERVED_05 = 17;
   private static final int V_RESERVED_06 = 18;
   private static final int V_RESERVED_07 = 19;
   private static final int V_RESERVED_08 = 20;
   private static final int V_RESERVED_09 = 21;
   private static final int V_RESERVED_10 = 22;
   private static final int V_RESERVED_11 = 23;
   private static final int V_RESERVED_12 = 48;
   private static final int V_RESERVED_13 = 49;
   private static final int V_RESERVED_14 = 50;
   private static final int V_RESERVED_15 = 51;
   private static final int V_RESERVED_16 = 52;
   private static final int V_RESERVED_17 = 53;
   private static final int V_RESERVED_18 = 54;
   private static final int V_RESERVED_19 = 55;
   private static final int V_RESERVED_20 = 56;
   private static final int V_RESERVED_21 = 57;
   private static final int V_RESERVED_22 = 58;
   private static final int V_RESERVED_23 = 59;
   private static final int V_RESERVED_24 = 60;
   private static final int V_RESERVED_25 = 61;
   private static final int V_RESERVED_26 = 62;
   private static final int V_RESERVED_27 = 63;
   private static final int V_SPURIOUS_EXCEPTION = 24;
   private static final int V_SYSTEM_PC = 1;
   private static final int V_SYSTEM_SSP = 0;
   private static final int V_TRACE_EXCEPTION = 9;
   private static final int V_TRAPV_EXCEPTION = 7;
   private static final int V_TRAP_00_EXCEPTION = 32;
   private static final int V_TRAP_01_EXCEPTION = 33;
   private static final int V_TRAP_02_EXCEPTION = 34;
   private static final int V_TRAP_03_EXCEPTION = 35;
   private static final int V_TRAP_04_EXCEPTION = 36;
   private static final int V_TRAP_05_EXCEPTION = 37;
   private static final int V_TRAP_06_EXCEPTION = 38;
   private static final int V_TRAP_07_EXCEPTION = 39;
   private static final int V_TRAP_08_EXCEPTION = 40;
   private static final int V_TRAP_09_EXCEPTION = 41;
   private static final int V_TRAP_10_EXCEPTION = 42;
   private static final int V_TRAP_11_EXCEPTION = 43;
   private static final int V_TRAP_12_EXCEPTION = 44;
   private static final int V_TRAP_13_EXCEPTION = 45;
   private static final int V_TRAP_14_EXCEPTION = 46;
   private static final int V_TRAP_15_EXCEPTION = 47;
   private static final int[] _SMASK;
   public static long cnt;
   private static final int[] ea_times_bw;
   private static final int[] ea_times_jmp;
   private static final int[] ea_times_jsr;
   private static final int[] ea_times_l;
   private static final int[] ea_times_lea;
   private static final int[] ea_times_movem_mr;
   private static final int[] ea_times_movem_rm;
   private static final int[] ea_times_pea;
   private static final int[][] times_move_bw;
   private static final int[][] times_move_l;
   private int[] A = new int[8];
   private final Opcode ABCD = new Opcode() {
      final void execute() {
         jM68k.this.abcd();
      }
   };
   private final Opcode ADDA_L = new Opcode() {
      final void execute() {
         jM68k.this.adda_l();
      }
   };
   private final Opcode ADDA_W = new Opcode() {
      final void execute() {
         jM68k.this.adda_w();
      }
   };
   private final Opcode ADDI = new Opcode() {
      final void execute() {
         jM68k.this.addi();
      }
   };
   private final Opcode ADDQ_A = new Opcode() {
      final void execute() {
         jM68k.this.addq_a();
      }
   };
   private final Opcode ADDQ_D_B = new Opcode() {
      final void execute() {
         jM68k.this.addq_d_b();
      }
   };
   private final Opcode ADDQ_D_L = new Opcode() {
      final void execute() {
         jM68k.this.addq_d_l();
      }
   };
   private final Opcode ADDQ_D_W = new Opcode() {
      final void execute() {
         jM68k.this.addq_d_w();
      }
   };
   private final Opcode ADDQ_M_B = new Opcode() {
      final void execute() {
         jM68k.this.addq_m_b();
      }
   };
   private final Opcode ADDQ_M_L = new Opcode() {
      final void execute() {
         jM68k.this.addq_m_l();
      }
   };
   private final Opcode ADDQ_M_W = new Opcode() {
      final void execute() {
         jM68k.this.addq_m_w();
      }
   };
   private int[] ADDRESS_STACK = new int[8];
   private final Opcode ADDX = new Opcode() {
      final void execute() {
         jM68k.this.addx();
      }
   };
   private final Opcode ADD_MR_B = new Opcode() {
      final void execute() {
         jM68k.this.add_mr_b();
      }
   };
   private final Opcode ADD_MR_L = new Opcode() {
      final void execute() {
         jM68k.this.add_mr_l();
      }
   };
   private final Opcode ADD_MR_W = new Opcode() {
      final void execute() {
         jM68k.this.add_mr_w();
      }
   };
   private final Opcode ADD_RM_B = new Opcode() {
      final void execute() {
         jM68k.this.add_rm_b();
      }
   };
   private final Opcode ADD_RM_L = new Opcode() {
      final void execute() {
         jM68k.this.add_rm_l();
      }
   };
   private final Opcode ADD_RM_W = new Opcode() {
      final void execute() {
         jM68k.this.add_rm_w();
      }
   };
   private final Opcode AND = new Opcode() {
      final void execute() {
         jM68k.this.and();
      }
   };
   private final Opcode ANDI = new Opcode() {
      final void execute() {
         jM68k.this.andi();
      }
   };
   private final Opcode ANDICCR = new Opcode() {
      final void execute() {
         jM68k.this.andiccr();
      }
   };
   private final Opcode ANDISR = new Opcode() {
      final void execute() {
         jM68k.this.andisr();
      }
   };
   private final Opcode ASL = new Opcode() {
      final void execute() {
         jM68k.this.asl();
      }
   };
   private final Opcode ASLM = new Opcode() {
      final void execute() {
         jM68k.this.aslm();
      }
   };
   private final Opcode ASR = new Opcode() {
      final void execute() {
         jM68k.this.asr();
      }
   };
   private final Opcode ASRM = new Opcode() {
      final void execute() {
         jM68k.this.asrm();
      }
   };
   private int Ai;
   private final Opcode BCC = new Opcode() {
      final void execute() {
         jM68k.this.bcc();
      }
   };
   private final Opcode BCC_I = new Opcode() {
      final void execute() {
         jM68k.this.bcc_i();
      }
   };
   private final Opcode BCHG = new Opcode() {
      final void execute() {
         jM68k.this.bchg();
      }
   };
   private final Opcode BCLR = new Opcode() {
      final void execute() {
         jM68k.this.bclr();
      }
   };
   private final Opcode BCS = new Opcode() {
      final void execute() {
         jM68k.this.bcs();
      }
   };
   private final Opcode BCS_I = new Opcode() {
      final void execute() {
         jM68k.this.bcs_i();
      }
   };
   private final Opcode BEQ = new Opcode() {
      final void execute() {
         jM68k.this.beq();
      }
   };
   private final Opcode BEQ_I = new Opcode() {
      final void execute() {
         jM68k.this.beq_i();
      }
   };
   private final Opcode BGE = new Opcode() {
      final void execute() {
         jM68k.this.bge();
      }
   };
   private final Opcode BGE_I = new Opcode() {
      final void execute() {
         jM68k.this.bge_i();
      }
   };
   private final Opcode BGT = new Opcode() {
      final void execute() {
         jM68k.this.bgt();
      }
   };
   private final Opcode BGT_I = new Opcode() {
      final void execute() {
         jM68k.this.bgt_i();
      }
   };
   private final Opcode BHI = new Opcode() {
      final void execute() {
         jM68k.this.bhi();
      }
   };
   private final Opcode BHI_I = new Opcode() {
      final void execute() {
         jM68k.this.bhi_i();
      }
   };
   private final Opcode BKPT = new Opcode() {
      final void execute() {
         jM68k.this.bkpt();
      }
   };
   private final Opcode BLE = new Opcode() {
      final void execute() {
         jM68k.this.ble();
      }
   };
   private final Opcode BLE_I = new Opcode() {
      final void execute() {
         jM68k.this.ble_i();
      }
   };
   private final Opcode BLS = new Opcode() {
      final void execute() {
         jM68k.this.bls();
      }
   };
   private final Opcode BLS_I = new Opcode() {
      final void execute() {
         jM68k.this.bls_i();
      }
   };
   private final Opcode BLT = new Opcode() {
      final void execute() {
         jM68k.this.blt();
      }
   };
   private final Opcode BLT_I = new Opcode() {
      final void execute() {
         jM68k.this.blt_i();
      }
   };
   private final Opcode BMI = new Opcode() {
      final void execute() {
         jM68k.this.bmi();
      }
   };
   private final Opcode BMI_I = new Opcode() {
      final void execute() {
         jM68k.this.bmi_i();
      }
   };
   private final Opcode BNE = new Opcode() {
      final void execute() {
         jM68k.this.bne();
      }
   };
   private final Opcode BNE_I = new Opcode() {
      final void execute() {
         jM68k.this.bne_i();
      }
   };
   private final Opcode BPL = new Opcode() {
      final void execute() {
         jM68k.this.bpl();
      }
   };
   private final Opcode BPL_I = new Opcode() {
      final void execute() {
         jM68k.this.bpl_i();
      }
   };
   private final Opcode BRA = new Opcode() {
      final void execute() {
         jM68k.this.bra();
      }
   };
   private final Opcode BRA_I = new Opcode() {
      final void execute() {
         jM68k.this.bra_i();
      }
   };
   private final Opcode BSET = new Opcode() {
      final void execute() {
         jM68k.this.bset();
      }
   };
   private final Opcode BSR = new Opcode() {
      final void execute() {
         jM68k.this.bsr();
      }
   };
   private final Opcode BSR_I = new Opcode() {
      final void execute() {
         jM68k.this.bsr_i();
      }
   };
   private final Opcode BTST = new Opcode() {
      final void execute() {
         jM68k.this.btst();
      }
   };
   private final Opcode BVC = new Opcode() {
      final void execute() {
         jM68k.this.bvc();
      }
   };
   private final Opcode BVC_I = new Opcode() {
      final void execute() {
         jM68k.this.bvc_i();
      }
   };
   private final Opcode BVS = new Opcode() {
      final void execute() {
         jM68k.this.bvs();
      }
   };
   private final Opcode BVS_I = new Opcode() {
      final void execute() {
         jM68k.this.bvs_i();
      }
   };
   private final Opcode CHK = new Opcode() {
      final void execute() {
         jM68k.this.chk();
      }
   };
   private final Opcode CLR = new Opcode() {
      final void execute() {
         jM68k.this.clr();
      }
   };
   private final Opcode CMPA = new Opcode() {
      final void execute() {
         jM68k.this.cmpa();
      }
   };
   private final Opcode CMPI_B = new Opcode() {
      final void execute() {
         jM68k.this.cmpi_b();
      }
   };
   private final Opcode CMPI_L = new Opcode() {
      final void execute() {
         jM68k.this.cmpi_l();
      }
   };
   private final Opcode CMPI_W = new Opcode() {
      final void execute() {
         jM68k.this.cmpi_w();
      }
   };
   private final Opcode CMPM = new Opcode() {
      final void execute() {
         jM68k.this.cmpm();
      }
   };
   private final Opcode CMP_B = new Opcode() {
      final void execute() {
         jM68k.this.cmp_b();
      }
   };
   private final Opcode CMP_L = new Opcode() {
      final void execute() {
         jM68k.this.cmp_l();
      }
   };
   private final Opcode CMP_W = new Opcode() {
      final void execute() {
         jM68k.this.cmp_w();
      }
   };
   private int[] D = new int[8];
   private final Opcode DBCC = new Opcode() {
      final void execute() {
         jM68k.this.dbcc();
      }
   };
   private final Opcode DIVS = new Opcode() {
      final void execute() {
         jM68k.this.divs();
      }
   };
   private final Opcode DIVU = new Opcode() {
      final void execute() {
         jM68k.this.divu();
      }
   };
   private int Di;
   private final Opcode EOR = new Opcode() {
      final void execute() {
         jM68k.this.eor();
      }
   };
   private final Opcode EORI = new Opcode() {
      final void execute() {
         jM68k.this.eori();
      }
   };
   private final Opcode EORICCR = new Opcode() {
      final void execute() {
         jM68k.this.eoriccr();
      }
   };
   private final Opcode EORISR = new Opcode() {
      final void execute() {
         jM68k.this.eorisr();
      }
   };
   private final Opcode EXG = new Opcode() {
      final void execute() {
         jM68k.this.exg();
      }
   };
   private final Opcode EXT = new Opcode() {
      final void execute() {
         jM68k.this.ext();
      }
   };
   private final Opcode ILLEGAL = new Opcode() {
      final void execute() {
         jM68k.this.illegal();
      }
   };
   private final Opcode JMP = new Opcode() {
      final void execute() {
         jM68k.this.jmp();
      }
   };
   private final Opcode JSR = new Opcode() {
      final void execute() {
         jM68k.this.jsr();
      }
   };
   private final Opcode LEA = new Opcode() {
      final void execute() {
         jM68k.this.lea();
      }
   };
   private final Opcode LINK = new Opcode() {
      final void execute() {
         jM68k.this.link();
      }
   };
   private final Opcode LSL = new Opcode() {
      final void execute() {
         jM68k.this.lsl();
      }
   };
   private final Opcode LSLM = new Opcode() {
      final void execute() {
         jM68k.this.lslm();
      }
   };
   private final Opcode LSR = new Opcode() {
      final void execute() {
         jM68k.this.lsr();
      }
   };
   private final Opcode LSRM = new Opcode() {
      final void execute() {
         jM68k.this.lsrm();
      }
   };
   private final Opcode MOVEA_L = new Opcode() {
      final void execute() {
         jM68k.this.movea_l();
      }
   };
   private final Opcode MOVEA_W = new Opcode() {
      final void execute() {
         jM68k.this.movea_w();
      }
   };
   private final Opcode MOVEFCCR = new Opcode() {
      final void execute() {
         jM68k.this.movefccr();
      }
   };
   private final Opcode MOVEFSR = new Opcode() {
      final void execute() {
         jM68k.this.movefsr();
      }
   };
   private final Opcode MOVEM = new Opcode() {
      final void execute() {
         jM68k.this.movem();
      }
   };
   private final Opcode MOVEP = new Opcode() {
      final void execute() {
         jM68k.this.movep();
      }
   };
   private final Opcode MOVEQ = new Opcode() {
      final void execute() {
         jM68k.this.moveq();
      }
   };
   private final Opcode MOVETCCR = new Opcode() {
      final void execute() {
         jM68k.this.movetccr();
      }
   };
   private final Opcode MOVETSR = new Opcode() {
      final void execute() {
         jM68k.this.movetsr();
      }
   };
   private final Opcode MOVEUSP = new Opcode() {
      final void execute() {
         jM68k.this.moveusp();
      }
   };
   private final Opcode MOVE_B = new Opcode() {
      final void execute() {
         jM68k.this.move_b();
      }
   };
   private final Opcode MOVE_L = new Opcode() {
      final void execute() {
         jM68k.this.move_l();
      }
   };
   private final Opcode MOVE_W = new Opcode() {
      final void execute() {
         jM68k.this.move_w();
      }
   };
   private final Opcode MULS = new Opcode() {
      final void execute() {
         jM68k.this.muls();
      }
   };
   private final Opcode MULU = new Opcode() {
      final void execute() {
         jM68k.this.mulu();
      }
   };
   private final Opcode NBCD = new Opcode() {
      final void execute() {
         jM68k.this.nbcd();
      }
   };
   private final Opcode NEG = new Opcode() {
      final void execute() {
         jM68k.this.neg();
      }
   };
   private final Opcode NEGX = new Opcode() {
      final void execute() {
         jM68k.this.negx();
      }
   };
   private final Opcode NOP = new Opcode() {
      final void execute() {
         jM68k.this.nop();
      }
   };
   private final Opcode NOT = new Opcode() {
      final void execute() {
         jM68k.this.not();
      }
   };
   public int OC;
   private final Opcode[] OPTABLE = new Opcode[65536];
   private final Opcode OR = new Opcode() {
      final void execute() {
         jM68k.this.or();
      }
   };
   private final Opcode ORI = new Opcode() {
      final void execute() {
         jM68k.this.ori();
      }
   };
   private final Opcode ORICCR = new Opcode() {
      final void execute() {
         jM68k.this.oriccr();
      }
   };
   private final Opcode ORISR = new Opcode() {
      final void execute() {
         jM68k.this.orisr();
      }
   };
   public int PC;
   private final Opcode PEA = new Opcode() {
      final void execute() {
         jM68k.this.pea();
      }
   };
   private final Opcode RESET = new Opcode() {
      final void execute() {
         jM68k.this.opReset();
      }
   };
   private final Opcode ROL = new Opcode() {
      final void execute() {
         jM68k.this.rol();
      }
   };
   private final Opcode ROLM = new Opcode() {
      final void execute() {
         jM68k.this.rolm();
      }
   };
   private final Opcode ROR = new Opcode() {
      final void execute() {
         jM68k.this.ror();
      }
   };
   private final Opcode RORM = new Opcode() {
      final void execute() {
         jM68k.this.rorm();
      }
   };
   private final Opcode ROXL = new Opcode() {
      final void execute() {
         jM68k.this.roxl();
      }
   };
   private final Opcode ROXLM = new Opcode() {
      final void execute() {
         jM68k.this.roxlm();
      }
   };
   private final Opcode ROXR = new Opcode() {
      final void execute() {
         jM68k.this.roxr();
      }
   };
   private final Opcode ROXRM = new Opcode() {
      final void execute() {
         jM68k.this.roxrm();
      }
   };
   private final Opcode RTE = new Opcode() {
      final void execute() {
         jM68k.this.rte();
      }
   };
   private final Opcode RTR = new Opcode() {
      final void execute() {
         jM68k.this.rtr();
      }
   };
   private final Opcode RTS = new Opcode() {
      final void execute() {
         jM68k.this.rts();
      }
   };
   private int Rx;
   private int Ry;
   private final Opcode SBCD = new Opcode() {
      final void execute() {
         jM68k.this.sbcd();
      }
   };
   private final Opcode SCC = new Opcode() {
      final void execute() {
         jM68k.this.scc();
      }
   };
   public int SSP;
   private final Opcode STOP = new Opcode() {
      final void execute() {
         jM68k.this.stop();
      }
   };
   private final Opcode SUB = new Opcode() {
      final void execute() {
         jM68k.this.sub();
      }
   };
   private final Opcode SUBA = new Opcode() {
      final void execute() {
         jM68k.this.suba();
      }
   };
   private final Opcode SUBI = new Opcode() {
      final void execute() {
         jM68k.this.subi();
      }
   };
   private final Opcode SUBQ = new Opcode() {
      final void execute() {
         jM68k.this.subq();
      }
   };
   private final Opcode SUBX = new Opcode() {
      final void execute() {
         jM68k.this.subx();
      }
   };
   private final Opcode SWAP = new Opcode() {
      final void execute() {
         jM68k.this.swap();
      }
   };
   private final Opcode TAS = new Opcode() {
      final void execute() {
         jM68k.this.tas();
      }
   };
   private final Opcode TRAP = new Opcode() {
      final void execute() {
         jM68k.this.trap();
      }
   };
   private final Opcode TRAPV = new Opcode() {
      final void execute() {
         jM68k.this.trapv();
      }
   };
   private final Opcode TST_B = new Opcode() {
      final void execute() {
         jM68k.this.tst_b();
      }
   };
   private final Opcode TST_L = new Opcode() {
      final void execute() {
         jM68k.this.tst_l();
      }
   };
   private final Opcode TST_W = new Opcode() {
      final void execute() {
         jM68k.this.tst_w();
      }
   };
   private final Opcode UNLK = new Opcode() {
      final void execute() {
         jM68k.this.unlk();
      }
   };
   private int Xidd;
   private int addressStackPointer = 0;
   private int cc;
   public long cc_timeslice;
   private long clock_cycles;
   private boolean condition;
   private boolean cpuIsRunning;
   public long cycles;
   private int d;
   private jM68k_Debug debug;
   private int dst;
   private int ea;
   private int ea_mode_dst;
   private int ea_mode_src;
   private boolean error;
   private boolean exceptionState;
   private int extend_cycles;
   private int fC;
   private int fN;
   private int fSS;
   private int fTM;
   private int fV;
   private int fX;
   private int fnZ;
   private long frozen_cycles;
   private long h_cycles;
   private int haltPC;
   private boolean ignore_TAS_WriteBack;
   private List intAckListeners = new ArrayList();
   private int intMask;
   private boolean interruptAcknowledged;
   private boolean interruptPending;
   public int lPC;
   public int lPC_;
   private int loopc;
   int markBusyLoopOpc = '\uffff';
   private int mask;
   private CpuBoard memory;
   private int mode;
   private int msbd;
   private int msbr;
   private int msbs;
   private int num;
   private int offset;
   private int opmode;
   private long p_clock_cycles;
   private int pendingInterrupt;
   private int reg;
   private int res;
   private WriteHandler resetCallBack;
   private int src;
   private boolean suspendedState;
   private int sz;
   private long tLastUpdate;
   private long tNow;
   private long tPassed;
   public String tag = Integer.toHexString(this.hashCode());
   private int tmp;
   private int tmpSR;
   int trNum = 0;

   static {
      int[] var3 = new int[]{4, 4, 8, 8, 8, 12, 14, 12, 16, 0, 0, 0};
      int[] var5 = new int[]{4, 4, 8, 8, 8, 12, 14, 12, 16, 0, 0, 0};
      int[] var7 = new int[]{8, 8, 12, 12, 12, 16, 18, 16, 20, 0, 0, 0};
      int[] var10 = new int[]{8, 8, 12, 12, 12, 16, 18, 16, 20, 0, 0, 0};
      int[] var6 = new int[]{10, 10, 14, 14, 14, 18, 20, 18, 22, 0, 0, 0};
      int[] var0 = new int[]{12, 12, 16, 16, 16, 20, 22, 20, 24, 0, 0, 0};
      int[] var8 = new int[]{14, 14, 18, 18, 18, 22, 24, 22, 26, 0, 0, 0};
      int[] var2 = new int[]{12, 12, 16, 16, 16, 20, 22, 20, 24, 0, 0, 0};
      int[] var4 = new int[]{16, 16, 20, 20, 20, 24, 26, 24, 28, 0, 0, 0};
      int[] var11 = new int[]{12, 12, 16, 16, 16, 20, 22, 20, 24, 0, 0, 0};
      int[] var9 = new int[]{14, 14, 18, 18, 18, 22, 24, 22, 26, 0, 0, 0};
      int[] var1 = new int[]{8, 8, 12, 12, 12, 16, 18, 16, 20, 0, 0, 0};
      times_move_bw = new int[][]{var3, var5, var7, var10, var6, var0, var8, var2, var4, var11, var9, var1};
      var3 = new int[]{4, 4, 12, 12, 12, 16, 18, 16, 20, 0, 0, 0};
      var6 = new int[]{4, 4, 12, 12, 12, 16, 18, 16, 20, 0, 0, 0};
      var2 = new int[]{12, 12, 20, 20, 20, 24, 26, 24, 28, 0, 0, 0};
      var4 = new int[]{12, 12, 20, 20, 20, 24, 26, 24, 28, 0, 0, 0};
      var11 = new int[]{14, 14, 22, 22, 22, 26, 28, 26, 30, 0, 0, 0};
      var9 = new int[]{16, 16, 24, 24, 24, 28, 30, 28, 32, 0, 0, 0};
      var5 = new int[]{18, 18, 26, 26, 26, 30, 32, 30, 34, 0, 0, 0};
      var0 = new int[]{16, 16, 24, 24, 24, 28, 30, 28, 32, 0, 0, 0};
      var10 = new int[]{20, 20, 28, 28, 28, 32, 34, 32, 36, 0, 0, 0};
      var7 = new int[]{16, 16, 24, 24, 24, 28, 30, 28, 32, 0, 0, 0};
      var8 = new int[]{18, 18, 26, 26, 26, 30, 32, 30, 34, 0, 0, 0};
      var1 = new int[]{12, 12, 20, 20, 20, 24, 26, 24, 28, 0, 0, 0};
      times_move_l = new int[][]{var3, var6, var2, var4, var11, var9, var5, var0, var10, var7, var8, var1};
      var0 = new int[12];
      var0[2] = 8;
      var0[5] = 10;
      var0[6] = 14;
      var0[7] = 10;
      var0[8] = 12;
      var0[9] = 10;
      var0[10] = 14;
      ea_times_jmp = var0;
      var0 = new int[12];
      var0[2] = 16;
      var0[5] = 18;
      var0[6] = 22;
      var0[7] = 18;
      var0[8] = 20;
      var0[9] = 18;
      var0[10] = 22;
      ea_times_jsr = var0;
      var0 = new int[12];
      var0[2] = 4;
      var0[5] = 8;
      var0[6] = 12;
      var0[7] = 8;
      var0[8] = 12;
      var0[9] = 8;
      var0[10] = 12;
      ea_times_lea = var0;
      var0 = new int[12];
      var0[2] = 12;
      var0[5] = 16;
      var0[6] = 20;
      var0[7] = 16;
      var0[8] = 20;
      var0[9] = 16;
      var0[10] = 20;
      ea_times_pea = var0;
      var0 = new int[12];
      var0[2] = 12;
      var0[3] = 12;
      var0[5] = 16;
      var0[6] = 18;
      var0[7] = 16;
      var0[8] = 20;
      var0[9] = 16;
      var0[10] = 18;
      ea_times_movem_mr = var0;
      var0 = new int[12];
      var0[2] = 8;
      var0[4] = 8;
      var0[5] = 12;
      var0[6] = 14;
      var0[7] = 12;
      var0[8] = 16;
      ea_times_movem_rm = var0;
      var0 = new int[]{0, 0, 4, 4, 6, 8, 10, 8, 12, 8, 10, 4};
      ea_times_bw = var0;
      var0 = new int[]{0, 0, 8, 8, 10, 12, 14, 12, 16, 12, 14, 8};
      ea_times_l = var0;
      IRQ_LEVEL = new int[]{24, 25, 26, 27, 28, 29, 30, 31};
      var0 = new int[]{0, 1, 2, 4, 8, 16, 32, 64};
      IRQ_LEVEL2 = var0;
      var0 = new int[]{0, 255, '\uffff', 0, -1};
      SMASK = var0;
      var0 = new int[]{0, -256, -65536, 0, 0};
      _SMASK = var0;
      cnt = 0L;
   }

   public jM68k() {
      this.createOpTable();
      this.ignore_TAS_WriteBack = false;
   }

   public jM68k(jM68k_Bus var1) {
      this.createOpTable();
      this.initCPU();
      this.ignore_TAS_WriteBack = false;
   }

   private final void abcd() {
      this.mode = this.OC >> 3 & 1;
      this.Ry = this.OC & 7;
      this.Rx = this.OC >> 9 & 7;
      int var1;
      switch(this.mode) {
      case 0:
         this.src = this.D[this.Ry] & 255;
         this.dst = this.D[this.Rx] & 255;
         var1 = this.dst;
         var1 = this.src;
         var1 = this.fX;
         this.res = (this.dst & 15) + (this.src & 15) + (this.fX & 1);
         if(this.res > 9) {
            this.res += 6;
         }

         this.res += (this.dst & 240) + (this.src & 240);
         if(this.res > 153) {
            this.res -= 160;
            this.fX = 1;
            this.fC = 1;
         } else {
            this.fX = 0;
            this.fC = 0;
         }

         this.fnZ |= this.res & 255;
         this.fN = this.res >>> 7 & 1;
         this.D[this.Rx] = this.D[this.Rx] & -256 | this.res & 255;
         this.clock_cycles += 6L;
         break;
      case 1:
         int[] var2;
         if(this.Ry == 7) {
            var2 = this.A;
            var1 = this.Ry;
            var2[var1] -= 2;
         } else {
            var2 = this.A;
            var1 = this.Ry;
            --var2[var1];
         }

         this.src = this.readByte(this.A[this.Ry]) & 255;
         if(this.Rx == 7) {
            var2 = this.A;
            var1 = this.Rx;
            var2[var1] -= 2;
         } else {
            var2 = this.A;
            var1 = this.Rx;
            --var2[var1];
         }

         this.dst = this.readByte(this.A[this.Rx]) & 255;
         var1 = this.dst;
         var1 = this.src;
         var1 = this.fX;
         this.res = (this.dst & 15) + (this.src & 15) + (this.fX & 1);
         if(this.res > 9) {
            this.res += 6;
         }

         this.res += (this.dst & 240) + (this.src & 240);
         if(this.res > 153) {
            this.res -= 160;
            this.fX = 1;
            this.fC = 1;
         } else {
            this.fX = 0;
            this.fC = 0;
         }

         this.fnZ |= this.res & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeByte(this.A[this.Rx], this.res & 255);
         this.clock_cycles += 18L;
      }

   }

   private final void add_mr_b() {
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di] & 255;
      this.src = this.readEA_8();
      this.fnZ = this.dst + this.src;
      this.fN = this.fnZ >>> 7 & 1;
      int var1 = this.fnZ >>> 8 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fnZ &= 255;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 7 & 1;
      this.D[this.Di] = this.D[this.Di] & -256 | this.fnZ;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
   }

   private final void add_mr_l() {
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di];
      this.src = this.readEA_32();
      int[] var3 = this.D;
      int var1 = this.Di;
      int var2 = this.dst + this.src;
      this.fnZ = var2;
      var3[var1] = var2;
      this.fN = this.fnZ >>> 31 & 1;
      var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 31 & 1;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 7);
      if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
         this.clock_cycles += 2L;
      }

   }

   private final void add_mr_w() {
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di] & '\uffff';
      this.src = this.readEA_16();
      this.fnZ = this.dst + this.src;
      this.fN = this.fnZ >>> 15 & 1;
      int var1 = this.fnZ >>> 16 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fnZ &= '\uffff';
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 15 & 1;
      this.D[this.Di] = this.D[this.Di] & -65536 | this.fnZ;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
   }

   private final void add_rm_b() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di] & 255;
      this.src = this.getEA_OP_8();
      this.fnZ = this.dst + this.src;
      this.fN = this.fnZ >>> 7 & 1;
      int var1 = this.fnZ >>> 8 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fnZ &= 255;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 7 & 1;
      this.writeEA_8(this.mode, this.reg, this.fnZ);
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void add_rm_l() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di];
      this.src = this.getEA_OP_32();
      this.fnZ = this.src + this.dst;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 31 & 1;
      this.fN = this.fnZ >>> 31 & 1;
      int var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
      this.fX = var1;
      this.fC = var1;
      this.writeEA_32(this.mode, this.reg, this.fnZ);
      this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst]);
   }

   private final void add_rm_w() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di] & '\uffff';
      this.src = this.getEA_OP_16();
      this.fnZ = this.src + this.dst;
      this.fN = this.fnZ >>> 15 & 1;
      int var1 = this.fnZ >>> 16 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fnZ &= '\uffff';
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 15 & 1;
      this.writeEA_16(this.mode, this.reg, this.fnZ);
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void adda_l() {
      this.dst = this.readEA_32();
      int[] var2 = this.A;
      int var1 = this.OC >> 9 & 7;
      var2[var1] += this.dst;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 7);
      if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
         this.clock_cycles += 2L;
      }

   }

   private final void adda_w() {
      this.dst = (short)this.readEA_16();
      int[] var2 = this.A;
      int var1 = this.OC >> 9 & 7;
      var2[var1] += this.dst;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 9);
   }

   private final void addi() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.src = this.fetchOpByte(this.PC + 1);
         this.PC += 2;
         this.dst = this.getEA_OP_8();
         this.res = this.dst + this.src;
         var1 = this.res >>> 8 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 7 & 1;
         this.res &= 255;
         this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.dst = this.getEA_OP_16();
         this.res = this.dst + this.src;
         var1 = this.res >>> 16 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 15 & 1;
         this.res &= '\uffff';
         this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.src = this.fetchOpLong(this.PC);
         this.PC += 4;
         this.dst = this.getEA_OP_32();
         this.res = this.dst + this.src;
         var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 31 & 1;
         this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 16;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 25 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fnZ = this.res;
   }

   private final void addq_a() {
      int[] var2 = this.A;
      int var1 = this.OC & 7;
      var2[var1] += ((this.OC >>> 9) - 1 & 7) + 1;
   }

   private final void addq_d_b() {
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9) - 1 & 7) + 1;
      this.dst = this.D[this.reg] & 255;
      this.fnZ = this.dst + this.src;
      int var1 = this.fnZ >>> 8 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fN = this.fnZ >>> 7 & 1;
      this.fnZ &= 255;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 7 & 1;
      this.D[this.reg] = this.D[this.reg] & -256 | this.fnZ;
      this.clock_cycles += 5L;
   }

   private final void addq_d_l() {
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9) - 1 & 7) + 1;
      this.dst = this.D[this.reg];
      int[] var3 = this.D;
      int var1 = this.reg;
      int var2 = this.dst + this.src;
      this.fnZ = var2;
      var3[var1] = var2;
      var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 31 & 1;
      this.fN = this.fnZ >>> 31 & 1;
      this.clock_cycles += 9L;
   }

   private final void addq_d_w() {
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9) - 1 & 7) + 1;
      this.dst = this.D[this.reg] & '\uffff';
      this.fnZ = this.dst + this.src;
      int var1 = this.fnZ >>> 16 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fN = this.fnZ >>> 15 & 1;
      this.fnZ &= '\uffff';
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 15 & 1;
      this.D[this.reg] = this.D[this.reg] & -65536 | this.fnZ;
      this.clock_cycles += 5L;
   }

   private final void addq_m_b() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9) - 1 & 7) + 1;
      this.dst = this.getEA_OP_8();
      this.fnZ = this.dst + this.src;
      int var1 = this.fnZ >>> 8 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fN = this.fnZ >>> 7 & 1;
      this.fnZ &= 255;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 7 & 1;
      this.writeEA_8(this.mode, this.reg, this.fnZ);
      this.clock_cycles += (long)((ea_times_bw[this.ea_mode_src] << 1) + 10);
   }

   private final void addq_m_l() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9) - 1 & 7) + 1;
      this.dst = this.getEA_OP_32();
      this.fnZ = this.dst + this.src;
      int var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 31 & 1;
      this.fN = this.fnZ >>> 31 & 1;
      this.writeEA_32(this.mode, this.reg, this.fnZ);
      this.clock_cycles += (long)((ea_times_l[this.ea_mode_src] << 1) + 15 + ea_times_l[this.ea_mode_src]);
   }

   private final void addq_m_w() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9) - 1 & 7) + 1;
      this.dst = this.getEA_OP_16();
      this.fnZ = this.dst + this.src;
      int var1 = this.fnZ >>> 16 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fN = this.fnZ >>> 15 & 1;
      this.fnZ &= '\uffff';
      this.fV = ((this.src ^ this.fnZ) & (this.dst ^ this.fnZ)) >>> 15 & 1;
      this.writeEA_16(this.mode, this.reg, this.fnZ);
      this.clock_cycles += (long)((ea_times_bw[this.ea_mode_src] << 1) + 10);
   }

   private final void addx() {
      this.sz = this.OC >> 6 & 3;
      this.Rx = this.OC >> 9 & 7;
      this.Ry = this.OC & 7;
      this.cc = this.fX;
      int var1;
      label32:
      switch(this.OC & 8) {
      case 0:
         this.src = this.D[this.Ry];
         this.dst = this.D[this.Rx];
         switch(this.sz) {
         case 0:
            this.src &= 255;
            this.dst &= 255;
            this.res = this.src + this.dst + this.cc;
            var1 = this.res >>> 8 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 7 & 1;
            this.res &= 255;
            this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 7 & 1;
            this.D[this.Rx] = this.D[this.Rx] & -256 | this.res;
            this.clock_cycles += 5L;
            break label32;
         case 1:
            this.src &= '\uffff';
            this.dst &= '\uffff';
            this.res = this.src + this.dst + this.cc;
            this.fN = this.res >>> 15 & 1;
            var1 = this.res >>> 16 & 1;
            this.fX = var1;
            this.fC = var1;
            this.res &= '\uffff';
            this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 15 & 1;
            this.D[this.Rx] = this.D[this.Rx] & -65536 | this.res;
            this.clock_cycles += 5L;
            break label32;
         case 2:
            this.res = this.src + this.dst + this.cc;
            this.fN = this.res >>> 31 & 1;
            this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 31 & 1;
            var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
            this.fX = var1;
            this.fC = var1;
            this.D[this.Rx] = this.res;
            this.clock_cycles += 9L;
         default:
            break label32;
         }
      case 8:
         int[] var4;
         switch(this.sz) {
         case 0:
            byte var5;
            if(this.Ry == 7) {
               var5 = 2;
            } else {
               var5 = 1;
            }

            byte var2;
            if(this.Rx == 7) {
               var2 = 2;
            } else {
               var2 = 1;
            }

            var4 = this.A;
            int var3 = this.Ry;
            var4[var3] -= var5;
            this.src = this.readByte(this.A[this.Ry]);
            var4 = this.A;
            var1 = this.Rx;
            var4[var1] -= var2;
            this.dst = this.readByte(this.A[this.Rx]);
            this.res = this.src + this.dst + this.cc;
            this.fN = this.res >>> 7 & 1;
            var1 = this.res >>> 8 & 1;
            this.fX = var1;
            this.fC = var1;
            this.res &= 255;
            this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 7 & 1;
            this.writeByte(this.A[this.Rx], this.res);
            this.clock_cycles += 22L;
            break;
         case 1:
            var4 = this.A;
            var1 = this.Ry;
            var4[var1] -= 2;
            this.src = this.readWord(this.A[this.Ry]);
            var4 = this.A;
            var1 = this.Rx;
            var4[var1] -= 2;
            this.dst = this.readWord(this.A[this.Rx]);
            this.res = this.src + this.dst + this.cc;
            this.fN = this.res >>> 15 & 1;
            var1 = this.res >>> 16 & 1;
            this.fX = var1;
            this.fC = var1;
            this.res &= '\uffff';
            this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 15 & 1;
            this.writeWord(this.A[this.Rx], this.res);
            this.clock_cycles += 22L;
            break;
         case 2:
            var4 = this.A;
            var1 = this.Ry;
            var4[var1] -= 4;
            this.src = this.readLong(this.A[this.Ry]);
            var4 = this.A;
            var1 = this.Rx;
            var4[var1] -= 4;
            this.dst = this.readLong(this.A[this.Rx]);
            this.res = this.src + this.dst + this.cc;
            this.fV = ((this.src ^ this.res) & (this.dst ^ this.res)) >>> 31 & 1;
            this.fN = this.res >>> 31 & 1;
            var1 = (this.src & this.dst & 1) + (this.src >>> 1) + (this.dst >>> 1) >>> 31 & 1;
            this.fX = var1;
            this.fC = var1;
            this.writeLong(this.A[this.Rx], this.res);
            this.clock_cycles += 37L;
         }
      }

      this.fnZ |= this.res;
   }

   private final void and() {
      this.reg = this.OC & 7;
      this.mode = this.OC >> 3 & 7;
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di];
      switch(this.OC & 448) {
      case 0:
         this.src = this.readEA_8();
         this.dst &= 255;
         this.res = this.src & this.dst & 255;
         this.fN = this.res >>> 7 & 1;
         this.D[this.Di] = this.D[this.Di] & -256 | this.res;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         break;
      case 64:
         this.src = this.readEA_16();
         this.dst &= '\uffff';
         this.res = this.src & this.dst & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         break;
      case 128:
         this.src = this.readEA_32();
         this.res = this.src & this.dst;
         this.D[this.Di] = this.res;
         this.fN = this.res >>> 31 & 1;
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 7);
         if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
            this.clock_cycles += 2L;
         }
         break;
      case 256:
         this.src = this.dst & 255;
         this.dst = this.getEA_OP_8();
         this.res = this.src & this.dst & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         break;
      case 320:
         this.src = this.dst & '\uffff';
         this.dst = this.getEA_OP_16();
         this.res = this.src & this.dst & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         break;
      case 384:
         this.src = this.dst;
         this.dst = this.getEA_OP_32();
         this.res = this.src & this.dst;
         this.fN = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst]);
      }

      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
   }

   private final void andi() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.src = this.fetchOpByte(this.PC + 1);
         this.PC += 2;
         this.dst = this.getEA_OP_8();
         this.res = this.dst & this.src & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.dst = this.getEA_OP_16();
         this.res = this.dst & this.src & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.src = this.fetchOpLong(this.PC);
         this.PC += 4;
         this.dst = this.getEA_OP_32();
         this.res = this.dst & this.src;
         this.fN = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 16;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 25 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
   }

   private final void andiccr() {
      this.src = this.fetchOpByte(this.PC + 1);
      this.PC += 2;
      this.dst = this.buildSR();
      this.setStatusReg(this.dst & '\uff00' | this.src & this.dst & 255);
      this.clock_cycles += 20L;
   }

   private final void andisr() {
      this.clock_cycles += 20L;
      if(this.fSS == 0) {
         this.generateException(8);
      } else {
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.setStatusReg(this.buildSR() & this.src);
         if(this.fSS == 0) {
            int[] var1 = this.A;
            var1[7] ^= this.SSP;
            this.SSP ^= this.A[7];
            var1 = this.A;
            var1[7] ^= this.SSP;
         }
      }

   }

   private final void asl() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      int var2;
      byte var3;
      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 8) {
               var2 = this.src << var1 >>> 8 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src << var1 & 255;
               this.fN = this.res >>> 7 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res;
               this.fV = 0;
               var1 = Integer.MIN_VALUE >> var1 + 24 & 255;
               this.src &= var1;
               if(this.src != 0 && this.src != var1) {
                  this.fV = 1;
               }

               this.clock_cycles += 6L;
            } else {
               if(var1 == 256) {
                  this.fC = this.src & 1;
               } else {
                  this.fC = 0;
               }

               this.fX = this.fC;
               if(this.src == 0) {
                  var3 = 0;
               } else {
                  var3 = 1;
               }

               this.fV = var3;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res;
               this.fN = 0;
               this.fnZ = 0;
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 16) {
               var2 = this.src << var1 >>> 16 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src << var1 & '\uffff';
               this.fN = this.res >>> 15 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
               this.fV = 0;
               var1 = Integer.MIN_VALUE >> var1 + 16 & '\uffff';
               this.src &= var1;
               if(this.src != 0 && this.src != var1) {
                  this.fV = 1;
               }

               this.clock_cycles += 6L;
            } else {
               if(var1 == 65536) {
                  this.fC = this.src & 1;
               } else {
                  this.fC = 0;
               }

               this.fX = this.fC;
               if(this.src == 0) {
                  var3 = 0;
               } else {
                  var3 = 1;
               }

               this.fV = var3;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
               this.fN = 0;
               this.fnZ = 0;
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 32) {
               var2 = this.src >>> 32 - var1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src << var1;
               this.fN = this.res >>> 31 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.res;
               this.fV = 0;
               var1 = Integer.MIN_VALUE >> var1 + 0 & -1;
               this.src &= var1;
               if(this.src != 0 && this.src != var1) {
                  this.fV = 1;
               }

               this.clock_cycles += 8L;
            } else {
               if(var1 == 0) {
                  this.fC = this.src & 1;
               } else {
                  this.fC = 0;
               }

               this.fX = this.fC;
               if(this.src != 0) {
                  this.fV = 1;
               } else {
                  this.fV = 0;
               }

               this.res = 0;
               this.D[this.Di] = this.res;
               this.fN = 0;
               this.fnZ = 0;
               this.clock_cycles += 8L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 8L;
         }
      }

   }

   private final void aslm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.res = this.src << 1 & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      int var1 = this.src >>> 15 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fV = (this.src ^ this.res) >>> 15 & 1;
      this.fN = this.res >>> 15 & 1;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void asr() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      int var2;
      switch(this.sz) {
      case 0:
         this.src = (byte)(this.D[this.Di] & 255);
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 8) {
               this.fV = 0;
               var2 = this.src >> var1 - 1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src >> var1;
               this.fN = this.res >>> 7 & 1;
               this.fnZ = this.res & 255;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
               this.clock_cycles += 6L;
            } else if((this.src & 128) != 0) {
               this.fN = 1;
               this.fnZ = 1;
               this.fV = 0;
               this.fX = 1;
               this.fC = 1;
               this.res = 255;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
               this.clock_cycles += 6L;
            } else {
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.fX = 0;
               this.fC = 0;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 1:
         this.src = (short)this.D[this.Di];
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 16) {
               this.fV = 0;
               var2 = this.src >> var1 - 1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src >> var1;
               this.fN = this.res >>> 15 & 1;
               this.fnZ = this.res & '\uffff';
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
               this.clock_cycles += 6L;
            } else if((this.src & '耀') != 0) {
               this.fN = 1;
               this.fnZ = 1;
               this.fV = 0;
               this.fX = 1;
               this.fC = 1;
               this.res = '\uffff';
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
               this.clock_cycles += 6L;
            } else {
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.fX = 0;
               this.fC = 0;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 32) {
               this.fV = 0;
               var2 = this.src >> var1 - 1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src >> var1;
               this.fN = this.res >>> 31 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            } else if((this.src & Integer.MIN_VALUE) != 0) {
               this.fN = 1;
               this.fnZ = 1;
               this.fV = 0;
               this.fX = 1;
               this.fC = 1;
               this.res = -1;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            } else {
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.fX = 0;
               this.fC = 0;
               this.res = 0;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 8L;
         }
      }

   }

   private final void asrm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.res = (this.src >> 1 | this.src & '耀') & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      this.fV = 0;
      int var1 = this.src & 1;
      this.fX = var1;
      this.fC = var1;
      this.fN = this.res >>> 15 & 1;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void autovector(int var1) {
      this.tmpSR = this.buildSR();
      this.enterSupervisorMode();
      this.fTM = 0;
      this.intMask = var1 & 7;
      int[] var2 = this.A;
      var2[7] -= 4;
      this.writeLong(this.A[7], this.PC);
      var2 = this.A;
      var2[7] -= 2;
      this.writeWord(this.A[7], this.tmpSR & '\uffff');
      this.lPC = this.PC;
      this.PC = this.readLong(var1 + 24 << 2);
      this.clock_cycles += 44L;
   }

   private final void bcc() {
      if(this.fC == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bcc_i() {
      if(this.fC == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bchg() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      if(this.mode == 0) {
         this.dst = this.D[this.reg];
         if((this.OC & 448) == 320) {
            this.src = this.D[this.OC >> 9 & 7] & 31;
            this.clock_cycles += 8L;
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 31;
            this.PC += 2;
            this.clock_cycles += 12L;
         }

         this.D[this.reg] = this.dst ^ 1 << this.src;
      } else {
         if((this.OC & 448) == 320) {
            this.src = this.D[this.OC >> 9 & 7] & 7;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10);
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 7;
            this.PC += 2;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 15);
         }

         this.dst = this.getEA_OP_8();
         this.res = this.dst ^ 1 << this.src;
         this.writeEA_8(this.mode, this.reg, this.res & 255);
      }

      this.fnZ = this.dst >>> this.src & 1;
   }

   private final void bclr() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      if(this.mode == 0) {
         this.dst = this.D[this.reg];
         if((this.OC & 448) == 384) {
            this.src = this.D[this.OC >> 9 & 7] & 31;
            this.clock_cycles += 10L;
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 31;
            this.PC += 2;
            this.clock_cycles += 14L;
         }

         this.D[this.reg] = this.dst & ~(1 << this.src);
      } else {
         if((this.OC & 448) == 384) {
            this.src = this.D[this.OC >> 9 & 7] & 7;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10);
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 7;
            this.PC += 2;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 15);
         }

         this.dst = this.getEA_OP_8();
         this.res = this.dst & ~(1 << this.src);
         this.writeEA_8(this.mode, this.reg, this.res & 255);
      }

      this.fnZ = this.dst >>> this.src & 1;
   }

   private final void bcs() {
      if(this.fC != 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bcs_i() {
      if(this.fC != 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void beq() {
      if(this.fnZ == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void beq_i() {
      if(this.fnZ == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bge() {
      if(((this.fN ^ this.fV) & 1) == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bge_i() {
      if(((this.fN ^ this.fV) & 1) == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bgt() {
      if(this.fnZ != 0 && ((this.fN ^ this.fV) & 1) == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bgt_i() {
      if(this.fnZ != 0 && ((this.fN ^ this.fV) & 1) == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bhi() {
      if(this.fnZ != 0 && this.fC == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bhi_i() {
      if(this.fnZ != 0 && this.fC == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bkpt() {
      System.out.println("bkpt");
      this.illegal();
   }

   private final void ble() {
      if(this.fnZ != 0 && ((this.fN ^ this.fV) & 1) == 0) {
         this.clock_cycles += 9L;
      } else {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      }

   }

   private final void ble_i() {
      if(this.fnZ != 0 && ((this.fN ^ this.fV) & 1) == 0) {
         this.PC += 2;
         this.clock_cycles += 13L;
      } else {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      }

   }

   private final void bls() {
      if(this.fnZ != 0 && this.fC == 0) {
         this.clock_cycles += 9L;
      } else {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      }

   }

   private final void bls_i() {
      if(this.fnZ != 0 && this.fC == 0) {
         this.PC += 2;
         this.clock_cycles += 13L;
      } else {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      }

   }

   private final void blt() {
      if(((this.fN ^ this.fV) & 1) != 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void blt_i() {
      if(((this.fN ^ this.fV) & 1) != 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bmi() {
      if(this.fN != 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bmi_i() {
      if(this.fN != 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bne() {
      if(this.fnZ != 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bne_i() {
      if(this.fnZ != 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bpl() {
      if(this.fN == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bpl_i() {
      if(this.fN == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bra() {
      this.lPC = this.PC;
      this.PC += (byte)(this.OC & 255);
      this.clock_cycles += 12L;
   }

   private final void bra_i() {
      this.lPC = this.PC;
      this.PC += (short)this.fetchOpWord(this.PC);
      this.clock_cycles += 12L;
   }

   private final void bset() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      if(this.mode == 0) {
         this.dst = this.D[this.reg];
         if((this.OC & 448) == 448) {
            this.src = this.D[this.OC >> 9 & 7] & 31;
            this.clock_cycles += 8L;
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 31;
            this.PC += 2;
            this.clock_cycles += 12L;
         }

         this.D[this.reg] = this.dst | 1 << this.src;
      } else {
         if((this.OC & 448) == 448) {
            this.src = this.D[this.OC >> 9 & 7] & 7;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10);
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 7;
            this.PC += 2;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 15);
         }

         this.dst = this.getEA_OP_8();
         this.res = this.dst | 1 << this.src;
         this.writeEA_8(this.mode, this.reg, this.res & 255);
      }

      this.fnZ = this.dst >>> this.src & 1;
   }

   private final void bsr() {
      int[] var1 = this.A;
      var1[7] -= 4;
      this.writeLong(this.A[7], this.PC);
      this.lPC = this.PC;
      this.PC += (byte)(this.OC & 255);
      this.clock_cycles += 22L;
   }

   private final void bsr_i() {
      int[] var1 = this.A;
      var1[7] -= 4;
      this.writeLong(this.A[7], this.PC + 2);
      this.lPC = this.PC;
      this.PC += (short)this.fetchOpWord(this.PC);
      this.clock_cycles += 22L;
   }

   private final void btst() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      if(this.mode == 0) {
         this.dst = this.D[this.reg];
         if((this.OC & 448) == 256) {
            this.src = this.D[this.OC >> 9 & 7] & 31;
            this.clock_cycles += 6L;
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 31;
            this.PC += 2;
            this.clock_cycles += 10L;
         }
      } else {
         if((this.OC & 448) == 256) {
            this.src = this.D[this.OC >> 9 & 7] & 7;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         } else {
            this.src = this.fetchOpByte(this.PC + 1) & 7;
            this.PC += 2;
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10);
         }

         this.dst = this.readEA_8();
      }

      this.fnZ = this.dst >>> this.src & 1;
   }

   private final void bvc() {
      if(this.fV == 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bvc_i() {
      if(this.fV == 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void bvs() {
      if(this.fV != 0) {
         this.PC += (byte)(this.OC & 255);
         this.clock_cycles += 12L;
      } else {
         this.clock_cycles += 9L;
      }

   }

   private final void bvs_i() {
      if(this.fV != 0) {
         this.PC += (short)this.fetchOpWord(this.PC);
         this.clock_cycles += 12L;
      } else {
         this.PC += 2;
         this.clock_cycles += 13L;
      }

   }

   private final void checkInterrupts() {
      int var4 = this.intMask;
      int var5 = this.pendingInterrupt;
      int var2 = 0;

      for(int var1 = 7; var1 >= 0; --var1) {
         int var3 = 7 - var1;
         if((IRQ_LEVEL2[var3] & var5) != 0) {
            var2 = var3;
         }
      }

      if(var2 > var4 || var2 == 7) {
         this.pendingInterrupt = var5 & ~IRQ_LEVEL2[var2];
         this.autovector(var2);
         this.interruptPending = false;
         this.suspendedState = false;
         this.fire_IntAck();
      }

   }

   private final boolean check_condition(int var1) {
      boolean var2;
      switch(var1 & 15) {
      case 0:
         var2 = true;
         break;
      case 1:
         var2 = false;
         break;
      case 2:
         if(this.fnZ != 0 && this.fC == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 3:
         if(this.fnZ != 0 && this.fC == 0) {
            var2 = false;
         } else {
            var2 = true;
         }
         break;
      case 4:
         if(this.fC == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 5:
         if(this.fC != 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 6:
         if(this.fnZ != 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 7:
         if(this.fnZ == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 8:
         if(this.fV == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 9:
         if(this.fV != 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 10:
         if(this.fN == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 11:
         if(this.fN != 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 12:
         if(((this.fN ^ this.fV) & 1) == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 13:
         if(((this.fN ^ this.fV) & 1) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 14:
         if(this.fnZ != 0 && ((this.fN ^ this.fV) & 1) == 0) {
            var2 = true;
         } else {
            var2 = false;
         }
         break;
      case 15:
         if(this.fnZ != 0 && ((this.fN ^ this.fV) & 1) == 0) {
            var2 = false;
         } else {
            var2 = true;
         }
         break;
      default:
         var2 = false;
      }

      return var2;
   }

   private final void chk() {
      this.ea = this.readEA_16();
      this.dst = this.D[this.OC >> 9 & 7] & '\uffff';
      if(this.dst < 0) {
         this.fN = 1;
         this.condition = true;
      } else if(this.dst > this.ea) {
         this.fN = 0;
         this.condition = true;
      } else {
         this.condition = false;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10);
      }

      this.fnZ = this.dst;
      this.fC = 0;
      this.fV = 0;
      if(this.condition) {
         this.generateException(6);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 44);
      }

   }

   private final void clr() {
      this.mode = this.OC >> 3 & 7;
      this.sz = this.OC >> 6 & 3;
      int var1;
      long var2;
      if(this.sz == 0) {
         this.writeEA_8(this.mode, this.OC & 7, 0);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_dst] + 10;
         }

         this.clock_cycles = var2 + (long)var1;
      } else if(this.sz == 1) {
         this.writeEA_16(this.mode, this.OC & 7, 0);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_dst] + 10;
         }

         this.clock_cycles = var2 + (long)var1;
      } else {
         this.writeEA_32(this.mode, this.OC & 7, 0);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 6;
         } else {
            var1 = ea_times_l[this.ea_mode_dst] + 15;
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fnZ = 0;
      this.fC = 0;
      this.fV = 0;
      this.fN = 0;
   }

   private final void cmp_b() {
      this.dst = this.D[this.OC >> 9 & 7] & 255;
      this.src = this.readEA_8();
      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 7 & 1;
      this.fC = this.fnZ >>> 8 & 1;
      this.fnZ &= 255;
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 7 & 1;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
   }

   private final void cmp_l() {
      this.dst = this.D[this.OC >> 9 & 7];
      this.src = this.readEA_32();
      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 31 & 1;
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 31 & 1;
      this.fC = (this.src & this.fnZ & 1) + (this.src >>> 1) + (this.fnZ >>> 1) >> 31 & 1;
      this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 7);
   }

   private final void cmp_w() {
      this.dst = this.D[this.OC >> 9 & 7] & '\uffff';
      this.src = this.readEA_16();
      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 15 & 1;
      this.fC = this.fnZ >>> 16 & 1;
      this.fnZ &= '\uffff';
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 15 & 1;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
   }

   private final void cmpa() {
      this.dst = this.A[this.OC >> 9 & 7];
      switch(this.opmode) {
      case 3:
         this.src = (short)this.readEA_16();
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 7);
         break;
      case 7:
         this.src = this.readEA_32();
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 7);
         break;
      default:
         System.out.println("M68000  Illegal OP-Mode \'cmpa\'");
         return;
      }

      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 31 & 1;
      this.fC = (this.src & this.fnZ & 1) + (this.src >>> 1) + (this.fnZ >>> 1) >> 31 & 1;
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 31 & 1;
   }

   private final void cmpi_b() {
      this.src = this.fetchOpByte(this.PC + 1);
      this.PC += 2;
      this.dst = this.readEA_8();
      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 7 & 1;
      this.fC = this.fnZ >>> 8 & 1;
      this.fnZ &= 255;
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 7 & 1;
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 8;
      } else {
         var1 = ea_times_bw[this.ea_mode_src] + 10;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void cmpi_l() {
      this.src = this.fetchOpLong(this.PC);
      this.PC += 4;
      this.dst = this.readEA_32();
      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 31 & 1;
      this.fC = (this.src & this.fnZ & 1) + (this.src >>> 1) + (this.fnZ >>> 1) >> 31 & 1;
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 31 & 1;
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 14;
      } else {
         var1 = ea_times_l[this.ea_mode_src] + 15;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void cmpi_w() {
      this.src = this.fetchOpWord(this.PC);
      this.PC += 2;
      this.dst = this.readEA_16();
      this.fnZ = this.dst - this.src;
      this.fN = this.fnZ >>> 15 & 1;
      this.fC = this.fnZ >>> 16 & 1;
      this.fnZ &= '\uffff';
      this.fV = ((this.src ^ this.dst) & (this.fnZ ^ this.dst)) >>> 15 & 1;
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 8;
      } else {
         var1 = ea_times_bw[this.ea_mode_src] + 10;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void cmpm() {
      this.Ry = this.OC & 7;
      this.Rx = this.OC >> 9 & 7;
      int var1;
      int[] var4;
      switch(this.opmode) {
      case 4:
         byte var5;
         if(this.Ry == 7) {
            var5 = 2;
         } else {
            var5 = 1;
         }

         byte var2;
         if(this.Rx == 7) {
            var2 = 2;
         } else {
            var2 = 1;
         }

         this.src = this.readByte(this.A[this.Ry]) & 255;
         var4 = this.A;
         int var3 = this.Ry;
         var4[var3] += var5;
         this.dst = this.readByte(this.A[this.Rx]) & 255;
         var4 = this.A;
         var1 = this.Rx;
         var4[var1] += var2;
         this.res = this.dst - this.src;
         this.fN = this.res >>> 7 & 1;
         this.fC = this.res >>> 8 & 1;
         this.res &= 255;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
         this.clock_cycles += 12L;
         break;
      case 5:
         this.src = this.readWord(this.A[this.Ry]) & '\uffff';
         var4 = this.A;
         var1 = this.Ry;
         var4[var1] += 2;
         this.dst = this.readWord(this.A[this.Rx]) & '\uffff';
         var4 = this.A;
         var1 = this.Rx;
         var4[var1] += 2;
         this.res = this.dst - this.src;
         this.fN = this.res >>> 15 & 1;
         this.fC = this.res >>> 16 & 1;
         this.res &= '\uffff';
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
         this.clock_cycles += 12L;
         break;
      case 6:
         this.src = this.readLong(this.A[this.Ry]);
         var4 = this.A;
         var1 = this.Ry;
         var4[var1] += 4;
         this.dst = this.readLong(this.A[this.Rx]);
         var4 = this.A;
         var1 = this.Rx;
         var4[var1] += 4;
         this.res = this.dst - this.src;
         this.fN = this.res >>> 31 & 1;
         this.fC = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >> 31 & 1;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
         this.clock_cycles += 20L;
         break;
      default:
         System.out.println("M68000  Illegal OP-Mode cmpm");
         return;
      }

      this.fnZ = this.res;
   }

   private final void createOpTable() {
      for(int var1 = 0; var1 < 65536; ++var1) {
         this.OPTABLE[var1] = this.decodeOpcode(var1);
      }

   }

   private final void dbcc() {
      this.lPC = this.PC;
      if(this.check_condition(this.OC >> 8 & 15)) {
         this.PC += 2;
         this.clock_cycles += 14L;
      } else {
         this.Di = this.OC & 7;
         this.src = this.D[this.Di] & '\uffff';
         this.D[this.Di] = this.D[this.Di] & -65536 | this.src - 1 & '\uffff';
         if(this.src == 0) {
            this.PC += 2;
            this.clock_cycles += 17L;
         } else {
            this.offset = (short)this.fetchOpWord(this.PC);
            this.PC += this.offset;
            this.clock_cycles += 12L;
         }
      }

   }

   private final Opcode decodeOpcode(int var1) {
      Opcode var2;
      label359:
      switch(var1 >>> 12) {
      case 0:
         if((var1 & 56) == 8) {
            var2 = this.MOVEP;
            return var2;
         }

         switch(var1) {
         case 60:
            var2 = this.ORICCR;
            return var2;
         case 124:
            var2 = this.ORISR;
            return var2;
         case 572:
            var2 = this.ANDICCR;
            return var2;
         case 636:
            var2 = this.ANDISR;
            return var2;
         case 2620:
            var2 = this.EORICCR;
            return var2;
         case 2684:
            var2 = this.EORISR;
            return var2;
         default:
            switch(var1 >> 8 & 15) {
            case 0:
               var2 = this.ORI;
               return var2;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 9:
            case 11:
            default:
               switch(var1 >> 6 & 3) {
               case 0:
                  var2 = this.BTST;
                  return var2;
               case 1:
                  var2 = this.BCHG;
                  return var2;
               case 2:
                  var2 = this.BCLR;
                  return var2;
               case 3:
                  var2 = this.BSET;
                  return var2;
               default:
                  break label359;
               }
            case 2:
               var2 = this.ANDI;
               return var2;
            case 4:
               var2 = this.SUBI;
               return var2;
            case 6:
               var2 = this.ADDI;
               return var2;
            case 10:
               var2 = this.EORI;
               return var2;
            case 12:
               switch(var1 >> 6 & 3) {
               case 0:
                  var2 = this.CMPI_B;
                  return var2;
               case 1:
                  var2 = this.CMPI_W;
                  return var2;
               case 2:
                  var2 = this.CMPI_L;
                  return var2;
               default:
                  var2 = this.ILLEGAL;
                  return var2;
               }
            }
         }
      case 1:
         var2 = this.MOVE_B;
         return var2;
      case 2:
         if((var1 >> 6 & 7) != 1) {
            var2 = this.MOVE_L;
         } else {
            var2 = this.MOVEA_L;
         }

         return var2;
      case 3:
         if((var1 >> 6 & 7) != 1) {
            var2 = this.MOVE_W;
         } else {
            var2 = this.MOVEA_W;
         }

         return var2;
      case 4:
         switch(var1) {
         case 19196:
            var2 = this.ILLEGAL;
            return var2;
         case 20080:
            var2 = this.RESET;
            return var2;
         case 20081:
            var2 = this.NOP;
            return var2;
         case 20082:
            var2 = this.STOP;
            return var2;
         case 20083:
            var2 = this.RTE;
            return var2;
         case 20085:
            var2 = this.RTS;
            return var2;
         case 20086:
            var2 = this.TRAPV;
            return var2;
         case 20087:
            var2 = this.RTR;
            return var2;
         default:
            switch(65528 & var1) {
            case 18496:
               var2 = this.SWAP;
               return var2;
            case 18504:
               var2 = this.BKPT;
               return var2;
            case 20048:
               var2 = this.LINK;
               return var2;
            case 20056:
               var2 = this.UNLK;
               return var2;
            default:
               switch(65472 & var1) {
               case 16576:
                  var2 = this.MOVEFSR;
                  return var2;
               case 17088:
                  var2 = this.MOVEFCCR;
                  return var2;
               case 17600:
                  var2 = this.MOVETCCR;
                  return var2;
               case 18112:
                  var2 = this.MOVETSR;
                  return var2;
               case 18432:
                  var2 = this.NBCD;
                  return var2;
               case 18496:
                  var2 = this.PEA;
                  return var2;
               case 19136:
                  var2 = this.TAS;
                  return var2;
               case 20032:
                  if((var1 & 48) == 0) {
                     var2 = this.TRAP;
                  } else {
                     var2 = this.MOVEUSP;
                  }

                  return var2;
               case 20096:
                  var2 = this.JSR;
                  return var2;
               case 20160:
                  var2 = this.JMP;
                  return var2;
               default:
                  switch(65280 & var1) {
                  case 16384:
                     var2 = this.NEGX;
                     return var2;
                  case 16896:
                     var2 = this.CLR;
                     return var2;
                  case 17408:
                     var2 = this.NEG;
                     return var2;
                  case 17920:
                     var2 = this.NOT;
                     return var2;
                  case 18944:
                     switch(var1 >> 6 & 3) {
                     case 0:
                        var2 = this.TST_B;
                        return var2;
                     case 1:
                        var2 = this.TST_W;
                        return var2;
                     case 2:
                        var2 = this.TST_L;
                        return var2;
                     default:
                        var2 = this.ILLEGAL;
                        return var2;
                     }
                  default:
                     if((var1 & 448) == 448 && (var1 & 56) != 0) {
                        var2 = this.LEA;
                        return var2;
                     } else if((var1 & 896) == 128 && (var1 & 56) != 0) {
                        var2 = this.MOVEM;
                        return var2;
                     } else if((var1 & 448) != 256 && (var1 & 448) != 384) {
                        if((var1 & 3584) == 2048 && (var1 & 56) == 0) {
                           var2 = this.EXT;
                        } else {
                           var2 = this.ILLEGAL;
                        }

                        return var2;
                     } else {
                        var2 = this.CHK;
                        return var2;
                     }
                  }
               }
            }
         }
      case 5:
         if((var1 & 192) != 192) {
            if((var1 & 256) == 0) {
               switch(var1 >> 3 & 7) {
               case 0:
                  switch(var1 >> 6 & 3) {
                  case 0:
                     var2 = this.ADDQ_D_B;
                     return var2;
                  case 1:
                     var2 = this.ADDQ_D_W;
                     return var2;
                  case 2:
                     var2 = this.ADDQ_D_L;
                     return var2;
                  default:
                     var2 = this.ILLEGAL;
                     return var2;
                  }
               case 1:
                  var2 = this.ADDQ_A;
                  return var2;
               default:
                  switch(var1 >> 6 & 3) {
                  case 0:
                     var2 = this.ADDQ_M_B;
                     return var2;
                  case 1:
                     var2 = this.ADDQ_M_W;
                     return var2;
                  case 2:
                     var2 = this.ADDQ_M_L;
                     return var2;
                  default:
                     var2 = this.ILLEGAL;
                  }
               }
            } else {
               var2 = this.SUBQ;
            }

            return var2;
         } else {
            if((var1 & 56) != 8) {
               var2 = this.SCC;
            } else {
               var2 = this.DBCC;
            }

            return var2;
         }
      case 6:
         switch(var1 & 3840) {
         case 0:
            if((var1 & 255) == 0) {
               var2 = this.BRA_I;
            } else {
               var2 = this.BRA;
            }

            return var2;
         case 256:
            if((var1 & 255) == 0) {
               var2 = this.BSR_I;
            } else {
               var2 = this.BSR;
            }

            return var2;
         default:
            if((var1 & 255) == 0) {
               switch(var1 >>> 8 & 15) {
               case 2:
                  var2 = this.BHI_I;
                  return var2;
               case 3:
                  var2 = this.BLS_I;
                  return var2;
               case 4:
                  var2 = this.BCC_I;
                  return var2;
               case 5:
                  var2 = this.BCS_I;
                  return var2;
               case 6:
                  var2 = this.BNE_I;
                  return var2;
               case 7:
                  var2 = this.BEQ_I;
                  return var2;
               case 8:
                  var2 = this.BVC_I;
                  return var2;
               case 9:
                  var2 = this.BVS_I;
                  return var2;
               case 10:
                  var2 = this.BPL_I;
                  return var2;
               case 11:
                  var2 = this.BMI_I;
                  return var2;
               case 12:
                  var2 = this.BGE_I;
                  return var2;
               case 13:
                  var2 = this.BLT_I;
                  return var2;
               case 14:
                  var2 = this.BGT_I;
                  return var2;
               case 15:
                  var2 = this.BLE_I;
                  return var2;
               }
            } else {
               switch(var1 >>> 8 & 15) {
               case 2:
                  var2 = this.BHI;
                  return var2;
               case 3:
                  var2 = this.BLS;
                  return var2;
               case 4:
                  var2 = this.BCC;
                  return var2;
               case 5:
                  var2 = this.BCS;
                  return var2;
               case 6:
                  var2 = this.BNE;
                  return var2;
               case 7:
                  var2 = this.BEQ;
                  return var2;
               case 8:
                  var2 = this.BVC;
                  return var2;
               case 9:
                  var2 = this.BVS;
                  return var2;
               case 10:
                  var2 = this.BPL;
                  return var2;
               case 11:
                  var2 = this.BMI;
                  return var2;
               case 12:
                  var2 = this.BGE;
                  return var2;
               case 13:
                  var2 = this.BLT;
                  return var2;
               case 14:
                  var2 = this.BGT;
                  return var2;
               case 15:
                  var2 = this.BLE;
                  return var2;
               }
            }

            System.out.println("Error building opcode table");
            var2 = this.ILLEGAL;
            return var2;
         }
      case 7:
         var2 = this.MOVEQ;
         return var2;
      case 8:
         this.mode = var1 >> 3 & 7;
         switch(var1 & 448) {
         case 192:
            var2 = this.DIVU;
            return var2;
         case 448:
            var2 = this.DIVS;
            return var2;
         default:
            if(this.mode != 1 && (this.mode != 0 || (var1 & 256) != 256)) {
               var2 = this.OR;
            } else {
               var2 = this.SBCD;
            }

            return var2;
         }
      case 9:
         if((var1 & 256) == 256 && (var1 & 192) != 192 && (var1 & 48) == 0) {
            var2 = this.SUBX;
            return var2;
         } else {
            if((var1 & 448) != 192 && (var1 & 448) != 448) {
               var2 = this.SUB;
            } else {
               var2 = this.SUBA;
            }

            return var2;
         }
      case 10:
         var2 = this.ILLEGAL;
         return var2;
      case 11:
         this.opmode = var1 >> 6 & 7;
         this.mode = var1 >> 3 & 7;
         if(this.opmode != 0 && this.opmode != 1 && this.opmode != 2) {
            if(this.opmode != 3 && this.opmode != 7) {
               if(this.mode == 1) {
                  var2 = this.CMPM;
               } else {
                  var2 = this.EOR;
               }

               return var2;
            } else {
               var2 = this.CMPA;
               return var2;
            }
         } else if(this.opmode == 0) {
            var2 = this.CMP_B;
            return var2;
         } else if(this.opmode == 1) {
            var2 = this.CMP_W;
            return var2;
         } else {
            if(this.opmode == 2) {
               var2 = this.CMP_L;
            } else {
               var2 = this.ILLEGAL;
            }

            return var2;
         }
      case 12:
         if((var1 & 496) == 256) {
            var2 = this.ABCD;
            return var2;
         } else if((var1 & 448) == 448) {
            var2 = this.MULS;
            return var2;
         } else if((var1 & 448) == 192) {
            var2 = this.MULU;
            return var2;
         } else {
            if((var1 & 256) != 256 || (var1 & 56) != 0 && (var1 & 56) != 8) {
               var2 = this.AND;
            } else {
               var2 = this.EXG;
            }

            return var2;
         }
      case 13:
         if((var1 & 256) == 256 && (var1 & 192) != 192 && (var1 & 48) == 0) {
            var2 = this.ADDX;
            return var2;
         } else if((var1 & 448) != 192 && (var1 & 448) != 448) {
            switch(var1 & 448) {
            case 0:
               var2 = this.ADD_MR_B;
               return var2;
            case 64:
               var2 = this.ADD_MR_W;
               return var2;
            case 128:
               var2 = this.ADD_MR_L;
               return var2;
            case 256:
               var2 = this.ADD_RM_B;
               return var2;
            case 320:
               var2 = this.ADD_RM_W;
               return var2;
            case 384:
               var2 = this.ADD_RM_L;
               return var2;
            default:
               var2 = this.ILLEGAL;
               return var2;
            }
         } else {
            if((var1 & 448) == 192) {
               var2 = this.ADDA_W;
            } else {
               var2 = this.ADDA_L;
            }

            return var2;
         }
      case 14:
         this.sz = var1 >> 6 & 3;
         this.d = var1 >> 8 & 1;
         switch(this.sz) {
         case 3:
            switch(var1 >> 9 & 3) {
            case 0:
               if(this.d == 0) {
                  var2 = this.ASRM;
               } else {
                  var2 = this.ASLM;
               }

               return var2;
            case 1:
               if(this.d == 0) {
                  var2 = this.LSRM;
               } else {
                  var2 = this.LSLM;
               }

               return var2;
            case 2:
               if(this.d == 0) {
                  var2 = this.ROXRM;
               } else {
                  var2 = this.ROXLM;
               }

               return var2;
            case 3:
               if(this.d == 0) {
                  var2 = this.RORM;
               } else {
                  var2 = this.ROLM;
               }

               return var2;
            default:
               break label359;
            }
         default:
            switch(var1 >> 3 & 3) {
            case 0:
               if(this.d == 0) {
                  var2 = this.ASR;
               } else {
                  var2 = this.ASL;
               }

               return var2;
            case 1:
               if(this.d == 0) {
                  var2 = this.LSR;
               } else {
                  var2 = this.LSL;
               }

               return var2;
            case 2:
               if(this.d == 0) {
                  var2 = this.ROXR;
               } else {
                  var2 = this.ROXL;
               }

               return var2;
            case 3:
               if(this.d == 0) {
                  var2 = this.ROR;
               } else {
                  var2 = this.ROL;
               }

               return var2;
            default:
               break label359;
            }
         }
      case 15:
         var2 = this.ILLEGAL;
         return var2;
      }

      var2 = this.ILLEGAL;
      return var2;
   }

   private final void divs() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.src = (short)this.readEA_16();
      if(this.src == 0) {
         this.generateException(5);
      } else {
         this.dst = this.D[this.Di];
         if(this.dst == Integer.MIN_VALUE && this.src == -1) {
            this.fnZ = 0;
            this.fN = 0;
            this.fC = 0;
            this.fV = 0;
            this.D[this.Di] = 0;
            this.clock_cycles += 50L;
         } else {
            int var3 = this.dst / this.src;
            int var2 = this.dst;
            int var1 = this.src;
            if(var3 <= 32767 && var3 >= -32768) {
               var3 &= '\uffff';
               this.D[this.Di] = ('\uffff' & var2 % var1) << 16 | var3;
               this.fC = 0;
               this.fV = 0;
               this.fnZ = var3;
               this.fN = var3 >>> 15 & 1;
               this.clock_cycles += 158L;
            } else {
               this.fV = 1;
               this.clock_cycles += 80L;
            }
         }
      }

   }

   private final void divu() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.src = this.readEA_16();
      if(this.src == 0) {
         this.generateException(5);
         this.clock_cycles += 14L;
      } else {
         this.dst = this.D[this.Di];
         int var2 = (int)(((long)this.dst << 32 >>> 32) / ((long)this.src << 32 >>> 32));
         int var1 = (int)(((long)this.dst << 32 >>> 32) % ((long)this.src << 32 >>> 32));
         if((-65536 & var2) != 0) {
            this.fV = 1;
            this.clock_cycles += 74L;
         } else {
            var2 &= '\uffff';
            this.D[this.Di] = (var1 & '\uffff') << 16 | var2;
            this.fV = 0;
            this.fC = 0;
            this.fnZ = var2;
            this.fN = var2 >>> 15 & 1;
            this.clock_cycles += 140L;
         }
      }

   }

   private final void enterSupervisorMode() {
      if(this.fSS == 0) {
         int[] var1 = this.A;
         var1[7] ^= this.SSP;
         this.SSP ^= this.A[7];
         var1 = this.A;
         var1[7] ^= this.SSP;
      }

      this.fSS = 1;
   }

   private final void eor() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.src = this.D[this.Di];
      switch(this.opmode) {
      case 4:
         this.dst = this.getEA_OP_8();
         this.src &= 255;
         this.res = (this.src ^ this.dst) & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         if(this.mode == 0) {
            this.clock_cycles += 4L;
         } else {
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         }
         break;
      case 5:
         this.dst = this.getEA_OP_16();
         this.src &= '\uffff';
         this.res = (this.src ^ this.dst) & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         if(this.mode == 0) {
            this.clock_cycles += 4L;
         } else {
            this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         }
         break;
      case 6:
         this.dst = this.getEA_OP_32();
         this.res = this.src ^ this.dst;
         this.fN = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         if(this.mode == 0) {
            this.clock_cycles += 8L;
         } else {
            this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst]);
         }
      }

      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
   }

   private final void eori() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.src = this.fetchOpByte(this.PC + 1);
         this.PC += 2;
         this.dst = this.getEA_OP_8();
         this.res = (this.dst ^ this.src) & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.dst = this.getEA_OP_16();
         this.res = (this.dst ^ this.src) & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.src = this.fetchOpLong(this.PC);
         this.PC += 4;
         this.dst = this.getEA_OP_32();
         this.res = this.dst ^ this.src;
         this.msbr = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 16;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 25 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
   }

   private final void eoriccr() {
      this.src = this.fetchOpByte(this.PC + 1);
      this.PC += 2;
      this.dst = this.buildSR();
      this.setStatusReg(this.dst & '\uff00' | this.src ^ this.dst & 255);
      this.clock_cycles += 20L;
   }

   private final void eorisr() {
      this.clock_cycles += 20L;
      if(this.fSS == 0) {
         this.generateException(8);
      } else {
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.setStatusReg(this.buildSR() ^ this.src);
         if(this.fSS == 0) {
            int[] var1 = this.A;
            var1[7] ^= this.SSP;
            this.SSP ^= this.A[7];
            var1 = this.A;
            var1[7] ^= this.SSP;
         }
      }

   }

   private final void exg() {
      this.Rx = this.OC >> 9 & 7;
      this.Ry = this.OC & 7;
      switch(this.OC & 248) {
      case 64:
         this.dst = this.D[this.Ry];
         this.D[this.Ry] = this.D[this.Rx];
         this.D[this.Rx] = this.dst;
         break;
      case 72:
         this.dst = this.A[this.Ry];
         this.A[this.Ry] = this.A[this.Rx];
         this.A[this.Rx] = this.dst;
         break;
      case 136:
         this.dst = this.A[this.Ry];
         this.A[this.Ry] = this.D[this.Rx];
         this.D[this.Rx] = this.dst;
         break;
      default:
         System.out.println("Illegal EXG instruction.");
      }

      this.clock_cycles += 6L;
   }

   private final void ext() {
      this.Di = this.OC & 7;
      switch(this.OC & 448) {
      case 128:
         this.res = (byte)(this.D[this.Di] & 255) & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
         break;
      case 192:
         this.res = (short)this.D[this.Di];
         this.fN = this.res >>> 31 & 1;
         this.D[this.Di] = this.res;
         break;
      default:
         System.out.println("M68000 Illegal EXT Opcode! " + Integer.toBinaryString(this.OC) + " " + Integer.toHexString(this.OC));
      }

      this.clock_cycles += 4L;
      this.fC = 0;
      this.fV = 0;
      this.fnZ = this.res;
   }

   private int fetchOpByte(int var1) {
      return this.memory.read8opc(var1 & 16777215);
   }

   private int fetchOpLong(int var1) {
      int var2 = var1 & 16777215;
      CpuBoard var4 = this.memory;
      var1 = var2 + 1;
      int var3 = var4.read8opc(var2);
      var4 = this.memory;
      var2 = var1 + 1;
      return var3 << 24 | var4.read8opc(var1) << 16 | this.memory.read8opc(var2) << 8 | this.memory.read8opc(var2 + 1);
   }

   private int fetchOpWord(int var1) {
      var1 &= 16777215;
      return this.memory.read8opc(var1) << 8 | this.memory.read8opc(var1 + 1);
   }

   private final void fire_IntAck() {
      Iterator var1 = this.intAckListeners.iterator();

      while(var1.hasNext()) {
         ((IntAckListener)var1.next()).interruptAcknowledged(this.pendingInterrupt);
      }

   }

   private String formatHex(int var1, int var2) {
      String var3;
      for(var3 = Integer.toHexString(var1); var3.length() < var2; var3 = "0" + var3) {
         ;
      }

      return var3.toUpperCase();
   }

   private final int getEA(int var1, int var2) {
      this.ea = -1;
      switch(var1) {
      case 2:
         this.ea_mode_src = 2;
         var1 = this.A[var2];
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var2];
         this.ea += (short)this.fetchOpWord(this.PC);
         this.PC += 2;
         var1 = this.ea;
         break;
      case 6:
         this.ea_mode_src = 6;
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea = this.A[var2];
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var1 = this.ea + (short)this.D[this.Xidd >> 12 & 15];
            } else {
               var1 = this.ea + this.D[this.Xidd >> 12 & 15];
            }
         } else if((this.Xidd & 2048) == 0) {
            var1 = this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8];
         } else {
            var1 = this.ea + this.A[(this.Xidd >> 12 & 15) - 8];
         }
         break;
      case 7:
         switch(var2) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.ea;
            return var1;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            var1 = this.ea;
            return var1;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.ea;
            return var1;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.ea + (short)this.D[this.Xidd >> 12 & 15];
               } else {
                  var1 = this.ea + this.D[this.Xidd >> 12 & 15];
               }

               return var1;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8];
               } else {
                  var1 = this.ea + this.A[(this.Xidd >> 12 & 15) - 8];
               }

               return var1;
            }
         default:
            System.out.println("ILLEGAL EA MODE GET_EA");
         }
      case 3:
      case 4:
      default:
         System.out.println("get illegal ea mode: " + var1);
         var1 = this.ea;
      }

      return var1;
   }

   private final int getEA_OP_16() {
      int var1 = this.OC;
      int var2 = this.OC & 7;
      this.ea = 0;
      switch(var1 >>> 3 & 7) {
      case 0:
         this.ea_mode_src = 0;
         var1 = this.D[var2] & '\uffff';
         break;
      case 1:
         this.ea_mode_src = 1;
         var1 = this.A[var2] & '\uffff';
         break;
      case 2:
         this.ea_mode_src = 2;
         var1 = this.readWord(this.A[var2]);
         break;
      case 3:
         this.ea_mode_src = 3;
         this.ea = this.A[var2];
         var1 = this.readWord(this.ea);
         break;
      case 4:
         this.ea_mode_src = 4;
         this.ea = this.A[var2] - 2;
         var1 = this.readWord(this.ea);
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var2];
         this.ea += (short)this.fetchOpWord(this.PC);
         var1 = this.readWord(this.ea);
         break;
      case 6:
         this.ea_mode_src = 6;
         this.ea = this.A[var2];
         this.Xidd = this.fetchOpWord(this.PC);
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var1 = this.readWord(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
            } else {
               var1 = this.readWord(this.ea + this.D[this.Xidd >>> 12 & 15]);
            }
         } else if((this.Xidd & 2048) == 0) {
            var1 = this.readWord(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
         } else {
            var1 = this.readWord(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
         }
         break;
      case 7:
         switch(var2) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            var1 = this.readWord(this.ea);
            return var1;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            var1 = this.readWord(this.ea);
            return var1;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            var1 = this.readWord(this.ea);
            return var1;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readWord(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
               } else {
                  var1 = this.readWord(this.ea + this.D[this.Xidd >>> 12 & 15]);
               }

               return var1;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readWord(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
               } else {
                  var1 = this.readWord(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
               }

               return var1;
            }
         case 4:
            this.ea_mode_src = 11;
            this.ea = this.fetchOpWord(this.PC);
            var1 = this.ea;
            return var1;
         }
      default:
         System.out.println("ILLEGAL EA MODE !!!");
         var1 = this.ea;
      }

      return var1;
   }

   private final int getEA_OP_32() {
      int var1 = this.OC;
      int var2 = this.OC & 7;
      this.ea = 0;
      switch(var1 >>> 3 & 7) {
      case 0:
         this.ea_mode_src = 0;
         var1 = this.D[var2];
         break;
      case 1:
         this.ea_mode_src = 1;
         var1 = this.A[var2];
         break;
      case 2:
         this.ea_mode_src = 2;
         var1 = this.readLong(this.A[var2]);
         break;
      case 3:
         this.ea_mode_src = 3;
         this.ea = this.A[var2];
         var1 = this.readLong(this.ea);
         break;
      case 4:
         this.ea_mode_src = 4;
         this.ea = this.A[var2] - 4;
         var1 = this.readLong(this.ea);
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var2];
         this.ea += (short)this.fetchOpWord(this.PC);
         var1 = this.readLong(this.ea);
         break;
      case 6:
         this.ea_mode_src = 6;
         this.ea = this.A[var2];
         this.Xidd = this.fetchOpWord(this.PC);
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var1 = this.readLong(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
            } else {
               var1 = this.readLong(this.ea + this.D[this.Xidd >>> 12 & 15]);
            }
         } else if((this.Xidd & 2048) == 0) {
            var1 = this.readLong(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
         } else {
            var1 = this.readLong(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
         }
         break;
      case 7:
         switch(var2) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            var1 = this.readLong(this.ea);
            return var1;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            var1 = this.readLong(this.ea);
            return var1;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            var1 = this.readLong(this.ea);
            return var1;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readLong(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
               } else {
                  var1 = this.readLong(this.ea + this.D[this.Xidd >>> 12 & 15]);
               }

               return var1;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readLong(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
               } else {
                  var1 = this.readLong(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
               }

               return var1;
            }
         case 4:
            this.ea_mode_src = 11;
            this.ea = this.fetchOpLong(this.PC);
            var1 = this.ea;
            return var1;
         }
      default:
         System.out.println("ILLEGAL EA MODE !!!");
         var1 = this.ea;
      }

      return var1;
   }

   private final int getEA_OP_8() {
      int var2 = this.OC;
      int var1 = this.OC & 7;
      this.ea = 0;
      switch(var2 >>> 3 & 7) {
      case 0:
         this.ea_mode_src = 0;
         var1 = this.D[var1] & 255;
         break;
      case 1:
         this.ea_mode_src = 1;
         System.out.println("READ BYTE EA OPERAND FROM ADDRESS REGISTER!");
         var1 = this.A[var1] & 255;
         break;
      case 2:
         this.ea_mode_src = 2;
         var1 = this.readByte(this.A[var1]);
         break;
      case 3:
         this.ea_mode_src = 3;
         this.ea = this.A[var1];
         var1 = this.readByte(this.ea);
         break;
      case 4:
         this.ea_mode_src = 4;
         var2 = this.A[var1];
         byte var3;
         if(var1 == 7) {
            var3 = 2;
         } else {
            var3 = 1;
         }

         this.ea = var2 - var3;
         var1 = this.readByte(this.ea);
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var1];
         this.ea += (short)this.fetchOpWord(this.PC);
         var1 = this.readByte(this.ea);
         break;
      case 6:
         this.ea_mode_src = 6;
         this.ea = this.A[var1];
         this.Xidd = this.fetchOpWord(this.PC);
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var1 = this.readByte(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
            } else {
               var1 = this.readByte(this.ea + this.D[this.Xidd >>> 12 & 15]);
            }
         } else if((this.Xidd & 2048) == 0) {
            var1 = this.readByte(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
         } else {
            var1 = this.readByte(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
         }
         break;
      case 7:
         switch(var1) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            var1 = this.readByte(this.ea);
            return var1;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            var1 = this.readByte(this.ea);
            return var1;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            var1 = this.readByte(this.ea);
            return var1;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readByte(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
               } else {
                  var1 = this.readByte(this.ea + this.D[this.Xidd >>> 12 & 15]);
               }

               return var1;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readByte(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
               } else {
                  var1 = this.readByte(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
               }

               return var1;
            }
         case 4:
            this.ea_mode_src = 11;
            this.ea = this.fetchOpByte(this.PC + 1);
            var1 = this.ea;
            return var1;
         }
      default:
         System.out.println("ILLEGAL EA MODE !!!");
         var1 = this.ea;
      }

      return var1;
   }

   private final void illegal() {
      System.out.println(this.tag + " illegal() " + Integer.toHexString(this.OC) + " at " + Integer.toHexString(this.PC - 2));
      if(this.OC == 19196) {
         this.PC -= 2;
         this.generateException(4);
         this.clock_cycles += 34L;
      } else if((this.OC & '\uf000') == '\uf000') {
         this.PC -= 2;
         this.generateException(11);
         this.clock_cycles += 34L;
      } else if((this.OC & '\uf000') == 'ꀀ') {
         this.PC -= 2;
         this.generateException(10);
         this.clock_cycles += 34L;
      } else {
         this.nop();
      }

   }

   private final void illegalOC() {
      if(!this.error) {
         System.out.println("*M68000 ::: unknown OC @ $" + Integer.toHexString(this.PC - 2) + " | OC: " + Integer.toHexString(this.OC) + " last PC: " + Integer.toHexString(this.lPC_));
         this.error = true;
      }

   }

   private final void jmp() {
      this.lPC = this.PC;
      this.PC = this.getEA(this.OC >> 3 & 7, this.OC & 7);
      this.clock_cycles += (long)ea_times_jmp[this.ea_mode_src];
   }

   private final void jsr() {
      this.lPC = this.PC;
      this.ea = this.getEA(this.OC >> 3 & 7, this.OC & 7);
      int[] var1 = this.A;
      var1[7] -= 4;
      this.writeLong(this.A[7], this.PC);
      this.PC = this.ea;
      this.clock_cycles += (long)ea_times_jsr[this.ea_mode_src];
   }

   private final void lea() {
      this.A[this.OC >> 9 & 7] = this.getEA(this.OC >> 3 & 7, this.OC & 7);
      this.clock_cycles += (long)ea_times_lea[this.ea_mode_src];
   }

   private final void link() {
      this.offset = (short)this.fetchOpWord(this.PC);
      this.PC += 2;
      this.Ai = this.OC & 7;
      if(this.Ai == 7) {
         System.out.println("A7 LINK!!!");
      }

      int[] var1 = this.A;
      var1[7] -= 4;
      this.writeLong(this.A[7], this.A[this.Ai]);
      this.A[this.Ai] = this.A[7];
      var1 = this.A;
      var1[7] += this.offset;
      this.clock_cycles += 16L;
   }

   private final void lsl() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      int var2;
      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 <= 8) {
               var2 = this.src << var1 >>> 8 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src << var1 & 255;
               this.fV = 0;
               this.fN = this.res >>> 7 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res;
               this.clock_cycles += 6L;
            } else {
               this.fC = 0;
               this.fX = 0;
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res;
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 <= 16) {
               var2 = this.src << var1 >>> 16 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src << var1 & '\uffff';
               this.fV = 0;
               this.fN = this.res >>> 15 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
               this.clock_cycles += 6L;
            } else {
               this.fC = 0;
               this.fX = 0;
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 32) {
               var2 = this.src >>> 32 - var1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src << var1;
               this.fV = 0;
               this.fN = this.res >>> 31 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            } else {
               if(var1 == 32) {
                  this.fC = this.src & 1;
               } else {
                  this.fC = 0;
               }

               this.fX = this.fC;
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.res = 0;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 8L;
         }
      }

   }

   private final void lslm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.res = this.src << 1 & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      this.fN = this.res >>> 15 & 1;
      this.fV = 0;
      int var1 = this.src >>> 15 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void lsr() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      int var2;
      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 <= 8) {
               this.fV = 0;
               this.fN = 0;
               var2 = this.src >>> var1 - 1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src >>> var1;
               this.fnZ = this.res & 255;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
               this.clock_cycles += 6L;
            } else {
               this.fC = 0;
               this.fX = 0;
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 <= 16) {
               this.fV = 0;
               this.fN = 0;
               var2 = this.src >>> var1 - 1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src >>> var1;
               this.fnZ = this.res & '\uffff';
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
               this.clock_cycles += 6L;
            } else {
               this.fC = 0;
               this.fX = 0;
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.res = 0;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
               this.clock_cycles += 6L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            if(var1 < 32) {
               this.fV = 0;
               this.fN = 0;
               var2 = this.src >>> var1 - 1 & 1;
               this.fC = var2;
               this.fX = var2;
               this.res = this.src >>> var1;
               this.fnZ = this.res;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            } else {
               if(var1 == 32) {
                  this.fC = this.src >>> 31 & 1;
               } else {
                  this.fC = 0;
               }

               this.fX = this.fC;
               this.fN = 0;
               this.fnZ = 0;
               this.fV = 0;
               this.res = 0;
               this.D[this.Di] = this.res;
               this.clock_cycles += 8L;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 8L;
         }
      }

   }

   private final void lsrm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.res = this.src >>> 1 & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      int var1 = this.src & 1;
      this.fX = var1;
      this.fC = var1;
      this.fV = 0;
      this.fN = 0;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void move_b() {
      this.fnZ = this.readEA_8();
      this.writeEA_8(this.OC >> 6 & 7, this.OC >> 9 & 7, this.fnZ);
      this.fV = 0;
      this.fC = 0;
      this.fN = this.fnZ >>> 7 & 1;
      this.clock_cycles += (long)times_move_bw[this.ea_mode_src][this.ea_mode_dst];
   }

   private final void move_l() {
      this.fnZ = this.readEA_32();
      this.mode = this.OC >> 6 & 7;
      if(this.mode == 4) {
         this.reg = this.OC >> 9 & 7;
         int[] var2 = this.A;
         int var1 = this.reg;
         var2[var1] -= 2;
         this.writeWord(this.A[this.reg], this.fnZ & '\uffff');
         var2 = this.A;
         var1 = this.reg;
         var2[var1] -= 2;
         this.writeWord(this.A[this.reg], this.fnZ >>> 16 & '\uffff');
         this.ea_mode_dst = 4;
      } else {
         this.writeEA_32(this.mode, this.OC >> 9 & 7, this.fnZ);
      }

      this.fV = 0;
      this.fC = 0;
      this.fN = this.fnZ >>> 31 & 1;
      this.clock_cycles += (long)times_move_l[this.ea_mode_src][this.ea_mode_dst];
   }

   private final void move_w() {
      this.fnZ = this.readEA_16();
      this.writeEA_16(this.OC >> 6 & 7, this.OC >> 9 & 7, this.fnZ);
      this.fV = 0;
      this.fC = 0;
      this.fN = this.fnZ >>> 15 & 1;
      this.clock_cycles += (long)times_move_bw[this.ea_mode_src][this.ea_mode_dst];
   }

   private final void movea_l() {
      this.A[this.OC >> 9 & 7] = this.readEA_32();
      this.clock_cycles += (long)times_move_l[this.ea_mode_src][1];
   }

   private final void movea_w() {
      this.A[this.OC >> 9 & 7] = (short)this.readEA_16();
      this.clock_cycles += (long)times_move_bw[this.ea_mode_src][1];
   }

   private final void movefccr() {
      this.mode = this.OC >> 3 & 7;
      this.writeEA_16(this.mode, this.OC & 7, this.buildSR() & 255);
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 6;
      } else {
         var1 = ea_times_bw[this.ea_mode_dst] + 8;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void movefsr() {
      this.mode = this.OC >> 3 & 7;
      this.writeEA_16(this.mode, this.OC & 7, this.buildSR());
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 6;
      } else {
         var1 = ea_times_bw[this.ea_mode_dst] + 8;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void movem() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.mask = this.fetchOpWord(this.PC);
      this.PC += 2;
      this.num = 0;
      switch(this.OC & 64) {
      case 0:
         if((this.OC & 1024) == 0) {
            if(this.mode == 4) {
               this.dst = this.A[this.reg];

               for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.dst -= 2;
                     this.writeWord(this.dst, this.A[7 - this.loopc] & '\uffff');
                     ++this.num;
                  }
               }

               for(this.loopc = 8; this.loopc < 16; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.dst -= 2;
                     this.writeWord(this.dst, this.D[15 - this.loopc] & '\uffff');
                     ++this.num;
                  }
               }

               this.A[this.reg] = this.dst;
            } else {
               this.dst = this.getEA(this.mode, this.reg);

               for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.writeWord(this.dst, this.D[this.loopc] & '\uffff');
                     ++this.num;
                     this.dst += 2;
                  }
               }

               for(this.loopc = 8; this.loopc < 16; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.writeWord(this.dst, this.A[this.loopc - 8] & '\uffff');
                     ++this.num;
                     this.dst += 2;
                  }
               }
            }

            this.clock_cycles += (long)(ea_times_movem_rm[this.ea_mode_dst] + (this.num << 2));
         } else {
            if(this.mode == 3) {
               this.dst = this.A[this.reg];
            } else {
               this.dst = this.getEA(this.mode, this.reg);
            }

            for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
               if((this.mask >>> this.loopc & 1) == 1) {
                  this.D[this.loopc] = (short)this.readWord(this.dst);
                  ++this.num;
                  this.dst += 2;
               }
            }

            for(this.loopc = 8; this.loopc < 16; ++this.loopc) {
               if((this.mask >>> this.loopc & 1) == 1) {
                  this.A[this.loopc - 8] = (short)this.readWord(this.dst);
                  ++this.num;
                  this.dst += 2;
               }
            }

            if(this.mode == 3) {
               this.A[this.reg] = this.dst;
            }

            this.clock_cycles += (long)(ea_times_movem_mr[this.ea_mode_src] + (this.num << 2));
         }
         break;
      default:
         if((this.OC & 1024) == 0) {
            if(this.mode == 4) {
               this.dst = this.A[this.reg];

               for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.dst -= 4;
                     this.writeLong(this.dst, this.A[7 - this.loopc]);
                     ++this.num;
                  }
               }

               for(this.loopc = 8; this.loopc < 16; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.dst -= 4;
                     this.writeLong(this.dst, this.D[15 - this.loopc]);
                     ++this.num;
                  }
               }

               this.A[this.reg] = this.dst;
            } else {
               this.dst = this.getEA(this.mode, this.reg);

               for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.writeLong(this.dst, this.D[this.loopc]);
                     ++this.num;
                     this.dst += 4;
                  }
               }

               for(this.loopc = 8; this.loopc < 16; ++this.loopc) {
                  if((this.mask >>> this.loopc & 1) == 1) {
                     this.writeLong(this.dst, this.A[this.loopc - 8]);
                     ++this.num;
                     this.dst += 4;
                  }
               }
            }

            this.clock_cycles += (long)(ea_times_movem_rm[this.ea_mode_dst] + (this.num << 3));
         } else {
            if(this.mode == 3) {
               this.dst = this.A[this.reg];
            } else {
               this.dst = this.getEA(this.mode, this.reg);
            }

            for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
               if((this.mask >>> this.loopc & 1) == 1) {
                  this.D[this.loopc] = this.readLong(this.dst);
                  ++this.num;
                  this.dst += 4;
               }
            }

            for(this.loopc = 8; this.loopc < 16; ++this.loopc) {
               if((this.mask >>> this.loopc & 1) == 1) {
                  this.A[this.loopc - 8] = this.readLong(this.dst);
                  ++this.num;
                  this.dst += 4;
               }
            }

            if(this.mode == 3) {
               this.A[this.reg] = this.dst;
            }

            this.clock_cycles += (long)(ea_times_movem_mr[this.ea_mode_src] + (this.num << 3));
         }
      }

   }

   private final void movep() {
      this.Di = this.OC >> 9 & 7;
      this.offset = (short)this.fetchOpWord(this.PC);
      this.PC += 2;
      this.ea = this.A[this.OC & 7] + this.offset;
      switch(this.OC & 448) {
      case 256:
         this.src = this.readByte(this.ea);
         this.dst = this.readByte(this.ea + 2);
         this.D[this.Di] = this.D[this.Di] & -65536 | (this.src & 255) << 8 | this.dst & 255;
         this.clock_cycles += 16L;
         break;
      case 320:
         this.src = this.readByte(this.ea) & 255;
         this.dst = this.readByte(this.ea + 2) & 255;
         this.res = (this.src & 255) << 24 | (this.dst & 255) << 16;
         this.src = this.readByte(this.ea + 4) & 255;
         this.dst = this.readByte(this.ea + 6) & 255;
         this.D[this.Di] = this.res & -65536 | (this.src & 255) << 8 | this.dst;
         this.clock_cycles += 24L;
         break;
      case 384:
         this.writeByte(this.ea, this.D[this.Di] >> 8 & 255);
         this.writeByte(this.ea + 2, this.D[this.Di] & 255);
         this.clock_cycles += 16L;
         break;
      case 448:
         this.writeByte(this.ea, this.D[this.Di] >> 24 & 255);
         this.writeByte(this.ea + 2, this.D[this.Di] >> 16 & 255);
         this.writeByte(this.ea + 4, this.D[this.Di] >> 8 & 255);
         this.writeByte(this.ea + 6, this.D[this.Di] & 255);
         this.clock_cycles += 24L;
      }

   }

   private final void moveq() {
      int[] var3 = this.D;
      int var1 = this.OC;
      byte var2 = (byte)(this.OC & 255);
      this.fnZ = var2;
      var3[var1 >> 9 & 7] = var2;
      this.fN = this.fnZ >>> 31 & 1;
      this.fV = 0;
      this.fC = 0;
      this.clock_cycles += 4L;
   }

   private final void movetccr() {
      this.mode = this.OC >> 3 & 7;
      this.src = this.readEA_16();
      this.setStatusReg(this.buildSR() & '\uff00' | this.src & 255);
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 12;
      } else {
         var1 = ea_times_bw[this.ea_mode_src] + 12;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void movetsr() {
      this.mode = this.OC >> 3 & 7;
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 12;
      } else {
         var1 = ea_times_bw[this.ea_mode_src] + 12;
      }

      this.clock_cycles = var2 + (long)var1;
      if(this.fSS == 0) {
         this.generateException(8);
      } else {
         this.setStatusReg(this.readEA_16() & '\uffff');
         if(this.fSS == 0) {
            int[] var4 = this.A;
            var4[7] ^= this.SSP;
            this.SSP ^= this.A[7];
            var4 = this.A;
            var4[7] ^= this.SSP;
         }
      }

   }

   private final void moveusp() {
      this.clock_cycles += 4L;
      if(this.fSS == 0) {
         this.generateException(8);
      } else {
         switch(this.OC & 8) {
         case 0:
            this.SSP = this.A[this.OC & 7];
            break;
         case 8:
            this.A[this.OC & 7] = this.SSP;
         }
      }

   }

   private final void muls() {
      this.src = (short)this.readEA_16();
      this.Di = this.OC >> 9 & 7;
      this.dst = (short)this.D[this.Di];
      this.res = this.src * this.dst;
      this.D[this.Di] = this.res;
      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
      this.fN = this.res >>> 31 & 1;
      this.clock_cycles += 70L;
   }

   private final void mulu() {
      this.src = this.readEA_16();
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di] & '\uffff';
      this.res = (int)(((long)this.src << 32 >>> 32) * ((long)this.dst << 32 >>> 32));
      this.D[this.Di] = this.res;
      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
      this.fN = this.res >>> 31 & 1;
      this.clock_cycles += 70L;
   }

   private final void nbcd() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.res = this.getEA_OP_8();
      this.res = 154 - this.res - (this.fX & 1) & 255;
      if(this.res != 154) {
         if((this.res & 15) == 10) {
            this.res = (this.res & 240) + 16;
         }

         this.res &= 255;
         this.writeEA_8(this.mode, this.reg, this.res);
         this.fnZ |= this.res;
         this.fX = 1;
         this.fC = 1;
      } else {
         this.fX = 0;
         this.fC = 0;
      }

      this.fN = this.res >>> 7 & 1;
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode > 1) {
         var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
      } else {
         var1 = 6;
      }

      this.clock_cycles = var2 + (long)var1;
   }

   private final void neg() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.dst = this.getEA_OP_8();
         this.res = -this.dst;
         this.fN = this.res >>> 7 & 1;
         var1 = this.res >>> 8 & 1;
         this.fX = var1;
         this.fC = var1;
         this.res &= 255;
         this.fV = (this.res & this.dst) >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.dst = this.getEA_OP_16();
         this.res = -this.dst;
         this.fN = this.res >>> 15 & 1;
         var1 = this.res >>> 16 & 1;
         this.fX = var1;
         this.fC = var1;
         this.res &= '\uffff';
         this.fV = (this.res & this.dst) >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.dst = this.getEA_OP_32();
         this.res = -this.dst;
         this.fN = this.res >>> 31 & 1;
         this.fV = (this.res & this.dst) >>> 31 & 1;
         var1 = (this.dst & this.res & 1) + (this.dst >>> 1) + (this.res >>> 1) >> 31 & 1;
         this.fX = var1;
         this.fC = var1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 6;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fnZ = this.res;
   }

   private final void negx() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.cc = this.fX;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.dst = this.getEA_OP_8();
         this.res = -this.dst - this.cc;
         this.fN = this.res >>> 7 & 1;
         var1 = this.res >>> 8 & 1;
         this.fX = var1;
         this.fC = var1;
         this.res &= 255;
         this.fV = (this.res & this.dst) >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.dst = this.getEA_OP_16();
         this.res = -this.dst - this.cc;
         this.fN = this.res >>> 15 & 1;
         var1 = this.res >>> 16 & 1;
         this.fX = var1;
         this.fC = var1;
         this.res &= '\uffff';
         this.fV = (this.res & this.dst) >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.dst = this.getEA_OP_32();
         this.res = -this.dst - this.cc;
         this.fN = this.res >>> 31 & 1;
         var1 = (this.dst & this.res & 1) + (this.dst >>> 1) + (this.res >>> 1) >> 31 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fV = (this.res & this.dst) >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 6;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fnZ |= this.res;
   }

   private final void nop() {
      this.clock_cycles += 4L;
   }

   private final void not() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.dst = this.getEA_OP_8();
         this.res = ~this.dst & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.dst = this.getEA_OP_16();
         this.res = ~this.dst & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 4;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.dst = this.getEA_OP_32();
         this.res = ~this.dst;
         this.fN = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 6;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fC = 0;
      this.fV = 0;
      this.fnZ = this.res;
   }

   private final void or() {
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di];
      switch(this.OC & 448) {
      case 0:
         this.src = this.readEA_8();
         this.dst &= 255;
         this.res = (this.src | this.dst) & 255;
         this.fN = this.res >>> 7 & 1;
         this.D[this.Di] = this.D[this.Di] & -256 | this.res;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
            this.clock_cycles += 2L;
         }
         break;
      case 64:
         this.src = this.readEA_16();
         this.dst &= '\uffff';
         this.res = (this.src | this.dst) & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
            this.clock_cycles += 2L;
         }
         break;
      case 128:
         this.src = this.readEA_32();
         this.res = this.src | this.dst;
         this.fN = this.res >>> 31 & 1;
         this.D[this.Di] = this.res;
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 7);
         if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
            this.clock_cycles += 2L;
         }
         break;
      case 256:
         this.src = this.dst & 255;
         this.dst = this.getEA_OP_8();
         this.res = (this.src | this.dst) & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         break;
      case 320:
         this.src = this.dst & '\uffff';
         this.dst = this.getEA_OP_16();
         this.res = (this.src | this.dst) & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         break;
      case 384:
         this.src = this.dst;
         this.dst = this.getEA_OP_32();
         this.res = this.src | this.dst;
         this.fN = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst]);
      }

      this.fC = 0;
      this.fV = 0;
      this.fnZ = this.res;
   }

   private final void ori() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.src = this.fetchOpByte(this.PC + 1);
         this.PC += 2;
         this.dst = this.getEA_OP_8();
         this.res = (this.dst | this.src) & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.dst = this.getEA_OP_16();
         this.res = (this.dst | this.src) & '\uffff';
         this.fN = this.res >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.src = this.fetchOpLong(this.PC);
         this.PC += 4;
         this.dst = this.getEA_OP_32();
         this.res = this.dst | this.src;
         this.fN = this.res >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 16;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 25 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fV = 0;
      this.fC = 0;
      this.fnZ = this.res;
   }

   private final void oriccr() {
      this.src = this.fetchOpByte(this.PC + 1);
      this.PC += 2;
      this.dst = this.buildSR();
      this.setStatusReg(this.dst & '\uff00' | this.src | this.dst & 255);
      this.clock_cycles += 20L;
   }

   private final void orisr() {
      this.clock_cycles += 20L;
      if(this.fSS == 0) {
         this.generateException(8);
      } else {
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.setStatusReg(this.buildSR() | this.src);
         if(this.fSS == 0) {
            System.out.println("Impossible ;)");
            int[] var1 = this.A;
            var1[7] ^= this.SSP;
            this.SSP ^= this.A[7];
            var1 = this.A;
            var1[7] ^= this.SSP;
         }
      }

   }

   private final void pea() {
      this.ea = this.getEA(this.OC >> 3 & 7, this.OC & 7);
      int[] var1 = this.A;
      var1[7] -= 4;
      this.writeLong(this.A[7], this.ea);
      this.clock_cycles += (long)ea_times_pea[this.ea_mode_src];
   }

   private final void processInterrupts() {
      if(this.pendingInterrupt > this.intMask || this.pendingInterrupt == 7) {
         this.autovector(this.pendingInterrupt);
         this.interruptPending = false;
         this.suspendedState = false;
         this.fire_IntAck();
      }

   }

   private int readByte(int var1) {
      return this.memory.read8(var1 & 16777215);
   }

   private final int readEA_16() {
      int var2 = this.OC;
      int var1 = this.OC & 7;
      this.ea = 0;
      int[] var3;
      switch(var2 >>> 3 & 7) {
      case 0:
         this.ea_mode_src = 0;
         var1 = this.D[var1] & '\uffff';
         break;
      case 1:
         this.ea_mode_src = 1;
         var1 = this.A[var1] & '\uffff';
         break;
      case 2:
         this.ea_mode_src = 2;
         var1 = this.readWord(this.A[var1]);
         break;
      case 3:
         this.ea_mode_src = 3;
         this.ea = this.A[var1];
         var3 = this.A;
         var3[var1] += 2;
         var1 = this.readWord(this.ea);
         break;
      case 4:
         this.ea_mode_src = 4;
         var3 = this.A;
         var3[var1] -= 2;
         var1 = this.readWord(this.A[var1]);
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var1];
         this.ea += (short)this.fetchOpWord(this.PC);
         this.PC += 2;
         var1 = this.readWord(this.ea);
         break;
      case 6:
         this.ea_mode_src = 6;
         this.ea = this.A[var1];
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var1 = this.readWord(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
            } else {
               var1 = this.readWord(this.ea + this.D[this.Xidd >>> 12 & 15]);
            }
         } else if((this.Xidd & 2048) == 0) {
            var1 = this.readWord(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
         } else {
            var1 = this.readWord(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
         }
         break;
      case 7:
         switch(var1) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.readWord(this.ea);
            return var1;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            var1 = this.readWord(this.ea);
            return var1;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.readWord(this.ea);
            return var1;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readWord(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
               } else {
                  var1 = this.readWord(this.ea + this.D[this.Xidd >>> 12 & 15]);
               }

               return var1;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readWord(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
               } else {
                  var1 = this.readWord(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
               }

               return var1;
            }
         case 4:
            this.ea_mode_src = 11;
            this.ea = this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.ea;
            return var1;
         }
      default:
         System.out.println("ILLEGAL EA MODE !!!");
         var1 = this.ea;
      }

      return var1;
   }

   private final int readEA_32() {
      int var2 = this.OC;
      int var1 = this.OC & 7;
      this.ea = 0;
      int[] var3;
      switch(var2 >>> 3 & 7) {
      case 0:
         this.ea_mode_src = 0;
         var1 = this.D[var1];
         break;
      case 1:
         this.ea_mode_src = 1;
         var1 = this.A[var1];
         break;
      case 2:
         this.ea_mode_src = 2;
         var1 = this.readLong(this.A[var1]);
         break;
      case 3:
         this.ea_mode_src = 3;
         this.ea = this.A[var1];
         var3 = this.A;
         var3[var1] += 4;
         var1 = this.readLong(this.ea);
         break;
      case 4:
         this.ea_mode_src = 4;
         var3 = this.A;
         var3[var1] -= 4;
         var1 = this.readLong(this.A[var1]);
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var1];
         this.ea += (short)this.fetchOpWord(this.PC);
         this.PC += 2;
         var1 = this.readLong(this.ea);
         break;
      case 6:
         this.ea_mode_src = 6;
         this.ea = this.A[var1];
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var1 = this.readLong(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
            } else {
               var1 = this.readLong(this.ea + this.D[this.Xidd >>> 12 & 15]);
            }
         } else if((this.Xidd & 2048) == 0) {
            var1 = this.readLong(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
         } else {
            var1 = this.readLong(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
         }
         break;
      case 7:
         switch(var1) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.readLong(this.ea);
            return var1;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            var1 = this.readLong(this.ea);
            return var1;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var1 = this.readLong(this.ea);
            return var1;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readLong(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
               } else {
                  var1 = this.readLong(this.ea + this.D[this.Xidd >>> 12 & 15]);
               }

               return var1;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var1 = this.readLong(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
               } else {
                  var1 = this.readLong(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
               }

               return var1;
            }
         case 4:
            this.ea_mode_src = 11;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            var1 = this.ea;
            return var1;
         }
      default:
         System.out.println("ILLEGAL EA MODE !!!");
         var1 = this.ea;
      }

      return var1;
   }

   private final int readEA_8() {
      byte var2 = 1;
      byte var1 = 1;
      int var4 = this.OC;
      int var3 = this.OC & 7;
      this.ea = 0;
      int[] var5;
      int var6;
      switch(var4 >>> 3 & 7) {
      case 0:
         this.ea_mode_src = 0;
         var6 = this.D[var3] & 255;
         break;
      case 1:
         this.ea_mode_src = 1;
         System.out.println("read byte from address register");
         var6 = this.A[var3] & 255;
         break;
      case 2:
         this.ea_mode_src = 2;
         var6 = this.readByte(this.A[var3]);
         break;
      case 3:
         this.ea_mode_src = 3;
         this.ea = this.A[var3];
         var5 = this.A;
         int var7 = var5[var3];
         if(var3 == 7) {
            var1 = 2;
         }

         var5[var3] = var7 + var1;
         var6 = this.readByte(this.ea);
         break;
      case 4:
         this.ea_mode_src = 4;
         var5 = this.A;
         var4 = var5[var3];
         var1 = var2;
         if(var3 == 7) {
            var1 = 2;
         }

         var5[var3] = var4 - var1;
         var6 = this.readByte(this.A[var3]);
         break;
      case 5:
         this.ea_mode_src = 5;
         this.ea = this.A[var3];
         this.ea += (short)this.readWord(this.PC);
         this.PC += 2;
         var6 = this.readByte(this.ea);
         break;
      case 6:
         this.ea_mode_src = 6;
         this.ea = this.A[var3];
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               var6 = this.readByte(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
            } else {
               var6 = this.readByte(this.ea + this.D[this.Xidd >>> 12 & 15]);
            }
         } else if((this.Xidd & 2048) == 0) {
            var6 = this.readByte(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
         } else {
            var6 = this.readByte(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
         }
         break;
      case 7:
         switch(var3) {
         case 0:
            this.ea_mode_src = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var6 = this.readByte(this.ea);
            return var6;
         case 1:
            this.ea_mode_src = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            var6 = this.readByte(this.ea);
            return var6;
         case 2:
            this.ea_mode_src = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            var6 = this.readByte(this.ea);
            return var6;
         case 3:
            this.ea_mode_src = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  var6 = this.readByte(this.ea + (short)this.D[this.Xidd >>> 12 & 15]);
               } else {
                  var6 = this.readByte(this.ea + this.D[this.Xidd >>> 12 & 15]);
               }

               return var6;
            } else {
               if((this.Xidd & 2048) == 0) {
                  var6 = this.readByte(this.ea + (short)this.A[(this.Xidd >>> 12 & 15) - 8]);
               } else {
                  var6 = this.readByte(this.ea + this.A[(this.Xidd >>> 12 & 15) - 8]);
               }

               return var6;
            }
         case 4:
            this.ea_mode_src = 11;
            this.ea = this.fetchOpByte(this.PC + 1);
            this.PC += 2;
            var6 = this.ea;
            return var6;
         }
      default:
         System.out.println("ILLEGAL EA MODE !!!");
         var6 = this.ea;
      }

      return var6;
   }

   private int readLong(int var1) {
      int var2 = var1 & 16777215;
      CpuBoard var4 = this.memory;
      var1 = var2 + 1;
      int var3 = var4.read8(var2);
      var4 = this.memory;
      var2 = var1 + 1;
      return var3 << 24 | var4.read8(var1) << 16 | this.memory.read8(var2) << 8 | this.memory.read8(var2 + 1);
   }

   private int readWord(int var1) {
      var1 &= 16777215;
      return this.memory.read8(var1) << 8 | this.memory.read8(var1 + 1);
   }

   private final void rol() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         if(var1 != 0) {
            this.clock_cycles += (long)((var1 << 1) + 6);
            var1 &= 7;
            if(var1 != 0) {
               this.fC = this.src << var1 >>> 8 & 1;
               this.res = (this.src << var1 | this.src >>> 8 - var1) & 255;
               this.fV = 0;
               this.fN = this.res >>> 7 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.D[this.Di] & -256 | this.res;
            } else {
               this.fV = 0;
               this.fC = this.src & 1;
               this.fN = this.src >>> 7 & 1;
               this.fnZ = this.src;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         if(var1 != 0) {
            this.clock_cycles += (long)((var1 << 1) + 6);
            var1 &= 15;
            if(var1 != 0) {
               this.fC = this.src << var1 >>> 16 & 1;
               this.res = (this.src << var1 | this.src >>> 16 - var1) & '\uffff';
               this.fV = 0;
               this.fN = this.res >>> 15 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
            } else {
               this.fV = 0;
               this.fC = this.src & 1;
               this.fN = this.src >>> 15 & 1;
               this.fnZ = this.src;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 6L;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         if(var1 != 0) {
            this.clock_cycles += (long)((var1 << 1) + 8);
            var1 &= 31;
            if(var1 != 0) {
               this.fC = this.src >>> 32 - var1 & 1;
               this.res = this.src << var1 | this.src >>> 32 - var1;
               this.fV = 0;
               this.fN = this.res >>> 31 & 1;
               this.fnZ = this.res;
               this.D[this.Di] = this.res;
            } else {
               this.fV = 0;
               this.fC = this.src & 1;
               this.fN = this.src >>> 31 & 1;
               this.fnZ = this.src;
            }
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
            this.clock_cycles += 8L;
         }
      }

   }

   private final void rolm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.fC = this.src >>> 15 & 1;
      this.res = (this.src << 1 | this.fC) & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      this.fN = this.res >>> 15 & 1;
      this.fV = 0;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void ror() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >>> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         this.clock_cycles += 6L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 &= 7;
            this.fC = this.src >>> var1 - 1 & 1;
            this.res = this.src >>> var1 | this.src << 8 - var1;
            this.fV = 0;
            this.fN = this.res >>> 7 & 1;
            this.fnZ = this.res & 255;
            this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         this.clock_cycles += 6L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 &= 15;
            this.fC = this.src >>> var1 - 1 & 1;
            this.res = this.src >>> var1 | this.src << 16 - var1;
            this.fV = 0;
            this.fN = this.res >>> 15 & 1;
            this.fnZ = this.res & '\uffff';
            this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         this.clock_cycles += 8L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 &= 31;
            this.fC = this.src >>> var1 - 1 & 1;
            this.res = this.src >>> var1 | this.src << 32 - var1;
            this.fV = 0;
            this.fN = this.res >>> 31 & 1;
            this.fnZ = this.res;
            this.D[this.Di] = this.res;
         } else {
            this.fV = 0;
            this.fC = 0;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
         }
      }

   }

   private final void rorm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      int var1 = this.src & 1;
      this.fC = var1;
      this.fN = var1;
      this.res = (this.src >>> 1 | this.fC << 15) & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      this.fV = 0;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void roxl() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >>> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         this.clock_cycles += 6L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 %= 9;
            if(this.fX != 0) {
               this.src |= 256;
            }

            this.res = this.src << var1 | this.src >>> 9 - var1;
            var1 = this.res >>> 8 & 1;
            this.fC = var1;
            this.fX = var1;
            this.fV = 0;
            this.fN = this.res >>> 7 & 1;
            this.fnZ = this.res & 255;
            this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
         } else {
            this.fV = 0;
            this.fC = this.fX;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         this.clock_cycles += 6L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 %= 17;
            if(this.fX != 0) {
               this.src |= 65536;
            }

            this.res = this.src << var1 | this.src >>> 17 - var1;
            var1 = this.res >>> 16 & 1;
            this.fC = var1;
            this.fX = var1;
            this.fV = 0;
            this.fN = this.res >>> 15 & 1;
            this.fnZ = this.res & '\uffff';
            this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
         } else {
            this.fV = 0;
            this.fC = this.fX;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         this.clock_cycles += 8L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 %= 33;
            if(var1 != 0) {
               if(var1 == 1) {
                  this.res = this.src << 1 | this.fX & 1;
               } else {
                  this.res = this.src << var1 | this.src >>> 33 - var1 | (this.fX & 1) << var1 - 1;
               }

               this.fX = this.src >>> 32 - var1 & 1;
            } else {
               this.res = this.src;
            }

            this.fC = this.fX;
            this.fV = 0;
            this.fN = this.res >>> 31 & 1;
            this.fnZ = this.res;
            this.D[this.Di] = this.res;
         } else {
            this.fV = 0;
            this.fC = this.fX;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
         }
      }

   }

   private final void roxlm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.res = (this.src << 1 | this.fX) & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      int var1 = this.src >>> 15 & 1;
      this.fX = var1;
      this.fC = var1;
      this.fN = this.res >>> 15 & 1;
      this.fV = 0;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void roxr() {
      this.Di = this.OC & 7;
      int var1;
      if((this.OC & 32) == 32) {
         var1 = this.D[this.OC >>> 9 & 7] & 63;
      } else {
         var1 = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      }

      switch(this.sz) {
      case 0:
         this.src = this.D[this.Di] & 255;
         this.clock_cycles += 6L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 %= 9;
            if(this.fX != 0) {
               this.src |= 256;
            }

            this.res = this.src >>> var1 | this.src << 9 - var1;
            var1 = this.res >>> 8 & 1;
            this.fC = var1;
            this.fX = var1;
            this.fV = 0;
            this.fN = this.res >>> 7 & 1;
            this.fnZ = this.res & 255;
            this.D[this.Di] = this.D[this.Di] & -256 | this.res & 255;
         } else {
            this.fV = 0;
            this.fC = this.fX;
            this.fN = this.src >>> 7 & 1;
            this.fnZ = this.src;
         }
         break;
      case 1:
         this.src = this.D[this.Di] & '\uffff';
         this.clock_cycles += 6L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 %= 17;
            if(this.fX != 0) {
               this.src |= 65536;
            }

            this.res = this.src >>> var1 | this.src << 17 - var1;
            var1 = this.res >>> 16 & 1;
            this.fC = var1;
            this.fX = var1;
            this.fV = 0;
            this.fN = this.res >>> 15 & 1;
            this.fnZ = this.res & '\uffff';
            this.D[this.Di] = this.D[this.Di] & -65536 | this.res & '\uffff';
         } else {
            this.fV = 0;
            this.fC = this.fX;
            this.fN = this.src >>> 15 & 1;
            this.fnZ = this.src;
         }
         break;
      case 2:
         this.src = this.D[this.Di];
         this.clock_cycles += 8L;
         if(var1 != 0) {
            this.clock_cycles += (long)(var1 << 1);
            var1 %= 33;
            if(var1 != 0) {
               if(var1 == 1) {
                  this.res = this.src >>> 1 | (this.fX & 1) << 31;
               } else {
                  this.res = this.src >>> var1 | this.src << 33 - var1 | (this.fX & 1) << 32 - var1;
               }

               this.fX = this.src >>> var1 - 1 & 1;
            } else {
               this.res = this.src;
            }

            this.fC = this.fX;
            this.fV = 0;
            this.fN = this.res >>> 31 & 1;
            this.fnZ = this.res;
            this.D[this.Di] = this.res;
         } else {
            this.fV = 0;
            this.fC = this.fX;
            this.fN = this.src >>> 31 & 1;
            this.fnZ = this.src;
         }
      }

   }

   private final void roxrm() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = this.getEA_OP_16();
      this.fN = this.fX;
      this.res = (this.fX << 15 | this.src >>> 1) & '\uffff';
      this.writeEA_16(this.mode, this.reg, this.res);
      int var1 = this.src & 1;
      this.fX = var1;
      this.fC = var1;
      this.fV = 0;
      this.fnZ = this.res;
      this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
   }

   private final void rte() {
      this.clock_cycles += 20L;
      if(this.fSS == 0) {
         this.generateException(8);
      } else {
         this.src = this.readWord(this.A[7]) & '\uffff';
         int[] var1 = this.A;
         var1[7] += 2;
         this.setStatusReg(this.src);
         this.PC = this.readLong(this.A[7]);
         var1 = this.A;
         var1[7] += 4;
         if(this.fSS == 0) {
            var1 = this.A;
            var1[7] ^= this.SSP;
            this.SSP ^= this.A[7];
            var1 = this.A;
            var1[7] ^= this.SSP;
         }
      }

   }

   private final void rtr() {
      this.lPC = this.PC;
      this.src = this.readWord(this.A[7]);
      int[] var1 = this.A;
      var1[7] += 2;
      this.setStatusReg(this.buildSR() & '\uff00' | this.src & 255);
      this.PC = this.readLong(this.A[7]);
      var1 = this.A;
      var1[7] += 4;
      this.clock_cycles += 20L;
   }

   private final void rts() {
      this.lPC = this.PC;
      this.PC = this.readLong(this.A[7]);
      int[] var1 = this.A;
      var1[7] += 4;
      this.clock_cycles += 16L;
   }

   private final void sbcd() {
      this.Ry = this.OC & 7;
      this.Rx = this.OC >> 9 & 7;
      this.mode = this.OC >> 3 & 1;
      int var1;
      switch(this.mode) {
      case 0:
         this.src = this.D[this.Ry] & 255;
         this.dst = this.D[this.Rx] & 255;
         var1 = this.dst;
         var1 = this.src;
         var1 = this.fX;
         this.res = (this.dst & 15) - (this.src & 15) - (this.fX & 1);
         if((this.res & 255) > 9) {
            this.res -= 6;
         }

         this.res += (this.dst & 240) - (this.src & 240);
         if((this.res & '\uffff') > 153) {
            this.res += 160;
            this.fC = 1;
            this.fX = 1;
         } else {
            this.fC = 0;
            this.fX = 0;
         }

         this.fnZ |= this.res & 255;
         this.fN = this.res >>> 7 & 1;
         this.D[this.Rx] = this.D[this.Rx] & -256 | this.res & 255;
         this.clock_cycles += 6L;
         break;
      case 1:
         int[] var2;
         if(this.Ry == 7) {
            var2 = this.A;
            var1 = this.Ry;
            var2[var1] -= 2;
         } else {
            var2 = this.A;
            var1 = this.Ry;
            --var2[var1];
         }

         this.src = this.readByte(this.A[this.Ry]);
         if(this.Rx == 7) {
            var2 = this.A;
            var1 = this.Rx;
            var2[var1] -= 2;
         } else {
            var2 = this.A;
            var1 = this.Rx;
            --var2[var1];
         }

         this.dst = this.readByte(this.A[this.Rx]);
         var1 = this.dst;
         var1 = this.src;
         var1 = this.fX;
         this.res = (this.dst & 15) - (this.src & 15) - (this.fX & 1);
         if((this.res & 255) > 9) {
            this.res -= 6;
         }

         this.res += (this.dst & 240) - (this.src & 240);
         if((this.res & '\uffff') > 153) {
            this.res += 160;
            this.fC = 1;
            this.fX = 1;
         } else {
            this.fC = 0;
            this.fX = 0;
         }

         this.fnZ |= this.res & 255;
         this.fN = this.res >>> 7 & 1;
         this.writeByte(this.A[this.Rx], this.res & 255);
         this.clock_cycles += 18L;
      }

   }

   private final void scc() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.condition = this.check_condition(this.OC >> 8 & 15);
      short var1;
      if(this.condition) {
         var1 = 255;
      } else {
         var1 = 0;
      }

      this.res = var1;
      if(this.mode == 0) {
         this.D[this.reg] = this.D[this.reg] & -256 | this.res;
         long var2 = this.clock_cycles;
         byte var4;
         if(this.condition) {
            var4 = 7;
         } else {
            var4 = 5;
         }

         this.clock_cycles = var2 + (long)var4;
      } else {
         this.writeEA_8(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_dst] + 10);
      }

   }

   private final void stop() {
      boolean var1;
      if(this.fSS != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.condition = var1;
      this.setStatusReg(this.fetchOpWord(this.PC));
      this.PC += 2;
      if(this.fSS == 0 && this.condition) {
         this.generateException(8);
      } else if(this.fTM != 0) {
         this.generateException(9);
      } else {
         this.haltCPU();
      }

      this.clock_cycles += 4L;
   }

   private final void sub() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.Di = this.OC >> 9 & 7;
      this.dst = this.D[this.Di];
      int var1;
      switch(this.OC & 448) {
      case 0:
         this.src = this.readEA_8();
         this.dst &= 255;
         this.res = this.dst - this.src;
         var1 = this.res >>> 8 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 7 & 1;
         this.res &= 255;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
         this.D[this.Di] = this.D[this.Di] & -256 | this.res;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         break;
      case 64:
         this.src = this.readEA_16();
         this.dst &= '\uffff';
         this.res = this.dst - this.src;
         var1 = this.res >>> 16 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 15 & 1;
         this.res &= '\uffff';
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
         this.D[this.Di] = this.D[this.Di] & -65536 | this.res;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 5);
         break;
      case 128:
         this.src = this.readEA_32();
         this.res = this.dst - this.src;
         var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 31 & 1;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
         this.D[this.Di] = this.res;
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 7);
         if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
            this.clock_cycles += 2L;
         }
         break;
      case 256:
         this.src = this.dst & 255;
         this.dst = this.getEA_OP_8();
         this.res = this.dst - this.src;
         var1 = this.res >>> 8 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 7 & 1;
         this.res &= 255;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         break;
      case 320:
         this.src = this.dst & '\uffff';
         this.dst = this.getEA_OP_16();
         this.res = this.dst - this.src;
         var1 = this.res >>> 16 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 15 & 1;
         this.res &= '\uffff';
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res & '\uffff');
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst]);
         break;
      case 384:
         this.src = this.dst;
         this.dst = this.getEA_OP_32();
         this.res = this.dst - this.src;
         var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 31 & 1;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
         this.writeEA_32(this.mode, this.reg, this.res);
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 15 + ea_times_l[this.ea_mode_dst]);
      }

      this.fnZ = this.res;
   }

   private final void suba() {
      int var1;
      int[] var2;
      switch(this.OC & 448) {
      case 192:
         this.dst = (short)this.readEA_16();
         var2 = this.A;
         var1 = this.OC >> 9 & 7;
         var2[var1] -= this.dst;
         this.clock_cycles += (long)(ea_times_bw[this.ea_mode_src] + 9);
         break;
      case 448:
         this.dst = this.readEA_32();
         var2 = this.A;
         var1 = this.OC >> 9 & 7;
         var2[var1] -= this.dst;
         this.clock_cycles += (long)(ea_times_l[this.ea_mode_src] + 7);
         if(this.ea_mode_src == 0 || this.ea_mode_src == 1 || this.ea_mode_src == 11) {
            this.clock_cycles += 2L;
         }
      }

   }

   private final void subi() {
      this.sz = this.OC >> 6 & 3;
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      int var1;
      long var2;
      switch(this.sz) {
      case 0:
         this.src = this.fetchOpByte(this.PC + 1);
         this.PC += 2;
         this.dst = this.getEA_OP_8();
         this.res = this.dst - this.src;
         var1 = this.res >>> 8 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 7 & 1;
         this.res &= 255;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
         this.writeEA_8(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 1:
         this.src = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.dst = this.getEA_OP_16();
         this.res = this.dst - this.src;
         var1 = this.res >>> 16 & 1;
         this.fX = var1;
         this.fC = var1;
         this.fN = this.res >>> 15 & 1;
         this.res &= '\uffff';
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
         this.writeEA_16(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 8;
         } else {
            var1 = ea_times_bw[this.ea_mode_src] + 15 + ea_times_bw[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
         break;
      case 2:
         this.src = this.fetchOpLong(this.PC);
         this.PC += 4;
         this.dst = this.getEA_OP_32();
         this.res = this.dst - this.src;
         this.fN = this.res >>> 31 & 1;
         this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
         var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
         this.fX = var1;
         this.fC = var1;
         this.writeEA_32(this.mode, this.reg, this.res);
         var2 = this.clock_cycles;
         if(this.mode == 0) {
            var1 = 16;
         } else {
            var1 = ea_times_l[this.ea_mode_src] + 25 + ea_times_l[this.ea_mode_dst];
         }

         this.clock_cycles = var2 + (long)var1;
      }

      this.fnZ = this.res;
   }

   private final void subq() {
      this.mode = this.OC >> 3 & 7;
      this.reg = this.OC & 7;
      this.src = ((this.OC >>> 9 & 7) - 1 & 7) + 1;
      this.sz = this.OC >> 6 & 3;
      int var1;
      switch(this.mode) {
      case 0:
         switch(this.sz) {
         case 0:
            this.dst = this.D[this.reg] & 255;
            this.res = this.dst - this.src;
            var1 = this.res >>> 8 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 7 & 1;
            this.res &= 255;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
            this.fnZ = this.res;
            this.D[this.reg] = this.D[this.reg] & -256 | this.res;
            this.clock_cycles += 5L;
            return;
         case 1:
            this.dst = this.D[this.reg] & '\uffff';
            this.res = this.dst - this.src;
            var1 = this.res >>> 16 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 15 & 1;
            this.res &= '\uffff';
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
            this.fnZ = this.res;
            this.D[this.reg] = this.D[this.reg] & -65536 | this.res;
            this.clock_cycles += 5L;
            return;
         case 2:
            this.dst = this.D[this.reg];
            this.res = this.dst - this.src;
            var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
            this.fN = this.res >>> 31 & 1;
            this.fnZ = this.res;
            this.D[this.reg] = this.res;
            this.clock_cycles += 9L;
            return;
         default:
            return;
         }
      case 1:
         int[] var2 = this.A;
         var1 = this.reg;
         var2[var1] -= this.src;
         break;
      default:
         switch(this.sz) {
         case 0:
            this.dst = this.getEA_OP_8();
            this.res = this.dst - this.src;
            var1 = this.res >>> 8 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 7 & 1;
            this.res &= 255;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
            this.fnZ = this.res;
            this.writeEA_8(this.mode, this.reg, this.res);
            this.clock_cycles += (long)((ea_times_bw[this.ea_mode_src] << 1) + 10);
            break;
         case 1:
            this.dst = this.getEA_OP_16();
            this.res = this.dst - this.src;
            var1 = this.res >>> 16 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 15 & 1;
            this.res &= '\uffff';
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
            this.fnZ = this.res;
            this.writeEA_16(this.mode, this.reg, this.res);
            this.clock_cycles += (long)((ea_times_bw[this.ea_mode_src] << 1) + 10);
            break;
         case 2:
            this.dst = this.getEA_OP_32();
            this.res = this.dst - this.src;
            var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
            this.fN = this.res >>> 31 & 1;
            this.fnZ = this.res;
            this.writeEA_32(this.mode, this.reg, this.res);
            this.clock_cycles += (long)((ea_times_l[this.ea_mode_src] << 1) + 15 + ea_times_l[this.ea_mode_src]);
         }
      }

   }

   private final void subx() {
      this.sz = this.OC >> 6 & 3;
      this.Rx = this.OC >> 9 & 7;
      this.Ry = this.OC & 7;
      this.cc = this.fX;
      int var1;
      label32:
      switch(this.OC & 8) {
      case 0:
         this.src = this.D[this.Ry];
         this.dst = this.D[this.Rx];
         switch(this.sz) {
         case 0:
            this.dst &= 255;
            this.src &= 255;
            this.res = this.dst - this.src - this.cc;
            var1 = this.res >>> 8 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 7 & 1;
            this.res &= 255;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
            this.D[this.Rx] = this.D[this.Rx] & -256 | this.res;
            this.clock_cycles += 5L;
            break label32;
         case 1:
            this.dst &= '\uffff';
            this.src &= '\uffff';
            this.res = this.dst - this.src - this.cc;
            var1 = this.res >>> 16 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 15 & 1;
            this.res &= '\uffff';
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
            this.D[this.Rx] = this.D[this.Rx] & -65536 | this.res;
            this.clock_cycles += 5L;
            break label32;
         case 2:
            this.res = this.dst - this.src - this.cc;
            var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
            this.fN = this.res >>> 31 & 1;
            this.D[this.Rx] = this.res;
            this.clock_cycles += 9L;
         default:
            break label32;
         }
      case 8:
         int[] var4;
         switch(this.sz) {
         case 0:
            byte var5;
            if(this.Ry == 7) {
               var5 = 2;
            } else {
               var5 = 1;
            }

            byte var2;
            if(this.Rx == 7) {
               var2 = 2;
            } else {
               var2 = 1;
            }

            var4 = this.A;
            int var3 = this.Ry;
            var4[var3] -= var5;
            this.src = this.readByte(this.A[this.Ry]) & 255;
            var4 = this.A;
            var1 = this.Rx;
            var4[var1] -= var2;
            this.dst = this.readByte(this.A[this.Rx]) & 255;
            this.res = this.dst - this.src - this.cc;
            var1 = this.res >>> 8 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 7 & 1;
            this.res &= 255;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 7 & 1;
            this.writeByte(this.A[this.Rx], this.res);
            this.clock_cycles += 22L;
            break;
         case 1:
            var4 = this.A;
            var1 = this.Ry;
            var4[var1] -= 2;
            this.src = this.readWord(this.A[this.Ry]) & '\uffff';
            var4 = this.A;
            var1 = this.Rx;
            var4[var1] -= 2;
            this.dst = this.readWord(this.A[this.Rx]) & '\uffff';
            this.res = this.dst - this.src - this.cc;
            var1 = this.res >>> 16 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fN = this.res >>> 15 & 1;
            this.res &= '\uffff';
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 15 & 1;
            this.writeWord(this.A[this.Rx], this.res);
            this.clock_cycles += 22L;
            break;
         case 2:
            var4 = this.A;
            var1 = this.Ry;
            var4[var1] -= 4;
            this.src = this.readLong(this.A[this.Ry]);
            var4 = this.A;
            var1 = this.Rx;
            var4[var1] -= 4;
            this.dst = this.readLong(this.A[this.Rx]);
            this.res = this.dst - this.src - this.cc;
            var1 = (this.src & this.res & 1) + (this.src >>> 1) + (this.res >>> 1) >>> 31 & 1;
            this.fX = var1;
            this.fC = var1;
            this.fV = ((this.src ^ this.dst) & (this.res ^ this.dst)) >>> 31 & 1;
            this.fN = this.res >>> 31 & 1;
            this.writeLong(this.A[this.Rx], this.res);
            this.clock_cycles += 37L;
         }
      }

      this.fnZ |= this.res;
   }

   private final void swap() {
      this.Di = this.OC & 7;
      this.D[this.Di] = (this.D[this.Di] & -65536) >>> 16 & '\uffff' | (this.D[this.Di] & '\uffff') << 16 & -65536;
      this.fC = 0;
      this.fV = 0;
      this.fN = this.D[this.Di] >>> 31 & 1;
      this.fnZ = this.D[this.Di];
      this.clock_cycles += 4L;
   }

   private final void tas() {
      this.mode = this.OC >> 3 & 7;
      boolean var1;
      if(this.mode != 0 && this.mode != 1) {
         var1 = false;
      } else {
         var1 = true;
      }

      int var3 = this.PC;
      int var2 = this.readEA_8();
      this.dst = var2;
      this.fnZ = var2;
      this.fN = this.dst >>> 7 & 1;
      this.fV = 0;
      this.fC = 0;
      if(!this.ignore_TAS_WriteBack || var1) {
         this.PC = var3;
         this.dst |= 128;
         this.writeEA_8(this.mode, this.OC & 7, this.dst);
      }

      long var4 = this.clock_cycles;
      int var6;
      if(this.mode == 0) {
         var6 = 4;
      } else {
         var6 = ea_times_bw[this.ea_mode_src] + 10 + ea_times_bw[this.ea_mode_dst];
      }

      this.clock_cycles = var4 + (long)var6;
   }

   private final void trap() {
      this.generateException((this.OC & 15) + 32);
      this.clock_cycles += 38L;
   }

   private final void trapv() {
      if(this.fV != 0) {
         this.generateException(7);
         this.clock_cycles += 34L;
      } else {
         this.clock_cycles += 4L;
      }

   }

   private final void tst_b() {
      this.fnZ = this.readEA_8();
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 4;
      } else {
         var1 = ea_times_bw[this.ea_mode_src] + 5;
      }

      this.clock_cycles = var2 + (long)var1;
      this.fN = this.fnZ >>> 7 & 1;
      this.fC = 0;
      this.fV = 0;
   }

   private final void tst_l() {
      this.fnZ = this.readEA_32();
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 4;
      } else {
         var1 = ea_times_l[this.ea_mode_src] + 5;
      }

      this.clock_cycles = var2 + (long)var1;
      this.fN = this.fnZ >>> 31 & 1;
      this.fC = 0;
      this.fV = 0;
   }

   private final void tst_w() {
      this.fnZ = this.readEA_16();
      long var2 = this.clock_cycles;
      int var1;
      if(this.mode == 0) {
         var1 = 4;
      } else {
         var1 = ea_times_bw[this.ea_mode_src] + 5;
      }

      this.clock_cycles = var2 + (long)var1;
      this.fN = this.fnZ >>> 15 & 1;
      this.fC = 0;
      this.fV = 0;
   }

   private final void unlk() {
      this.Ai = this.OC & 7;
      this.A[7] = this.A[this.Ai];
      this.A[this.Ai] = this.readLong(this.A[7]);
      int[] var1 = this.A;
      var1[7] += 4;
      this.clock_cycles += 12L;
   }

   private void writeByte(int var1, int var2) {
      this.memory.write8(var1 & 16777215, var2);
   }

   private final void writeEA_16(int var1, int var2, int var3) {
      this.ea = 0;
      int[] var4;
      switch(var1) {
      case 0:
         this.ea_mode_dst = 0;
         this.D[var2] = this.D[var2] & -65536 | '\uffff' & var3;
         break;
      case 1:
         this.ea_mode_dst = 1;
         this.A[var2] = this.A[var2] & -65536 | '\uffff' & var3;
         break;
      case 2:
         this.ea_mode_dst = 2;
         this.writeWord(this.A[var2], var3);
         break;
      case 3:
         this.ea_mode_dst = 3;
         this.ea = this.A[var2];
         var4 = this.A;
         var4[var2] += 2;
         this.writeWord(this.ea, var3);
         break;
      case 4:
         this.ea_mode_dst = 4;
         var4 = this.A;
         var4[var2] -= 2;
         this.writeWord(this.A[var2], var3);
         break;
      case 5:
         this.ea_mode_dst = 5;
         this.ea = this.A[var2];
         this.ea += (short)this.fetchOpWord(this.PC);
         this.PC += 2;
         this.writeWord(this.ea, var3);
         break;
      case 6:
         this.ea_mode_dst = 6;
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea = this.A[var2];
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               this.writeWord(this.ea + (short)this.D[this.Xidd >> 12 & 15], var3);
            } else {
               this.writeWord(this.ea + this.D[this.Xidd >> 12 & 15], var3);
            }
         } else if((this.Xidd & 2048) == 0) {
            this.writeWord(this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8], var3);
         } else {
            this.writeWord(this.ea + this.A[(this.Xidd >> 12 & 15) - 8], var3);
         }
         break;
      case 7:
         switch(var2) {
         case 0:
            this.ea_mode_dst = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            this.writeWord(this.ea, var3);
            return;
         case 1:
            this.ea_mode_dst = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            this.writeWord(this.ea, var3);
            return;
         case 2:
            this.ea_mode_dst = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            this.writeWord(this.ea, var3);
            return;
         case 3:
            this.ea_mode_dst = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  this.writeWord(this.ea + (short)this.D[this.Xidd >> 12 & 15], var3);
               } else {
                  this.writeWord(this.ea + this.D[this.Xidd >> 12 & 15], var3);
               }

               return;
            } else {
               if((this.Xidd & 2048) == 0) {
                  this.writeWord(this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8], var3);
               } else {
                  this.writeWord(this.ea + this.A[(this.Xidd >> 12 & 15) - 8], var3);
               }

               return;
            }
         case 4:
            this.ea_mode_dst = 11;
            System.out.println("detected immediate memory write. OC:" + Integer.toHexString(this.OC) + " PC:" + Integer.toHexString(this.PC) + " LPC:" + Integer.toHexString(this.lPC_));
            return;
         default:
            System.out.println("ILLEGAL EA MODE WRITE : WORD");
            return;
         }
      default:
         System.out.println("ILLEGAL EA MODE WRITE : WORD");
      }

   }

   private final void writeEA_32(int var1, int var2, int var3) {
      this.ea = 0;
      int[] var4;
      switch(var1) {
      case 0:
         this.ea_mode_dst = 0;
         this.D[var2] = var3;
         break;
      case 1:
         this.ea_mode_dst = 1;
         this.A[var2] = var3;
         break;
      case 2:
         this.ea_mode_dst = 2;
         this.writeLong(this.A[var2], var3);
         break;
      case 3:
         this.ea_mode_dst = 3;
         this.ea = this.A[var2];
         var4 = this.A;
         var4[var2] += 4;
         this.writeLong(this.ea, var3);
         break;
      case 4:
         this.ea_mode_dst = 4;
         var4 = this.A;
         var4[var2] -= 4;
         this.writeLong(this.A[var2], var3);
         break;
      case 5:
         this.ea_mode_dst = 5;
         this.ea = this.A[var2];
         this.ea += (short)this.fetchOpWord(this.PC);
         this.PC += 2;
         this.writeLong(this.ea, var3);
         break;
      case 6:
         this.ea_mode_dst = 6;
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea = this.A[var2];
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               this.writeLong(this.ea + (short)this.D[this.Xidd >> 12 & 15], var3);
            } else {
               this.writeLong(this.ea + this.D[this.Xidd >> 12 & 15], var3);
            }
         } else if((this.Xidd & 2048) == 0) {
            this.writeLong(this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8], var3);
         } else {
            this.writeLong(this.ea + this.A[(this.Xidd >> 12 & 15) - 8], var3);
         }
         break;
      case 7:
         switch(var2) {
         case 0:
            this.ea_mode_dst = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            this.writeLong(this.ea, var3);
            return;
         case 1:
            this.ea_mode_dst = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            this.writeLong(this.ea, var3);
            return;
         case 2:
            this.ea_mode_dst = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            this.writeLong(this.ea, var3);
            return;
         case 3:
            this.ea_mode_dst = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  this.writeLong(this.ea + (short)this.D[this.Xidd >> 12 & 15], var3);
               } else {
                  this.writeLong(this.ea + this.D[this.Xidd >> 12 & 15], var3);
               }

               return;
            } else {
               if((this.Xidd & 2048) == 0) {
                  this.writeLong(this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8], var3);
               } else {
                  this.writeLong(this.ea + this.A[(this.Xidd >> 12 & 15) - 8], var3);
               }

               return;
            }
         case 4:
            this.ea_mode_dst = 11;
            System.out.println("detected immediate memory write. OC:" + Integer.toHexString(this.OC) + " PC:" + Integer.toHexString(this.PC) + " LPC:" + Integer.toHexString(this.lPC_));
            return;
         default:
            System.out.println("ILLEGAL EA MODE WRITE : LONG");
            return;
         }
      default:
         System.out.println("ILLEGAL EA MODE WRITE : LONG");
      }

   }

   private final void writeEA_8(int var1, int var2, int var3) {
      byte var5 = 1;
      byte var4 = 1;
      this.ea = 0;
      int[] var6;
      byte var7;
      switch(var1) {
      case 0:
         this.ea_mode_dst = 0;
         this.D[var2] = this.D[var2] & -256 | var3 & 255;
         break;
      case 1:
         this.ea_mode_dst = 1;
         this.A[var2] = this.A[var2] & -256 | var3 & 255;
         System.out.println("write byte to address register");
         break;
      case 2:
         this.ea_mode_dst = 2;
         this.writeByte(this.A[var2], var3);
         break;
      case 3:
         this.ea_mode_dst = 3;
         this.ea = this.A[var2];
         var6 = this.A;
         int var9 = var6[var2];
         var7 = var4;
         if(var2 == 7) {
            var7 = 2;
         }

         var6[var2] = var9 + var7;
         this.writeByte(this.ea, var3);
         break;
      case 4:
         this.ea_mode_dst = 4;
         var6 = this.A;
         int var8 = var6[var2];
         var7 = var5;
         if(var2 == 7) {
            var7 = 2;
         }

         var6[var2] = var8 - var7;
         this.writeByte(this.A[var2], var3);
         break;
      case 5:
         this.ea_mode_dst = 5;
         this.ea = this.A[var2];
         this.ea += (short)this.fetchOpWord(this.PC);
         this.PC += 2;
         this.writeByte(this.ea, var3);
         break;
      case 6:
         this.ea_mode_dst = 6;
         this.Xidd = this.fetchOpWord(this.PC);
         this.PC += 2;
         this.ea = this.A[var2];
         this.ea += (byte)(this.Xidd & 255);
         if((this.Xidd & '耀') == 0) {
            if((this.Xidd & 2048) == 0) {
               this.writeByte(this.ea + (short)this.D[this.Xidd >> 12 & 15], var3);
            } else {
               this.writeByte(this.ea + this.D[this.Xidd >> 12 & 15], var3);
            }
         } else if((this.Xidd & 2048) == 0) {
            this.writeByte(this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8], var3);
         } else {
            this.writeByte(this.ea + this.A[(this.Xidd >> 12 & 15) - 8], var3);
         }
         break;
      case 7:
         switch(var2) {
         case 0:
            this.ea_mode_dst = 7;
            this.ea = (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            this.writeByte(this.ea, var3);
            return;
         case 1:
            this.ea_mode_dst = 8;
            this.ea = this.fetchOpLong(this.PC);
            this.PC += 4;
            this.writeByte(this.ea, var3);
            return;
         case 2:
            this.ea_mode_dst = 9;
            this.ea = this.PC;
            this.ea += (short)this.fetchOpWord(this.PC);
            this.PC += 2;
            this.writeByte(this.ea, var3);
            return;
         case 3:
            this.ea_mode_dst = 10;
            this.ea = this.PC;
            this.Xidd = this.fetchOpWord(this.PC);
            this.PC += 2;
            this.ea += (byte)(this.Xidd & 255);
            if((this.Xidd & '耀') == 0) {
               if((this.Xidd & 2048) == 0) {
                  this.writeByte(this.ea + (short)this.D[this.Xidd >> 12 & 15], var3);
               } else {
                  this.writeByte(this.ea + this.D[this.Xidd >> 12 & 15], var3);
               }

               return;
            } else {
               if((this.Xidd & 2048) == 0) {
                  this.writeByte(this.ea + (short)this.A[(this.Xidd >> 12 & 15) - 8], var3);
               } else {
                  this.writeByte(this.ea + this.A[(this.Xidd >> 12 & 15) - 8], var3);
               }

               return;
            }
         case 4:
            this.ea_mode_dst = 11;
            System.out.println("detected immediate memory write. OC:" + Integer.toHexString(this.OC) + " PC:" + Integer.toHexString(this.PC) + " LPC:" + Integer.toHexString(this.lPC_));
            return;
         default:
            System.out.println("ILLEGAL EA MODE WRITE : BYTE");
            return;
         }
      default:
         System.out.println("ILLEGAL EA MODE WRITE : BYTE");
      }

   }

   private void writeLong(int var1, int var2) {
      int var3 = var1 & 16777215;
      CpuBoard var4 = this.memory;
      var1 = var3 + 1;
      var4.write8(var3, var2 >> 24 & 255);
      var4 = this.memory;
      var3 = var1 + 1;
      var4.write8(var1, var2 >> 16 & 255);
      this.memory.write8(var3, var2 >> 8 & 255);
      this.memory.write8(var3 + 1, var2 & 255);
   }

   private void writeWord(int var1, int var2) {
      var1 &= 16777215;
      this.memory.write8(var1, var2 >> 8 & 255);
      this.memory.write8(var1 + 1, var2 & 255);
   }

   public final void addIntAckListener(IntAckListener var1) {
      synchronized(this){}

      try {
         this.intAckListeners.add(var1);
      } finally {
         ;
      }

   }

   public final void attachDebugger(jM68k_Debug var1) {
      this.debug = var1;
   }

   public final int buildSR() {
      byte var1;
      if(this.fnZ == 0) {
         var1 = 0;
      } else {
         var1 = 1;
      }

      this.fnZ = var1;
      return (this.fTM << 15 | this.fSS << 13 | this.intMask << 8 | this.fX << 4 | this.fN << 3 | (this.fnZ ^ 1) << 2 | this.fV << 1 | this.fC << 0) & '\uffff';
   }

   public final void burnCycles(long var1) {
      this.cycles += var1;
   }

   public final void clearInterrupt(int var1) {
      if(var1 == 7) {
         this.pendingInterrupt = 0;
      } else {
         this.pendingInterrupt &= ~IRQ_LEVEL[var1];
      }

      this.clock_cycles += 4L;
   }

   public final void debugCycle() {
      if(this.interruptPending) {
         this.processInterrupts();
      }

      this.OC = this.readWord(this.PC);
      this.PC += 2;
      this.opmode = this.OC >> 6 & 7;
      this.mode = this.OC >> 3 & 7;
      this.sz = this.opmode & 3;
      this.OPTABLE[this.OC].execute();
   }

   public void dumpRegisters() {
      int var1 = this.trNum;
      this.trNum = var1 + 1;
      if(var1 >= 2200000) {
         String var4 = "SR:" + this.formatHex(this.getSR(), 8) + " ";
         var1 = 0;

         int var2;
         String var3;
         do {
            if(var1 < 8) {
               var3 = var4 + "A" + var1 + ":" + this.formatHex(this.A[var1], 8) + " ";
            } else {
               var3 = var4 + "A8:" + this.formatHex(this.SSP, 8) + " ";
            }

            var2 = var1 + 1;
            var1 = var2;
            var4 = var3;
         } while(var2 < 9);

         var1 = 0;

         do {
            var4 = var3 + "D" + var1 + ":" + this.formatHex(this.D[var1], 8) + " ";
            var2 = var1 + 1;
            var1 = var2;
            var3 = var4;
         } while(var2 < 8);

         var3 = var4 + " " + this.formatHex(this.getPC(), 6) + ": F" + Integer.toHexString(this.OC) + " SupV:" + this.fSS + " PendingIRQ:" + this.pendingInterrupt;
         System.out.println(var3);
      }

   }

   public final void endExec() {
      this.cycles = 0L;
      this.cc_timeslice = 0L;
   }

   public void exec(int var1) {
      this.execCPU(var1);
   }

   public final int execCPU(int var1) {
      this.cc_timeslice = this.cycles + (long)var1;

      while(true) {
         if(this.pendingInterrupt != 0) {
            this.checkInterrupts();
         }

         if(this.suspendedState) {
            var1 = (int)(this.clock_cycles - this.cc_timeslice);
            break;
         }

         this.OC = this.fetchOpWord(this.PC);
         this.lPC_ = this.PC;
         this.PC += 2;
         this.p_clock_cycles = this.clock_cycles;
         this.opmode = this.OC >> 6 & 7;
         this.mode = this.OC >> 3 & 7;
         this.sz = this.opmode & 3;
         this.OPTABLE[this.OC].execute();
         this.cycles += this.clock_cycles - this.p_clock_cycles;
         if(this.cycles >= this.cc_timeslice) {
            this.p_clock_cycles = this.cycles - this.cc_timeslice;
            this.extend_cycles = (int)(this.cycles - this.cc_timeslice);
            this.clock_cycles = 0L;
            var1 = this.extend_cycles;
            break;
         }
      }

      return var1;
   }

   public final int execNextInstruction() {
      if(this.interruptPending) {
         this.processInterrupts();
      }

      int var1;
      if(this.suspendedState) {
         var1 = 120;
      } else {
         this.OC = this.fetchOpWord(this.PC);
         this.lPC_ = this.PC;
         this.PC += 2;
         this.opmode = this.OC >> 6 & 7;
         this.mode = this.OC >> 3 & 7;
         this.sz = this.opmode & 3;
         this.clock_cycles = 0L;
         this.OPTABLE[this.OC].execute();
         this.cycles += this.clock_cycles;
         var1 = (int)this.clock_cycles;
      }

      return var1;
   }

   public final void freeze(long var1) {
      this.frozen_cycles = var1;
   }

   public final void generateException(int var1) {
      if(var1 == 9 && this.suspendedState) {
         this.suspendedState = false;
      }

      this.tmpSR = this.buildSR();
      this.enterSupervisorMode();
      int[] var2 = this.A;
      var2[7] -= 4;
      this.writeLong(this.A[7], this.PC);
      var2 = this.A;
      var2[7] -= 2;
      this.writeWord(this.A[7], this.tmpSR & '\uffff');
      this.lPC = this.PC;
      this.PC = this.readLong(var1 << 2);
   }

   public int[] getA() {
      return this.A;
   }

   public int[] getAR() {
      return this.A;
   }

   public int getCF() {
      return this.fC;
   }

   public final long getCPUTicks() {
      return this.cycles;
   }

   public int getClockCycles() {
      return (int)this.clock_cycles;
   }

   public final long getCycles() {
      return this.clock_cycles;
   }

   public int getCyclesLeft() {
      return 0;
   }

   public int[] getD() {
      return this.D;
   }

   public int[] getDR() {
      return this.D;
   }

   public int getDebug() {
      return 0;
   }

   public int getEF() {
      return this.fX;
   }

   public final long getFreeze() {
      return this.frozen_cycles;
   }

   public int getINT() {
      return this.intMask;
   }

   public long getInstruction() {
      return 0L;
   }

   public int getNF() {
      return this.fN;
   }

   public int getOC() {
      return this.OC;
   }

   public int getPC() {
      return this.PC;
   }

   public final int getPendingIRQ() {
      return this.pendingInterrupt;
   }

   public int getSR() {
      return this.buildSR();
   }

   public int getSS() {
      return this.fSS;
   }

   public int getSSP() {
      return this.SSP;
   }

   public int getTM() {
      return this.fTM;
   }

   public String getTag() {
      return null;
   }

   public int getVF() {
      return this.fV;
   }

   public int getZF() {
      byte var1;
      if(this.fnZ == 0) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public final void haltCPU() {
      this.suspendedState = true;
   }

   public boolean init(CpuBoard var1, int var2) {
      this.memory = var1;
      this.reset();
      return true;
   }

   public final void initCPU() {
      if(this.memory == null) {
         throw new IllegalStateException("M68000 Init Exception. No Memory available to CPU.");
      } else {
         for(this.loopc = 0; this.loopc < 8; ++this.loopc) {
            this.D[this.loopc] = 0;
            this.A[this.loopc] = 0;
         }

         this.SSP = this.readLong(0);
         this.A[7] = this.SSP;
         this.PC = this.readLong(4);
         this.OC = 0;
         this.tmpSR = 0;
         this.setStatusReg(9984);
         this.pendingInterrupt = 0;
         this.interruptPending = false;
         this.exceptionState = false;
         this.interruptAcknowledged = false;
         this.error = false;
         this.suspendedState = false;
         this.dst = 0;
         this.res = 0;
         this.src = 0;
         this.loopc = 0;
         this.frozen_cycles = 0L;
         this.p_clock_cycles = 0L;
         this.h_cycles = 0L;
         this.clock_cycles = 0L;
         this.cc_timeslice = 0L;
         this.sz = 0;
         this.opmode = 0;
         this.mode = 0;
         this.reg = 0;
      }
   }

   public void interrupt(int var1, boolean var2) {
      if(var2) {
         this.requestInterrupt(var1);
      } else {
         this.clearInterrupt(var1);
      }

   }

   public final boolean isFrozen() {
      boolean var1;
      if(this.frozen_cycles > 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void markBusyLoop(char[] var1, int var2) {
      int var3 = (var1[var2] << 8) + var1[var2 + 1];
      var1[var2] = (char)(this.markBusyLoopOpc >> 8);
      var1[var2 + 1] = (char)(this.markBusyLoopOpc & 255);
      System.out.println("Busy loop installed at " + Integer.toHexString(var2) + ", orig opcode=" + Integer.toHexString(var3));
      this.OPTABLE[this.markBusyLoopOpc] = new BUSYLOOP(var3);
      --this.markBusyLoopOpc;
   }

   public final void opReset() {
      System.out.println("*M68000 ::: RESET external devices");
      if(this.resetCallBack != null) {
         this.resetCallBack.write(0, 0);
      }

      this.nop();
   }

   public final void removeIntAckListener(IntAckListener var1) {
      synchronized(this){}

      try {
         this.intAckListeners.remove(var1);
      } finally {
         ;
      }

   }

   public final void requestInterrupt(int var1) {
      this.pendingInterrupt |= IRQ_LEVEL2[var1];
      this.clock_cycles += 4L;
   }

   public void reset() {
      this.resetCPU(0, 0);
   }

   public final void resetCPU(int var1, int var2) {
      this.haltCPU();
      this.initCPU();
   }

   public final void resetCPUTicks() {
      this.cycles = 0L;
   }

   public void setDebug(int var1) {
   }

   public final void setIgnoreTASWriteBackCycle(boolean var1) {
      this.ignore_TAS_WriteBack = var1;
   }

   public final void setPC(int var1) {
      this.PC = var1;
   }

   public void setProperty(int var1, int var2) {
   }

   public void setResetCallBack(WriteHandler var1) {
      this.resetCallBack = var1;
   }

   public final void setStatusReg(int var1) {
      var1 &= '\uffff';
      this.fTM = var1 >>> 15 & 1;
      this.fSS = var1 >>> 13 & 1;
      this.intMask = var1 >>> 8 & 7;
      this.fX = var1 >>> 4 & 1;
      this.fN = var1 >>> 3 & 1;
      this.fnZ = var1 >>> 2 & 1 ^ 1;
      this.fV = var1 >>> 1 & 1;
      this.fC = var1 >>> 0 & 1;
   }

   public void setTag(String var1) {
   }

   public final long unfreeze(long var1) {
      this.frozen_cycles -= var1;
      if(this.frozen_cycles < 0L) {
         this.frozen_cycles = 0L;
      }

      this.cycles += var1;
      return this.frozen_cycles;
   }

   class BUSYLOOP extends Opcode {
      int origOpcode;

      public BUSYLOOP(int var2) {
         this.origOpcode = var2;
      }

      void execute() {
         jM68k.this.opmode = this.origOpcode >> 6 & 7;
         jM68k.this.mode = this.origOpcode & 7;
         jM68k.this.sz = jM68k.this.opmode & 3;
         jM68k.this.OPTABLE[this.origOpcode].execute();
         jM68k.this.endExec();
      }
   }
}
