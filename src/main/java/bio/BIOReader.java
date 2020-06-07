package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BIOReader implements Runnable {


    private Socket socket;

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s;
        while (true) {
            try {
                if ((s = bufferedReader.readLine()) != null) {
                    System.out.println(s);
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BIOReader(Socket socket) {
        this.socket = socket;
    }

}
