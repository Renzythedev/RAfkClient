package me.renzy.afkclient.protocol;

import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public interface Session {

    void disconnect();

    void connect(@Nonnull String host, int port);

    void connect(@Nonnull String host);

    void sendPacket(@Nonnull Packet packet);

    SocketAddress getAddress();

    @Nonnull TcpClientSession getSession();

    @Nonnull String getSessionName();

    void setSessionName(@Nonnull String name);

    boolean isConnected();

    void setConnected(boolean v);
}
