package com.iamneaz.implementations;

import com.iamneaz.interfaces.Read;
import com.iamneaz.rssMaterial.Feed;
import com.iamneaz.rssMaterial.Header;
import com.iamneaz.rssMaterial.Items;
import com.iamneaz.rssMaterial.Media;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReadImpl implements Read {


    public Feed runRSSReader(String address, boolean url) throws IOException {

        List<Items> entries = new LinkedList<>();
        List<String> items = new ArrayList<>();
        // reading all the header elements
        Header header;
        if (url) {
            header = getHeaderItems(address, true);
        } else {
            header = getHeaderItems(address, false);
        }


        // getting each item
        try {
            if (url) {
                items = getItems(address, true);
            } else {
                items = getItems(address, false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // extracting information from that item and adding to entries
        for (String string : items) {
            Items rssItems = new Items();
            rssItems.setTitle(getElement(string, "<title>", "</title>", false));
            rssItems.setLink(getElement(string, "<link>", "</link>", false));
            rssItems.setGuid(getElement(string, "<guid", "</guid>", false));
            List<Media> mediaList = getMediaElements(string);
            rssItems.setMediaList(mediaList);
            entries.add(rssItems);

        }
        // adding the header and entries to rss feed
        Feed feed = new Feed();
        feed.setHeader(header);
        feed.setEntries(entries);

        return feed;

    }


    public List<String> getItems(String address, boolean url) throws IOException {
        BufferedReader in;
        String line;
        if (url) {
            URL rssURL = new URL(address);
            in = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        } else {
            in = new BufferedReader(new FileReader(address));
        }
        List<String> items = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            int itemEndIndex = 0;
            int itemStartIndex = 0;
            while (itemStartIndex >= 0) {
                itemStartIndex = line.indexOf("<item>", itemEndIndex);
                if (itemStartIndex >= 0) {
                    itemEndIndex = line.indexOf("</item>", itemStartIndex);
                    if (itemEndIndex >= 0) {
                        items.add(line.substring(itemStartIndex + "<item>".length(), itemEndIndex));
                    } else {
                        break;
                    }
                }
            }
        }
        in.close();
        return items;
    }

    public String getElement(String line, String startElement, String endElement, boolean xml) {
        String element = "";
        int elementStartIndex = 0;
        int elementEndIndex = 0;

        while (elementStartIndex >= 0) {
            elementStartIndex = line.indexOf(startElement, elementEndIndex);
            if (elementStartIndex >= 0) {
                elementEndIndex = line.indexOf(endElement, elementEndIndex);
                if (elementEndIndex >= elementStartIndex) {
                    if (xml) {
                        element = line.substring(elementStartIndex + startElement.length(), elementEndIndex + endElement.length());
                    } else {
                        element = line.substring(elementStartIndex + startElement.length(), elementEndIndex);
                    }

                } else {
                    break;
                }
            }
        }
        return element;
    }

    public List<Media> getMediaElements(String itemString) {
        List<Media> mediaList = new ArrayList<>();
        int titleEndIndex = 0;
        int titleStartIndex = 0;
        while (titleStartIndex >= 0) {
            titleStartIndex = itemString.indexOf("<media:", titleEndIndex);
            if (titleStartIndex >= 0) {
                titleEndIndex = itemString.indexOf("\" />", titleStartIndex);
                if (titleEndIndex >= titleStartIndex) {
                    String rssMediaString = itemString.substring(titleStartIndex + "<media:".length(), titleEndIndex);
                    Media media = new Media();
                    media.setMedium(getElement(rssMediaString, "medium=", "url=", false));
                    media.setUrl(getElement(rssMediaString, "url=", " height", false));
                    media.setHeight(getElement(rssMediaString, "height=", " width", false));
                    media.setWidth(getElement(rssMediaString, "width=", " type", false));
                    mediaList.add(media);
                } else {
                    break;
                }
            }
        }
        return mediaList;
    }

    public Header getHeaderItems(String address, boolean url) throws IOException {
        BufferedReader in;
        if (url) {
            URL rssURL = new URL(address);
            in = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        } else {
            in = new BufferedReader(new FileReader(address));
        }
        Header header = new Header();
        String line;
        while ((line = in.readLine()) != null) {
            int itemEndIndex;
            int itemStartIndex = 0;
            while (itemStartIndex >= 0) {
                //itemStartIndex = line.indexOf("<", itemEndIndex);
                if (itemStartIndex >= 0) {
                    itemEndIndex = line.indexOf("/><item>", itemStartIndex);
                    if (itemEndIndex >= itemStartIndex) {

                        line = line.substring(itemStartIndex, itemEndIndex);
                        String[] xml = new String[2];
                        xml[0] = getElement(line, "<?xml", "xsl\"?>", true).replace("?>", "");
                        xml[1] = getElement(line, ".xsl\"?><?xml", "css\"?>", true).replace("?>", "");
                        header.setXml(xml);
                        header.setRss(getElement(line, "<rss ", "><channel>", false));
                        header.setTitle(getElement(line, "<title>", "</title>", false));
                        header.setDescription(getElement(line, "<description>", "</description>", false));
                        header.setLink(getElement(line, "<link>", "</link>", false));
                        header.setImageURL(getElement(line, "<image><url>", "</url>", false));
                        header.setImageTitle(getElement(line, "</url><title>", "</title>", false));
                        header.setImageLink(getElement(line, "</title><link>", "</link>", false));
                        header.setGenerator(getElement(line, "<generator>", "</generator>", false));
                        header.setCopyright(getElement(line, "<copyright>", "</copyright>", false));
                        header.setLastBuildDate(getElement(line, "<lastBuildDate>", "</lastBuildDate>", false));
                        header.setPubDate(getElement(line, "<pubDate>", "</pubDate>", false));
                        header.setLanguage(getElement(line, "<language>", "</language>", false));
                        header.setTtl(getElement(line, "<ttl>", "</ttl>", false));
                        String[] atom = new String[2];
                        atom[0] = getElement(line, "</ttl><atom10:link", "/><feedburner:info", false);
                        atom[1] = getElement(line, "\" /><atom10:link", ".com/\"", true);
                        header.setAtom10(atom);
                        header.setFeedBurner(getElement(line, "<feedburner:info", "/><atom10:link", false));
                    } else {
                        break;
                    }
                }
            }
        }
        in.close();
        return header;
    }
}
