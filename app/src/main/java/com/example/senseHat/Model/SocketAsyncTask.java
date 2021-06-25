package com.example.senseHat.Model;

import android.os.AsyncTask;

import com.example.senseHat.View.HomePageActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketAsyncTask extends AsyncTask<Void, Void, Void>
{
    Socket socket;
    public static String CMD = "00000";
    public static Byte CMD_Byte = '1';
    public static boolean tmpSocket = true;

    @Override
    protected Void doInBackground (Void... params){
        try {
            tmpSocket = false;
            // take ip and socket number to connection
            InetAddress inetAddress = InetAddress.getByName(HomePageActivity.ipAddress);
            socket = new Socket(inetAddress, HomePageActivity.socketAddress);
            //socket.setSoTimeout(2000);
            tmpSocket = socket.isOutputShutdown();
            // create a data output stream from the output stream so we can send data through it
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String inputMes = dataInputStream.readUTF();
            dataOutputStream.writeBytes(CMD);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done
            socket.close();
            if(inputMes == "OK"){
                tmpSocket = true;
            }
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
