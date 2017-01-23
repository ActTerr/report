package mac.yk.report.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mac-yk on 2017/1/23.
 */

public class HttpUtil {
    public static void sendRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(address);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream in=connection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=bufferedReader.readLine())!=null){
                        response.append(line);
                    }
                    if (response!=null){
                        listener.finish(response.toString());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.error(e);
                }finally {
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
