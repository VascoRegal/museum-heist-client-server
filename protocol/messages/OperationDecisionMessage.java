package protocol.messages;
/**
 * Message for master thief decisions
 */
public class OperationDecisionMessage extends Message {
    /**
     * opeartion to be carried
     */
    private char op;

    /**
     * 
     * @param cmd
     * @param op
     */
    public OperationDecisionMessage(Command cmd, char op)
    {
        super(cmd);
        this.op = op;
    }

    /**
     * get the operation
     * @return operation
     */
    public char getOperation()
    {
        return this.op;
    }

    /**
     * set it
     * @param op
     */
    public void setOperation(char op)
    {
        this.op = op;
    }
}
