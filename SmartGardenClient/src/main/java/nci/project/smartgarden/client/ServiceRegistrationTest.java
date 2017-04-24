/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden.client;

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
public class ServiceRegistrationTest {
    public static void main(String[] args) throws InterruptedException {
        try {
            //create aJmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            
            //Register  a service
            ServiceInfo serviceInfo = ServiceInfo.create("_nando._tcp.local.", "example", 1234, "path=index.html");
            jmdns.registerService(serviceInfo);
            System.out.println("Service registered...");
            
            // Wait a bit
            Thread.sleep(25000);

            // Unregister all services
            //jmdns.unregisterAllServices();
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServiceRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
