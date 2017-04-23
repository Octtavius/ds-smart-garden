/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
/**
 *
 * @author octavian
 */
public class ServiceDiscovery {
    public static class SGListener implements ServiceListener {

        public void serviceAdded(ServiceEvent se) {
            System.out.println("Service added: " + se.getInfo()); //To change body of generated methods, choose Tools | Templates.
        }

        public void serviceRemoved(ServiceEvent se) {
            System.out.println("Service removed: " + se.getInfo()); //To change body of generated methods, choose Tools | Templates.
        }

        public void serviceResolved(ServiceEvent se) {
            System.out.println("Service Resolved: " + se.getInfo()); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        try {
            //create the JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            
            //add a service listener
            jmdns.addServiceListener("_http._tcp.local.", new SGListener());
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServiceDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
