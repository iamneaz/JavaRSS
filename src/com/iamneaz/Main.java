package com.iamneaz;

import com.iamneaz.threads.PrintThread;
import com.iamneaz.threads.UpdateThread;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args){
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/com/iamneaz/properties/address.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        //updates \a.rss in every 15 seconds with the content fetched from http://rss.cnn.com/rss/edition.rss
        UpdateThread updateThread = new UpdateThread(prop.getProperty("urlAddress"),prop.getProperty("fileAddress"));
        updateThread.run();
        service.scheduleAtFixedRate(updateThread, 0, 15, TimeUnit.SECONDS);

        //reads a.rss and prints the list of jpg image references in the file.
        PrintThread printThread = new PrintThread(prop.getProperty("urlAddress"),prop.getProperty("fileAddress"));
        service.scheduleAtFixedRate(printThread, 0, 20, TimeUnit.SECONDS);

        /*
            In the given problem, There is no mention of when the threads would stop.
            To stop the ScheduledExecutorService, this bellow thread is executed to stop all the threads after 1 minute
         */

        Runnable stopThreads = new Runnable() {
            @Override
            public void run() {
                service.shutdown();
            }
        };
        service.scheduleAtFixedRate(stopThreads, 1, 1, TimeUnit.MINUTES);


    }
}
