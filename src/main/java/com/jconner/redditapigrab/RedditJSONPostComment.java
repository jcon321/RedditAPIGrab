/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jconner.redditapigrab;

/**
 *
 * @author jconner
 */
public class RedditJSONPostComment {
    
    private String author;
    private String body;
    
    public RedditJSONPostComment() {
        
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
}
