/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.amazon.notification.controllers;

import com.amazon.notification.utils.DataManager;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author vbalu
 */
@Controller
public class NotificationController {
    
    @RequestMapping(value = "/data", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public String getData(@RequestParam String query){
        
        Map<String,String> map = DataManager.getInstance().getDataMap().hgetAll(query);
        String output  = "";
        if(map != null){
            output=map.toString();
        }
        return output;
    }
    
}
