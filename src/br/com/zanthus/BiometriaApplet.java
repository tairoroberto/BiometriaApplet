package br.com.zanthus;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 * Created by tairo.miguel on 01/10/2015.
 */
public class BiometriaApplet extends JApplet {

    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    public void init() {
        new Thread() {
            @Override
            public void run() {

                String sdk = JOptionPane.showInputDialog("Insira a Chave da Licença");

                if (sdk.equalsIgnoreCase("veridis")){
                    new Main();
                }else {
                    new Principal();
                }

                getSystemProperties();
            }
        }.start();
    }


    public void getSystemProperties() {
        InetAddress IP;
        try {
            IP = InetAddress.getLocalHost();
            System.out.println("IP: " + IP.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("java.runtime.version: " + System.getProperty("java.runtime.version"));
        System.out.println("sun.desktop: " + System.getProperty("sun.desktop"));
        System.out.println("file.separator: " + System.getProperty("file.separator"));
        System.out.println("path.separator: " + System.getProperty("path.separator"));
        System.out.println("java.io.tmp: " + System.getProperty("java.io.tmpdir"));
        System.out.println("os.name: " + System.getProperty("os.name"));
        System.out.println("os.arch: " + System.getProperty("os.arch"));
        System.out.println("os.version: " + System.getProperty("os.version"));

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

}
