package com.iamneaz;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSReaderImpl implements RSSReader {


    public RSSFeed runRSSReader(String address,boolean url) throws IOException {

        List<RSSItems> entries = new ArrayList<>();
        List<String> items = new ArrayList<>();
        // reading all the header elements
        RSSHeader rssHeader = new RSSHeader();
        if(url)
        {
            rssHeader = getHeaderItems(address,true);
        }
        else
        {
            rssHeader = getHeaderItems(address,false);
        }


        // getting each item
        try {
            if(url)
            {
                items = getItems(address,true);
            }
            else
            {
                items = getItems(address,false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // extracting information from that item and adding to entries
        for (String string : items) {
            RSSItems rssItems = new RSSItems();
            rssItems.setTitle(getElement(string, "<title>", "</title>", false));
            //System.out.println();
            rssItems.setLink(getElement(string, "<link>", "</link>", false));
            rssItems.setGuid(getElement(string, "<guid", "</guid>", false));
            List<RSSMedia> rssMediaList = getMediaElements(string);
            rssItems.setRssMediaList(rssMediaList);
            entries.add(rssItems);

        }
        // adding the header and entries to rss feed
        RSSFeed rssFeed = new RSSFeed();
        rssFeed.setRssHeader(rssHeader);
        rssFeed.setEntries(entries);

        return rssFeed;

    }


    public List<String> getItems(String address,boolean url) throws IOException {
        BufferedReader in;
        if(url)
        {
            URL rssURL = new URL(address);
            in = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        }
        else
        {
            in = new BufferedReader(new FileReader(address));
        }
        String line="";

        List<String> items = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            int itemEndIndex = 0;
            int itemStartIndex = 0;
            while (itemStartIndex >= 0) {
                itemStartIndex = line.indexOf("<item>", itemEndIndex);
                if (itemStartIndex >= 0) {
                    itemEndIndex = line.indexOf("</item>", itemStartIndex);
                    if (itemEndIndex >= 0) {

                        //System.out.println(line.substring(itemStartIndex + "<item>".length(), itemEndIndex));
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

    public List<RSSMedia> getMediaElements(String itemString) {
        List<RSSMedia> rssMediaList = new ArrayList<>();
        int titleEndIndex = 0;
        int titleStartIndex = 0;
        while (titleStartIndex >= 0) {
            titleStartIndex = itemString.indexOf("<media:", titleEndIndex);
            if (titleStartIndex >= 0) {
                titleEndIndex = itemString.indexOf("\" />", titleStartIndex);
                if (titleEndIndex >= titleStartIndex) {
                    //System.out.println(line.substring(titleStartIndex + "<item>".length(), titleEndIndex));
                    //sourceCode += line.substring(titleStartIndex + "<item>".length(), titleEndIndex) + "\n";
                    String rssMediaString = itemString.substring(titleStartIndex + "<media:".length(), titleEndIndex);
                    RSSMedia rssMedia = new RSSMedia();
                    rssMedia.setMedium(getElement(rssMediaString, "medium=", "url=", false));
                    rssMedia.setUrl(getElement(rssMediaString, "url=", " height", false));
                    rssMedia.setHeight(getElement(rssMediaString, "height=", " width", false));
                    rssMedia.setWidth(getElement(rssMediaString, "width=", " type", false));
                    //rssMedia.setType(getElement(rssMediaString, "type=", "\"", false));
                    rssMediaList.add(rssMedia);
                }
                else
                {
                    break;
                }
            }
        }

        return rssMediaList;

    }

    public RSSHeader getHeaderItems(String address,boolean url) throws IOException {
        BufferedReader in;
        if(url)
        {
            URL rssURL = new URL(address);
            in = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        }
        else
        {
            in = new BufferedReader(new FileReader(address));
        }

        RSSHeader rssHeader = new RSSHeader();
        String line;
        while ((line = in.readLine()) != null) {
            int itemEndIndex ;
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
                        rssHeader.setXml(xml);
                        rssHeader.setRss(getElement(line, "<rss ", "><channel>", false));
                        rssHeader.setTitle(getElement(line, "<title>", "</title>", false));
                        rssHeader.setDescription(getElement(line, "<description>", "</description>", false));
                        rssHeader.setLink(getElement(line, "<link>", "</link>", false));
                        rssHeader.setImageURL(getElement(line, "<image><url>", "</url>", false));
                        rssHeader.setImageTitle(getElement(line, "</url><title>", "</title>", false));
                        rssHeader.setImageLink(getElement(line, "</title><link>", "</link>", false));
                        rssHeader.setGenerator(getElement(line, "<generator>", "</generator>", false));
                        rssHeader.setCopyright(getElement(line, "<copyright>", "</copyright>", false));
                        rssHeader.setLastBuildDate(getElement(line, "<lastBuildDate>", "</lastBuildDate>", false));
                        rssHeader.setPubDate(getElement(line, "<pubDate>", "</pubDate>", false));
                        rssHeader.setLanguage(getElement(line, "<language>", "</language>", false));
                        rssHeader.setTtl(getElement(line, "<ttl>", "</ttl>", false));
                        String[] atom = new String[2];
                        atom[0]=getElement(line, "</ttl><atom10:link", "/><feedburner:info", false);
                        atom[1]=getElement(line, "\" /><atom10:link", ".com/\"", true);
                        rssHeader.setAtom10(atom);
                        rssHeader.setFeedBurner(getElement(line, "<feedburner:info", "/><atom10:link", false));
                    } else {
                        //System.out.println("break");
                        break;
                    }

                }
            }
        }
        in.close();
        return rssHeader;
    }
}
