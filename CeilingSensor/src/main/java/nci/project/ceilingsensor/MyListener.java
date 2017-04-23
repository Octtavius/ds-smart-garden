/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.ceilingsensor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

/**
 *
 * @author octavian
 */
public class MyListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("$$$$$$$$ Ceiling Listener initializd....");
        
        try {
            //create aJmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            
            //Register  a service
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "ceiling", 4111, "path=index.html");
            jmdns.registerService(serviceInfo);
            
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
