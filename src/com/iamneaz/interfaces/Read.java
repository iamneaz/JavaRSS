package com.iamneaz.interfaces;

import com.iamneaz.rssMaterial.Feed;
import com.iamneaz.rssMaterial.Header;
import com.iamneaz.rssMaterial.Media;

import java.io.IOException;
import java.util.List;

public interface Read {

    Feed runRSSReader(String urlAddress, boolean url) throws IOException;

    String getElement(String line, String startElement, String endElement, boolean xml);

    List<Media> getMediaElements(String itemString);

    List<String> getItems(String urlAddress, boolean url) throws IOException;

    Header getHeaderItems(String urlAddress, boolean url) throws IOException;
}
