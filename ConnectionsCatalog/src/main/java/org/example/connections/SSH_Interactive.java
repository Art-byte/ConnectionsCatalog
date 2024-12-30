package org.example.connections;
import com.jcraft.jsch.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class SSH_Interactive {

    public static void executeInteractiveScript(String username,
                                                String password,
                                                String host,
                                                int port,
                                                List<String> commands) throws Exception {
        Session session = null;
        ChannelShell channelShell = null;

        try {
            // Creamos y configuramos la sesión SSH
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Abrimos un canal de tipo shell
            channelShell = (ChannelShell) session.openChannel("shell");

            InputStream in = channelShell.getInputStream();
            OutputStream out = channelShell.getOutputStream();

            channelShell.connect();

            for(String command : commands){
                out.write((command + "\n").getBytes());
                out.flush();
                out.close();
            }

            // Leemos la respuesta
            byte[] buffer = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int bytesRead = in.read(buffer, 0, buffer.length);
                    if (bytesRead < 0) break;
                    System.out.print(new String(buffer, 0, bytesRead));
                }
                if (channelShell.isClosed()) break;
                Thread.sleep(100);
            }
        } finally {
            if (channelShell != null && channelShell.isConnected()) {
                channelShell.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
