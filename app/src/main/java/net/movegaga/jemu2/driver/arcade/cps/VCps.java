package net.movegaga.jemu2.driver.arcade.cps;

import net.movegaga.jemu2.JEmu2;
import net.movegaga.jemu2.driver.BaseVideo;

import java.util.Random;

import jef.map.MemoryMap;
import jef.map.ReadHandler;
import jef.map.VoidFunction;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VCps extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer {
   static final int CTT_16X16 = 8;
   static final int CTT_32X32 = 24;
   static final int CTT_8X8 = 0;
   static final int CTT_CARE = 2;
   static final int CTT_FLIPX = 1;
   static final int CTT_PMSK = 32;
   static final int CTT_ROWS = 4;
   int CU_BPP;
   boolean CU_CARE;
   boolean CU_FLIPX;
   boolean CU_PMSK;
   boolean CU_ROWS;
   int CU_SIZE;
   public int Cps = 1;
   int[] CpsCalc = new int[2];
   ReadHandler CpsFind0;
   ReadHandler CpsFind1;
   ReadHandler CpsFind2;
   ReadHandler CpsFind3;
   private int[] CpsGfx;
   int[] CpsId = new int[2];
   int[] CpsLayEn = new int[4];
   int CpsLcReg = 0;
   int[] CpsMult = new int[4];
   int[] CpsPal;
   int[] CpsPalSrc;
   int[] CpsPmReg = new int[4];
   int CpsRecalcPal;
   int[] CpsReg;
   int[] CpsSavePal;
   int[] CpsSaveReg;
   int CpsScrHigh;
   int CpsrBase;
   int CpsrRows;
   int CpstPal;
   int CpstPmsk;
   int[] CpstRowShift;
   boolean DEBUG = false;
   VoidFunction DO_PIX;
   int KnowBlank;
   int[] LayDraw;
   int LayMask;
   int LayerCont;
   int[] ObjMem;
   VoidFunction PIX;
   int[] PriGrid;
   int[] Rows;
   int RowsPlus;
   VoidFunction SKIP;
   int START_ADDR = 0;
   ReadHandler StdBigFind0 = new CStdBigFind0();
   ReadHandler StdBigFind1 = new CStdBigFind1();
   ReadHandler StdBigFind2 = new CStdBigFind2();
   ReadHandler StdBigFind3 = new CStdBigFind3();
   ReadHandler StdFind02 = new CStdFind02();
   ReadHandler StdFind1 = new CStdFind1();
   ReadHandler StdFind3 = new CStdFind3();
   int _Rows;
   ObjRect _objRect;
   int adv;
   int b;
   boolean bDone;
   BitMap black;
   int c;
   CpsrLineInfo[] cpsrLineInfo;
   int ctp;
   boolean do_pix;
   String game;
   MemoryMap memMap;
   int nBlank;
   int nBurnBpp;
   int nBurnLayer;
   int nBurnPitch;
   private int nCpsGfxLen;
   int nCpsPalCount;
   int nCpsrRowStart;
   int nCpsrScrX;
   int nCpsrScrY;
   int nCpstFlip;
   int nCpstTile;
   int nCpstType;
   int nCpstX;
   int nCpstY;
   int nCtvRollX;
   int nCtvRollY;
   int nCtvTileAdd;
   int nFrameCount;
   int nGetNext;
   int nMax;
   int nShiftY;
   ObjFrame[] of;
   int[] outPx;
   int pBurnDraw;
   int pCtvLine;
   int pCtvTile;
   int pPix;
   int pix;
   int plot_adv;
   int pmsk;
   Random random;
   int rx;
   int skip;
   char[] vram;
   int y;

   public VCps() {
      this.CpsFind0 = this.StdFind02;
      this.CpsFind1 = this.StdFind1;
      this.CpsFind2 = this.StdFind02;
      this.CpsFind3 = this.StdFind3;
      this.nBurnLayer = 16777215;
      this.pBurnDraw = 0;
      this.nBurnPitch = 384;
      this.nBurnBpp = 1;
      this.LayDraw = new int[]{-1, -1, -1, -1};
      this.LayMask = 0;
      this.LayerCont = 0;
      this.CpsRecalcPal = 0;
      this.random = new Random();
      this.CpsReg = new int[256];
      this.CpsSaveReg = new int[256];
      this.CpsSavePal = new int[4096];
      this.CpsrBase = -1;
      this.nCpsrScrX = 0;
      this.nCpsrScrY = 0;
      this.CpsrRows = 0;
      this.nCpsrRowStart = 0;
      this.nShiftY = 0;
      this.cpsrLineInfo = new CpsrLineInfo[15];
      this.nCpsPalCount = 2048;
      this.nMax = 0;
      this.nGetNext = 0;
      this.PriGrid = new int[336];
      this._objRect = new ObjRect();
      this.CpstPal = 0;
      this.nCpstType = 0;
      this.nCpstX = 0;
      this.nCpstY = 0;
      this.nCpstTile = 0;
      this.nCpstFlip = 0;
      this.CpstRowShift = null;
      this.CpstPmsk = 0;
      this.CpsScrHigh = 0;
      this.nCtvRollX = 0;
      this.nCtvRollY = 0;
      this.pCtvTile = 0;
      this.nCtvTileAdd = 0;
      this.pCtvLine = 0;
      this.nBlank = -1;
      this.adv = 1;
      this.skip = 1;
      this.do_pix = false;
      this.pix = 1;
   }

   private int BurnHighCol(int var1, int var2, int var3, int var4) {
      return var3 << 16 | var2 << 8 | var1;
   }

   private int DrawScroll1() {
      int var3 = this.READ_WORD(this.CpsSaveReg, 2);
      int var1 = this.READ_WORD(this.CpsSaveReg, 12);
      int var2 = this.READ_WORD(this.CpsSaveReg, 14);
      var3 = this.CpsFindGfxRam(var3 << 8 & 16760832, 16384);
      byte var4;
      if(var3 == -1) {
         var4 = 1;
      } else {
         this.CpsScr1Draw(var3, var1 + 64, var2 + 16);
         var4 = 0;
      }

      return var4;
   }

   private void DrawScroll2Exit() {
      this.CpsrBase = -1;
      this.nCpsrScrX = 0;
      this.nCpsrScrY = 0;
      this.CpsrRows = 0;
   }

   private int MEM_READ_WORD(int var1) {
      var1 -= 9437184;
      return this.vram[var1] << 8 | this.vram[var1 + 1];
   }

   private int MEM_READ_WORD(int var1, int var2) {
      var1 = var1 + var2 * 2 - 9437184;
      return this.vram[var1] << 8 | this.vram[var1 + 1];
   }

   private void MEM_WRITE_WORD(int var1, int var2, int var3) {
      var1 += var2 * 2;
      this.memMap.write(var1, var3 >> 8);
      this.memMap.write(var1 + 1, var3 & 255);
   }

   private int READ_INT(int[] var1, int var2) {
      int var3 = var2 + 1;
      int var4 = var1[var2];
      var2 = var3 + 1;
      return var4 << 24 | var1[var3] << 16 | var1[var2] << 8 | var1[var2 + 1];
   }

   private int READ_WORD(int[] var1, int var2) {
      return var1[var2] << 8 | var1[var2 + 1];
   }

   static int StdBigFind3(int var0) {
      return (var0 | 16384) << 9;
   }

   private void WRITE_WORD(int[] var1, int var2, int var3) {
      var1[var2] = var3 >> 8 & 255;
      var1[var2 + 1] = var3 & 255;
   }

   private void skip() {
      this.pPix += this.adv;
      switch(this.skip) {
      case 1:
         this.b <<= 4;
         break;
      case 2:
         this.b >>= 4;
      }

   }

   void CRP(int var1, int var2) {
      if(this.LayDraw[var1] == this.LayDraw[var2]) {
         this.LayDraw[var2] = -1;
      }

   }

   int CalcAll() {
      int var1 = 0;
      int var2 = 0;

      for(int var3 = 0; var3 < this.nCpsPalCount; ++var2) {
         this.CpsPal[var2] = this.CalcCol(this.READ_WORD(this.CpsPalSrc, var1 * 2), var3);
         ++var3;
         ++var1;
      }

      return 0;
   }

   int CalcCol(int var1, int var2) {
      int var5 = (('\uf000' & var1) >> 12) + 5;
      int var4 = ((var1 & 3840) >> 4) * var5 / 20;
      int var3 = (var1 & 240) * var5 / 20;
      var1 = ((var1 & 15) << 4) * var5 / 20;
      if(JEmu2.IS_APPLET) {
         var1 = this.BurnHighCol(var1, var3, var4, var2);
      } else {
         var1 = this.BurnHighCol(var4, var3, var1, var2);
      }

      return var1;
   }

   int CpsBgHigh(int var1) {
      var1 = this.READ_WORD(this.CpsSaveReg, this.CpsPmReg[var1 >> 7 & 3]);
      byte var2;
      if(var1 == 32767) {
         var2 = 0;
      } else if(var1 == 0) {
         var2 = 1;
      } else {
         this.nCpstType |= 32;
         this.CpstPmsk = var1;
         var2 = 0;
      }

      return var2;
   }

   int CpsDraw() {
      int[] var3 = this.getBackBuffer().getPixels();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = 0;
      }

      this.CpsPalUpdate(this.CpsSavePal, 1);
      this.CpsRecalcPal = 0;
      this.LayerCont = this.READ_WORD(this.CpsSaveReg, this.CpsLcReg);
      this.LayInit();
      this.DrawScroll2Init();
      this.LayDrawCps1();
      this.DrawScroll2Exit();
      return 0;
   }

   int CpsFindGfxRam(int var1, int var2) {
      var1 &= 16777215;
      if(var1 < 9437184 || var1 + var2 > 9633792) {
         var1 = -1;
      }

      return var1;
   }

   void CpsGetPalette(int var1, int var2) {
      int var3 = this.CpsFindGfxRam(this.READ_WORD(this.CpsReg, 10) << 8 & 16775168, 4096);
      if(var3 != -1) {
         int var4 = var1 << 10;

         for(var1 = 0; var1 < var2 << 10; ++var1) {
            this.CpsSavePal[var4 + var1] = this.memMap.read(var3 + var4 + var1);
         }
      }

   }

   int CpsObjDraw(int var1, int var2) {
      ObjFrame var16 = this.of[this.nGetNext];
      int var4 = var16.Obj;
      int var5 = 8;
      int var3 = var4;
      if(this.Cps == 1) {
         var3 = var4 + (var16.Count - 1 << 3);
         var5 = -8;
      }

      int var7 = 0;

      for(int var6 = var3; var7 < var16.Count; var6 += var5) {
         label79: {
            if(this.Cps == 2) {
               var3 = this.READ_WORD(this.ObjMem, var6) >> 13;
               if(var3 < var1 || var3 > var2) {
                  break label79;
               }
            }

            var4 = this.READ_WORD(this.ObjMem, var6);
            int var8 = this.READ_WORD(this.ObjMem, var6 + 2);
            int var12 = this.READ_WORD(this.ObjMem, var6 + 4);
            int var9 = this.READ_WORD(this.ObjMem, var6 + 6);
            if(this.Cps == 2) {
               var4 = (var4 & 1023 ^ 512) - 512;
               var3 = (var8 & 1023 ^ 512) - 512;
            } else {
               var3 = var4;
               if(var4 >= 448) {
                  var3 = var4 - 512;
               }

               var8 = (var8 & 511 ^ 256) - 256;
               var4 = var3;
               var3 = var8;
            }

            int var10 = var4 + var16.ShiftX;
            int var11 = var3 + var16.ShiftY;
            var3 = this.READ_WORD(this.ObjMem, var6 + 2);
            var12 = this.CpsFind0.read(var12 | (var3 & 24576) << 3);
            if(var12 >= 0) {
               this.CpstSetPal(var9 & 31);
               int var13 = var9 >> 5 & 3;
               int var14 = (var9 >> 8 & 15) + 1;
               int var15 = (var9 >> 12 & 15) + 1;
               if(var10 >= 0 && var11 >= 0 && (var14 << 4) + var10 <= 384 && (var15 << 4) + var11 <= 224) {
                  this.nCpstType = 8;
               } else {
                  this.nCpstType = 10;
               }

               this.nCpstFlip = var13;

               for(var3 = 0; var3 < var15; ++var3) {
                  for(var4 = 0; var4 < var14; ++var4) {
                     if((var13 & 1) != 0) {
                        var8 = var14 - var4 - 1;
                     } else {
                        var8 = var4;
                     }

                     if((var13 & 2) != 0) {
                        var9 = var15 - var3 - 1;
                     } else {
                        var9 = var3;
                     }

                     this.nCpstX = (var8 << 4) + var10;
                     this.nCpstY = (var9 << 4) + var11;
                     this.nCpstTile = (var3 << 11) + var12 + (var4 << 7);
                     this.CpstOne();
                  }
               }
            }
         }

         ++var7;
      }

      return 0;
   }

   int CpsObjGet() {
      int var1 = 0;
      ObjFrame var5 = this.of[this.nGetNext];
      var5.Count = 0;
      int var3 = var5.Obj;
      var5.ShiftX = -64;
      var5.ShiftY = -16;
      if(this.Cps != 2) {
         var1 = this.CpsFindGfxRam(this.READ_WORD(this.CpsReg, 0) << 8 & 16775168, 2048);
      }

      byte var6;
      if(var1 == -1) {
         var6 = 1;
      } else {
         this.memset((int[])this.PriGrid, 255, this.PriGrid.length);
         byte var4 = 0;
         int var2 = var1;

         for(var1 = var4; var1 < this.nMax && (this.Cps == 2 || this.MEM_READ_WORD(var2, 3) != '\uff00'); ++var1) {
            if((this.MEM_READ_WORD(var2, 0) | this.MEM_READ_WORD(var2, 3)) != 0) {
               for(int var7 = 0; var7 < 8; ++var7) {
                  this.ObjMem[var3 + var7] = this.memMap.read(var2 + var7);
               }

               ++var5.Count;
               var3 += 8;
            }

            var2 += 8;
         }

         ++this.nGetNext;
         if(this.nGetNext >= this.nFrameCount) {
            this.nGetNext = 0;
         }

         var6 = 0;
      }

      return var6;
   }

   int CpsObjInit() {
      this.nMax = 256;
      if(this.Cps == 2) {
         this.nMax = 1024;
      }

      if(this.Cps == 2) {
         this.nFrameCount = 3;
      } else {
         this.nFrameCount = 2;
      }

      this.ObjMem = this.malloc((this.nMax << 3) * this.nFrameCount);
      this.of = new ObjFrame[this.nFrameCount];

      for(int var1 = 0; var1 < this.nFrameCount; ++var1) {
         this.of[var1] = new ObjFrame();
         this.of[var1].Obj = (this.nMax << 3) * var1;
         this.of[var1].Count = 0;
      }

      this.nGetNext = 0;
      return 0;
   }

   int CpsPalInit() {
      int var1 = this.nCpsPalCount << 1;
      this.CpsPalSrc = this.malloc(var1);
      this.memset((int[])this.CpsPalSrc, 0, var1);
      this.CpsPal = this.malloc(this.nCpsPalCount);
      this.CalcAll();
      return 0;
   }

   int CpsPalUpdate(int[] var1, int var2) {
      int var3 = 0;
      byte var5 = 0;
      if(var2 != 0) {
         System.arraycopy(var1, 0, this.CpsPalSrc, 0, var1.length);
         this.CalcAll();
      } else {
         int var4 = 0;

         for(var2 = var5; var4 < this.nCpsPalCount; ++var3) {
            int var6 = this.READ_WORD(var1, var3 * 2);
            if(this.READ_WORD(this.CpsPalSrc, var2 * 2) != var6) {
               this.WRITE_WORD(this.CpsPalSrc, var2 * 2, var6);
               this.CpsPal[var4] = this.CalcCol(var6, var4);
            }

            ++var4;
            ++var2;
         }
      }

      return 0;
   }

   int CpsScr1Draw(int var1, int var2, int var3) {
      int var4 = -1;

      for(int var6 = -1; var6 < 28; ++var6) {
         for(int var7 = -1; var7 < 48; ++var7) {
            int var5 = (var3 >> 3) + 1 + var6;
            int var9 = var1 + ((var5 & 32) << 8 | ((var2 >> 3) + 1 + var7 & 63) << 7 | (var5 & 31) << 2);
            var5 = this.MEM_READ_WORD(var9 + 0);
            if(var5 != var4) {
               int var8 = this.CpsFind1.read(var5);
               if(var8 < 0) {
                  var4 = var5;
               } else {
                  var9 = this.MEM_READ_WORD(var9, 1);
                  this.nCpstType = 0;
                  if(this.CpsScrHigh != 0 && this.CpsBgHigh(var9) != 0) {
                     var4 = var5;
                  } else {
                     this.CpstSetPal(var9 & 31 | 32);
                     if(var7 >= 0 && var7 < 47 && var6 >= 0 && var6 < 27) {
                        this.nCpstType |= 0;
                     } else {
                        this.nCpstType |= 2;
                     }

                     this.nCpstX = (var7 << 3) + (8 - (var2 & 7));
                     this.nCpstY = (var6 << 3) + (8 - (var3 & 7));
                     this.nCpstTile = var8;
                     this.nCpstFlip = var9 >> 5 & 3;
                     if(this.CpstOne() != 0) {
                        var4 = var5;
                     }
                  }
               }
            }
         }
      }

      return 0;
   }

   int CpsScr3Draw(int var1, int var2, int var3) {
      int var4 = -1;

      for(int var6 = -1; var6 < 7; ++var6) {
         for(int var7 = -1; var7 < 12; ++var7) {
            int var5 = (var3 >> 5) + 1 + var6;
            int var9 = var1 + ((var5 & 56) << 8 | ((var2 >> 5) + 1 + var7 & 63) << 5 | (var5 & 7) << 2);
            var5 = this.MEM_READ_WORD(var9 + 0);
            if(var5 != var4) {
               int var8 = this.CpsFind3.read(var5);
               if(var8 < 0) {
                  var4 = var5;
               } else {
                  var9 = this.MEM_READ_WORD(var9, 1);
                  this.nCpstType = 0;
                  if(this.CpsScrHigh != 0 && this.CpsBgHigh(var9) != 0) {
                     var4 = var5;
                  } else {
                     this.CpstSetPal(var9 & 31 | 96);
                     if(var7 >= 0 && var7 < 11 && var6 >= 0 && var6 < 6) {
                        this.nCpstType |= 24;
                     } else {
                        this.nCpstType |= 26;
                     }

                     this.nCpstX = (var7 << 5) + (32 - (var2 & 31));
                     this.nCpstY = (var6 << 5) + (32 - (var3 & 31));
                     this.nCpstTile = var8;
                     this.nCpstFlip = var9 >> 5 & 3;
                     if(this.CpstOne() != 0) {
                        var4 = var5;
                     }
                  }
               }
            }
         }
      }

      return 0;
   }

   int CpsrPrepare() {
      byte var1;
      if(this.CpsrBase == -1) {
         var1 = 1;
      } else {
         this.nShiftY = 16 - (this.nCpsrScrY & 15);
         int var2 = -1;

         for(int var8 = 0; var2 < 14; ++var8) {
            int[] var6 = new int[1];
            int[] var7 = new int[1];
            if(this.CpsrRows != -1) {
               int var4 = (var2 << 4) + this.nShiftY;
               int var5 = var4 + 16;
               int var3 = var4;
               if(var4 < 0) {
                  var3 = 0;
               }

               var4 = var5;
               if(var5 > 224) {
                  var4 = 224;
               }

               this.GetRowsRange(var6, var7, var3 + this.nCpsrRowStart, var4 + this.nCpsrRowStart);
            }

            var6[0] += this.nCpsrScrX;
            var6[0] &= 1023;
            this.cpsrLineInfo[var8].nStart = var6[0];
            this.cpsrLineInfo[var8].nWidth = var7[0];
            this.cpsrLineInfo[var8].nTileStart = var6[0] >> 4;
            this.cpsrLineInfo[var8].nTileEnd = var6[0] + var7[0] + 399 >> 4;
            ++var2;
         }

         this.PrepareRows();
         var1 = 0;
      }

      return var1;
   }

   int CpsrRender() {
      byte var1;
      if(this.CpsrBase == -1) {
         var1 = 1;
      } else {
         this.KnowBlank = -1;
         int var2 = -1;

         for(int var3 = 0; var2 < 14; ++var3) {
            if(this.cpsrLineInfo[var3].nWidth == 0) {
               this.TileLine(var2, this.cpsrLineInfo[var3].nStart);
            } else {
               this.TileLineRows(var2, this.cpsrLineInfo[var3]);
            }

            ++var2;
         }

         var1 = 0;
      }

      return var1;
   }

   int CpstOne() {
      int var1 = (this.nCpstType & 24) + 8;
      if((this.nCpstType & 2) != 0) {
         if((this.nCpstType & 4) == 0) {
            if(this.nCpstX < -var1) {
               var1 = 0;
               return var1;
            }

            if(this.nCpstX >= 384) {
               var1 = 0;
               return var1;
            }

            if(this.nCpstY < -var1) {
               var1 = 0;
               return var1;
            }

            if(this.nCpstY >= 224) {
               var1 = 0;
               return var1;
            }
         }

         this.nCtvRollX = 1073742207 + this.nCpstX * 32767;
         this.nCtvRollY = 1073742047 + this.nCpstY * 32767;
      }

      if(this.nCpstTile >= this.nCpsGfxLen) {
         var1 = 1;
      } else {
         this.pCtvTile = this.nCpstTile;
         this.pCtvLine = this.pBurnDraw + this.nCpstY * this.nBurnPitch + this.nCpstX;
         if(var1 == 32) {
            this.nCtvTileAdd = 16;
         } else {
            this.nCtvTileAdd = 8;
         }

         if((this.nCpstFlip & 2) != 0) {
            if(var1 == 16) {
               this.nCtvTileAdd = -8;
               this.pCtvTile += 120;
            } else if(var1 == 32) {
               this.nCtvTileAdd = -16;
               this.pCtvTile += 496;
            } else {
               this.nCtvTileAdd = -8;
               this.pCtvTile += 56;
            }
         }

         var1 = this.CtvDoX(this.nCpstType & 62 | this.nCpstFlip & 1);
      }

      return var1;
   }

   void CpstSetPal(int var1) {
      this.CpstPal = var1 << 4 & 2032;
   }

   int CtvDoX(int var1) {
      this.nBlank = -1;
      this.outPx = this.getBackBuffer().getPixels();
      boolean var2;
      if((var1 & 1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.CU_FLIPX = var2;
      if((var1 & 2) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.CU_CARE = var2;
      if((var1 & 4) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.CU_ROWS = var2;
      this.CU_SIZE = (var1 & 24) + 8;
      if((var1 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.CU_PMSK = var2;
      this.CU_BPP = this.nBurnBpp;
      this.Rows = null;
      this._Rows = 0;
      this.RowsPlus = 0;
      this.pmsk = 0;
      this.outPx = this.getBackBuffer().getPixels();
      if(this.CU_ROWS) {
         this.Rows = this.CpstRowShift;
         this.RowsPlus = 1;
      }

      if(this.CU_PMSK) {
         this.pmsk = this.CpstPmsk;
      }

      this.ctp = this.CpstPal;

      for(this.y = 0; this.y < this.CU_SIZE; this._Rows += this.RowsPlus) {
         label91: {
            if(this.CU_CARE) {
               this.rx = this.nCtvRollX;
               if((this.nCtvRollY & 536887296) != 0) {
                  this.nCtvRollY += 32767;
                  break label91;
               }

               this.nCtvRollY += 32767;
            }

            this.pPix = this.pCtvLine;
            if(this.CU_ROWS) {
               this.pPix += this.Rows[this._Rows];
               if(this.CU_CARE) {
                  this.rx += this.Rows[this._Rows] * 32767;
               }
            }

            if(!this.CU_FLIPX) {
               this.skip = 1;
               if(!this.CU_PMSK) {
                  this.pix = 1;
               } else {
                  this.pix = 2;
               }
            } else {
               this.skip = 2;
               if(!this.CU_PMSK) {
                  this.pix = 3;
               } else {
                  this.pix = 4;
               }
            }

            if(this.CU_CARE) {
               this.do_pix = true;
            } else {
               this.do_pix = false;
            }

            switch(this.CU_SIZE) {
            case 8:
               this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 0);
               this.take_b();
               break;
            case 16:
               if(!this.CU_FLIPX) {
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 0);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 4);
                  this.take_b();
               } else {
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 4);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 0);
                  this.take_b();
               }
               break;
            case 32:
               if(!this.CU_FLIPX) {
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 0);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 4);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 8);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 12);
                  this.take_b();
               } else {
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 12);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 8);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 4);
                  this.take_b();
                  this.b = this.READ_INT(this.CpsGfx, this.pCtvTile + 0);
                  this.take_b();
               }
               break;
            default:
               throw new RuntimeException("Invalid CU_SIZE " + this.CU_SIZE);
            }
         }

         ++this.y;
         this.pCtvLine += this.nBurnPitch;
         this.pCtvTile += this.nCtvTileAdd;
      }

      byte var3;
      if(this.nBlank == -1) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      return var3;
   }

   int DrawScroll2Do() {
      byte var1;
      if(this.CpsrBase == -1) {
         var1 = 1;
      } else {
         this.CpsrRender();
         var1 = 0;
      }

      return var1;
   }

   int DrawScroll2Init() {
      this.CpsrBase = this.CpsFindGfxRam(this.READ_WORD(this.CpsSaveReg, 4) << 8 & 16760832, 16384);
      byte var1;
      if(this.CpsrBase == -1) {
         var1 = 1;
      } else {
         this.nCpsrScrX = this.READ_WORD(this.CpsSaveReg, 16);
         this.nCpsrScrX += 64;
         this.nCpsrScrX &= 1023;
         this.nCpsrScrY = this.READ_WORD(this.CpsSaveReg, 18);
         this.nCpsrScrY += 16;
         this.nCpsrScrY &= 1023;
         int var2 = this.READ_WORD(this.CpsSaveReg, 34);
         this.CpsrRows = -1;
         if((var2 & 1) != 0) {
            this.CpsrRows = this.CpsFindGfxRam(this.READ_WORD(this.CpsSaveReg, 8) << 8 & 16775168, 2048);
            this.nCpsrRowStart = this.READ_WORD(this.CpsSaveReg, 32) + 16;
         }

         this.CpsrPrepare();
         var1 = 0;
      }

      return var1;
   }

   int DrawScroll3() {
      int var3 = this.READ_WORD(this.CpsSaveReg, 6);
      int var2 = this.READ_WORD(this.CpsSaveReg, 20);
      int var1 = this.READ_WORD(this.CpsSaveReg, 22);
      var3 = this.CpsFindGfxRam(var3 << 8 & 16760832, 16384);
      byte var4;
      if(var3 == -1) {
         var4 = 1;
      } else {
         this.CpsScr3Draw(var3, var2 + 64, var1 + 16);
         var4 = 0;
      }

      return var4;
   }

   int FindTile(int var1, int var2) {
      return this.CpsrBase + ((var2 & 48) << 8 | (var1 & 63) << 6 | (var2 & 15) << 2);
   }

   ObjRect GetObjRect(int var1) {
      ObjRect var4 = this._objRect;
      int var3 = this.MEM_READ_WORD(var1, 0);
      int var2 = this.MEM_READ_WORD(var1, 1);
      var1 = this.MEM_READ_WORD(var1, 3);
      var3 = (var3 & 1023 ^ 512) - 512 - 64;
      var2 = (var2 & 1023 ^ 512) - 512 - 16;
      var4.x1 = var3;
      var4.y1 = var2;
      var4.x2 = ((var1 >> 8 & 15) + 1 << 4) + var3;
      var4.y2 = ((var1 >> 12 & 15) + 1 << 4) + var2;
      return var4;
   }

   void GetRowsRange(int[] var1, int[] var2, int var3, int var4) {
      int var6 = this.MEM_READ_WORD(this.CpsrRows, var3 & 1023) & 1023;
      byte var5 = 0;
      int var7 = var3;

      int var10;
      for(var3 = var5; var7 < var4; var3 = var10) {
         int var9 = ((this.MEM_READ_WORD(this.CpsrRows, var7 & 1023) & 1023) - var6 + 512 & 1023) - 512;
         int var8;
         if(var9 >= 0) {
            var8 = var6;
            var10 = var3;
            if(var9 >= var3) {
               var10 = var9;
               var8 = var6;
            }
         } else {
            var8 = var6 + var9 & 1023;
            var10 = var3 - var9;
         }

         ++var7;
         var6 = var8;
      }

      var4 = var3;
      if(var3 > 1024) {
         var4 = 1024;
      }

      var1[0] = var6;
      var2[0] = var4;
   }

   void LayDrawCps1() {
      for(int var1 = 3; var1 >= 0; --var1) {
         switch(this.LayDraw[var1]) {
         case 0:
            if((this.nBurnLayer & 1) != 0) {
               this.CpsObjDraw(0, 7);
            }

            if(var1 < 3) {
               this.CpsScrHigh = 1;
               switch(this.LayDraw[var1 + 1]) {
               case 1:
                  if((this.nBurnLayer & 16) != 0 && (this.LayMask & 2) != 0) {
                     this.DrawScroll1();
                  }
                  break;
               case 2:
                  if((this.nBurnLayer & 16) != 0 && (this.LayMask & 4) != 0) {
                     this.DrawScroll2Do();
                  }
                  break;
               case 3:
                  if((this.nBurnLayer & 16) != 0 && (this.LayMask & 8) != 0) {
                     this.DrawScroll3();
                  }
               }

               this.CpsScrHigh = 0;
            }
            break;
         case 1:
            if((this.nBurnLayer & 2) != 0 && (this.LayMask & 2) != 0) {
               this.DrawScroll1();
            }
            break;
         case 2:
            if((this.nBurnLayer & 4) != 0 && (this.LayMask & 4) != 0) {
               this.DrawScroll2Do();
            }
            break;
         case 3:
            if((this.nBurnLayer & 8) != 0 && (this.LayMask & 8) != 0) {
               this.DrawScroll3();
            }
         }
      }

   }

   void LayInit() {
      this.LayMask = 0;
      if((this.LayerCont & this.CpsLayEn[1]) != 0) {
         this.LayMask |= 2;
      }

      if((this.LayerCont & this.CpsLayEn[2]) != 0) {
         this.LayMask |= 4;
      }

      if((this.LayerCont & this.CpsLayEn[3]) != 0) {
         this.LayMask |= 8;
      }

      this.LayDraw[0] = this.LayerCont >> 12 & 3;
      this.LayDraw[1] = this.LayerCont >> 10 & 3;
      this.LayDraw[2] = this.LayerCont >> 8 & 3;
      this.LayDraw[3] = this.LayerCont >> 6 & 3;
      this.CRP(0, 1);
      this.CRP(0, 2);
      this.CRP(0, 3);
      this.CRP(1, 2);
      this.CRP(1, 3);
      this.CRP(2, 3);
   }

   int LoadObjRect(ObjRect var1) {
      int var4 = 255;
      int var3 = var1.y1;

      for(int var2 = var1.y1 * 24 + var1.x1; var3 < var1.y2; var2 += 24) {
         int var5 = var1.x1;

         int var7;
         for(int var6 = var2; var5 < var1.x2; var4 = var7) {
            int var8 = this.PriGrid[var6];
            var7 = var4;
            if(var8 < var4) {
               var7 = var8;
            }

            ++var5;
            ++var6;
         }

         ++var3;
      }

      return var4;
   }

   int PrepareRows() {
      int var1 = this.nShiftY - 16;
      int var4 = -1;

      int var8;
      for(int var5 = 0; var4 < 14; var1 = var8) {
         int var3 = 0;
         int var2 = 0;
         int var6;
         int var7;
         int var9;
         int var10;
         if(this.CpsrRows == -1) {
            var6 = (this.cpsrLineInfo[var5].nTileStart << 4) - this.nCpsrScrX;
            var7 = 0;

            for(var8 = 0; var7 < 16; ++var8) {
               this.cpsrLineInfo[var5].Rows[var8] = var6;
               ++var7;
            }

            var8 = var1;
            var9 = var6;
            var10 = var6;
         } else {
            var6 = 0;
            var7 = 0;

            while(true) {
               var10 = var3;
               var9 = var2;
               var8 = var1;
               if(var6 >= 16) {
                  break;
               }

               if(var1 >= 0 && var1 < 224) {
                  var10 = ((this.cpsrLineInfo[var5].nTileStart << 4) - this.nCpsrScrX - this.MEM_READ_WORD(this.CpsrRows, this.nCpsrRowStart + var1 & 1023) + 512 & 1023) - 512;
                  this.cpsrLineInfo[var5].Rows[var7] = var10;
                  if(var10 < var3) {
                     var9 = var10;
                     var8 = var2;
                  } else {
                     var9 = var3;
                     var8 = var2;
                     if(var10 > var2) {
                        var8 = var10;
                        var9 = var3;
                     }
                  }
               } else {
                  this.cpsrLineInfo[var5].Rows[var7] = 0;
                  var9 = var3;
                  var8 = var2;
               }

               ++var6;
               ++var7;
               ++var1;
               var3 = var9;
               var2 = var8;
            }
         }

         this.cpsrLineInfo[var5].nMaxLeft = var10;
         this.cpsrLineInfo[var5].nMaxRight = var9;
         ++var4;
         ++var5;
      }

      return 0;
   }

   void QuickFix(int var1) {
      int var4 = this.MEM_READ_WORD(var1, 0) >> 13;
      if(var4 != 4) {
         ObjRect var5 = this.GetObjRect(var1);
         this.RectToInt(var5);
         int var3 = this.LoadObjRect(var5);
         int var2 = var4;
         if(var4 > var3) {
            var2 = var3;
            this.MEM_WRITE_WORD(var1, 0, this.MEM_READ_WORD(var1, 0) & 8191);
            this.MEM_WRITE_WORD(var1, 0, this.MEM_READ_WORD(var1, 0) | var3 << 13);
         }

         this.SaveObjRect(var5, var2);
      }

   }

   void RectToInt(ObjRect var1) {
      var1.x1 >>= 4;
      if(var1.x1 < 0) {
         var1.x1 = 0;
      }

      var1.y1 >>= 4;
      if(var1.y1 < 0) {
         var1.y1 = 0;
      }

      var1.x2 = var1.x2 + 15 >> 4;
      if(var1.x2 > 24) {
         var1.x2 = 24;
      }

      var1.y2 = var1.y2 + 15 >> 4;
      if(var1.y2 > 14) {
         var1.y2 = 14;
      }

   }

   void SaveObjRect(ObjRect var1, int var2) {
      int var4 = var1.y1;

      for(int var3 = var1.y1 * 24 + var1.x1; var4 < var1.y2; var3 += 24) {
         int var6 = var1.x1;

         for(int var5 = var3; var6 < var1.x2; ++var5) {
            this.PriGrid[var5] = var2;
            ++var6;
         }

         ++var4;
      }

   }

   void TileLine(int var1, int var2) {
      boolean var3 = false;
      if(var1 < 0 || var1 >= 13) {
         var3 = true;
      }

      int var4 = this.nCpsrScrY;
      int var5 = this.nCpsrScrY;
      this.nCpstY = (var1 << 4) + (16 - (var4 & 15));

      for(var4 = -1; var4 < 24; ++var4) {
         if(!var3 && var4 >= 0 && var4 < 23) {
            this.nCpstType = 8;
         } else {
            this.nCpstType = 10;
         }

         int var8 = this.FindTile((var2 >> 4) + 1 + var4, (var5 >> 4) + 1 + var1);
         int var6 = this.MEM_READ_WORD(var8);
         if(var6 != this.KnowBlank) {
            int var7 = this.CpsFind2.read(var6);
            if(var7 < 0) {
               this.KnowBlank = var6;
            } else {
               var8 = this.MEM_READ_WORD(var8, 1);
               this.nCpstType &= -33;
               if(this.CpsScrHigh != 0 && this.CpsBgHigh(var8) != 0) {
                  this.KnowBlank = var6;
               } else {
                  this.CpstSetPal(var8 & 31 | 64);
                  this.nCpstX = (var4 << 4) + (16 - (var2 & 15));
                  this.nCpstTile = var7;
                  this.nCpstFlip = var8 >> 5 & 3;
                  if(this.CpstOne() != 0) {
                     this.KnowBlank = var6;
                  }
               }
            }
         }
      }

   }

   void TileLineRows(int var1, CpsrLineInfo var2) {
      boolean var3 = false;
      if(var1 < 0 || var1 >= 13) {
         var3 = true;
      }

      int var11 = var2.nTileEnd;
      int var10 = var2.nTileStart;
      int var4 = this.nCpsrScrY;
      int var9 = this.nCpsrScrY;
      this.nCpstY = (var1 << 4) + (16 - (var4 & 15));
      this.CpstRowShift = var2.Rows;
      int var8 = var2.nMaxLeft;
      int var6 = var2.nMaxRight;

      for(int var7 = 0; var7 < var11 - var10; var6 += 16) {
         int var12 = var2.nTileStart;
         boolean var13 = var3;
         if(!var3) {
            var13 = var3;
            if(var8 < 0) {
               var13 = true;
            }

            if(var6 > 368) {
               var13 = true;
            }
         }

         if(var13) {
            this.nCpstType = 14;
         } else {
            this.nCpstType = 12;
         }

         var12 = this.FindTile(var12 + var7, (var9 >> 4) + 1 + var1);
         int var5 = this.MEM_READ_WORD(var12 + 0);
         if(var5 != this.KnowBlank) {
            var4 = this.CpsFind2.read(var5);
            if(var4 < 0) {
               this.KnowBlank = var5;
            } else {
               var12 = this.MEM_READ_WORD(var12, 1);
               this.nCpstType &= -33;
               if(this.CpsScrHigh == 0 || this.CpsBgHigh(var12) == 0) {
                  this.CpstSetPal(var12 & 31 | 64);
                  this.nCpstX = var7 << 4;
                  this.nCpstTile = var4;
                  this.nCpstFlip = var12 >> 5 & 3;
                  if(this.CpstOne() != 0) {
                     this.KnowBlank = var5;
                  }
               }
            }
         }

         ++var7;
         var8 += 16;
      }

   }

   int _cps1_output_r(int var1) {
      if(var1 == this.CpsId[0]) {
         var1 = this.CpsId[1] >> 8;
      } else if(var1 == this.CpsId[0] + 1) {
         var1 = this.CpsId[1] & 255;
      } else {
         var1 = this.CpsReg[var1];
      }

      return var1;
   }

   void _cps1_output_w(int var1, int var2) {
      this.CpsReg[var1] = var2;
   }

   public ReadHandler cps1_output_r() {
      return new Cps1_output_r();
   }

   public WriteHandler cps1_output_w() {
      return new Cps1_output_w();
   }

   public void do_pix_true() {
      if((this.rx & 536887296) != 0) {
         this.skip();
      } else {
         this.pix();
      }

      this.rx += 32767;
   }

   public int initVideo() {
      int var1;
      for(var1 = 0; var1 < 15; ++var1) {
         this.cpsrLineInfo[var1] = new CpsrLineInfo();
      }

      this.nCpsGfxLen = this.CpsGfx.length;

      for(var1 = 0; var1 < 31 && 1 << var1 < this.nCpsGfxLen; ++var1) {
         ;
      }

      if(this.nCpsGfxLen <= 8388608) {
         this.CpsFind0 = this.StdFind02;
         this.CpsFind1 = this.StdFind1;
         this.CpsFind2 = this.StdFind02;
         this.CpsFind3 = this.StdFind3;
      } else {
         this.CpsFind0 = this.StdBigFind0;
         this.CpsFind1 = this.StdBigFind1;
         this.CpsFind2 = this.StdBigFind2;
         this.CpsFind3 = this.StdBigFind3;
      }

      this.CpsLcReg = 102;
      this.CpsPmReg[0] = 104;
      this.CpsPmReg[1] = 106;
      this.CpsPmReg[2] = 108;
      this.CpsPmReg[3] = 110;
      this.CpsLayEn[1] = 2;
      this.CpsLayEn[2] = 4;
      this.CpsLayEn[3] = 8;
      if(this.game == null || this.game.equals("sf2")) {
         this.CpsId[0] = 72;
         this.CpsId[1] = 1031;
         this.CpsLcReg = 84;
         this.CpsPmReg[0] = 82;
         this.CpsPmReg[1] = 80;
         this.CpsPmReg[2] = 78;
         this.CpsPmReg[3] = 76;
         this.CpsLayEn[1] = 8;
         this.CpsLayEn[2] = 16;
         this.CpsLayEn[3] = 2;
         this.CpsFind1 = new CSf2Find1();
         this.CpsFind2 = new CSf2Find2();
         this.CpsFind3 = new CSf2Find3();
      }

      this.CpsPalInit();
      this.CpsObjInit();
      this.black = new BitMapImpl(this.getBackBuffer().getWidth(), this.getBackBuffer().getHeight());
      return 0;
   }

   protected int[] malloc(int var1) {
      return new int[var1];
   }

   protected void memcpy(int[] var1, int[] var2, int var3) {
      System.arraycopy(var2, 0, var1, 0, var3);
   }

   protected void memset(int[] var1, int var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         var1[var4] = var2;
      }

   }

   public final void memset(boolean[] var1, int var2, int var3) {
      boolean var4;
      if(var2 == 0) {
         var4 = false;
      } else {
         var4 = true;
      }

      for(var2 = 0; var2 < var3; ++var2) {
         var1[var2] = var4;
      }

   }

   public void pix() {
      int var1;
      int[] var2;
      switch(this.pix) {
      case 1:
         if((this.b & -268435456) != 0) {
            this.c = this.CpsPal[this.ctp + (this.b >>> 28) ^ 15];
            var2 = this.outPx;
            var1 = this.pPix;
            this.pPix = var1 + 1;
            var2[var1] = this.c;
            this.b <<= 4;
         } else {
            this.skip();
         }
         break;
      case 2:
         this.c = this.b >> 28 ^ 15;
         if((this.pmsk & 1 << this.c) != 0) {
            this.c = this.CpsPal[this.ctp + this.c];
            var2 = this.outPx;
            var1 = this.pPix;
            this.pPix = var1 + 1;
            var2[var1] = this.c;
            this.b <<= 4;
         } else {
            this.skip();
         }
         break;
      case 3:
         if((this.b & 15) != 0) {
            this.c = this.CpsPal[this.ctp + (this.b & 15) ^ 15];
            var2 = this.outPx;
            var1 = this.pPix;
            this.pPix = var1 + 1;
            var2[var1] = this.c;
            this.b >>>= 4;
         } else {
            this.skip();
         }
         break;
      case 4:
         this.c = this.b & 15 ^ 15;
         if((this.pmsk & 1 << this.c) != 0) {
            this.c = this.CpsPal[this.ctp + this.c];
            var2 = this.outPx;
            var1 = this.pPix;
            this.pPix = var1 + 1;
            var2[var1] = this.c;
            this.b >>>= 4;
         } else {
            this.skip();
         }
      }

   }

   public BitMap renderVideo() {
      this.CpsGetPalette(0, 4);
      this.CpsObjGet();
      this.memcpy(this.CpsSaveReg, this.CpsReg, 256);
      this.CpsDraw();
      return this.getBackBuffer();
   }

   public void renderVideoPost() {
   }

   public void setRegions(int[] var1, MemoryMap var2) {
      this.CpsGfx = var1;
      this.memMap = var2;
      this.vram = var2.findMemory(9437184, 9633791);
   }

   void take_b() {
      this.nBlank &= this.b;
      this.b = ~this.b;
      if(this.do_pix) {
         this.do_pix_true();
         this.do_pix_true();
         this.do_pix_true();
         this.do_pix_true();
         this.do_pix_true();
         this.do_pix_true();
         this.do_pix_true();
         this.do_pix_true();
      } else {
         this.pix();
         this.pix();
         this.pix();
         this.pix();
         this.pix();
         this.pix();
         this.pix();
         this.pix();
      }

   }

   public class CSf2Find1 implements ReadHandler {
      public int read(int var1) {
         return 65536 + var1 << 6;
      }
   }

   public class CSf2Find2 implements ReadHandler {
      public int read(int var1) {
         return '耀' + var1 << 7;
      }
   }

   public class CSf2Find3 implements ReadHandler {
      public int read(int var1) {
         return var1 + 8192 << 9;
      }
   }

   public class CStdBigFind0 implements ReadHandler {
      public int read(int var1) {
         return var1 << 7;
      }
   }

   public class CStdBigFind1 implements ReadHandler {
      public int read(int var1) {
         return (131072 | var1) << 6;
      }
   }

   public class CStdBigFind2 implements ReadHandler {
      public int read(int var1) {
         return (65536 | var1) << 7;
      }
   }

   public class CStdBigFind3 implements ReadHandler {
      public int read(int var1) {
         return (var1 | 16384) << 9;
      }
   }

   public class CStdFind02 implements ReadHandler {
      public int read(int var1) {
         return ('\uffff' & var1) << 7;
      }
   }

   public class CStdFind1 implements ReadHandler {
      public int read(int var1) {
         return (131071 & var1) << 6;
      }
   }

   public class CStdFind3 implements ReadHandler {
      public int read(int var1) {
         return (var1 & 16383) << 9;
      }
   }

   public class Cps1_output_r implements ReadHandler {
      public int read(int var1) {
         return VCps.this._cps1_output_r(var1);
      }
   }

   public class Cps1_output_w implements WriteHandler {
      public void write(int var1, int var2) {
         VCps.this._cps1_output_w(var1, var2);
      }
   }

   class CpsrLineInfo {
      int[] Rows = new int[16];
      int nMaxLeft;
      int nMaxRight;
      int nStart;
      int nTileEnd;
      int nTileStart;
      int nWidth;
   }

   class ObjFrame {
      int Count;
      int Obj;
      int ShiftX;
      int ShiftY;
   }

   class ObjRect {
      int x1;
      int x2;
      int y1;
      int y2;
   }
}
