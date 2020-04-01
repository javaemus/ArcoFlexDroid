package net.movegaga.jemu2;

public class Game implements Comparable {
   private String driver;
   private String name;
   private String online;
   private String sound;
   private String system;

   public Game(String var1, String var2, String var3, String var4) {
      this.driver = var1;
      this.name = var2;
      this.online = var4;
      this.sound = var3;
      if(var1.startsWith("msx2")) {
         this.system = "MSX2";
      } else if(var1.startsWith("msx")) {
         this.system = "MSX";
      } else if(var1.startsWith("sms")) {
         this.system = "Sega Master System";
      } else if(var1.startsWith("spec_")) {
         this.system = "ZX Spectrum";
      } else if(var1.startsWith("st_")) {
         this.system = "Atari ST";
      } else {
         this.system = "Arcade";
      }

   }

   public int compareTo(Object var1) {
      return this.name.compareTo(var1.toString());
   }

   public String getDriver() {
      return this.driver;
   }

   public String getName() {
      return this.name;
   }

   public String getOnline() {
      return this.online;
   }

   public String getSound() {
      return this.sound;
   }

   public String getSystem() {
      return this.system;
   }

   public String toString() {
      return this.name;
   }
}
