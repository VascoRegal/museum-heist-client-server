package protocol.messages;

public enum Command {

    // System operations
    INIT_LOG,
    ACK,
    UNACK,

    // Server
    ACKPRTY,
    ACKNEEDED,

    // Thief Operations
    SET_STATE,
    HSTSTATE,

    // MASTER THIEF
    STRTOPS,
    APPRSIT,
    PRPPRTY,
    TKREST,
    SNDPRTY,

    // ORDINARY THIEF
    AMNEEDED,
    PRPEXCRS,
}
