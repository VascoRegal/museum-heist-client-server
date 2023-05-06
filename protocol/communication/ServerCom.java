package protocol.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCom {
       
    private ServerSocket listeningSocket = null;

    private Socket commSock = null;
    
    private int port;

    private ObjectInputStream in = null;

    private ObjectOutputStream out = null;

    public ServerCom(int port)
    {
        this.port = port;
    }

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
