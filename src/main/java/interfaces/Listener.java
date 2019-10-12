package interfaces;

public interface Listener extends Runnable {

    void listener();

    @Override
    default void run() {
        listener();
    }
}
