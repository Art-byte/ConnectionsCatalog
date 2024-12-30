package org.example.connections;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SSH_Parameters {

    public static void initConnection(String username,
                                      String password,
                                      String host,
                                      int port,
                                      List<String> commands) throws Exception {
        Session session = null;

        try {
            // Creamos y configuramos la sesión SSH
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Connecting to the SSH server...");
            session.connect();
            System.out.println("Connected to the SSH server!");

            // Ejecutamos cada comando en un nuevo canal
            for (String command : commands) {
                System.out.println("Executing command: " + command);
                ChannelExec channelExec = null;

                try {
                    // Abrimos el canal y configuramos el comando
                    channelExec = (ChannelExec) session.openChannel("exec");
                    channelExec.setCommand(command);

                    // Capturamos la salida del comando
                    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
                    channelExec.setOutputStream(responseStream);
                    channelExec.setErrStream(errorStream);

                    // Ejecutamos el comando
                    channelExec.connect();
                    while (channelExec.isConnected()) {
                        Thread.sleep(100);
                    }

                    // Mostramos las salidas y errores
                    String response = responseStream.toString();
                    String errors = errorStream.toString();

                    System.out.println("Command> " + command);
                    System.out.println(response);

                    if (!errors.isEmpty()) {
                        System.err.println("Error> " + command);
                        System.err.println(errors);
                    }
                } catch (JSchException e) {
                    System.err.println("Failed to execute command: " + command);
                    e.printStackTrace();
                } finally {
                    if (channelExec != null && channelExec.isConnected()) {
                        channelExec.disconnect();
                    }
                }
            }
        } finally {
            if (session != null && session.isConnected()) {
                System.out.println("Disconnecting from the SSH server...");
                session.disconnect();
            }
        }
    }
}
