package protocol.messages;

/**
 * Possible message commands
 */
public enum Command {

    // System operations
    INIT_LOG,
    ACK,
    UNACK,

    // Server
    ACKPRTY,
    ACKNEEDED,
    ACKROOM,
    ACKROOMLOC,
    ACKCNVS,
    UNACKCNVS,

    // Thief Operations
    SET_STATE,
    HSTSTATE,

    // MASTER THIEF
    STRTOPS,
    APPRSIT,
    PRPPRTY,
    TKREST,
    SNDPRTY,
    AVLROOM,
    COLCNVAS,

    // ORDINARY THIEF
    AMNEEDED,
    PRPEXCRS,
    ROOMLOC,
    CRWLIN,
    CRWLOT,
    PCKCNVAS,
    HNDCNVAS,
    ROOMCMPLT,
    ROOMUPDT
}
