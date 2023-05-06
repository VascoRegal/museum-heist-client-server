package server.shared;

import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.PartyOperationMessage;
import server.entities.PartiesClientProxy;

public class PartiesMemoryInterface {

    private final PartiesMemory partiesMemory;

    public PartiesMemoryInterface(PartiesMemory mem)
    {
        this.partiesMemory = mem;
    }

    public Message process(Message in)
    {
        System.out.println(in);
        PartyOperationMessage m;
        PartiesClientProxy caleeProxy = ((PartiesClientProxy) Thread.currentThread() );
        
        caleeProxy.setThiefId(in.getThiefId());
        caleeProxy.setThiefState(in.getCurrentThiefState());

        switch (in.getCommand())
        {
            case PRPEXCRS:
                m = (PartyOperationMessage) in;
                partiesMemory.prepareExcursion(m.getPartyId());
                return MessageFactory.serverCreate(Command.ACK);

            case SNDPRTY:
                m  = (PartyOperationMessage) in;
                partiesMemory.sendAssaultParty(m.getPartyId());
                return MessageFactory.serverCreate(Command.ACK);


            default:
                break;
        }

        return null;
    }
}
