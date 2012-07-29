/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mctitan.ChatGetter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Czahrien
 */
public class ChatGetter extends JavaPlugin implements Runnable {
    SimpleServer ss;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        Set<String> allowedhosts = new HashSet<>();
        for(String host : getConfig().getStringList("allowedhosts") ) {
            try { 
               InetAddress addr = InetAddress.getByName(host);
               allowedhosts.add(addr.getHostAddress());
            } catch(UnknownHostException e) {
                getLogger().warning("Invalid host in configuration file: " + host);
            }
        }
        ss = new SimpleServer(80,allowedhosts);
        ss.start();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 20, 20); 
    }
    
    public void onDisable() {
        ss.stop();
    }

    @Override
    public void run() {
        String s;
        while((s = ss.getMessage()) != null ) {
            getServer().broadcastMessage(s);
        }
    }
}
