package server.shared;

import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.UpdateStateMessage;
import server.entities.CollectionSiteClientProxy;

public class CollectionSiteMemoryInterface {

    private final CollectionSiteMemory collectionSiteMemory;

    public CollectionSiteMemoryInterface(CollectionSiteMemory mem)
    {
        this.collectionSiteMemory = mem;
    }

    public Message process(Message in)
    {
        Command cmd;
        System.out.println(in);

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
                boolean needed = collectionSiteMemory.amINeeded();
                if (needed)
                {
                    cmd = Command.ACK;
                } else {
                    cmd = Command.UNACK;
                }
                return MessageFactory.serverCreate(cmd);

            case APPRSIT:
                char op = collectionSiteMemory.appraiseSit();
                return MessageFactory.serverOperationDecision(op);

            case PRPPRTY:
                int partyId = collectionSiteMemory.prepareAssaultParty();
                return MessageFactory.serverCreatedParty(partyId);

            default:
                break;
        }

        return null;
    }
}
