package edu.umkc.ms.mytweets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import java.io.File;
import java.io.FileWriter;

public class TwitterStreamConsumer extends Thread {

    private static final String STREAM_URI = "https://stream.twitter.com/1.1/statuses/filter.json";

    public void run(){
        try{
            System.out.println("Starting Twitter public stream consumer thread.");

            // Enter your consumer key and secret below
            OAuthService service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey("m5sdv8dg0aP7exWEqchlNI7CP")
                    .apiSecret("kRR8jJZyP03kBskIpe8gurtzjaIRm7KFxGCcrZAS0hMoku07xN")
                    .build();

            // Set your access token
            Token accessToken = new Token("141897544-s18Jy1z7wpjuLIMDhpxkIUrkaeE6f1oyEz6lbPVv", "2DIvDR7Zi9oemwbY7XlKSCuGbZgvhJ4zrEbFEil1h4gcs");

            // Let's generate the request
            System.out.println("Connecting to Twitter Public Stream");
            OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
            request.addHeader("version", "HTTP/1.1");
            request.addHeader("host", "stream.twitter.com");
            request.setConnectionKeepAlive(true);
            request.addHeader("user-agent", "Twitter Stream Reader");
            request.addBodyParameter("track", "#FastFurious,@FastFurious,#FastFurious7,@FastFurious7,#Fast,@Fast,fast and furiuos,fast n furiuos"); // Set keywords you'd like to track here
            service.signRequest(accessToken, request);
            Response response = request.send();

            // Create a reader to read Twitter's stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));
            File file = new File("../collectedTweets.json");
            if (!file.exists()) {
				file.createNewFile();
			}
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);			
			
            String line;
            int noTweets=0;
            while ((line = reader.readLine()) != null) {
            	noTweets++;
                //System.out.println(line);
    			bw.write(line);
    			System.out.println(noTweets);
    			if(noTweets==5000){
    				break;
    			}
            }
			bw.close();
			System.out.println("Completed. Toatal tweets are 25000");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

    }
}