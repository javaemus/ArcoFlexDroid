package net.movegaga.jemu2;

import android.inputmethodservice.Keyboard;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import jef.machine.Emulator;

public class ControlsConfig {
   public static final String[] KEYS = new String[]{"controls.left", "controls.right", "controls.up", "controls.down", "controls.button1", "controls.button2", "controls.button3", "controls.button4", "controls.button5", "controls.button6"};
   public static final int KEY_BUTTON1 = 4;
   public static final int KEY_BUTTON2 = 5;
   public static final int KEY_BUTTON3 = 6;
   public static final int KEY_BUTTON4 = 7;
   public static final int KEY_BUTTON5 = 8;
   public static final int KEY_BUTTON6 = 9;
   public static final int KEY_DOWN = 3;
   public static final int KEY_LEFT = 0;
   private static final int[][] KEY_MAP = new int[0][];
   public static final int KEY_RIGHT = 1;
   public static final int KEY_UP = 2;
   private static final int[] MOUSE_BTN;
   private final int controller;
   private final int[] key_ids = new int[10];
   private final boolean[] key_states = new boolean[10];
   private final Map keysFields;
   private final Map keysValues;
   private Emulator machine;

   static {
      int[] var0 = new int[]{-1, -1, -1, -1, 0, 1, -1, -1, -1, -1};
      MOUSE_BTN = var0;
   }

   public ControlsConfig() {
      Field[] var2 = Keyboard.class.getDeclaredFields();
      this.keysValues = new HashMap();
      this.keysFields = new HashMap();

      int var1;
      for(var1 = 0; var1 < var2.length; ++var1) {
         if(var2[var1].getName().startsWith("KEY_")) {
            try {
               this.keysValues.put(var2[var1].getName(), var2[var1].get((Object)null).toString());
               this.keysFields.put(var2[var1].get((Object)null).toString(), var2[var1].getName());
            } catch (IllegalArgumentException var4) {
               var4.printStackTrace();
            } catch (IllegalAccessException var5) {
               var5.printStackTrace();
            }
         }
      }

      for(var1 = 0; var1 < this.key_ids.length; ++var1) {
         this.key_ids[var1] = this.getKey(Config.get(KEYS[var1]));
      }

      this.controller = 0;
   }

   private void processNetKey(int var1, int var2) {
      if(var2 == 0) {
         this.machine.keyRelease(var1);
      } else {
         this.machine.keyPress(var1);
      }

   }

   public Object[] getAllKeyValues() {
      return this.keysValues.entrySet().toArray();
   }

   public int getKey(String var1) {
      return Integer.parseInt((String)this.keysValues.get(var1));
   }

   public String getKeyField(String var1) {
      return Config.get(var1);
   }

   public void poll() {
      for(int var1 = 0; var1 < this.key_states.length; ++var1) {
         ;
      }

   }

   public void setKey(String var1, String var2) {
      Config.set(var1, var2);

      for(int var3 = 0; var3 < this.key_ids.length; ++var3) {
         if(var1.equals(KEYS[var3])) {
            this.key_ids[var3] = this.getKey(Config.get(KEYS[var3]));
            break;
         }
      }

   }

   public void setMachine(Emulator var1) {
      this.machine = var1;
   }
}
