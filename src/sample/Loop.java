package sample;

import javafx.scene.control.TextArea;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Loop extends Thread {

    public void executeLoop(TextArea t, TextArea k, int id, int threadsNUM, String url, int timeOut) throws Exception {

        String getProxies[] = t.getText().split("\n");

        String proxyIP;
        int proxyPORT;
        System.out.println("LinesQuantity:" + getProxies.length);

        for (int i = 0; i < getProxies.length; i++) {

            int value = i - 1 + (threadsNUM * i) + id;

            System.out.println("Thread: " + id + " Line: " + value);
            String proxy[] = getProxies[value].split(":");

            if (proxy.length == 2 & proxy[0].trim().length() > 0 && proxy[1].trim().length() > 0) {
                proxyIP = proxy[0];
                proxyPORT = Integer.parseInt(proxy[1]);

                try {
                    testProxy(proxyIP, proxyPORT, k, url, timeOut);
                } catch (Exception ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private void testProxy(String proxyIP, int proxyPORT, TextArea k, String url, int timeOut) throws Exception {

        URL obj = new URL(url);

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPORT));

        HttpURLConnection con;

        if (url.contains("https")) {
            con = (HttpsURLConnection) obj.openConnection(proxy);
        } else {
            con = (HttpURLConnection) obj.openConnection(proxy);
        }

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setConnectTimeout(timeOut);
        con.setReadTimeout(timeOut);

        int response = con.getResponseCode();
        System.out.println("\nResponse:" + response);

        if (response == 200) {
            System.out.println("Working : " + proxyIP + ":" + proxyPORT);
            k.appendText(proxyIP + ":" + proxyPORT + "\n");
        } else {
            System.out.println("Not Working : " + proxyIP + ":" + proxyPORT);
        }
        con.disconnect();

    }

}
