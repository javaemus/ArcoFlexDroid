package net.movegaga.jemu2;

import java.net.URL;

import cottage.mame.Driver;
import jef.machine.Emulator;
import jef.util.RomLoader;

public class EmulatorFactory {
   public static String currentDriver = "";

   public static final String[][] GAME_DRIVER_LIST = {

           //===========================================================
           // JEmu2 arcade drivers
           //===========================================================
           //
           //-ROM---------|-DRIVER CLASS----------------------------------------|-KNOWN PROBLEMS----------------
           // 1942
           { "1942",       "net.movegaga.jemu2.driver.arcade.c1942.C1942" },
           // 1943
           { "1943",       "net.movegaga.jemu2.driver.arcade.c1943.C1943" },
           { "1943kai",    "net.movegaga.jemu2.driver.arcade.c1943.C1943" },
           { "gunsmoke",   "net.movegaga.jemu2.driver.arcade.c1943.C1943" },
           // Arkanoid
           { "arkatayt",   "net.movegaga.jemu2.driver.arcade.arkanoid.Arkanoid" },
           // Bank Panic
           { "bankp",      "net.movegaga.jemu2.driver.arcade.bankp.Bankp" },
           // Black Tiger
           { "blktiger",   "net.movegaga.jemu2.driver.arcade.blktiger.Blktiger", "Imperfect sound" },
           // Bomb Jack
           { "bombjack",   "net.movegaga.jemu2.driver.arcade.bombjack.Bombjack" },
           // Bubble Bobble
           { "boblbobl",   "net.movegaga.jemu2.driver.arcade.bublbobl.Bublbobl", "No sound" },
           { "tokiob",     "net.movegaga.jemu2.driver.arcade.bublbobl.Bublbobl", "No sound" },
           // Bw8080 drivers
           { "280zzzap",   "net.movegaga.jemu2.driver.arcade.bw8080.Bw8080", "Bad Input/Crashes, No sound" },
           { "checkmat",   "net.movegaga.jemu2.driver.arcade.bw8080.Bw8080", "No sound" },
           { "invaders",   "net.movegaga.jemu2.driver.arcade.bw8080.Bw8080", "Imperfect sound" },
           { "maze",       "net.movegaga.jemu2.driver.arcade.bw8080.Bw8080", "No sound" },
           // Capcom Play System 1 (CPS1)
           { "sf2",        "net.movegaga.jemu2.driver.arcade.cps.Cps1",          "No sound" },
           { "forgottn",   "net.movegaga.jemu2.driver.arcade.cps.Cps1",          "No sound" },
           // Circus Charlie
           { "circusc",    "net.movegaga.jemu2.driver.arcade.circusc.Circusc",   "No sound" },
           // Commando
           { "commando",   "net.movegaga.jemu2.driver.arcade.commando.Commando" },
           // Donkey Kong
           { "dkong",      "net.movegaga.jemu2.driver.arcade.dkong.Dkong",       "No sound" },
           { "dkongjr",    "net.movegaga.jemu2.driver.arcade.dkong.Dkong",       "No sound" },
           { "dkong3",     "net.movegaga.jemu2.driver.arcade.dkong.Dkong",       "No sound" },
           { "radarscp",   "net.movegaga.jemu2.driver.arcade.dkong.Dkong",       "No sound" },
           // Galaga
           { "galaga",     "net.movegaga.jemu2.driver.arcade.galaga.Galaga",     "inf. loop at game over" },
           // Galaxian
           { "galaxian",   "net.movegaga.jemu2.driver.arcade.galaxian.Galaxian", "No sound" },
           { "scramblb",   "net.movegaga.jemu2.driver.arcade.galaxian.Galaxian", "No sound" },
           { "warofbug",   "net.movegaga.jemu2.driver.arcade.galaxian.Galaxian", "No sound" },
           { "zigzag",     "net.movegaga.jemu2.driver.arcade.galaxian.Galaxian", "No sound" },
           // Ghosts'n Goblins
           { "gng",        "net.movegaga.jemu2.driver.arcade.gng.Gng" },
           { "makaimur",   "net.movegaga.jemu2.driver.arcade.gng.Gng" },
           { "diamond",    "net.movegaga.jemu2.driver.arcade.gng.Gng" },
           // Green Beret
           { "gberet",     "net.movegaga.jemu2.driver.arcade.gberet.Gberet",     "Imperfect sound" },
           { "mrgoemon",   "net.movegaga.jemu2.driver.arcade.gberet.Gberet", },
           // Hexa drivers
           { "hexa",       "net.movegaga.jemu2.driver.arcade.hexa.Hexa" },
           // Hyper Sports drivers
           { "hyperspt",   "net.movegaga.jemu2.driver.arcade.hyperspt.Hyperspt", "No sound" },
           { "roadf",      "net.movegaga.jemu2.driver.arcade.hyperspt.Hyperspt", "Imperfect sound" },
           // Jr. Pac-Man
           { "jrpacman",   "net.movegaga.jemu2.driver.arcade.jrpacman.Jrpacman" },
           // M62 drivers
           { "ldrun",      "net.movegaga.jemu2.driver.arcade.m62.M62",           "No sound" },
           { "ldrun2",     "net.movegaga.jemu2.driver.arcade.m62.M62",           "No sound" },
           { "ldrun3",     "net.movegaga.jemu2.driver.arcade.m62.M62",           "No sound" },
           { "ldrun4",     "net.movegaga.jemu2.driver.arcade.m62.M62",           "No sound" },
           { "kungfum",    "net.movegaga.jemu2.driver.arcade.m62.M62",           "No sound" },
           // Mario Bros.
           { "mario",      "net.movegaga.jemu2.driver.arcade.mario.Mario",       "No sound" },
           // Pacman
           { "pacman",     "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "pacplus",    "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "mspacman",   "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "eyes",       "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "theglob",    "net.movegaga.jemu2.driver.arcade.pacman.Pacman",     "Doesn't work" },
           { "vanvan",     "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "dremshpr",   "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "mrtnt",      "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "lizwiz",     "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           { "ponpoko",    "net.movegaga.jemu2.driver.arcade.pacman.Pacman" },
           // Snow Bros. Nick & Tom
           { "snowbros",   "net.movegaga.jemu2.driver.arcade.snowbros.Snowbros", "No sound" },
           { "snowbro3",   "net.movegaga.jemu2.driver.arcade.snowbros.Snowbros", "Glitches, No sound" },
           // Solomon's Key
           { "solomon",    "net.movegaga.jemu2.driver.arcade.solomon.Solomon" },
           // Yie Ar Kung-Fu
           { "yiear",      "net.movegaga.jemu2.driver.arcade.yiear.Yiear",      "No sound" },
           // Sega X-Board
           { "aburner2",   "net.movegaga.jemu2.driver.arcade.segasys16.XBoard", "Missing road layer" },
           { "thbladej",   "net.movegaga.jemu2.driver.arcade.segasys16.XBoard", "Missing road layer, glitches" },
           // Sega Y-Board
           { "gforce2",    "net.movegaga.jemu2.driver.arcade.segasys16.YBoard" },
           { "pdrift",     "net.movegaga.jemu2.driver.arcade.segasys16.YBoard" },
           { "gloc",       "net.movegaga.jemu2.driver.arcade.segasys16.YBoard" },
           { "rchase",     "net.movegaga.jemu2.driver.arcade.segasys16.YBoard" },
           { "strkfgtr",   "net.movegaga.jemu2.driver.arcade.segasys16.YBoard" },
           // Sega Out Run
           { "outrun",     "net.movegaga.jemu2.driver.arcade.segasys16.Outrun" },
           { "shangon",    "net.movegaga.jemu2.driver.arcade.segasys16.Outrun" },
           // Sega Hang-On
           { "sharrier",   "net.movegaga.jemu2.driver.arcade.segasys16.HangOn" },
           { "shangupb",   "net.movegaga.jemu2.driver.arcade.segasys16.HangOn" },

           { "spectrum",   "net.movegaga.jemu2.driver.spectrum.Spectrum"       },

           //===========================================================
           // MSX enhancing drivers
           //===========================================================
           { "msxe_nemesis2", "net.movegaga.jemu2.driver.msx.enh.Nemesis2" },
           { "msxe_salamand", "net.movegaga.jemu2.driver.msx.enh.Salamander" },
           { "msxe_kmare",    "net.movegaga.jemu2.driver.msx.enh.Knightmare" },

           //===========================================================
           // CottAGE legacy drivers (to be removed or replaced)
           //===========================================================
           { "puckman", "cottage.drivers.Pacman_", "" },
           { "puckmana", "cottage.drivers.Pacman_", "" },
           { "puckmod", "cottage.drivers.Pacman_", "" },
           { "pacmod", "cottage.drivers.Pacman_", "" },
           { "hangly", "cottage.drivers.Pacman_", "" },
           { "hangly2", "cottage.drivers.Pacman_", "" },
           { "newpuckx", "cottage.drivers.Pacman_", "" },
           { "pacheart", "cottage.drivers.Pacman_", "" },
           { "piranha", "cottage.drivers.Pacman_", "" },
           { "mspacmab", "cottage.drivers.Pacman_", "" },
           { "mspacpls", "cottage.drivers.Pacman_", "" },
           { "pacgal", "cottage.drivers.Pacman_", "" },
           { "crush2", "cottage.drivers.Pacman_", "" },
           { "crush3", "cottage.drivers.Pacman_", "" },
           { "mbrush", "cottage.drivers.Pacman_", "" },
           { "paintrlr", "cottage.drivers.Pacman_", "" },
           { "ponpokov", "cottage.drivers.Pacman_", "" },
           { "eyes2", "cottage.drivers.Pacman_", "" },
           { "jumpshot", "cottage.drivers.Pacman_", "" },

           /* Gyruss drivers */
           { "gyruss", "cottage.drivers.Gyruss", "Glitches" },

           /* News drivers */
           { "news", "cottage.drivers.News", "No sound" },

           /* m79amb drivers */
           { "m79amb", "cottage.drivers.M79amb", "No sound" },

           /* Roc 'n rope drivers */
           { "rocnrope", "cottage.drivers.Rocnrope", "No sound" },
           { "rocnropk", "cottage.drivers.Rocnrope", "No sound" },

           /* Safari Rally drivers */
           { "safarir", "cottage.drivers.Safarir", "Not working" },

           /* Pingpong drivers */
           { "pingpong", "cottage.drivers.Pingpong" },

           /* Tropical Angel drivers */
           { "troangel", "cottage.drivers.Troangel", "No sound" },

           /* Mr. Jong drivers */
           { "mrjong", "cottage.drivers.Mrjong", "No sound" },
           { "crazyblk", "cottage.drivers.Mrjong", "No sound" },

           /* Pooyan drivers */
           { "pooyan", "cottage.drivers.Pooyan", "No sound" },
           { "pooyans", "cottage.drivers.Pooyan", "No sound" },
           { "pootan", "cottage.drivers.Pooyan", "No sound" },

           /* Sonson drivers */
           { "sonson", "cottage.drivers.Sonson", "No sound" },
           { "sonsonj", "cottage.drivers.Sonson", "No sound" },

           { "foodf", "cottage.drivers.Foodf", "Preliminary" },

           /* Mitchell drivers */
           { "pang", "cottage.drivers.Mitchell", "Preliminary" },
           { "pangb", "cottage.drivers.Mitchell", "Preliminary" },

           /* 4 En Raya drivers */
           { "4enraya", "cottage.drivers._4enraya", "Preliminary" },

           /* Rally X */
           { "rallyx", "cottage.drivers.Rallyx", "No sound" },

           { "", "", "" } /* End of array */
   };

   private String findClass(String var1) {
      int var2 = 0;

      while(true) {
         if(var2 >= GAME_DRIVER_LIST.length) {
            if(var1.startsWith("msx")) {
               var1 = "net.movegaga.MSX";
            } else if(var1.startsWith("sms_")) {
               var1 = "net.movegaga.SMS";
            } else if(var1.startsWith("st_")) {
               var1 = "net.movegaga.AtariST";
            } else if(var1.startsWith("spec_")) {
               var1 = "net.movegaga.jemu2.driver.spectrum.Spectrum";
            } else if(var1.startsWith("nes_")) {
               var1 = "net.movegaga.NES";
            } else {
               var1 = null;
            }
            break;
         }

         if(GAME_DRIVER_LIST[var2][0].equals(var1)) {
            var1 = GAME_DRIVER_LIST[var2][1];
            break;
         }

         ++var2;
      }

      return var1;
   }

   public Emulator createEmulator(String var1) throws Exception {
      System.out.println("Trying to select driver \'" + var1 + "\'...");
      String var2 = this.findClass(var1);
      System.out.println(var2);
      if(var2 != null) {
         Object var3;
         try {
            var3 = Class.forName(var2).newInstance();
         } catch (Exception var5) {
            System.out.println("ERROR : Driver class \'" + var2 + "\' does not exist!");
            var5.printStackTrace();
            throw new Exception("Driver class \'" + var2 + "\' does not exist.");
         }

         System.out.println("Ok!");
         RomLoader._currentROM = var1;
         URL var4 = this.getClass().getClassLoader().getResource(".");
         Emulator var6;
         if(var2.startsWith("cottage")) {
            System.out.println("INFO: " + var2 + " is a legacy CottAGE driver.");
            var6 = ((Driver)var3).getMachine(var4, var1);
         } else {
            System.out.println("INFO: " + var2 + " is a JEmu2 driver.");
            var6 = ((net.movegaga.jemu2.driver.Driver)var3).createEmulator(var4, var1);
         }

         //currentDriver = var1;

         return var6;
      } else {
         System.out.println("ERROR : \'" + var1 + "\' is not supported!");
         throw new Exception("Driver \'" + var1 + "\' is not supported!");
      }
   }
}
