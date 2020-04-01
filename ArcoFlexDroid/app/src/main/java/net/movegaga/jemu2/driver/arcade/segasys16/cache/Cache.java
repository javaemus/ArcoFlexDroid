package net.movegaga.jemu2.driver.arcade.segasys16.cache;

import java.util.LinkedList;

public class Cache {
   LinkedList cache;
   int cached;
   Element[] elements;
   int size;

   public Cache(int var1) {
      this.size = var1;
      this.cached = 0;
      this.elements = new Element[var1];

      for(var1 = 0; var1 < this.elements.length; ++var1) {
         this.elements[var1] = new Element();
      }

      this.cache = new LinkedList();
   }

   public Element getElement(long var1) {
      return null;
   }
}
