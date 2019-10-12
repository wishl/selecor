package abstracts;

import interfaces.Handler;

public abstract class AbstractHandler implements Handler {


    @Override
    public void run() {
        doHandler();
    }

}
