/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amazon.notification.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Vbalu
 */
public class PersistenceAppListener implements ServletContextListener {
 
  public void contextInitialized(ServletContextEvent evt) {
      DataManager.getInstance().createOutputMap();
  }

  public void contextDestroyed(ServletContextEvent evt) {
  
    DataManager.getInstance().closeEntityManagerFactory();
  }
}