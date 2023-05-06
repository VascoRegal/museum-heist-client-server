package protocol.messages;

public enum Command {

    // System operations
    INIT_LOG,
    ACK,
    UNACK,

    // Server
    ACKPRTY,

    // Thief Operations
    SET_STATE,
    HSTSTATE,

    // MASTER THIEF
    STRTOPS,
    APPRSIT,
    PRPPRTY,

    // ORDINARY THIEF
    AMNEEDED,
}
