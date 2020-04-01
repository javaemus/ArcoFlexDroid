package net.movegaga.jemu2.driver.arcade.pacman;

public class PacPlusDecoder {
   private Pacman driver;

   public PacPlusDecoder(Pacman var1) {
      this.driver = var1;
   }

   int decrypt(int var1, int var2) {
      byte var3 = 1;
      switch(var1 & 677) {
      case 1:
      case 5:
      case 37:
      case 128:
      case 132:
      case 133:
      case 165:
      case 512:
      case 513:
      case 545:
      case 673:
      case 677:
         var3 = 2;
         break;
      case 4:
      case 32:
      case 36:
      case 160:
      case 164:
      case 516:
      case 544:
      case 548:
      case 641:
      case 645:
      case 672:
      case 676:
         var3 = 3;
      }

      int var4 = var3;
      if((var1 & 2048) == 2048) {
         var4 = var3 + 3;
      }

      switch(var4) {
      case 1:
         var1 = this.decrypt1(var2);
         break;
      case 2:
         var1 = this.decrypt2(var2);
         break;
      case 3:
         var1 = this.decrypt3(var2);
         break;
      case 4:
         var1 = this.decrypt4(var2);
         break;
      case 5:
         var1 = this.decrypt5(var2);
         break;
      case 6:
         var1 = this.decrypt6(var2);
         break;
      default:
         var1 = 0;
      }

      return var1;
   }

   int decrypt1(int var1) {
      return var1;
   }

   int decrypt2(int var1) {
      return (~var1 & 128) >> 5 | (~var1 & 64) << 1 | (var1 & 32) >> 2 | (var1 & 16) >> 4 | (var1 & 8) << 2 | (~var1 & 4) << 2 | (var1 & 2) << 5 | (~var1 & 1) << 1;
   }

   int decrypt3(int var1) {
      return (var1 & 128) >> 2 | (~var1 & 64) >> 2 | (~var1 & 32) >> 5 | (var1 & 16) >> 1 | (~var1 & 8) << 3 | (~var1 & 4) >> 0 | (var1 & 2) >> 0 | (~var1 & 1) << 7;
   }

   int decrypt4(int var1) {
      return (var1 & 128) >> 0 | (var1 & 64) >> 0 | (~var1 & 32) >> 0 | (var1 & 16) >> 0 | (~var1 & 8) >> 0 | (var1 & 4) >> 0 | (var1 & 2) >> 0 | (var1 & 1) >> 0;
   }

   int decrypt5(int var1) {
      return (~var1 & 128) >> 5 | (~var1 & 64) << 1 | (~var1 & 32) >> 0 | (var1 & 16) >> 4 | (~var1 & 8) >> 0 | (~var1 & 4) << 2 | (var1 & 2) << 5 | (~var1 & 1) << 1;
   }

   int decrypt6(int var1) {
      return (~var1 & 128) >> 4 | (~var1 & 64) >> 2 | (~var1 & 32) >> 5 | (var1 & 16) << 1 | (~var1 & 8) << 3 | (~var1 & 4) >> 0 | (var1 & 2) >> 0 | (~var1 & 1) << 7;
   }

   public void pacplus_decode() {
      for(int var1 = 0; var1 < 16384; ++var1) {
         this.driver.mem_cpu[var1] = (char)this.decrypt(var1, this.driver.mem_cpu[var1]);
      }

   }
}
