package com.example.lib;
import java.net.*;
import java.io.*;


public class Sensor extends Thread{
    public void sensorLogic() throws IOException{
        Socket connectionSocket = new Socket("127.0.0.1",5000);
        String receivedMsg =null;
        //initializing IO streams
        InputStream inputStream = connectionSocket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        OutputStream outputStream = connectionSocket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeUTF("Hello Connect to Area Computer from sensor");
        receivedMsg = new String (dataInputStream.readUTF());
        //when the area computer node acknowledges the connection
        if(receivedMsg.equals("Hello Connected"))
        {
            //send sensors data
            System.out.println("Sensor: "+receivedMsg);
            dataOutputStream.writeUTF("sensors data");
            dataOutputStream.flush();
        }

        receivedMsg = new String (dataInputStream.readUTF());
        //when the area computer node acknowledges receiving data
        if(receivedMsg.equals("sensors data received"))
        {
            System.out.println("Sensor: "+receivedMsg);
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
                sensorLogic();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}