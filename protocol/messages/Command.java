package protocol.messages;

public enum Command {

    // System operations
    INIT_LOG,
    ACK,
    UNACK,

    // Thief Operations
    SET_STATE,
    HSTSTATE,

    // MASTER THIEF
    STRTOPS,
    APPRSIT
}
