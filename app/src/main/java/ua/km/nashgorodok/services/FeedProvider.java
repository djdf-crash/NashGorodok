package ua.km.nashgorodok.services;


import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.jsoup.Jsoup;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.km.nashgorodok.database.DatabaseUtil;
import ua.km.nashgorodok.database.models.Feed;

public class FeedProvider {

    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";
    static final String CATEGORY = "category";
    private static final String AUTHOR = "author";


    public static List<Feed> parse(Context ctx, String rssFeed) {

        List<Feed> list = new ArrayList<Feed>();
        XmlPullParser parser = Xml.newPullParser();
        InputStream stream = null;
        try {
            // auto-detect the encoding from the stream
            stream = new URL(rssFeed).openConnection().getInputStream();
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            boolean done = false;
            Feed item = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            Log.i("new item", "Create new item");
                            item = new Feed("","","","","","","", true);
                        } else if (item != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                Log.i("Attribute", "setLink");
                                item.setPostUrl(parser.nextText().concat("?format=feed&type=rss"));
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                Log.i("Attribute", "description");

                                int token = parser.nextToken();
                                while(token!=XmlPullParser.CDSECT){
                                    token = parser.nextToken();
                                }
                                String cdata = parser.getText();

                                String thumbnailUrl = cdata.substring(cdata.indexOf("src=")+5, cdata.indexOf("jpg")+3).trim();

                                item.setThumbnailUrl(thumbnailUrl);

                                String desc = cdata.substring(cdata.indexOf("<br />")+7).trim();
                                desc = Jsoup.parse(desc).text();
                                item.setBody(desc);
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                Log.i("Attribute", "date");
                                item.setPubDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                Log.i("Attribute", "title");
                                item.setTitle(parser.nextText().trim());
                            }else if (name.equalsIgnoreCase(AUTHOR)) {
                                Log.i("Attribute", "author");
                                item.setAuthor(parser.nextText().trim());
                            }else if (name.equalsIgnoreCase(CATEGORY)) {
                                Log.i("Attribute", "category");
                                item.setCategory(parser.nextText().trim());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        Log.i("End tag", name);
                        if (name.equalsIgnoreCase(ITEM) && item != null) {
                            Log.i("Added", item.toString());
                            Feed findFeed = DatabaseUtil.getFeedDao(ctx).getFeedByPostUrl(item.getPostUrl());
                            if (findFeed == null) {
                                list.add(item);
                                DatabaseUtil.getFeedDao(ctx).insertFeed(item);
                            }
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
