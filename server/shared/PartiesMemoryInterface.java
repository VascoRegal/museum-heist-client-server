package server.shared;

import client.entities.ThiefState;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.MovementMessage;
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
        //System.out.println(in);
        PartyOperationMessage m;
        MovementMessage mm;
        PartiesClientProxy caleeProxy = ((PartiesClientProxy) Thread.currentThread() );
        
        caleeProxy.setThiefId(in.getThiefId());
        caleeProxy.setThiefState(in.getCurrentThiefState());

        switch (in.getCommand())
        {
            case PRPEXCRS:
                m = (PartyOperationMessage) in;
                int room = partiesMemory.prepareExcursion(m.getPartyId());
                return MessageFactory.serverRoomAvailable(room);

            case SNDPRTY:
                m  = (PartyOperationMessage) in;
                partiesMemory.sendAssaultParty(m.getPartyId(), m.getRoomId());
                return MessageFactory.serverCreate(Command.ACK);

            case CRWLIN:
                mm = (MovementMessage) in;
                caleeProxy.setMaxDisplacement(mm.getMaxDisplacement());
                caleeProxy.setThiefState(ThiefState.CRAWLING_INWARDS);
                partiesMemory.crawlingIn(mm.getLocation());
                return MessageFactory.serverCreate(Command.ACK);

            case CRWLOT:
                caleeProxy.setThiefState(ThiefState.CRAWLING_OUTWARDS);
                partiesMemory.crawlingOut();
                return MessageFactory.serverCreate(Command.ACK);

            default:
                break;
        }

        return null;
    }
}
