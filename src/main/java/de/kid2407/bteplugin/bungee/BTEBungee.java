package de.kid2407.bteplugin.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * User: Tobias Franz
 * Date: 14.07.2020
 * Time: 18:34
 */
public class BTEBungee extends Plugin {

    @Override
    public void onEnable() {
        getProxy().registerChannel("bteplugin");
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeListener());
    }

    @Override
    public void onDisable() {
    }
}
