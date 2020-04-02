/*
 * ported to v0.56
 * using automatic conversion tool v0.01
 */ 
package mame056.cpu.z8000;

public class z8000H
{
	
	public static final int Z8000_PC=1;
        public static final int Z8000_NSP=2;
        public static final int Z8000_FCW=3;
        public static final int Z8000_PSAP=4;
        public static final int Z8000_REFRESH=5;
	public static final int Z8000_IRQ_REQ=6;
        public static final int Z8000_IRQ_SRV=7;
        public static final int Z8000_IRQ_VEC=8;
	public static final int Z8000_R0=9;
        public static final int Z8000_R1=10;
        public static final int Z8000_R2=11;
        public static final int Z8000_R3=12;
	public static final int Z8000_R4=13;
        public static final int Z8000_R5=14;
        public static final int Z8000_R6=15;
        public static final int Z8000_R7=16;
	public static final int Z8000_R8=17;
        public static final int Z8000_R9=18;
        public static final int Z8000_R10=19;
        public static final int Z8000_R11=20;
	public static final int Z8000_R12=21;
        public static final int Z8000_R13=22;
        public static final int Z8000_R14=23;
        public static final int Z8000_R15=24;
	public static final int Z8000_NMI_STATE=25;
        public static final int Z8000_NVI_STATE=26;
        public static final int Z8000_VI_STATE=27;
	
	/* Interrupt Types that can be generated by outside sources */
	public static final int Z8000_TRAP		= 0x4000;	/* internal trap */
	public static final int Z8000_NMI		= 0x2000;	/* non maskable interrupt */
	public static final int Z8000_SEGTRAP           = 0x1000;	/* segment trap (Z8001) */
	public static final int Z8000_NVI		= 0x0800;	/* non vectored interrupt */
	public static final int Z8000_VI		= 0x0400;	/* vectored interrupt (LSB is vector)  */
	public static final int Z8000_SYSCALL           = 0x0200;	/* system call (lsb is vector) */
	public static final int Z8000_HALT		= 0x0100;	/* halted flag	*/
	
	
}
