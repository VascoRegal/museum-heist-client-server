package protocol.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientCom {
    
    private Socket commSock = null;
    
    private String hostName;

    private int port;

    private ObjectInputStream in = null;

    private ObjectOutputStream out = null;

    public ClientCom(String hostName, int port)
    {
        this.hostName = hostName;
        this.port = port;
        this.open();
    }

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
