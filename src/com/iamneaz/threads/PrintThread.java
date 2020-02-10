package com.iamneaz.threads;

import com.iamneaz.implementations.ReadImpl;
import com.iamneaz.rssMaterial.Feed;
import com.iamneaz.rssMaterial.Items;
import com.iamneaz.rssMaterial.Media;

import java.io.IOException;

public class PrintThread implements Runnable {
    private String urlAddress;
    private String fileAddress;

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public PrintThread(String urlAddress, String fileAddress) {
        this.urlAddress = urlAddress;
        this.fileAddress = fileAddress;
    }

    @Override
    public void run() {
        //System.out.println("thread2");

        ReadImpl rssReader = new ReadImpl();
        try {
            Feed feed = rssReader.runRSSReader(this.fileAddress, false);
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
}
