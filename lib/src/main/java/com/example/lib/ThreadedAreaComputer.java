package com.example.lib;
import java.net.*;
import java.io.*;
import java.io.IOException;

class ClientHandler implements Runnable
{
    Socket clientConnectionSocket;

    public ClientHandler(Socket s)
    {
        this.clientConnectionSocket = s;
    }
    static String receivedFromMainServer;

    //for communicating with the main servers' nodes
    public void openaSocket()throws IOException{
        //initializing the socket with the port of the main servers
        Socket connectionSocket = new Socket("127.0.0.1",4000);
        String receivedMsg =null;
        //initializing IO streams
        InputStream inputStream = connectionSocket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        OutputStream outputStream = connectionSocket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        //send connection to the main server node
        dataOutputStream.writeUTF("Hello Connect to MainServer from Area Computer");
        receivedMsg = new String (dataInputStream.readUTF());
        //when the main server accepts the connection
        if(receivedMsg.equals("Hello Connected"))
        {
            System.out.println("Area Computer: "+receivedMsg);
            dataOutputStream.writeUTF("sending traffic data");
            dataOutputStream.flush();
        }
        //when the traffic data is received from the main server node
        receivedMsg = new String (dataInputStream.readUTF());
        if(receivedMsg.equals("traffic data received"))
        {
            System.out.println("Area Computer: "+receivedMsg);
            dataOutputStream.writeUTF("request traffic recommendation");
            dataOutputStream.flush();
        }
        //when sending the driver's location data to the main server
        receivedMsg = new String (dataInputStream.readUTF());
        if(receivedMsg.equals("send the location data"))
        {
            System.out.println("Area Computer: "+receivedMsg);
            dataOutputStream.writeUTF("location data");
            dataOutputStream.flush();
        }
        //when receiving the recommendation from the main server node
        receivedMsg = new String (dataInputStream.readUTF());
        if(receivedMsg.equals("the recommendation"))
        {
            System.out.println("Area Computer: "+receivedMsg);
            receivedFromMainServer = receivedMsg;
            //closing the connection with the main server
            dataOutputStream.writeUTF("Close the connection");
            dataOutputStream.flush();
            dataInputStream.close();
            inputStream.close();
            dataOutputStream.close();
            outputStream.close();
            connectionSocket.close();
        }
    }
    //wrapping the runnable logic in a function for catching IOExceptions
    public void clientHandlerLogic() throws IOException {
        {

            String receivedMsg= null;

            while(true)
            {
                //initializing IO streams
                OutputStream outputStream = clientConnectionSocket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream (outputStream);
                InputStream inputStream = clientConnectionSocket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);

                receivedMsg = new String(dataInputStream.readUTF());
                //when connecting with the sensor
                if (receivedMsg.equals("Hello Connect to Area Computer from sensor")) {
                    System.out.println("Area Computer: " + receivedMsg);
                    dataOutputStream.writeUTF("Hello Connected");
                    dataOutputStream.flush();
                    receivedMsg = new String(dataInputStream.readUTF());
                    if (receivedMsg.equals("sensors data")) {
                        System.out.println("Area Computer: " + receivedMsg);
                        dataOutputStream.writeUTF("sensors data received");
                        dataOutputStream.flush();
                    }
                }
                //when connecting to the driver
                else if(receivedMsg.equals("Hello Connect to Area Computer from driver")){
                    System.out.println("Area Computer: " + receivedMsg);
                    dataOutputStream.writeUTF("Hello Connected");
                    dataOutputStream.flush();
                     receivedMsg = new String(dataInputStream.readUTF());
                    if (receivedMsg.equals("request traffic recommendation")) {
                        System.out.println("Area Computer: " + receivedMsg);
                        dataOutputStream.writeUTF("send the location data");
                        dataOutputStream.flush();
                    }
                    receivedMsg = new String(dataInputStream.readUTF());
                    if (receivedMsg.equals("location data")) {
                        System.out.println("Area Computer: " + receivedMsg);
                        openaSocket();
                        dataOutputStream.writeUTF("the recommendation from main server is "+receivedFromMainServer);
                        dataOutputStream.flush();
                    }
                }
                //closing the connection
                receivedMsg = new String(dataInputStream.readUTF());
                if (receivedMsg.equals("Close the connection")) {
                    System.out.println("Area Computer: " + receivedMsg);
                    dataInputStream.close();
                    inputStream.close();
                    dataOutputStream.close();
                    outputStream.close();
                    clientConnectionSocket.close();

                }

            }
        }
    }


    @Override
    public void run()
    {
        try{
            synchronized (this) {
                clientHandlerLogic();
            }
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
public class ThreadedAreaComputer extends Thread{
    public void threadedAreaComputerLogic() throws IOException{
            //open server socket
            ServerSocket connectionServerSocket = new ServerSocket(5000);
            System.out.println("Area Computer Running");

            //busy waiting server
            while (true)
            {
                //accept connection
                Socket s = connectionServerSocket.accept();
                System.out.println("Client Accepted");

                //open thread for this client
                ClientHandler ch = new ClientHandler(s);
                Thread t = new Thread(ch);
                t.start();

            }


        }

    @Override
    public void run() {
        try {
            threadedAreaComputerLogic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
