package server.shared;

import client.entities.ThiefState;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.UpdateStateMessage;

public class GeneralMemoryInterface {
    
    private final GeneralMemory generalMem;

    public GeneralMemoryInterface(GeneralMemory mem)
    {
        this.generalMem = mem;
    }

    public Message process(Message in)
    {
        System.out.println(in);

        switch (in.getCommand())
        {
            case INIT_LOG:
                break;
            case SET_STATE:
                UpdateStateMessage message = (UpdateStateMessage) in;
                if (in.getThiefId() == -1)
                {
                    this.generalMem.setMasterThiefState(ThiefState.AT_A_ROOM);
                } else {
                    this.generalMem.setOrdinaryThiefState(message.getThiefId(), message.getThiefState());
                }
                System.out.println("changed mt state!");
                return MessageFactory.serverCreate(Command.ACK);
            default:
                break;
        }

        return null;
    }
}
