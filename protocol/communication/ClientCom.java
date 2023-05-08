package protocol.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Handles communication with a server
 */
public class ClientCom {
    
    /**
     * communication channel
     */
    private Socket commSock = null;
    
    /**
     * server hostname
     */
    private String hostName;

    /**
     * server port
     */
    private int port;

    /**
     * input stream
     */
    private ObjectInputStream in = null;

    /**
     * output stream
     */
    private ObjectOutputStream out = null;

    /**
     * 
     * @param hostName
     * @param port
     */
    public ClientCom(String hostName, int port)
    {
        this.hostName = hostName;
        this.port = port;
        this.open();
    }

    /**
     * open connection
     * create sockets and streams
     * @return true if open, false if not
     */
    public boolean open()
    {
        boolean res = true;

        SocketAddress serverAddr = new InetSocketAddress(this.hostName, this.port);

        try
        {
            commSock =  new Socket();
            commSock.connect(serverAddr);
        } catch (Exception e)
        {
            // TODO: catch
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            out = new ObjectOutputStream(commSock.getOutputStream());
            in =  new ObjectInputStream(commSock.getInputStream());
        } catch (Exception e)
        {
            // TODO: catch
            e.printStackTrace();
            System.exit(1);
        }

        return res;
    }

    /**
     * close sockets and stream
     */
    public void close()
    {
        try
        {
            out.close();
            in.close();
            commSock.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * receive message from server
     * @return Object
     */
    public Object recv()
    {
        Object o = null;
        try {
            o = in.readObject();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return o;
    }

    /**
     * send message to server
     * @param obj
     */
    public void send(Object obj)
    {
        try {
            out.writeObject(obj);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
