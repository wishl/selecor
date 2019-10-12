package interfaces;

public interface CallBack {

    <T> T callback(T t);

    <T> T fail();

}
