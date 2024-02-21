package org.spicord.bungee.server;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ClientConnectEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.SettingsChangedEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.event.TabCompleteResponseEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class BungeeListenerAdapter implements Listener {

    private BungeeEventProcessor eventProcessor;

    public BungeeListenerAdapter(BungeeEventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onClientConnect(ClientConnectEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onPermissionCheck(PermissionCheckEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onPlayerHandshake(PlayerHandshakeEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onProxyReload(ProxyReloadEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onServerDisconnect(ServerDisconnectEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onSettingsChanged(SettingsChangedEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        eventProcessor.handle(event);
    }

    @EventHandler
    public void onTabCompleteResponse(TabCompleteResponseEvent event) {
        eventProcessor.handle(event);
    }
}
