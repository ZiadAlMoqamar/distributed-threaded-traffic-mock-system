package com.example.lib;
import java.net.*;
import java.io.*;
import java.io.IOException;

class ComputerHandler implements Runnable
{
    Socket computerConnectionSocket;

    public ComputerHandler(Socket s)
    {
        this.computerConnectionSocket = s;
    }
    //wrapping the runnable logic in a function for catching IOExceptions
    public void computerHandlerLogic() throws IOException {
        {

            String receivedMsg=null;
            while(true)
            {
                OutputStream outputStream = computerConnectionSocket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream (outputStream);
                InputStream inputStream = computerConnectionSocket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);

                receivedMsg = new String(dataInputStream.readUTF());
                if (receivedMsg.equals("Hello Connect to MainServer from Area Computer")) {
                    System.out.println("Main Server: " + receivedMsg);
                    dataOutputStream.writeUTF("Hello Connected");
                    dataOutputStream.flush();
                }
                receivedMsg = new String(dataInputStream.readUTF());
                if (receivedMsg.equals("sending traffic data")) {
                    System.out.println("Main Server: " + receivedMsg);
                    dataOutputStream.writeUTF("traffic data received");
                    dataOutputStream.flush();
                }
                receivedMsg = new String(dataInputStream.readUTF());
                if (receivedMsg.equals("request traffic recommendation")) {
                    System.out.println("Main Server: " + receivedMsg);
                    dataOutputStream.writeUTF("send the location data");
                    dataOutputStream.flush();
                }
                receivedMsg = new String(dataInputStream.readUTF());
                if (receivedMsg.equals("location data")) {
                    System.out.println("Main Server: " + receivedMsg);
                    dataOutputStream.writeUTF("the recommendation");
                    dataOutputStream.flush();
                }
                receivedMsg = new String(dataInputStream.readUTF());
                if (receivedMsg.equals("Close the connection")) {
                    System.out.println("Main Server: " + receivedMsg);
                    dataInputStream.close();
                    inputStream.close();
                    dataOutputStream.close();
                    outputStream.close();
                    computerConnectionSocket.close();

                }

            }
        }
    }


    @Override
    public void run()
    {
        try{
            synchronized (this) {
                computerHandlerLogic();
            }
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
public class ThreadedMainServer extends Thread{
    public void threadedMainServerLogic() throws IOException{
        //open server socket
        ServerSocket sv = new ServerSocket(4000);
        System.out.println("Main Server Running");
        //busy waiting server
        while (true)
        {
            //accept connection
            Socket s = sv.accept();
            System.out.println("Computer Accepted");

            //open thread for this client
            ComputerHandler ch = new ComputerHandler(s);
            Thread t = new Thread(ch);
            t.start();

        }


    }

    @Override
    public void run() {
        try {
            threadedMainServerLogic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
