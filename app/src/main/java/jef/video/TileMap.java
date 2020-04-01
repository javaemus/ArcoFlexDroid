package jef.video;

public class TileMap {
   public int cols;
   public int height;
   public int pen;
   public int rows;
   public Get_tile_info tile_info;
   public int type;
   public int width;

   public TileMap(Get_tile_info var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.tile_info = var1;
      this.type = var3;
      this.width = var4;
      this.height = var5;
      this.cols = var6;
      this.rows = var7;
   }
}
