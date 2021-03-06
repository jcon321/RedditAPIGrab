/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jconner.controller;

import com.jconner.redditapigrab.RedditAPIGrab;
import com.jconner.redditapigrab.RedditJSONPost;
import com.jconner.wordcloud.WordCloudGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jconner
 */
@RestController
@EnableAutoConfiguration
public class Controller {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    
    @RequestMapping("/")
    String home() {
        RedditAPIGrab rAPI = new RedditAPIGrab();
        
        List<RedditJSONPost> topFromArchitecturePosts = rAPI.getTopFromSpecificSubReddit("architecture");

        for (RedditJSONPost post : topFromArchitecturePosts) {
            post.setComments(rAPI.getCommentsFromEachPost(post));
        }
        

        WordCloudGenerator wcGen = new WordCloudGenerator(topFromArchitecturePosts.get(0).getComments());
        wcGen.generateWordCloud();
        
        return "";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Controller.class, args);
    }


}
