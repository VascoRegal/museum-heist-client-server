package protocol.messages;

public class OperationDecisionMessage extends Message {
    private char op;

    public OperationDecisionMessage(Command cmd, char op)
    {
        super(cmd);
        this.op = op;
    }

    public char getOperation()
    {
        return this.op;
    }

    public void setOperation(char op)
    {
        this.op = op;
    }
}
