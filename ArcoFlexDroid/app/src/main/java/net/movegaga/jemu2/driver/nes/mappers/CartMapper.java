package net.movegaga.jemu2.driver.nes.mappers;

import jef.map.ReadWriteHandler;

public interface CartMapper extends ReadWriteHandler {
   int readVROM(int var1);
}
