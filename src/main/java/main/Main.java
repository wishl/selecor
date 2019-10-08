package main;

import selector.Reactor;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Thread thread = new Thread(new Reactor(8888));
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
