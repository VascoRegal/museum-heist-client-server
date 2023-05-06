package server.shared;

import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.UpdateStateMessage;

public class CollectionSiteMemoryInterface {

    private final CollectionSiteMemory collectionSiteMemory;

    public CollectionSiteMemoryInterface(CollectionSiteMemory mem)
    {
        this.collectionSiteMemory = mem;
    }

    public Message process(Message in)
    {
        System.out.println(in);

        switch (in.getCommand())
        {
            case HSTSTATE:
                Command cmd;
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

            case APPRSIT:
                char op = collectionSiteMemory.appraiseSit();
                return MessageFactory.serverOperationDecision(op);

            default:
                break;
        }

        return null;
    }
}
