package server.shared;

import client.entities.ThiefState;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.UpdateStateMessage;

/**
 * Interface to read request and dispatch them to shared memory operations
 */
public class GeneralMemoryInterface {
    
    /**
     * memeory reference
     */
    private final GeneralMemory generalMem;

    /**
     * constructor
     * @param mem
     */
    public GeneralMemoryInterface(GeneralMemory mem)
    {
        this.generalMem = mem;
    }

    /**
     * Process a message and return response
     * @param in
     * @return
     */
    public Message process(Message in)
    {


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
                return MessageFactory.serverCreate(Command.ACK);
            default:
                break;
        }

        return null;
    }
}
