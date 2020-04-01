package net.movegaga.jemu2.netplay;

public class NetMessage {
   public static final byte MSG_CLI_JOIN_REQ = 8;
   public static final byte MSG_CLI_SYNC_ACK = 9;
   public static final byte MSG_CONTROLLER_UPDATE = 6;
   public static final byte MSG_CONTROLLER_UPDATES_END = 7;
   public static final byte MSG_CONTROLLER_UPDATES_START = 5;
   public static final byte MSG_END = 99;
   public static final byte MSG_REQUEST_CONTROLLER_UPDATES = 4;
   public static final byte MSG_SER_JOIN_OK = 1;
   public static final byte MSG_SER_JOIN_REFUSED = 2;
   public static final byte MSG_SER_SYNC = 3;
}
