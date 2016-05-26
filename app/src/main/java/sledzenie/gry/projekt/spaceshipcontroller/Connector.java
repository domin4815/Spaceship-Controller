package sledzenie.gry.projekt.spaceshipcontroller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by domin4815 on 26.05.16.
 */
public class Connector {
    private DatagramSocket clientSocket;
    private InetAddress iPAddress;
    private String ipAddressStr;
    private String portStr;


    public Connector(final String ipAddressStr, final String portStr) throws SocketException, UnknownHostException {
        clientSocket = new DatagramSocket();
        this.portStr = portStr;
        System.out.println("ADDRESS: "+ ipAddressStr);

        Thread t = new Thread(){
            public void run() {
                try {
                    iPAddress =  InetAddress.getByName(ipAddressStr);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }

    public void send(String data) throws IOException{

        final byte[] sendData  = data.getBytes();

        Thread t = new Thread(){
            public void run() {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, iPAddress, Integer.parseInt(portStr));
                try {
                    clientSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void closeConnection(){
        clientSocket.close();
    }
}
