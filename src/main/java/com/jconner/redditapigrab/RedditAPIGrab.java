/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jconner.redditapigrab;

import com.jconner.utils.StringUtilities;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 *
 * @author jconner
 */
public class RedditAPIGrab {

    private static final Logger LOGGER = Logger.getLogger(RedditAPIGrab.class.getName());

    public RedditAPIGrab() {

    }

    public List<RedditJSONPost> getTopFromSpecificSubReddit(String subreddit) {
        List<RedditJSONPost> result = new ArrayList<>();
        try {
            URL url = new URL("https://www.reddit.com/r/" + subreddit + "/top.json?sort=top&t=all");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "jconner - Reddit API");
            InputStream is = conn.getInputStream();
            String isAsString = StringUtilities.getStringFromInputStream(is);

            Object obj = JSONValue.parse(isAsString);
            JSONObject redditJSON = (JSONObject) obj;
            JSONObject redditJSONData = (JSONObject) redditJSON.get("data");
            JSONArray redditJSONDataChildren = (JSONArray) redditJSONData.get("children");

            for (int x = 0; x < redditJSONDataChildren.size(); x++) {
                JSONObject postAsJSON = (JSONObject) ((JSONObject) redditJSONDataChildren.get(x)).get("data");

                RedditJSONPost post = new RedditJSONPost();
                post.setTitle(postAsJSON.get("title").toString());
                post.setAuthor(postAsJSON.get("author").toString());
                post.setPermalink(postAsJSON.get("permalink").toString());
                post.setUrl(postAsJSON.get("url").toString());

                result.add(post);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e);
        }

        return (result);
    }

    public List<String> getCommentsFromEachPost(RedditJSONPost post) {
        List<String> result = new ArrayList<>();

        // remove ending / and add .json to end
        String commentsURL = post.getPermalink().substring(0, post.getPermalink().length() - 1) + ".json";

        try {
            URL url = new URL("https://www.reddit.com" + commentsURL);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "jconner - Reddit API");
            InputStream is = conn.getInputStream();
            String isAsString = StringUtilities.getStringFromInputStream(is);

            // grab all strings in between "body": and "edited":
            // not worried about order of comments so not parsing json
            Pattern p = Pattern.compile("\\\"body\":(.*?)\\\"edited\":");
            Matcher m = p.matcher(isAsString);
            
            while (m.find()) {
                result.add(m.group(1));
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e);
        }

        return result;
    }
}
