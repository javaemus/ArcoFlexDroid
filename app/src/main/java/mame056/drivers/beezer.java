/*

  Beezer - (c) 1982 Tong Electronic

  Written by Mathis Rosenhauer

*/

/*
 * ported to v0.56
 * using automatic conversion tool v0.01
 */ 
package mame056.drivers;

import static WIP.mame056.machine.beezer.beezer_bankswitch_w;
import static WIP.mame056.machine.beezer.init_beezer;
import static WIP.mame056.vidhrdw.beezer.beezer_interrupt;
import static WIP.mame056.vidhrdw.beezer.beezer_ram;
import static WIP.mame056.vidhrdw.beezer.beezer_ram_w;
import static WIP.mame056.vidhrdw.beezer.beezer_vh_screenrefresh;
import static arcadeflex056.fucPtr.*;
import static common.libc.cstdlib.rand;
import static common.ptr.*;
import static common.libc.cstring.*;
import static arcadeflex056.fileio.*;
import static mame056.common.*;
import static mame056.commonH.REGION_CPU1;
import static mame056.commonH.REGION_CPU2;
import static mame056.commonH.REGION_PROMS;
import static mame056.commonH.ROM_END;
import static mame056.commonH.ROM_LOAD;
import static mame056.commonH.ROM_REGION;
import static mame056.cpuintrfH.CPU_M6809;
import static mame056.driverH.DEFAULT_REAL_60HZ_VBLANK_DURATION;
import static mame056.driverH.GAME_IMPERFECT_SOUND;
import static mame056.driverH.ORIENTATION_FLIP_X;
import static mame056.driverH.VIDEO_TYPE_RASTER;
import static mame056.inptportH.DEF_STR;
import static mame056.inptportH.INPUT_PORTS_END;
import static mame056.inptportH.IPF_REVERSE;
import static mame056.inptportH.IPT_COIN1;
import static mame056.inptportH.IPT_START1;
import static mame056.inptportH.IPT_START2;
import static mame056.inptportH.IPT_TILT;
import static mame056.inptportH.IPT_TRACKBALL_X;
import static mame056.inptportH.IPT_TRACKBALL_Y;
import static mame056.inptportH.IPT_UNKNOWN;
import static mame056.inptportH.IP_ACTIVE_LOW;
import static mame056.inptportH.PORT_ANALOG;
import static mame056.inptportH.PORT_BIT;
import static mame056.inptportH.PORT_DIPNAME;
import static mame056.inptportH.PORT_DIPSETTING;
import static mame056.inptportH.PORT_START;
import static mame056.machine._6522via.via_1_r;
import static mame056.machine._6522via.via_1_w;
import static mame056.mame.*;
import static mame056.memoryH.MEMPORT_DIRECTION_READ;
import static mame056.memoryH.MEMPORT_DIRECTION_WRITE;
import static mame056.memoryH.MEMPORT_MARKER;
import static mame056.memoryH.MEMPORT_TYPE_MEM;
import static mame056.memoryH.MEMPORT_WIDTH_8;
import static mame056.memoryH.MRA_BANK1;
import static mame056.memoryH.MRA_RAM;
import static mame056.memoryH.MRA_ROM;
import static mame056.memoryH.MWA_BANK1;
import static mame056.memoryH.MWA_RAM;
import static mame056.memoryH.MWA_ROM;
import static mame056.sndintrf.*;
import static mame056.commonH.*;
import static mame056.cpuexec.*;
import static mame056.inptportH.*;
import static mame056.cpuexecH.*;
import static mame056.cpuintrfH.*;
import static mame056.driverH.*;
import static mame056.memoryH.*;
import static mame056.inptport.*;
import static mame056.drawgfxH.*;

import static mame056.inputH.*;
import static mame056.memory.*;
import static mame056.sound.ay8910H.*;
import static mame056.sound.ay8910.*;
import static mame056.sndintrf.*;
import static mame056.sndintrfH.*;

import static mame056.vidhrdw.generic.*;
import static WIP.mame056.vidhrdw.beezer.*;
import static WIP.mame056.machine.beezer.*;

import static arcadeflex056.osdepend.logerror;
import static mame056.machine._6522via.*;

import static mame056.sound.sn76496.*;
import static mame056.sound.sn76496H.*;
import static mame056.sound.dac.*;
import static mame056.sound.dacH.*;

public class beezer
{
	
        public static Memory_ReadAddress readmem[]={
		new Memory_ReadAddress(MEMPORT_MARKER, MEMPORT_DIRECTION_READ | MEMPORT_TYPE_MEM | MEMPORT_WIDTH_8),	new Memory_ReadAddress( 0x0000, 0xbfff, MRA_RAM ),
		new Memory_ReadAddress( 0xc000, 0xcfff, MRA_BANK1 ),
		new Memory_ReadAddress( 0xd000, 0xffff, MRA_ROM ),
		new Memory_ReadAddress(MEMPORT_MARKER, 0)
	};
	public static Memory_WriteAddress writemem[]={
		new Memory_WriteAddress(MEMPORT_MARKER, MEMPORT_DIRECTION_WRITE | MEMPORT_TYPE_MEM | MEMPORT_WIDTH_8),	new Memory_WriteAddress( 0x0000, 0xbfff, beezer_ram_w, beezer_ram ),
		new Memory_WriteAddress( 0xc000, 0xcfff, MWA_BANK1 ),
		new Memory_WriteAddress( 0xd000, 0xffff, beezer_bankswitch_w ),
		new Memory_WriteAddress(MEMPORT_MARKER, 0)
	};
	public static Memory_ReadAddress readmem_sound[]={
		new Memory_ReadAddress(MEMPORT_MARKER, MEMPORT_DIRECTION_READ | MEMPORT_TYPE_MEM | MEMPORT_WIDTH_8),	new Memory_ReadAddress( 0x0000, 0x07ff, MRA_RAM ),
	//	new Memory_ReadAddress( 0x1000, 0x10ff, beezer_6840_r ),
		new Memory_ReadAddress( 0x1800, 0x18ff, via_1_r ),
		new Memory_ReadAddress( 0xe000, 0xffff, MRA_ROM ),
		new Memory_ReadAddress(MEMPORT_MARKER, 0)
	};
	public static Memory_WriteAddress writemem_sound[]={
		new Memory_WriteAddress(MEMPORT_MARKER, MEMPORT_DIRECTION_WRITE | MEMPORT_TYPE_MEM | MEMPORT_WIDTH_8),	new Memory_WriteAddress( 0x0000, 0x07ff, MWA_RAM ),
	//	new Memory_WriteAddress( 0x1000, 0x10ff, beezer_6840_w ),
		new Memory_WriteAddress( 0x1800, 0x18ff, via_1_w ),
	//	new Memory_WriteAddress( 0x8000, 0x9fff, beezer_dac_w ),
		new Memory_WriteAddress( 0xe000, 0xffff, MWA_ROM ),
		new Memory_WriteAddress(MEMPORT_MARKER, 0)
	};
	
	
	static InputPortPtr input_ports_beezer = new InputPortPtr(){ public void handler() { 
	PORT_START(); 	/* IN0 */
		PORT_BIT( 0x01, IP_ACTIVE_LOW, IPT_TILT );	PORT_BIT( 0x02, IP_ACTIVE_LOW, IPT_COIN1 );	PORT_BIT( 0x04, IP_ACTIVE_LOW, IPT_START2 );	PORT_BIT( 0x08, IP_ACTIVE_LOW, IPT_START1 );	PORT_BIT( 0x10, IP_ACTIVE_LOW, IPT_UNKNOWN );	PORT_BIT( 0x20, IP_ACTIVE_LOW, IPT_UNKNOWN );	PORT_BIT( 0x40, IP_ACTIVE_LOW, IPT_UNKNOWN );	PORT_BIT( 0x80, IP_ACTIVE_LOW, IPT_UNKNOWN );
		PORT_START(); 	/* IN1 */
		PORT_ANALOG( 0x0f, 0x00, IPT_TRACKBALL_X | IPF_REVERSE, 20, 10, 0, 0);	PORT_START(); 	/* IN2 */
		PORT_ANALOG( 0x0f, 0x00, IPT_TRACKBALL_Y | IPF_REVERSE, 20, 10, 0, 0);
		PORT_START(); 	/* IN3 */
		PORT_DIPNAME( 0x03, 0x03, DEF_STR( "Coinage") );
		PORT_DIPSETTING(    0x02, DEF_STR( "2C_1C") );
		PORT_DIPSETTING(    0x03, DEF_STR( "1C_1C") );
		PORT_DIPSETTING(    0x00, DEF_STR( "Free_Play") );
		PORT_DIPNAME( 0x04, 0x00, DEF_STR( "Lives") );
		PORT_DIPSETTING(    0x04, "3" );	PORT_DIPSETTING(    0x00, "4" );	PORT_DIPNAME( 0x08, 0x08, DEF_STR( "Demo_Sounds") );
		PORT_DIPSETTING(    0x00, DEF_STR( "Off") );
		PORT_DIPSETTING(    0x08, DEF_STR( "On") );
		PORT_DIPNAME( 0x30, 0x10, DEF_STR( "Bonus_Life") );
		PORT_DIPSETTING(    0x20, "30000" );	PORT_DIPSETTING(    0x10, "60000" );	PORT_DIPSETTING(    0x00, "90000" );	PORT_DIPSETTING(    0x30, DEF_STR( "No") );
		PORT_DIPNAME( 0xc0, 0xc0, DEF_STR( "Difficulty") );
		PORT_DIPSETTING(    0xc0, "Easy" );	PORT_DIPSETTING(    0x80, "Medium" );	PORT_DIPSETTING(    0x40, "Hard" );	PORT_DIPSETTING(    0x00, "Hardest" );INPUT_PORTS_END(); }}; 
	
	static DACinterface dac_interface = new DACinterface
	(
		1,
		new int[] { 100 }
	);
	
	static MachineDriver machine_driver_beezer = new MachineDriver
	(
		/* basic machine hardware */
		new MachineCPU[] {
			new MachineCPU(
				CPU_M6809,
				1000000,        /* 1 MHz */
				readmem,writemem,null,null,
				beezer_interrupt,128
			/*TODO*///),
			/*TODO*///new MachineCPU(
			/*TODO*///	CPU_M6809,
			/*TODO*///	1000000,        /* 1 MHz */
			/*TODO*///	readmem_sound,writemem_sound,null,null,
			/*TODO*///	null,0
                        )
		},
		60, DEFAULT_REAL_60HZ_VBLANK_DURATION,
		1,	/* 1 CPU slice per frame */
		null,	/* init machine */
	
		/* video hardware */
		256, 384, new rectangle( 0, 256-1, 16, 303 ),
		null,
		16, 0,
		null,
	
		VIDEO_TYPE_RASTER,
		null,
		null,
		null,
		beezer_vh_screenrefresh,
	
		/* sound hardware */
		0,0,0,0,
		/*TODO*///new MachineSound[] {
		/*TODO*///	new MachineSound(
		/*TODO*///		SOUND_DAC,
		/*TODO*///		dac_interface
		/*TODO*///	)
		/*TODO*///}
                
                null
	);
	
	/***************************************************************************
	
	  Game driver(s)
	
	***************************************************************************/
	
	static RomLoadPtr rom_beezer = new RomLoadPtr(){ public void handler(){ 
		ROM_REGION( 0x20000, REGION_CPU1, 0 );    /* 64k for main CPU */
		ROM_LOAD( "g1",   0x0d000, 0x1000, 0x3467a0ec );	ROM_LOAD( "g3",   0x0e000, 0x1000, 0x9950cdf2 );	ROM_LOAD( "g5",   0x0f000, 0x1000, 0xa4b09879 );
		ROM_LOAD( "f1",   0x12000, 0x2000, 0xce1b0b8b );	ROM_LOAD( "f3",   0x14000, 0x2000, 0x6a11072a );	ROM_LOAD( "e1",   0x16000, 0x1000, 0x21e4ca9b );	ROM_LOAD( "e3",   0x18000, 0x1000, 0xa4f735d7 );	ROM_LOAD( "e5",   0x1a000, 0x1000, 0x0485575b );	ROM_LOAD( "f5",   0x1c000, 0x1000, 0x4b11f572 );	ROM_LOAD( "f7",   0x1e000, 0x1000, 0xbef67473 );
		ROM_REGION( 0x10000, REGION_CPU2, 0 );    /* 64k for sound CPU */
		ROM_LOAD( "d7",   0xf000, 0x1000, 0x23b0782e );
		ROM_REGION( 0x0300, REGION_PROMS, 0 );	ROM_LOAD( "d1.cpu", 0x000, 0x0100, 0x8db17a40 );	ROM_LOAD( "e1.cpu", 0x100, 0x0100, 0x3c775c5e );ROM_END(); }}; 
	
	static RomLoadPtr rom_beezer1 = new RomLoadPtr(){ public void handler(){ 
		ROM_REGION( 0x20000, REGION_CPU1, 0 );    /* 64k for main CPU */
		ROM_LOAD( "g1.32",   0x0d000, 0x1000, 0x3134cb93 );	ROM_LOAD( "g3.32",   0x0e000, 0x1000, 0xa3cb2c2d );	ROM_LOAD( "g5.32",   0x0f000, 0x1000, 0x5e559bf9 );
		ROM_LOAD( "f1.64",   0x12000, 0x2000, 0xb8a78cca );	ROM_LOAD( "f3.32",   0x14000, 0x1000, 0xbfa023f5 );	ROM_LOAD( "e1",      0x16000, 0x1000, 0x21e4ca9b );	ROM_LOAD( "e3",      0x18000, 0x1000, 0xa4f735d7 );	ROM_LOAD( "e5",      0x1a000, 0x1000, 0x0485575b );	ROM_LOAD( "f5",      0x1c000, 0x1000, 0x4b11f572 );	ROM_LOAD( "f7",      0x1e000, 0x1000, 0xbef67473 );
		ROM_REGION( 0x10000, REGION_CPU2, 0 );    /* 64k for sound CPU */
		ROM_LOAD( "d7.32",   0xf000, 0x1000, 0xb11028b5 );
		ROM_REGION( 0x0300, REGION_PROMS, 0 );	ROM_LOAD( "d1.cpu", 0x000, 0x0100, 0x8db17a40 );	ROM_LOAD( "e1.cpu", 0x100, 0x0100, 0x3c775c5e );ROM_END(); }}; 
	
	public static GameDriver driver_beezer	   = new GameDriver("1982"	,"beezer"	,"beezer.java"	,rom_beezer,null	,machine_driver_beezer	,input_ports_beezer	,init_beezer	,ORIENTATION_FLIP_X	,	"Tong Electronic", "Beezer (set 1)", GAME_IMPERFECT_SOUND );
	public static GameDriver driver_beezer1	   = new GameDriver("1982"	,"beezer1"	,"beezer.java"	,rom_beezer1,driver_beezer	,machine_driver_beezer	,input_ports_beezer	,init_beezer	,ORIENTATION_FLIP_X	,	"Tong Electronic", "Beezer (set 2)", GAME_IMPERFECT_SOUND );
}
