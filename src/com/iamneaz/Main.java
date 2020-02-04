package com.iamneaz;

import com.iamneaz.implementations.ReadImpl;
import com.iamneaz.implementations.UpdateImpl;
import com.iamneaz.rssMaterial.Feed;
import com.iamneaz.rssMaterial.Items;
import com.iamneaz.rssMaterial.Media;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args){

        String urlAddress = "http://rss.cnn.com/rss/edition.rss";
        String fileAddress = "a.rss";
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        //updates \a.rss in every 15 seconds with the content fetched from http://rss.cnn.com/rss/edition.rss
        Runnable rssUpdater = new Runnable() {
            public void run() {
                System.out.println("thread1");
                UpdateImpl updateRSS = new UpdateImpl();
                updateRSS.runUpdateRSS(urlAddress, fileAddress);

            }
        };
        service.scheduleAtFixedRate(rssUpdater, 0, 15, TimeUnit.SECONDS);

        //reads a.rss and prints the list of jpg image references in the file.
        Runnable rssImagePrinter = new Runnable() {
            public void run() {
                System.out.println("thread2");

                ReadImpl rssReader = new ReadImpl();
                try {
                    Feed feed = rssReader.runRSSReader(fileAddress, false);
                    for (Items items : feed.getEntries()) {
                        for (Media rssMedia : items.getMediaList()) {
                            if (rssMedia.getUrl().endsWith(".jpg\"")) {
                                System.out.println(rssMedia.getUrl().replace("\"", ""));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };
        service.scheduleAtFixedRate(rssImagePrinter, 0, 20, TimeUnit.SECONDS);

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
        service.scheduleAtFixedRate(stopThreads, 1, 2, TimeUnit.MINUTES);


    }
}
