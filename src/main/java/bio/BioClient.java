package bio;

import java.io.*;
import java.net.Socket;

/**
 * @Author Guanmengyuan
 * @Date Created in 13:23 2020-03-24
 */
public class BioClient {

    private Socket socket;

    BioClient(String host,int port){
        try {
            this.socket = new Socket(host,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String s){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BioClient client = new BioClient("127.0.0.1",8888);
        client.sendData("gmy123456");
    }

}
