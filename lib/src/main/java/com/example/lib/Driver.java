package com.example.lib;
import java.net.*;
import java.io.*;


public class Driver extends Thread{
    public void driverLogic() throws IOException{
        Socket connectionSocket = new Socket("127.0.0.1",5000);
        String receivedMsg =null;
        InputStream inputStream = connectionSocket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        OutputStream outputStream = connectionSocket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeUTF("Hello Connect to Area Computer from driver");
        receivedMsg = new String (dataInputStream.readUTF());

        if(receivedMsg.equals("Hello Connected"))
        {
            System.out.println("Driver: "+receivedMsg);
            dataOutputStream.writeUTF("request traffic recommendation");
            dataOutputStream.flush();
        }
        receivedMsg = new String (dataInputStream.readUTF());
        if(receivedMsg.equals("send the location data"))
        {
            System.out.println("Driver: "+receivedMsg);
            dataOutputStream.writeUTF("location data");
            dataOutputStream.flush();
        }
        receivedMsg = new String (dataInputStream.readUTF());
        if(receivedMsg.contains("the recommendation"))
        {
            System.out.println("Driver: "+receivedMsg);
            dataOutputStream.writeUTF("Close the connection");
            dataOutputStream.flush();
            dataInputStream.close();
            inputStream.close();
            dataOutputStream.close();
            outputStream.close();
            connectionSocket.close();
        }
    }
    @Override

    public  void run() {
        try {
            synchronized (this) {
                driverLogic();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }
}
