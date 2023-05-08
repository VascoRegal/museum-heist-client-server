package server.shared;

import protocol.messages.Command;
import protocol.messages.HandCanvasMessage;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import server.entities.CollectionSiteClientProxy;

/**
 * Interface to read request and dispatch them to shared memory operations
 */
public class CollectionSiteMemoryInterface {

    /**
     * Memory reference
     */
    private final CollectionSiteMemory collectionSiteMemory;

    /**
     * constructor
     * @param mem
     */
    public CollectionSiteMemoryInterface(CollectionSiteMemory mem)
    {
        this.collectionSiteMemory = mem;
    }

    /**
     * Process a message and return response
     * @param in
     * @return
     */
    public Message process(Message in)
    {
        Command cmd;
        //System.out.println(in);

        CollectionSiteClientProxy caleeProxy = ((CollectionSiteClientProxy) Thread.currentThread() );
        
        caleeProxy.setThiefId(in.getThiefId());
        caleeProxy.setThiefState(in.getCurrentThiefState());

        switch (in.getCommand())
        {
            case HSTSTATE:
                if (collectionSiteMemory.getHeistStatus())
                {
                    cmd = Command.ACK;
                } else {
                    cmd = Command.UNACK;
                }
                return MessageFactory.serverCreate(cmd);

            case STRTOPS:
                collectionSiteMemory.startOperations();
                System.out.println("started ops!");
                return MessageFactory.serverCreate(Command.ACK);

            case AMNEEDED:
                int needed = collectionSiteMemory.amINeeded();
                return MessageFactory.serverNeededParty(needed);

            case APPRSIT:
                char op = collectionSiteMemory.appraiseSit();
                return MessageFactory.serverOperationDecision(op);

            case PRPPRTY:
                int partyId = collectionSiteMemory.prepareAssaultParty();
                return MessageFactory.serverCreatedParty(partyId);

            case TKREST:
                collectionSiteMemory.takeARest();
                return MessageFactory.serverCreate(Command.ACK);

            case HNDCNVAS:
                HandCanvasMessage hm = (HandCanvasMessage) in;
                caleeProxy.setCanvas(hm.getCanvas());
                collectionSiteMemory.handCanvas();
                return MessageFactory.serverCreate(Command.ACK);

            case COLCNVAS:
                collectionSiteMemory.collectCanvas();
                return MessageFactory.serverCreate(Command.ACK);

            default:
                break;
        }

        return null;
    }
}
