package com.iamneaz.threads;

import com.iamneaz.implementations.UpdateImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UpdateThread implements Runnable {
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

    public UpdateThread(String urlAddress, String fileAddress) {
        this.urlAddress = urlAddress;
        this.fileAddress = fileAddress;
    }

    @Override
    public void run() {

        UpdateImpl updateRSS = new UpdateImpl();
        updateRSS.runUpdateRSS(this.urlAddress,this.fileAddress);
    }
}
