package jef.sound.chip.ym2151;

import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.sound.SoundChip;
import jef.util.INT32;

public class YM2151 extends SoundChip {
   private static final byte[] DT1_tab;
   private static final int[] DT2_tab;
   private static final int EG_ATT = 4;
   private static final int EG_DEC = 3;
   private static final int EG_OFF = 0;
   private static final int EG_REL = 1;
   private static final int EG_SUS = 2;
   private static final int ENV_BITS = 10;
   private static final int ENV_LEN = 1024;
   private static final int ENV_MASK = 65535;
   private static final int ENV_QUIET = 832;
   private static final int ENV_SH = 16;
   private static final double ENV_STEP = 0.125D;
   private static final int FREQ_MASK = 65535;
   private static final int FREQ_SH = 16;
   private static final int LFO_BITS = 9;
   private static final int LFO_LEN = 512;
   private static final int LFO_MASK = 511;
   private static final int LFO_SH = 23;
   private static final int MAX_ATT_INDEX = 67108863;
   private static final int MIN_ATT_INDEX = 65535;
   private static final double PI = 3.141592653589793D;
   private static final int SIN_BITS = 10;
   private static final int SIN_LEN = 1024;
   private static final int SIN_MASK = 1023;
   private static final int TIMER_SH = 16;
   private static final int TL_RES_LEN = 256;
   private static int[] TL_TAB = new int[6656];
   private static final int TL_TAB_LEN = 6656;
   private static INT32 c1;
   private static INT32 c2;
   private static INT32[] chanout;
   private static int[] d1lTab;
   private static int[] lfoMDTab;
   private static int[] lfoTab = new int[4096];
   private static INT32 m2;
   private static final short[] phaseinc_rom;
   private static int[] sinTab = new int[1024];
   int amd;
   int clock;
   int csmReq;
   int ct;
   int[] dt1Freq;
   int[] egTabDlt;
   int[] freq;
   int irqEnable;
   YMInterruptHandler irqHandler;
   int lfa;
   int lfoFreq;
   int[] lfoFreqDlt;
   int lfoPhase;
   int lfoWave;
   int lfp;
   int noise;
   int noiseF;
   int noiseP;
   int noiseRNG;
   int[] noiseTab;
   Operator[] operators;
   int[] pan;
   int pmd;
   int regPort;
   int sampfreq;
   int status;
   int test;
   int timA;
   int timAIndex;
   int timAOldIndex;
   int timAVal;
   int timB;
   int timBIndex;
   int timBOldIndex;
   int timBVal;
   int[] timerA;
   int[] timerB;
   private float volume;
   WriteHandler writeHandler;

   static {
      int[] var0 = new int[]{0, 384, 500, 608};
      DT2_tab = var0;
      byte[] var1 = new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)2, (byte)2, (byte)2, (byte)2, (byte)2, (byte)3, (byte)3, (byte)3, (byte)4, (byte)4, (byte)4, (byte)5, (byte)5, (byte)6, (byte)6, (byte)7, (byte)8, (byte)8, (byte)8, (byte)8, (byte)1, (byte)1, (byte)1, (byte)1, (byte)2, (byte)2, (byte)2, (byte)2, (byte)2, (byte)3, (byte)3, (byte)3, (byte)4, (byte)4, (byte)4, (byte)5, (byte)5, (byte)6, (byte)6, (byte)7, (byte)8, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)16, (byte)16, (byte)16, (byte)16, (byte)2, (byte)2, (byte)2, (byte)2, (byte)2, (byte)3, (byte)3, (byte)3, (byte)4, (byte)4, (byte)4, (byte)5, (byte)5, (byte)6, (byte)6, (byte)7, (byte)8, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)16, (byte)17, (byte)19, (byte)20, (byte)22, (byte)22, (byte)22, (byte)22};
      DT1_tab = var1;
      phaseinc_rom = new short[]{(short)1299, (short)1300, (short)1301, (short)1302, (short)1303, (short)1304, (short)1305, (short)1306, (short)1308, (short)1309, (short)1310, (short)1311, (short)1313, (short)1314, (short)1315, (short)1316, (short)1318, (short)1319, (short)1320, (short)1321, (short)1322, (short)1323, (short)1324, (short)1325, (short)1327, (short)1328, (short)1329, (short)1330, (short)1332, (short)1333, (short)1334, (short)1335, (short)1337, (short)1338, (short)1339, (short)1340, (short)1341, (short)1342, (short)1343, (short)1344, (short)1346, (short)1347, (short)1348, (short)1349, (short)1351, (short)1352, (short)1353, (short)1354, (short)1356, (short)1357, (short)1358, (short)1359, (short)1361, (short)1362, (short)1363, (short)1364, (short)1366, (short)1367, (short)1368, (short)1369, (short)1371, (short)1372, (short)1373, (short)1374, (short)1376, (short)1377, (short)1378, (short)1379, (short)1381, (short)1382, (short)1383, (short)1384, (short)1386, (short)1387, (short)1388, (short)1389, (short)1391, (short)1392, (short)1393, (short)1394, (short)1396, (short)1397, (short)1398, (short)1399, (short)1401, (short)1402, (short)1403, (short)1404, (short)1406, (short)1407, (short)1408, (short)1409, (short)1411, (short)1412, (short)1413, (short)1414, (short)1416, (short)1417, (short)1418, (short)1419, (short)1421, (short)1422, (short)1423, (short)1424, (short)1426, (short)1427, (short)1429, (short)1430, (short)1431, (short)1432, (short)1434, (short)1435, (short)1437, (short)1438, (short)1439, (short)1440, (short)1442, (short)1443, (short)1444, (short)1445, (short)1447, (short)1448, (short)1449, (short)1450, (short)1452, (short)1453, (short)1454, (short)1455, (short)1458, (short)1459, (short)1460, (short)1461, (short)1463, (short)1464, (short)1465, (short)1466, (short)1468, (short)1469, (short)1471, (short)1472, (short)1473, (short)1474, (short)1476, (short)1477, (short)1479, (short)1480, (short)1481, (short)1482, (short)1484, (short)1485, (short)1486, (short)1487, (short)1489, (short)1490, (short)1492, (short)1493, (short)1494, (short)1495, (short)1497, (short)1498, (short)1501, (short)1502, (short)1503, (short)1504, (short)1506, (short)1507, (short)1509, (short)1510, (short)1512, (short)1513, (short)1514, (short)1515, (short)1517, (short)1518, (short)1520, (short)1521, (short)1523, (short)1524, (short)1525, (short)1526, (short)1528, (short)1529, (short)1531, (short)1532, (short)1534, (short)1535, (short)1536, (short)1537, (short)1539, (short)1540, (short)1542, (short)1543, (short)1545, (short)1546, (short)1547, (short)1548, (short)1550, (short)1551, (short)1553, (short)1554, (short)1556, (short)1557, (short)1558, (short)1559, (short)1561, (short)1562, (short)1564, (short)1565, (short)1567, (short)1568, (short)1569, (short)1570, (short)1572, (short)1573, (short)1575, (short)1576, (short)1578, (short)1579, (short)1580, (short)1581, (short)1583, (short)1584, (short)1586, (short)1587, (short)1590, (short)1591, (short)1592, (short)1593, (short)1595, (short)1596, (short)1598, (short)1599, (short)1601, (short)1602, (short)1604, (short)1605, (short)1607, (short)1608, (short)1609, (short)1610, (short)1613, (short)1614, (short)1615, (short)1616, (short)1618, (short)1619, (short)1621, (short)1622, (short)1624, (short)1625, (short)1627, (short)1628, (short)1630, (short)1631, (short)1632, (short)1633, (short)1637, (short)1638, (short)1639, (short)1640, (short)1642, (short)1643, (short)1645, (short)1646, (short)1648, (short)1649, (short)1651, (short)1652, (short)1654, (short)1655, (short)1656, (short)1657, (short)1660, (short)1661, (short)1663, (short)1664, (short)1666, (short)1667, (short)1669, (short)1670, (short)1672, (short)1673, (short)1675, (short)1676, (short)1678, (short)1679, (short)1681, (short)1682, (short)1685, (short)1686, (short)1688, (short)1689, (short)1691, (short)1692, (short)1694, (short)1695, (short)1697, (short)1698, (short)1700, (short)1701, (short)1703, (short)1704, (short)1706, (short)1707, (short)1709, (short)1710, (short)1712, (short)1713, (short)1715, (short)1716, (short)1718, (short)1719, (short)1721, (short)1722, (short)1724, (short)1725, (short)1727, (short)1728, (short)1730, (short)1731, (short)1734, (short)1735, (short)1737, (short)1738, (short)1740, (short)1741, (short)1743, (short)1744, (short)1746, (short)1748, (short)1749, (short)1751, (short)1752, (short)1754, (short)1755, (short)1757, (short)1759, (short)1760, (short)1762, (short)1763, (short)1765, (short)1766, (short)1768, (short)1769, (short)1771, (short)1773, (short)1774, (short)1776, (short)1777, (short)1779, (short)1780, (short)1782, (short)1785, (short)1786, (short)1788, (short)1789, (short)1791, (short)1793, (short)1794, (short)1796, (short)1798, (short)1799, (short)1801, (short)1802, (short)1804, (short)1806, (short)1807, (short)1809, (short)1811, (short)1812, (short)1814, (short)1815, (short)1817, (short)1819, (short)1820, (short)1822, (short)1824, (short)1825, (short)1827, (short)1828, (short)1830, (short)1832, (short)1833, (short)1835, (short)1837, (short)1838, (short)1840, (short)1841, (short)1843, (short)1845, (short)1846, (short)1848, (short)1850, (short)1851, (short)1853, (short)1854, (short)1856, (short)1858, (short)1859, (short)1861, (short)1864, (short)1865, (short)1867, (short)1868, (short)1870, (short)1872, (short)1873, (short)1875, (short)1877, (short)1879, (short)1880, (short)1882, (short)1884, (short)1885, (short)1887, (short)1888, (short)1891, (short)1892, (short)1894, (short)1895, (short)1897, (short)1899, (short)1900, (short)1902, (short)1904, (short)1906, (short)1907, (short)1909, (short)1911, (short)1912, (short)1914, (short)1915, (short)1918, (short)1919, (short)1921, (short)1923, (short)1925, (short)1926, (short)1928, (short)1930, (short)1932, (short)1933, (short)1935, (short)1937, (short)1939, (short)1940, (short)1942, (short)1944, (short)1946, (short)1947, (short)1949, (short)1951, (short)1953, (short)1954, (short)1956, (short)1958, (short)1960, (short)1961, (short)1963, (short)1965, (short)1967, (short)1968, (short)1970, (short)1972, (short)1975, (short)1976, (short)1978, (short)1980, (short)1982, (short)1983, (short)1985, (short)1987, (short)1989, (short)1990, (short)1992, (short)1994, (short)1996, (short)1997, (short)1999, (short)2001, (short)2003, (short)2004, (short)2006, (short)2008, (short)2010, (short)2011, (short)2013, (short)2015, (short)2017, (short)2019, (short)2021, (short)2022, (short)2024, (short)2026, (short)2028, (short)2029, (short)2032, (short)2033, (short)2035, (short)2037, (short)2039, (short)2041, (short)2043, (short)2044, (short)2047, (short)2048, (short)2050, (short)2052, (short)2054, (short)2056, (short)2058, (short)2059, (short)2062, (short)2063, (short)2065, (short)2067, (short)2069, (short)2071, (short)2073, (short)2074, (short)2077, (short)2078, (short)2080, (short)2082, (short)2084, (short)2086, (short)2088, (short)2089, (short)2092, (short)2093, (short)2095, (short)2097, (short)2099, (short)2101, (short)2103, (short)2104, (short)2107, (short)2108, (short)2110, (short)2112, (short)2114, (short)2116, (short)2118, (short)2119, (short)2122, (short)2123, (short)2125, (short)2127, (short)2129, (short)2131, (short)2133, (short)2134, (short)2137, (short)2139, (short)2141, (short)2142, (short)2145, (short)2146, (short)2148, (short)2150, (short)2153, (short)2154, (short)2156, (short)2158, (short)2160, (short)2162, (short)2164, (short)2165, (short)2168, (short)2170, (short)2172, (short)2173, (short)2176, (short)2177, (short)2179, (short)2181, (short)2185, (short)2186, (short)2188, (short)2190, (short)2192, (short)2194, (short)2196, (short)2197, (short)2200, (short)2202, (short)2204, (short)2205, (short)2208, (short)2209, (short)2211, (short)2213, (short)2216, (short)2218, (short)2220, (short)2222, (short)2223, (short)2226, (short)2227, (short)2230, (short)2232, (short)2234, (short)2236, (short)2238, (short)2239, (short)2242, (short)2243, (short)2246, (short)2249, (short)2251, (short)2253, (short)2255, (short)2256, (short)2259, (short)2260, (short)2263, (short)2265, (short)2267, (short)2269, (short)2271, (short)2272, (short)2275, (short)2276, (short)2279, (short)2281, (short)2283, (short)2285, (short)2287, (short)2288, (short)2291, (short)2292, (short)2295, (short)2297, (short)2299, (short)2301, (short)2303, (short)2304, (short)2307, (short)2308, (short)2311, (short)2315, (short)2317, (short)2319, (short)2321, (short)2322, (short)2325, (short)2326, (short)2329, (short)2331, (short)2333, (short)2335, (short)2337, (short)2338, (short)2341, (short)2342, (short)2345, (short)2348, (short)2350, (short)2352, (short)2354, (short)2355, (short)2358, (short)2359, (short)2362, (short)2364, (short)2366, (short)2368, (short)2370, (short)2371, (short)2374, (short)2375, (short)2378, (short)2382, (short)2384, (short)2386, (short)2388, (short)2389, (short)2392, (short)2393, (short)2396, (short)2398, (short)2400, (short)2402, (short)2404, (short)2407, (short)2410, (short)2411, (short)2414, (short)2417, (short)2419, (short)2421, (short)2423, (short)2424, (short)2427, (short)2428, (short)2431, (short)2433, (short)2435, (short)2437, (short)2439, (short)2442, (short)2445, (short)2446, (short)2449, (short)2452, (short)2454, (short)2456, (short)2458, (short)2459, (short)2462, (short)2463, (short)2466, (short)2468, (short)2470, (short)2472, (short)2474, (short)2477, (short)2480, (short)2481, (short)2484, (short)2488, (short)2490, (short)2492, (short)2494, (short)2495, (short)2498, (short)2499, (short)2502, (short)2504, (short)2506, (short)2508, (short)2510, (short)2513, (short)2516, (short)2517, (short)2520, (short)2524, (short)2526, (short)2528, (short)2530, (short)2531, (short)2534, (short)2535, (short)2538, (short)2540, (short)2542, (short)2544, (short)2546, (short)2549, (short)2552, (short)2553, (short)2556, (short)2561, (short)2563, (short)2565, (short)2567, (short)2568, (short)2571, (short)2572, (short)2575, (short)2577, (short)2579, (short)2581, (short)2583, (short)2586, (short)2589, (short)2590, (short)2593};
   }

   public YM2151(float var1, int var2, YMInterruptHandler var3, WriteHandler var4) {
      this.irqHandler = var3;
      this.writeHandler = var4;
      this.volume = var1;
      this.clock = var2;
   }

   private final void advance() {
      if((this.test & 2) == 0) {
         this.lfoPhase += this.lfoFreq;
      }

      this.noiseP += this.noiseF;
      int var1 = this.noiseP >> 16;

      for(this.noiseP &= '\uffff'; var1 != 0; --var1) {
         this.noiseRNG = unsigned((this.noiseRNG ^ this.noiseRNG >> 3) & 1 ^ 1) << 16 | this.noiseRNG >> 1;
      }

      int var2;
      Operator var6;
      if(this.csmReq != 0) {
         if(this.csmReq == 2) {
            var6 = this.operators[0];
            var1 = 32;

            do {
               if(var6.key == 0) {
                  var6.phase = 0;
                  var6.state = 4;
               }

               var2 = var1 - 1;
               if(var2 != 0) {
                  var6 = this.operators[32 - var2];
               }

               var1 = var2;
            } while(var2 != 0);

            this.csmReq = 1;
         } else {
            var6 = this.operators[0];
            var1 = 32;

            do {
               if(var6.key == 0 && var6.state > 1) {
                  var6.state = 1;
               }

               var2 = var1 - 1;
               if(var2 != 0) {
                  var6 = this.operators[32 - var2];
               }

               var1 = var2;
            } while(var2 != 0);

            this.csmReq = 0;
         }
      }

      var6 = this.operators[0];
      var1 = 32;

      do {
         var6.phase += var6.freq;
         switch(var6.state) {
         case 1:
            var2 = var6.volume + var6.deltaRR;
            var6.volume = var2;
            if(var2 > 67108863) {
               var6.state = 0;
               var6.volume = 67108863;
            }
            break;
         case 2:
            var2 = var6.volume + var6.deltaD2R;
            var6.volume = var2;
            if(var2 > 67108863) {
               var6.state = 0;
               var6.volume = 67108863;
            }
            break;
         case 3:
            var2 = var6.volume + var6.deltaD1R;
            var6.volume = var2;
            if(unsigned(var2) >= var6.d1L) {
               var6.volume = var6.d1L;
               var6.state = 2;
            }
            break;
         case 4:
            var2 = var6.volume;
            var6.volume -= var6.deltaAR;
            int var3 = (var2 >> 16) - (unsigned(var6.volume) >> 16);
            if(var3 > 0) {
               var2 = var6.volume + (var3 << 16);

               int var4;
               int var5;
               do {
                  var4 = var2 - 65536 - (var2 >> 4 & -65536);
                  if(var4 <= '\uffff') {
                     break;
                  }

                  var5 = var3 - 1;
                  var3 = var5;
                  var2 = var4;
               } while(var5 != 0);

               var6.volume = var4;
            }

            if(var6.volume <= '\uffff') {
               if(var6.volume < 0) {
                  var6.volume = 0;
               }

               var6.state = 3;
            }
         }

         var2 = var1 - 1;
         if(var2 != 0) {
            var6 = this.operators[32 - var2];
         }

         var1 = var2;
      } while(var2 != 0);

   }

   private final void calcChan7() {
      INT32 var8 = chanout[7];
      INT32 var6 = c1;
      INT32 var7 = m2;
      c2.value = 0;
      var7.value = 0;
      var6.value = 0;
      var8.value = 0;
      int var1 = 0;
      Operator var11 = this.operators[7];
      if(var11.ams != 0) {
         var1 = this.lfa << var11.ams - 1;
      }

      if(var11.pms != 0) {
         this.calcLfoPm(7);
      }

      int var5 = this.volume_calc(var11, var1);
      int var3 = var11.fb0;
      int var2 = var11.fb;
      var11.fb0 = var11.fb;
      if(var11.connect == null) {
         var8 = c1;
         var6 = m2;
         INT32 var9 = c2;
         int var4 = var11.fb0;
         var9.value = var4;
         var6.value = var4;
         var8.value = var4;
      } else {
         var11.connect.value = var11.fb0;
      }

      var11.fb = 0;
      if(var5 < 832) {
         var11.fb = this.opCalc1(7, var5, var3 + var2 << var11.feedBack);
      }

      Operator var10 = this.operators[23];
      var2 = this.volume_calc(var10, var1);
      if(var2 < 832) {
         var6 = var10.connect;
         var6.value += this.calcOp(23, var2, c1.value);
      }

      var10 = this.operators[15];
      var2 = this.volume_calc(var10, var1);
      if(var2 < 832) {
         var6 = var10.connect;
         var6.value += this.calcOp(15, var2, m2.value);
      }

      var2 = this.volume_calc(this.operators[31], var1);
      if((this.noise & 128) != 0) {
         var1 = 0;
         if(var2 < 1023) {
            var1 = (var2 ^ 1023) * 2;
         }

         var6 = chanout[7];
         var2 = var6.value;
         if((this.noiseRNG & 65536) == 0) {
            var1 *= -1;
         }

         var6.value = var2 + var1;
      } else if(var2 < 832) {
         var6 = chanout[7];
         var6.value += this.calcOp(31, var2, c2.value);
      }

   }

   private final void calcLfoPm(int var1) {
      Operator var6 = this.operators[var1];
      int var2 = this.lfp;
      if(var6.pms < 6) {
         var2 >>= 6 - var6.pms;
      } else {
         var2 <<= var6.pms - 5;
      }

      if(var2 != 0) {
         var2 = unsigned(var6.kcIndex + var2);
         int var4 = this.freq[var6.dt2 + var2];
         int var3 = var6.d1tv;
         int var5 = var6.mul;
         var6.phase += ((var4 + var3) * var5 >> 1) - var6.freq;
         var6 = this.operators[var1 + 8];
         var3 = this.freq[var6.dt2 + var2];
         var4 = var6.d1tv;
         var5 = var6.mul;
         var6.phase += ((var3 + var4) * var5 >> 1) - var6.freq;
         var6 = this.operators[var1 + 16];
         var5 = this.freq[var6.dt2 + var2];
         var3 = var6.d1tv;
         var4 = var6.mul;
         var6.phase += ((var5 + var3) * var4 >> 1) - var6.freq;
         var6 = this.operators[var1 + 24];
         var2 = this.freq[var6.dt2 + var2];
         var1 = var6.d1tv;
         var3 = var6.mul;
         var6.phase += ((var2 + var1) * var3 >> 1) - var6.freq;
      }

   }

   private final int calcOp(int var1, int var2, int var3) {
      Operator var4 = this.operators[var1];
      var1 = unsigned((var2 << 3) + sinTab[(var4.phase & -65536) + (var3 << 15) >> 16 & 1023]);
      if(var1 >= 6656) {
         var1 = 0;
      } else {
         var1 = TL_TAB[var1];
      }

      return var1;
   }

   private final void chan_calc(int var1) {
      INT32 var8 = chanout[var1];
      INT32 var7 = c1;
      INT32 var9 = m2;
      c2.value = 0;
      var9.value = 0;
      var7.value = 0;
      var8.value = 0;
      int var2 = 0;
      Operator var12 = this.operators[var1];
      if(var12.ams != 0) {
         var2 = unsigned(this.lfa << var12.ams - 1);
      }

      if(var12.pms != 0) {
         this.calcLfoPm(var1);
      }

      int var6 = unsigned(this.volume_calc(var12, var2));
      int var5 = var12.fb0;
      int var3 = var12.fb;
      var12.fb0 = var12.fb;
      if(var12.connect == null) {
         var8 = c1;
         INT32 var10 = m2;
         var7 = c2;
         int var4 = var12.fb0;
         var7.value = var4;
         var10.value = var4;
         var8.value = var4;
      } else {
         var12.connect.value = var12.fb0;
      }

      var12.fb = 0;
      if(var6 < 832) {
         var12.fb = this.opCalc1(var1, var6, var5 + var3 << var12.feedBack);
      }

      Operator var11 = this.operators[var1 + 16];
      var3 = unsigned(this.volume_calc(var11, var2));
      if(var3 < 832) {
         var7 = var11.connect;
         var7.value += this.calcOp(var1 + 16, var3, c1.value);
      }

      var11 = this.operators[var1 + 8];
      var3 = unsigned(this.volume_calc(var11, var2));
      if(var3 < 832) {
         var7 = var11.connect;
         var7.value += this.calcOp(var1 + 8, var3, m2.value);
      }

      var2 = unsigned(this.volume_calc(this.operators[var1 + 24], var2));
      if(var2 < 832) {
         var7 = chanout[var1];
         var7.value += this.calcOp(var1 + 24, var2, c2.value);
      }

   }

   private final void envelope_KONKOFF(int var1, int var2) {
      Operator var3 = this.operators[var1];
      if((var2 & 8) != 0) {
         if(var3.key == 0) {
            var3.key = 1;
            var3.phase = 0;
            var3.state = 4;
         }
      } else if(var3.key != 0) {
         var3.key = 0;
         if(var3.state > 1) {
            var3.state = 1;
         }
      }

      var1 += 8;
      var3 = this.operators[var1];
      if((var2 & 32) != 0) {
         if(var3.key == 0) {
            var3.key = 1;
            var3.phase = 0;
            var3.state = 4;
         }
      } else if(var3.key != 0) {
         var3.key = 0;
         if(var3.state > 1) {
            var3.state = 1;
         }
      }

      var1 += 8;
      var3 = this.operators[var1];
      if((var2 & 16) != 0) {
         if(var3.key == 0) {
            var3.key = 1;
            var3.phase = 0;
            var3.state = 4;
         }
      } else if(var3.key != 0) {
         var3.key = 0;
         if(var3.state > 1) {
            var3.state = 1;
         }
      }

      var3 = this.operators[var1 + 8];
      if((var2 & 64) != 0) {
         if(var3.key == 0) {
            var3.key = 1;
            var3.phase = 0;
            var3.state = 4;
         }
      } else if(var3.key != 0) {
         var3.key = 0;
         if(var3.state > 1) {
            var3.state = 1;
         }
      }

   }

   private void initChipTables() {
      double var3 = (double)this.clock / 64.0D / (double)this.sampfreq;
      System.out.println("Scaler=" + var3);

      double var1;
      int var5;
      int var6;
      for(var5 = 0; var5 < 768; ++var5) {
         var1 = (double)phaseinc_rom[var5];
         this.freq[var5 + 2304] = (int)(var1 * var3 * 64.0D) & -64;

         for(var6 = 0; var6 < 2; ++var6) {
            this.freq[var6 * 768 + 768 + var5] = this.freq[var5 + 2304] >> 2 - var6 & -64;
         }

         for(var6 = 3; var6 < 8; ++var6) {
            this.freq[var6 * 768 + 768 + var5] = this.freq[var5 + 2304] << var6 - 2;
         }
      }

      for(var5 = 0; var5 < 768; ++var5) {
         this.freq[var5 + 0] = this.freq[768];
      }

      for(var5 = 8; var5 < 10; ++var5) {
         for(var6 = 0; var6 < 768; ++var6) {
            this.freq[var5 * 768 + 768 + var6] = this.freq[6911];
         }
      }

      for(var5 = 0; var5 < 4; ++var5) {
         for(var6 = 0; var6 < 32; ++var6) {
            var1 = 1024.0D * ((double)DT1_tab[var5 * 32 + var6] * ((double)this.clock / 64.0D) / 1048576.0D) / (double)this.sampfreq;
            this.dt1Freq[(var5 + 0) * 32 + var6] = (int)(var1 * 65536.0D);
            this.dt1Freq[(var5 + 4) * 32 + var6] = -this.dt1Freq[(var5 + 0) * 32 + var6];
         }
      }

      var3 = (double)this.clock;

      for(var5 = 0; var5 < 256; ++var5) {
         var1 = (double)Math.abs((float)(var3 / 65536.0D / (double)(1 << var5 / 16) - var3 / 65536.0D / 32.0D / (double)(1 << var5 / 16) * (double)((var5 & 15) + 1)));
         this.lfoFreqDlt[255 - var5] = unsigned((int)(512.0D * var1 / (double)this.sampfreq * 8388608.0D));
      }

      for(var5 = 0; var5 < 34; ++var5) {
         this.egTabDlt[var5] = 0;
      }

      for(var5 = 2; var5 < 64; ++var5) {
         var3 = (double)this.clock / (double)this.sampfreq;
         var1 = var3;
         if(var5 < 60) {
            var1 = var3 * (1.0D + (double)(var5 & 3) * 0.25D);
         }

         var1 = var1 * (double)(1 << (var5 >> 2)) / 786432.0D;
         this.egTabDlt[var5 + 32] = unsigned((int)(var1 * 65536.0D));
      }

      for(var5 = 0; var5 < 32; ++var5) {
         this.egTabDlt[var5 + 96] = this.egTabDlt[95];
      }

      for(var5 = 0; var5 < 1024; ++var5) {
         var1 = 64.0D * (1024.0D - (double)var5) / (double)this.clock;
         this.timerA[var5] = (int)((double)this.sampfreq * var1 * 65536.0D);
      }

      for(var5 = 0; var5 < 256; ++var5) {
         var1 = 1024.0D * (256.0D - (double)var5) / (double)this.clock;
         this.timerB[var5] = (int)((double)this.sampfreq * var1 * 65536.0D);
      }

      var1 = (double)this.clock / 64.0D / (double)this.sampfreq;

      for(var5 = 0; var5 < 32; ++var5) {
         if(var5 != 31) {
            var6 = var5;
         } else {
            var6 = 30;
         }

         var6 = (int)(65536.0D / ((double)(32 - var6) * 32.0D));
         this.noiseTab[var5] = (int)((double)(var6 * 64) * var1);
      }

   }

   private static void initTables() {
      int var4 = 0;

      double var0;
      int var5;
      for(var0 = 0.0D; var4 < 256; ++var4) {
         var0 = Math.floor(65536.0D / Math.pow(2.0D, (double)(var4 + 1) * 0.03125D / 8.0D));
         var5 = (int)var0 >> 4;
         if((var5 & 1) != 0) {
            var5 = (var5 >> 1) + 1;
         } else {
            var5 >>= 1;
         }

         TL_TAB[var4 * 2 + 0] = var5 << 2;
         TL_TAB[var4 * 2 + 1] = -TL_TAB[var4 * 2 + 0];

         for(var5 = 1; var5 < 13; ++var5) {
            TL_TAB[var4 * 2 + 0 + var5 * 2 * 256] = TL_TAB[var4 * 2 + 0] >> var5;
            TL_TAB[var4 * 2 + 1 + var5 * 2 * 256] = -TL_TAB[var4 * 2 + 0 + var5 * 2 * 256];
         }
      }

      double var2;
      int[] var8;
      for(var4 = 0; var4 < 1024; var0 = var2) {
         var2 = Math.sin((double)(var4 * 2 + 1) * 3.141592653589793D / 1024.0D);
         if(var2 > 0.0D) {
            var0 = 8.0D * Math.log(1.0D / var2) / Math.log(2.0D);
         } else {
            var0 = 8.0D * Math.log(-1.0D / var2) / Math.log(2.0D);
         }

         var5 = (int)(var0 / 0.03125D * 2.0D);
         if((var5 & 1) != 0) {
            var5 = (var5 >> 1) + 1;
         } else {
            var5 >>= 1;
         }

         var8 = sinTab;
         byte var6;
         if(var2 >= 0.0D) {
            var6 = 0;
         } else {
            var6 = 1;
         }

         var8[var4] = var5 * 2 + var6;
         ++var4;
      }

      byte var7;
      int var9;
      for(var4 = 0; var4 < 4; ++var4) {
         for(var5 = 0; var5 < 512; ++var5) {
            switch(var4) {
            case 0:
               var0 = (double)(255 - var5 / 2);
               break;
            case 1:
               if(var5 < 256) {
                  var0 = 255.0D;
               } else {
                  var0 = 0.0D;
               }
               break;
            case 2:
               if(var5 < 256) {
                  var0 = (double)(255 - var5);
               } else {
                  var0 = (double)(var5 - 256);
               }
               break;
            case 3:
               var0 = (double)((int)(Math.random() * 255.0D));
            }

            if(var0 > 0.0D) {
               var2 = 8.0D * Math.log(255.0D / var0) / Math.log(2.0D);
            } else if(var0 < 0.0D) {
               var2 = 8.0D * Math.log(-255.0D / var0) / Math.log(2.0D);
            } else {
               var2 = 8.0D * Math.log(25500.0D) / Math.log(2.0D);
            }

            var9 = (int)(var2 / 0.03125D * 2.0D);
            if((var9 & 1) != 0) {
               var9 = (var9 >> 1) + 1;
            } else {
               var9 >>= 1;
            }

            var8 = lfoTab;
            if(var0 >= 0.0D) {
               var7 = 0;
            } else {
               var7 = 1;
            }

            var8[var4 * 512 * 2 + var5 * 2] = unsigned(var9 * 2 + var7);
         }
      }

      for(var4 = 0; var4 < 128; var0 = var2) {
         var2 = (double)(var4 * 2);
         if(var2 > 0.0D) {
            var0 = 8.0D * Math.log(8192.0D / var2) / Math.log(2.0D);
         } else {
            var0 = 8.0D * Math.log(819200.0D) / Math.log(2.0D);
         }

         var5 = (int)(var0 / 0.03125D * 2.0D);
         if((var5 & 1) != 0) {
            var5 = (var5 >> 1) + 1;
         } else {
            var5 >>= 1;
         }

         lfoMDTab[var4] = var5 * 2;
         ++var4;
      }

      for(var4 = 0; var4 < 4; ++var4) {
         for(var5 = 0; var5 < 512; ++var5) {
            switch(var4) {
            case 0:
               if(var5 < 256) {
                  var0 = (double)(var5 / 2);
               } else {
                  var0 = (double)(var5 / 2 - 256);
               }
               break;
            case 1:
               if(var5 < 256) {
                  var0 = 127.0D;
               } else {
                  var0 = -128.0D;
               }
               break;
            case 2:
               if(var5 < 128) {
                  var0 = (double)var5;
               } else if(var5 < 384) {
                  var0 = (double)(255 - var5);
               } else {
                  var0 = (double)(var5 - 511);
               }
               break;
            case 3:
               var0 = (double)((int)(Math.random() * 255.0D - 128.0D));
            }

            if(var0 > 0.0D) {
               var2 = 8.0D * Math.log(127.0D / var0) / Math.log(2.0D);
            } else if(var0 < 0.0D) {
               var2 = 8.0D * Math.log(-128.0D / var0) / Math.log(2.0D);
            } else {
               var2 = 8.0D * Math.log(12700.0D) / Math.log(2.0D);
            }

            var9 = (int)(var2 / 0.03125D * 2.0D);
            if((var9 & 1) != 0) {
               var9 = (var9 >> 1) + 1;
            } else {
               var9 >>= 1;
            }

            var8 = lfoTab;
            if(var0 >= 0.0D) {
               var7 = 0;
            } else {
               var7 = 1;
            }

            var8[var4 * 512 * 2 + var5 * 2 + 1] = unsigned(var9 * 2 + var7);
         }
      }

      for(var4 = 0; var4 < 16; ++var4) {
         if(var4 != 15) {
            var5 = var4;
         } else {
            var5 = var4 + 16;
         }

         var0 = (double)var5;
         d1lTab[var4] = (int)(65536.0D * var0 * 32.0D);
      }

   }

   private final void lfoCalc() {
      int var1;
      if((this.test & 2) != 0) {
         this.lfoPhase = 0;
         var1 = unsigned(this.lfoWave);
      } else {
         var1 = unsigned(unsigned(this.lfoPhase >> 23 & 511) * 2 + this.lfoWave);
      }

      int var2 = unsigned(lfoTab[var1] + this.amd);
      this.lfa = 0;
      if(var2 < 6656) {
         this.lfa = TL_TAB[var2];
      }

      var1 = unsigned(lfoTab[var1 + 1] + this.pmd);
      this.lfp = 0;
      if(var1 < 6656) {
         this.lfp = TL_TAB[var1];
      }

   }

   private final int opCalc1(int var1, int var2, int var3) {
      var1 = this.operators[var1].phase;
      var1 = unsigned((var2 << 3) + sinTab[(var1 & -65536) + var3 >> 16 & 1023]);
      if(var1 >= 6656) {
         var1 = 0;
      } else {
         var1 = TL_TAB[var1];
      }

      return var1;
   }

   private int readStatus() {
      return this.status;
   }

   private final void refreshEG(int var1) {
      Operator var4 = this.operators[var1];
      int var2 = unsigned(var4.kc);
      int var3 = unsigned(var2 >> var4.ks);
      if(var4.ar + var3 < 94) {
         var4.deltaAR = this.egTabDlt[var4.ar + var3];
      } else {
         var4.deltaAR = 67108864;
      }

      var4.deltaD1R = this.egTabDlt[var4.d1r + var3];
      var4.deltaD2R = this.egTabDlt[var4.d2r + var3];
      var4.deltaRR = this.egTabDlt[var4.rr + var3];
      var1 += 8;
      var4 = this.operators[var1];
      var3 = unsigned(var2 >> var4.ks);
      if(var4.ar + var3 < 94) {
         var4.deltaAR = this.egTabDlt[var4.ar + var3];
      } else {
         var4.deltaAR = 67108864;
      }

      var4.deltaD1R = this.egTabDlt[var4.d1r + var3];
      var4.deltaD2R = this.egTabDlt[var4.d2r + var3];
      var4.deltaRR = this.egTabDlt[var4.rr + var3];
      var1 += 8;
      var4 = this.operators[var1];
      var3 = unsigned(var2 >> var4.ks);
      if(var4.ar + var3 < 94) {
         var4.deltaAR = this.egTabDlt[var4.ar + var3];
      } else {
         var4.deltaAR = 67108864;
      }

      var4.deltaD1R = this.egTabDlt[var4.d1r + var3];
      var4.deltaD2R = this.egTabDlt[var4.d2r + var3];
      var4.deltaRR = this.egTabDlt[var4.rr + var3];
      var4 = this.operators[var1 + 8];
      var1 = unsigned(var2 >> var4.ks);
      if(var4.ar + var1 < 94) {
         var4.deltaAR = this.egTabDlt[var4.ar + var1];
      } else {
         var4.deltaAR = 67108864;
      }

      var4.deltaD1R = this.egTabDlt[var4.d1r + var1];
      var4.deltaD2R = this.egTabDlt[var4.d2r + var1];
      var4.deltaRR = this.egTabDlt[var4.rr + var1];
   }

   private void resetChip() {
      int var1;
      for(var1 = 0; var1 < 32; ++var1) {
         this.operators[var1].volume = 67108863;
      }

      this.lfoPhase = 0;
      this.lfoFreq = 0;
      this.lfoWave = 0;
      this.pmd = lfoMDTab[0] + 512;
      this.amd = lfoMDTab[0];
      this.lfa = 0;
      this.lfp = 0;
      this.test = 0;
      this.irqEnable = 0;
      this.timA = 0;
      this.timB = 0;
      this.timAVal = 0;
      this.timBVal = 0;
      this.timAIndex = 0;
      this.timBIndex = 0;
      this.timAOldIndex = 0;
      this.timBOldIndex = 0;
      this.noise = 0;
      this.noiseRNG = 0;
      this.noiseP = 0;
      this.noiseF = this.noiseTab[0];
      this.csmReq = 0;
      this.status = 0;
      this.writeReg(27, 0);

      for(var1 = 32; var1 < 256; ++var1) {
         this.writeReg(var1, 0);
      }

   }

   private final void setConnect(int var1, int var2, int var3) {
      Operator var4 = this.operators[var1];
      Operator var5 = this.operators[var1 + 8];
      Operator var6 = this.operators[var1 + 16];
      switch(var2 & 7) {
      case 0:
         var4.connect = c1;
         var6.connect = m2;
         var5.connect = c2;
         break;
      case 1:
         var4.connect = m2;
         var6.connect = m2;
         var5.connect = c2;
         break;
      case 2:
         var4.connect = c2;
         var6.connect = m2;
         var5.connect = c2;
         break;
      case 3:
         var4.connect = c1;
         var6.connect = c2;
         var5.connect = c2;
         break;
      case 4:
         var4.connect = c1;
         var6.connect = chanout[var3];
         var5.connect = c2;
         break;
      case 5:
         var4.connect = null;
         var6.connect = chanout[var3];
         var5.connect = chanout[var3];
         break;
      case 6:
         var4.connect = c1;
         var6.connect = chanout[var3];
         var5.connect = chanout[var3];
         break;
      case 7:
         var4.connect = chanout[var3];
         var6.connect = chanout[var3];
         var5.connect = chanout[var3];
      }

   }

   private static final int unsigned(int var0) {
      return Integer.MAX_VALUE & var0;
   }

   private final int volume_calc(Operator var1, int var2) {
      return var1.TL + (unsigned(var1.volume) >> 16) + (var1.amsMask & var2);
   }

   public WriteHandler getDataPortWrite() {
      return new WriteHandler() {
         public void write(int var1, int var2) {
            YM2151.this.writeReg(YM2151.this.regPort, var2);
         }
      };
   }

   public ReadHandler getReadStatusRead() {
      return new ReadHandler() {
         public int read(int var1) {
            return YM2151.this.readStatus();
         }
      };
   }

   public WriteHandler getRegisterPortWrite() {
      return new WriteHandler() {
         public void write(int var1, int var2) {
            YM2151.this.regPort = var2;
         }
      };
   }

   public void init(boolean var1, int var2, int var3) {
      super.init(false, var2, var3);
      this.sampfreq = var2;
      c1 = new INT32();
      c2 = new INT32();
      m2 = new INT32();
      this.pan = new int[16];
      this.timerA = new int[1024];
      this.timerB = new int[256];
      this.freq = new int[8448];
      this.dt1Freq = new int[256];
      this.egTabDlt = new int[128];
      this.lfoFreqDlt = new int[256];
      this.noiseTab = new int[32];
      lfoMDTab = new int[128];
      d1lTab = new int[16];
      chanout = new INT32[8];

      for(var2 = 0; var2 < chanout.length; ++var2) {
         chanout[var2] = new INT32();
      }

      this.operators = new Operator[32];

      for(var2 = 0; var2 < 32; ++var2) {
         this.operators[var2] = new Operator();
      }

      initTables();
      this.initChipTables();
      this.timA = 0;
      this.timB = 0;
      this.resetChip();
   }

   public final int read(int var1) {
      return this.readStatus();
   }

   public final void write(int var1, int var2) {
      this.writeReg(var1, var2);
   }

   public void writeBuffer() {
      this.clearBuffer();
      int var6 = super.getBufferLength();
      int var1;
      if(this.timB != 0) {
         this.timBVal -= var6 << 16;
         if(this.timBVal <= 0) {
            this.timBVal += this.timerB[this.timBIndex];
            if((this.irqEnable & 8) != 0) {
               var1 = this.status;
               this.status |= 2;
               if((var1 & 3) == 0 && this.irqHandler != null) {
                  this.irqHandler.irq(true);
               }
            }
         }
      }

      int var3 = 0;

      for(int var4 = 0; var3 < var6; ++var3) {
         this.chan_calc(0);
         this.chan_calc(1);
         this.chan_calc(2);
         this.chan_calc(3);
         this.chan_calc(4);
         this.chan_calc(5);
         this.chan_calc(6);
         this.calcChan7();
         int var30 = chanout[0].value;
         int var25 = this.pan[0];
         var1 = chanout[0].value;
         int var19 = this.pan[1];
         int var35 = chanout[1].value;
         int var24 = this.pan[2];
         int var10 = chanout[1].value;
         int var14 = this.pan[3];
         int var22 = chanout[2].value;
         int var26 = this.pan[4];
         int var7 = chanout[2].value;
         int var8 = this.pan[5];
         int var31 = chanout[3].value;
         int var33 = this.pan[6];
         int var11 = chanout[3].value;
         int var5 = this.pan[7];
         int var32 = chanout[4].value;
         int var27 = this.pan[8];
         int var20 = chanout[4].value;
         int var18 = this.pan[9];
         int var21 = chanout[5].value;
         int var2 = this.pan[10];
         int var16 = chanout[5].value;
         int var9 = this.pan[11];
         int var23 = chanout[6].value;
         int var34 = this.pan[12];
         int var17 = chanout[6].value;
         int var15 = this.pan[13];
         int var29 = chanout[7].value;
         int var28 = this.pan[14];
         int var13 = chanout[7].value;
         int var12 = this.pan[15];
         var2 = (int)((float)((var30 & var25) + (var35 & var24) + (var22 & var26) + (var31 & var33) + (var32 & var27) + (var21 & var2) + (var23 & var34) + (var29 & var28)) * this.volume);
         var5 = (int)((float)((var1 & var19) + (var10 & var14) + (var7 & var8) + (var11 & var5) + (var20 & var18) + (var16 & var9) + (var17 & var15) + (var13 & var12)) * this.volume);
         if(var2 > 32767) {
            var1 = 32767;
         } else {
            var1 = var2;
            if(var2 < -32768) {
               var1 = -32768;
            }
         }

         if(var5 > 32767) {
            var2 = 32767;
         } else {
            var2 = var5;
            if(var5 < -32768) {
               var2 = -32768;
            }
         }

         int[] var36 = this.linBuffer;
         var5 = var4 + 1;
         var36[var4] = var1;
         var36 = this.linBuffer;
         var4 = var5 + 1;
         var36[var5] = var2;
         if(this.timA != 0) {
            this.timAVal -= 65536;
            if(this.timAVal <= 0) {
               this.timAVal += this.timerA[this.timAIndex];
               if((this.irqEnable & 4) != 0) {
                  var1 = this.status;
                  this.status |= 1;
                  if((var1 & 3) == 0 && this.irqHandler != null) {
                     this.irqHandler.irq(true);
                  }
               }

               if((this.irqEnable & 128) != 0) {
                  this.csmReq = 2;
               }
            }
         }

         this.lfoCalc();
         this.advance();
      }

   }

   public final void writeReg(int var1, int var2) {
      Operator var5 = this.operators[var1 & 31];
      int var3 = var1 & 255;
      var2 &= 255;
      int var4;
      byte var6;
      switch(var3 & 224) {
      case 0:
         switch(var3) {
         case 1:
            this.test = var2;
            if((var2 & 2) != 0) {
               this.lfoPhase = 0;
            }

            return;
         case 8:
            this.envelope_KONKOFF(var2 & 7, var2);
            return;
         case 15:
            this.noise = var2;
            this.noiseF = this.noiseTab[var2 & 31];
            return;
         case 16:
            this.timAIndex = this.timAIndex & 3 | var2 << 2;
            return;
         case 17:
            this.timAIndex = this.timAIndex & 1020 | var2 & 3;
            return;
         case 18:
            this.timBIndex = var2;
            return;
         case 20:
            this.irqEnable = var2;
            if((var2 & 32) != 0) {
               var1 = this.status;
               this.status &= 253;
               if((var1 & 3) == 2 && this.irqHandler != null) {
                  this.irqHandler.irq(false);
               }
            }

            if((var2 & 16) != 0) {
               var1 = this.status;
               this.status &= 254;
               if((var1 & 3) == 1 && this.irqHandler != null) {
                  this.irqHandler.irq(false);
               }
            }

            if((var2 & 2) != 0) {
               if(this.timB == 0) {
                  this.timB = 1;
                  this.timBVal = this.timerB[this.timBIndex];
               }
            } else {
               this.timB = 0;
            }

            if((var2 & 1) != 0) {
               if(this.timA == 0) {
                  this.timA = 1;
                  this.timAVal = this.timerA[this.timAIndex];
                  return;
               }
            } else {
               this.timA = 0;
            }

            return;
         case 24:
            this.lfoFreq = this.lfoFreqDlt[var2];
            return;
         case 25:
            if((var2 & 128) != 0) {
               this.pmd = lfoMDTab[var2 & 127] + 512;
            } else {
               this.amd = lfoMDTab[var2 & 127];
            }

            return;
         case 27:
            this.ct = var2;
            this.lfoWave = (var2 & 3) * 512 * 2;
            if(this.writeHandler != null) {
               this.writeHandler.write(0, this.ct >> 6);
            }

            return;
         default:
            return;
         }
      case 32:
         var5 = this.operators[var3 & 7];
         switch(var3 & 24) {
         case 0:
            if((var2 >> 3 & 7) != 0) {
               var1 = (var2 >> 3 & 7) + 6;
            } else {
               var1 = 0;
            }

            var5.feedBack = var1;
            int[] var7 = this.pan;
            if((var2 & 64) != 0) {
               var6 = -1;
            } else {
               var6 = 0;
            }

            var7[(var3 & 7) * 2] = var6;
            var7 = this.pan;
            if((var2 & 128) != 0) {
               var6 = -1;
            } else {
               var6 = 0;
            }

            var7[(var3 & 7) * 2 + 1] = var6;
            this.setConnect(var3 & 7, var2, var3 & 7);
            return;
         case 8:
            var2 &= 127;
            if(var2 != var5.kc) {
               var1 = unsigned((var2 - (var2 >> 2)) * 64) + 768 | var5.kcIndex & 63;
               this.operators[(var3 & 7) + 0].kc = var2;
               this.operators[(var3 & 7) + 0].kcIndex = var1;
               this.operators[(var3 & 7) + 8].kc = var2;
               this.operators[(var3 & 7) + 8].kcIndex = var1;
               this.operators[(var3 & 7) + 16].kc = var2;
               this.operators[(var3 & 7) + 16].kcIndex = var1;
               this.operators[(var3 & 7) + 24].kc = var2;
               this.operators[(var3 & 7) + 24].kcIndex = var1;
               var2 = unsigned(var2 >> 2);
               this.operators[(var3 & 7) + 0].d1tv = this.dt1Freq[this.operators[(var3 & 7) + 0].dt1 + var2];
               this.operators[(var3 & 7) + 0].freq = unsigned((this.freq[this.operators[(var3 & 7) + 0].dt2 + var1] + this.operators[(var3 & 7) + 0].d1tv) * this.operators[(var3 & 7) + 0].mul) >> 1;
               this.operators[(var3 & 7) + 8].d1tv = this.dt1Freq[this.operators[(var3 & 7) + 8].dt1 + var2];
               this.operators[(var3 & 7) + 8].freq = unsigned((this.freq[this.operators[(var3 & 7) + 8].dt2 + var1] + this.operators[(var3 & 7) + 8].d1tv) * this.operators[(var3 & 7) + 8].mul) >> 1;
               this.operators[(var3 & 7) + 16].d1tv = this.dt1Freq[this.operators[(var3 & 7) + 16].dt1 + var2];
               this.operators[(var3 & 7) + 16].freq = unsigned((this.freq[this.operators[(var3 & 7) + 16].dt2 + var1] + this.operators[(var3 & 7) + 16].d1tv) * this.operators[(var3 & 7) + 16].mul) >> 1;
               this.operators[(var3 & 7) + 24].d1tv = this.dt1Freq[this.operators[(var3 & 7) + 24].dt1 + var2];
               this.operators[(var3 & 7) + 24].freq = unsigned((this.freq[this.operators[(var3 & 7) + 24].dt2 + var1] + this.operators[(var3 & 7) + 24].d1tv) * this.operators[(var3 & 7) + 24].mul) >> 1;
               this.refreshEG(var3 & 7);
            }

            return;
         case 16:
            var1 = var2 >> 2;
            if(var1 != (var5.kcIndex & 63)) {
               var1 |= var5.kcIndex & -64;
               this.operators[(var3 & 7) + 0].kcIndex = var1;
               this.operators[(var3 & 7) + 8].kcIndex = var1;
               this.operators[(var3 & 7) + 16].kcIndex = var1;
               this.operators[(var3 & 7) + 24].kcIndex = var1;
               this.operators[(var3 & 7) + 0].freq = unsigned((this.freq[this.operators[(var3 & 7) + 0].dt2 + var1] + this.operators[(var3 & 7) + 0].d1tv) * this.operators[(var3 & 7) + 0].mul) >> 1;
               this.operators[(var3 & 7) + 8].freq = unsigned((this.freq[this.operators[(var3 & 7) + 8].dt2 + var1] + this.operators[(var3 & 7) + 8].d1tv) * this.operators[(var3 & 7) + 8].mul) >> 1;
               this.operators[(var3 & 7) + 16].freq = unsigned((this.freq[this.operators[(var3 & 7) + 16].dt2 + var1] + this.operators[(var3 & 7) + 16].d1tv) * this.operators[(var3 & 7) + 16].mul) >> 1;
               this.operators[(var3 & 7) + 24].freq = unsigned((this.freq[this.operators[(var3 & 7) + 24].dt2 + var1] + this.operators[(var3 & 7) + 24].d1tv) * this.operators[(var3 & 7) + 24].mul) >> 1;
            }

            return;
         case 24:
            var5.pms = var2 >> 4 & 7;
            var5.ams = var2 & 3;
            return;
         default:
            return;
         }
      case 64:
         var3 = var5.dt1;
         var4 = var5.mul;
         var5.dt1 = (var2 & 112) << 1;
         if((var2 & 15) != 0) {
            var1 = (var2 & 15) << 1;
         } else {
            var1 = 1;
         }

         var5.mul = var1;
         if(var3 != var5.dt1) {
            var5.d1tv = this.dt1Freq[var5.dt1 + (var5.kc >> 2)];
         }

         if(var3 != var5.dt1 || var4 != var5.mul) {
            var5.freq = (this.freq[var5.kcIndex + var5.dt2] + var5.d1tv) * var5.mul >> 1;
         }
         break;
      case 96:
         var5.TL = (var2 & 127) << 3;
         break;
      case 128:
         var4 = var5.ks;
         var3 = var5.ar;
         var5.ks = 5 - (var2 >> 6);
         if((var2 & 31) != 0) {
            var1 = ((var2 & 31) << 1) + 32;
         } else {
            var1 = 0;
         }

         var5.ar = var1;
         if(var5.ar != var3 || var5.ks != var4) {
            if(var5.ar + (var5.kc >> var5.ks) < 94) {
               var5.deltaAR = this.egTabDlt[var5.ar + (var5.kc >> var5.ks)];
            } else {
               var5.deltaAR = 67108864;
            }
         }

         if(var5.ks != var4) {
            var5.deltaD1R = this.egTabDlt[var5.d1r + (var5.kc >> var5.ks)];
            var5.deltaD2R = this.egTabDlt[var5.d2r + (var5.kc >> var5.ks)];
            var5.deltaRR = this.egTabDlt[var5.rr + (var5.kc >> var5.ks)];
         }
         break;
      case 160:
         if((var2 & 128) != 0) {
            var6 = -1;
         } else {
            var6 = 0;
         }

         var5.amsMask = var6;
         if((var2 & 31) != 0) {
            var1 = ((var2 & 31) << 1) + 32;
         } else {
            var1 = 0;
         }

         var5.d1r = var1;
         var5.deltaD1R = this.egTabDlt[var5.d1r + (var5.kc >> var5.ks)];
         break;
      case 192:
         var1 = var5.dt2;
         var5.dt2 = DT2_tab[var2 >> 6];
         if(var5.dt2 != var1) {
            var5.freq = (this.freq[var5.kcIndex + var5.dt2] + var5.d1tv) * var5.mul >> 1;
         }

         if((var2 & 31) != 0) {
            var1 = ((var2 & 31) << 1) + 32;
         } else {
            var1 = 0;
         }

         var5.d2r = var1;
         var5.deltaD2R = this.egTabDlt[var5.d2r + (var5.kc >> var5.ks)];
         break;
      case 224:
         var5.d1L = d1lTab[var2 >> 4];
         var5.rr = ((var2 & 15) << 2) + 34;
         var5.deltaRR = this.egTabDlt[var5.rr + (var5.kc >> var5.ks)];
      }

   }
}
