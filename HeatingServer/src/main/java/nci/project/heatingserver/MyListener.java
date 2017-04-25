/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.heatingserver;

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
        System.out.println("$$$$$$$$ HeatingListener initializd....");
        
        try {
            //create aJmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            
            //Register  a service
            ServiceInfo serviceInfo1 = ServiceInfo.create("_nando._tcp.local.", "start", 4221, "path=index.html");
            ServiceInfo serviceInfo2 = ServiceInfo.create("_nando._tcp.local.", "stop", 4221, "path=index.html");
            ServiceInfo serviceInfo3 = ServiceInfo.create("_nando._tcp.local.", "decrease-temperature", 4221, "path=index.html");
            ServiceInfo serviceInfo4 = ServiceInfo.create("_nando._tcp.local.", "increase-temperature", 4221, "path=index.html");
            ServiceInfo serviceInfo5 = ServiceInfo.create("_nando._tcp.local.", "current-temperature", 4221, "path=index.html");
            jmdns.registerService(serviceInfo1);
            jmdns.registerService(serviceInfo2);
            jmdns.registerService(serviceInfo3);
            jmdns.registerService(serviceInfo4);
            jmdns.registerService(serviceInfo5);
            
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
