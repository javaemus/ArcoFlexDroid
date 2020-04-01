package jef.util;

public class LRUCache {
   private final int dataSize;
   private CachedEntry first;
   private CachedEntry last;
   private final int size;

   public LRUCache(int var1, int var2) {
      this.size = var1;
      this.dataSize = var2;
      this.first = new CachedEntry();
      this.last = new CachedEntry();
      CachedEntry var3 = this.first;

      for(var2 = 1; var2 < var1 - 1; ++var2) {
         CachedEntry var4 = new CachedEntry();
         var3.next = var4;
         var4.prev = var3;
         var3 = var4;
      }

      this.last.prev = var3;
      this.last.next = null;
      this.first.prev = null;
   }

   private final void moveFirst(CachedEntry var1) {
      if(var1.prev != null) {
         var1.prev.next = var1.next;
      }

      if(var1.next != null) {
         var1.next.prev = var1.prev;
      }

      var1.next = this.first;
      var1.prev = null;
      this.first = var1;
   }

   public final void clear() {
      CachedEntry var2 = this.first;

      for(int var1 = this.size; var1 > 0; --var1) {
         var2.initialized = false;
         var2 = var2.next;
      }

   }

   public CachedEntry get(long var1) {
      CachedEntry var4 = this.first;
      int var3 = this.size;

      while(true) {
         if(var3 <= 0) {
            var4 = this.last;
            this.moveFirst(var4);
            var4.initialized = false;
            break;
         }

         if(!var4.initialized) {
            this.moveFirst(var4);
            break;
         }

         if(var4.id == var1) {
            this.moveFirst(var4);
            break;
         }

         --var3;
      }

      return var4;
   }

   public class CachedEntry {
      public final int[] data;
      public long id = 0L;
      public boolean initialized = false;
      private CachedEntry next;
      private CachedEntry prev;

      CachedEntry() {
         this.data = new int[LRUCache.this.dataSize];
         this.prev = null;
         this.next = null;
      }
   }
}
