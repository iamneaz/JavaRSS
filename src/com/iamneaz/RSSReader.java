package com.iamneaz;

import java.io.IOException;
import java.util.List;

public interface RSSReader {

    void runRSSReader(String urlAddress) throws IOException;
    String getElement(String line,String startElement,String endElement,boolean xml);
    List<RSSMedia> getMediaElements(String itemString);
    List<String> getItems(String urlAddress)throws IOException;
    RSSHeader getHeaderItems(String urlAddress) throws IOException;
}
