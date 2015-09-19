/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amazon.notification.utils;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Vbalu
 */
public class NotificationUtils {

    @Autowired
    public NotificationConfig notificationConfig;
    
    @Autowired
    protected Receiver receiver;

    public void scanDirectory() throws FileNotFoundException {
        
        File inboundDirectory = new File(notificationConfig.getInboundFilePath());
        String[] fileList = inboundDirectory.list();
        for (String fileName : fileList) {
            pushData(fileName);
        }
    }

    public void pushData(String fileName) throws FileNotFoundException {
        
        String filePath = notificationConfig.getInboundFilePath() + fileName;
        CsvReader reader = new CsvReader(filePath);
        boolean isSuccess = DataManager.getInstance().updateMap(reader, true);
        reader.close();
        File inputFile = new File(filePath);
        String[] fileNameArray = fileName.split("\\.");
        String newFileName = fileNameArray[0] + Calendar.getInstance().get(Calendar.MILLISECOND) + "."
                + fileNameArray[1];
        if (isSuccess) {
            inputFile.renameTo(new File(notificationConfig.getArchiveFilePath() + newFileName));
        } else {
            inputFile.renameTo(new File(notificationConfig.getErrorFilePath() + newFileName));
        }
        try{
       
            System.out.println("************** Inside reciever message &&&&&&&&&&&&&&&&&&&&&&");
            receiver.receiveMessage();
        
        }catch(Exception e){
            e.printStackTrace();
        }
        

    }
}
