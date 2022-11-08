package mx.devapps.utils.interfaces;

public interface IOperation<T> {

    String getUrl();

    String getToken();

    Object getData();

    Class getType();

    void onPostExecute(T result);

    void onExecuteError();

}
