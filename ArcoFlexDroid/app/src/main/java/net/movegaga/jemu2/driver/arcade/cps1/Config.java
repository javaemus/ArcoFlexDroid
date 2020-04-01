package net.movegaga.jemu2.driver.arcade.cps1;

import java.util.HashMap;
import java.util.Map;

public class Config {
   static final CPSType CPS_B_01 = new CPSType(0, 0, 0, 0, 0, 0, 102, new int[]{104, 106, 108, 110}, 112, new int[]{2, 4, 8, 48, 48});
   static final CPSType CPS_B_02;
   static final CPSType CPS_B_03;
   static final CPSType CPS_B_04;
   static final CPSType CPS_B_05;
   static final CPSType CPS_B_11;
   static final CPSType CPS_B_12;
   static final CPSType CPS_B_13;
   static final CPSType CPS_B_14;
   static final CPSType CPS_B_15;
   static final CPSType CPS_B_16;
   static final CPSType CPS_B_17;
   static final CPSType CPS_B_18;
   static final Map CPS_CONFIG;

   static {
      int[] var0 = new int[]{2, 4, 8, 0, 0};
      CPS_B_02 = new CPSType(96, 2, 0, 0, 0, 0, 108, new int[]{106, 104, 102, 100}, 98, var0);
      var0 = new int[]{32, 16, 8, 0, 0};
      CPS_B_03 = new CPSType(0, 0, 0, 0, 0, 0, 112, new int[]{110, 108, 106, 104}, 102, var0);
      var0 = new int[]{2, 12, 12, 0, 0};
      CPS_B_04 = new CPSType(96, 4, 0, 0, 0, 0, 110, new int[]{102, 112, 104, 114}, 106, var0);
      CPS_B_05 = new CPSType(96, 5, 0, 0, 0, 0, 104, new int[]{106, 108, 110, 112}, 114, new int[]{2, 8, 32, 20, 20});
      var0 = new int[]{8, 16, 32, 0, 0};
      CPS_B_11 = new CPSType(114, 1025, 0, 0, 0, 0, 102, new int[]{104, 106, 108, 110}, 112, var0);
      var0 = new int[]{2, 4, 8, 0, 0};
      CPS_B_12 = new CPSType(96, 1026, 0, 0, 0, 0, 108, new int[]{106, 104, 102, 100}, 98, var0);
      var0 = new int[]{32, 2, 4, 0, 0};
      CPS_B_13 = new CPSType(110, 1027, 0, 0, 0, 0, 98, new int[]{100, 102, 104, 106}, 108, var0);
      var0 = new int[]{8, 32, 16, 0, 0};
      CPS_B_14 = new CPSType(94, 1028, 0, 0, 0, 0, 82, new int[]{84, 86, 88, 90}, 92, var0);
      var0 = new int[]{4, 2, 32, 0, 0};
      CPS_B_15 = new CPSType(78, 1029, 0, 0, 0, 0, 66, new int[]{68, 70, 72, 74}, 76, var0);
      var0 = new int[]{16, 10, 10, 0, 0};
      CPS_B_16 = new CPSType(64, 1030, 0, 0, 0, 0, 76, new int[]{74, 72, 70, 68}, 66, var0);
      var0 = new int[]{8, 16, 2, 0, 0};
      CPS_B_17 = new CPSType(72, 1031, 0, 0, 0, 0, 84, new int[]{82, 80, 78, 76}, 74, var0);
      var0 = new int[]{16, 8, 2, 0, 0};
      CPS_B_18 = new CPSType(208, 1032, 0, 0, 0, 0, 220, new int[]{218, 216, 214, 212}, 210, var0);
      CPS_CONFIG = new HashMap();
      add("sf2", CPS_B_11, 2, 2, 2, 0, '\uffff', 0, '\uffff', 0);
   }

   private static void add(String var0, CPSType var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      CPS_CONFIG.put(var0, new CPSConfig(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9));
   }

   public static CPSConfig createConfig(String var0) {
      return (CPSConfig)CPS_CONFIG.get(var0);
   }
}
