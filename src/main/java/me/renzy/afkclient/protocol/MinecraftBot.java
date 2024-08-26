package me.renzy.afkclient.protocol;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import me.renzy.afkclient.utils.LogManager;

import javax.annotation.Nonnull;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MinecraftBot {

    public static Session createSession(@Nonnull String defaultName, @Nonnull SessionAdapter adapter) {
        return new SessionImpl(defaultName,adapter);
    }
    final static class SessionImpl implements Session {

        private TcpClientSession session;

        private String sessionName;

        private final AtomicBoolean connected = new AtomicBoolean(false);

        private final SessionAdapter adapter;

        SessionImpl(String sessionName, SessionAdapter adapter){
            this.sessionName = sessionName;
            this.adapter = adapter;
        }

        @Override
        public void disconnect() {
            if (!this.connected.compareAndSet(true,false))  {
                LogManager.err("You are not connected to a server.");
                return;
            }

            this.session.disconnect("Disconnect from server.");
        }

        @Override
        public void connect(@Nonnull String host, int port) {
            if (this.connected.get()) {
                LogManager.err("You already connected to a server.");
                return;
            }

            final MinecraftProtocol protocol = new MinecraftProtocol(sessionName);

            final TcpClientSession session = new TcpClientSession(host,port,protocol);

            session.addListener(adapter);
            session.connect();
            this.connected.set(true);
            this.session = session;
        }

        @Override
        public void connect(@Nonnull String host) {
            connect(host,25565);
        }

        @Override
        public void sendPacket(@Nonnull Packet packet) {
            if (this.connected.get())  {
                this.session.send(packet);
                return;
            }


            LogManager.err("You are not connected to a server, you cannot send packet.");
        }


        @Override
        public SocketAddress getAddress() {
            if (this.connected.get())
                return this.session.getRemoteAddress();

            LogManager.err("You are not connected to a server.");
            return null;
        }

        @Nonnull
        @Override
        public TcpClientSession getSession() {
            if (this.connected.get())
                return this.session;

            LogManager.err("You are not connected to a server, session returns null.");
            return null;
        }

        @Nonnull
        @Override
        public String getSessionName() {
            return this.sessionName;
        }

        @Override
        public void setSessionName(@Nonnull String name) {
            this.sessionName = name;
        }

        @Override
        public boolean isConnected() {
            return this.connected.get();
        }

        @Override
        public void setConnected(boolean v) {
            this.connected.set(v);
        }
    }
}
