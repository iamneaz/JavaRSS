package com.iamneaz;

import java.io.IOException;
import java.util.List;

public interface RSSReader {

    RSSFeed runRSSReader(String urlAddress,boolean url) throws IOException;
    String getElement(String line,String startElement,String endElement,boolean xml);
    List<RSSMedia> getMediaElements(String itemString);
    List<String> getItems(String urlAddress,boolean url)throws IOException;
    RSSHeader getHeaderItems(String urlAddress,boolean url) throws IOException;
}
