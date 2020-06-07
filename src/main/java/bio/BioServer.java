package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @Author Guanmengyuan
 * @Date Created in 13:10 2020-03-24
 */
public class BioServer {

    private ServerSocket serverSocket;

    BioServer(int port){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            while (true) {
                Socket accept = serverSocket.accept();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
//                String s;
//                while ((s = bufferedReader.readLine()) != null) {
//                    System.out.println(s);
//                }
                BIOReader bioReader = new BIOReader(accept);
                ThreadPool.execute(bioReader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BioServer bioServer = new BioServer(8888);
        bioServer.start();
    }

}
