package mx.devapps.utils.interfaces;

public interface ILongOperation<T> {

    String doInBackground();

    void onPreExecute();

    void onPostExecute(int code, String result);

}
