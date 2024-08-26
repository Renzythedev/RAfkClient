package me.renzy.afkclient;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.codec.MinecraftCodec;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.command.Command;
import me.renzy.afkclient.command.Commands;
import me.renzy.afkclient.exception.Exceptions;
import me.renzy.afkclient.objects.PacketData;
import me.renzy.afkclient.protocol.MinecraftBot;
import me.renzy.afkclient.protocol.Packets;
import me.renzy.afkclient.protocol.Session;
import me.renzy.afkclient.terminable.Terminable;
import me.renzy.afkclient.utils.Colors;
import me.renzy.afkclient.utils.TextParser;
import me.renzy.afkclient.utils.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Client implements Terminable {

    private static final Client CLIENT = new Client();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private Session session;
    public final DefaultCommands commands = new DefaultCommands();
    public final DefaultPackets packets = new DefaultPackets();

    public final SessionAdapter ADAPTER = new SessionAdapter(){

        @Override
        public void connected(ConnectedEvent event) {
            LogManager.info("Connected to " + event.getSession().getHost() + ":" + event.getSession().getPort() + " with " + session.getSessionName() + " name.");
        }

        @Override
        public void disconnected(DisconnectedEvent event) {
            LogManager.info("Disconnected from " + event.getSession().getHost() + ":" + event.getSession().getPort() + " with: " + TextParser.parse(event.getReason()));
            session.setConnected(false);
        }

        @Override
        public void packetReceived(com.github.steveice10.packetlib.Session session, Packet packet) {
            Packets.callPacket(packet);
        }
    };

    private Client(){}

    public void start() {
        this.session = MinecraftBot.createSession("player" + ThreadLocalRandom.current().nextInt(100000),ADAPTER);
        BufferedReader consoleReader = new BufferedReader(System.console().reader());
        LogManager.info("Client running...");
        LogManager.info("'help' for commands.");
        while(!this.closed.get()) {
            try {
                if(!Commands.invokeCommand(consoleReader.readLine()))
                    LogManager.info("Unknown command use 'help' command for see all commands");
            }
            catch (IOException e) {
                e.fillInStackTrace();
            }
        }
    }

    public void shutdown() {
        try {
            close();
        } catch (Exception e) {
            Exceptions.reportTerminable(this,e);
        }
    }


    @Override
    public void close() throws Exception {
        if (!this.closed.compareAndSet(false,true))
            throw Exceptions.TERMINABLE_ALREADY_CLOSED;

        LogManager.warn("Client closing...");
    }

    @Override
    public boolean isClosed() {
        return this.closed.get();
    }

    public static Client getClient() {
        return CLIENT;
    }

    public AtomicBoolean getClosed() {
        return closed;
    }


    public Session getSession() {
        return session;
    }

    public final class DefaultCommands {

        public final Command NAME_COMMAND = Commands.getCommand("name")
                .description("sets/gets session name")
                .executor((args, cmd) -> {
                    if (args.length > 0){
                        if ("set".equalsIgnoreCase(args[0])) {
                            String name = args[1];
                            if (args.length > 1) {
                                if (name != null || !name.isEmpty()) {
                                    session.setSessionName(name);
                                    LogManager.info(Colors.GREEN + "Success! session name changed with '"+ name + "'!");
                                    return true;
                                }else {
                                    LogManager.err(Colors.RED + "Name cannot be null or empty.");
                                    return true;
                                }
                            }else {
                                LogManager.err(Colors.RED + "Please provide a name to set.");
                            }
                        } else if ("get".equalsIgnoreCase(args[0])) {
                            LogManager.info("Session name: "+ Colors.GREEN + session.getSessionName());
                            return true;
                        }
                    }

                    LogManager.info(Colors.PURPLE + "Name commands:");
                    LogManager.info(Colors.GREEN + "name set <name>: "+ Colors.RESET + "changes session name.");
                    LogManager.info(Colors.GREEN + "name get: "+ Colors.RESET + "returns session name.");
                    return true;
                });;
        public final Command CONNECT_COMMAND = Commands.getCommand("connect")
                .description("connects to server")
                .usage("connect <host> <port (optional)>")
                .executor((args, cmd) -> {
                    if (args.length < 1)
                        return false;

                    String host = args[0];
                    if (host == null || host.isEmpty()) {
                        LogManager.err(Colors.RED + "Host cannot be null or empty.");
                        return true;
                    }

                    try {
                        if (args.length == 1) {
                            session.connect(host);
                        } else {
                            int port = Integer.parseInt(args[1]);
                            session.connect(host, port);
                        }
                    } catch (NumberFormatException e) {
                        LogManager.err(Colors.RED + "Invalid port number.");
                        return true;
                    }

                    return true;
                });

        public final Command CLOSE_COMMAND = Commands.getCommand("close")
                .aliases("stop")
                .description("Closes the client.")
                .executor((args,cmd) -> {
            Client.getClient().shutdown();
            System.exit(0);
            return true;
        });

        public final Command DISCONNECT_COMMAND = Commands.getCommand("disconnect")
                .description("Disconnects from server")
                .executor((args, cmd) -> {
                    session.disconnect();
                    return true;
                });

        public final Command COMMAND_SENDER_COMMAND = Commands.getCommand("send")
                .description("sends message or command to server.")
                .aliases("command")
                .executor((args, command) -> {
                    StringBuilder cmd = new StringBuilder();
                    for (int i = 0; i<args.length; i++) {
                        cmd.append(args[i]).append(" ");
                    }

                    session.sendPacket(new ServerboundChatPacket(cmd.toString(),System.currentTimeMillis(),0,null,0, BitSet.valueOf("".getBytes())));
                    return true;
                });
        private DefaultCommands() {}
    }

    public final class DefaultPackets {

        public final PacketData<?> SYSTEM_MESSAGES = Packets.subscribe(ClientboundSystemChatPacket.class).listener(p -> LogManager.info(TextParser.parse(p.getContent()))).register();
        public final PacketData<?> PLAYER_MESSAGES = Packets.subscribe(ClientboundPlayerChatPacket.class).listener(p -> LogManager.info(TextParser.parse(p.getContent()))).register();

        private DefaultPackets(){}
    }

}
