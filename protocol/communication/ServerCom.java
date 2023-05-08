package protocol.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * handles server side communication with a client
 * 
 * accepts connection and creates communication channels
 */
public class ServerCom {
       
    /**
     * channel to listen for new connections
     */
    private ServerSocket listeningSocket = null;

    /**
     * channel for communication
     */
    private Socket commSock = null;
    
    /**
     * server port
     */
    private int port;

    /**
     * input stream
     */
    private ObjectInputStream in = null;

    /**
     * outrput stream
     */
    private ObjectOutputStream out = null;

    /**
     * instantiate the listening process
     * @param port
     */
    public ServerCom(int port)
    {
        this.port = port;
    }

    /**
     * generate new communication channel
     * @param port
     * @param lSock
     */
    public ServerCom(int port, ServerSocket lSock)
    {
        this.port = port;
        this.listeningSocket = lSock;
        try {
            this.commSock = listeningSocket.accept();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * setup listening socket
     */
    public void start()
    {
        try {
            listeningSocket = new ServerSocket(port);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * close listening socket
     */
    public void end()
    {
        try {
            listeningSocket.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * accept connections and create new channels for these connections
     * @return Communication channel for client
     */
    public ServerCom accept()
    {
        ServerCom scon;

        scon = new ServerCom(port, listeningSocket);

        try {
            scon.out = new ObjectOutputStream(scon.commSock.getOutputStream());
            scon.in = new ObjectInputStream(scon.commSock.getInputStream());
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return scon;
    }

    /**
     * close sockets and streams
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
     * recv form client
     * @return 
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
     * send to client
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
