package org.example.connections;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SSH {

    public static void initConnection(String username,
                                      String password,
                                      String host,
                                      int port,
                                      List<String> commands) throws Exception {
        Session session = null;
        ChannelExec channelExec = null;

        try{
            //Creamos y configuramos la sesion SSH
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            //Abrimos el canal
            channelExec = (ChannelExec) session.openChannel("exec");

            for(String command : commands){
                channelExec.setCommand(command);

                //Capturamos la salida del comando
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
                channelExec.setOutputStream(responseStream);
                channelExec.setErrStream(errorStream);

                //Ejecutamos el comando
                channelExec.connect();

                //Esperamos a que el comando se termine de ejecutar
                while(channelExec.isConnected()){
                    Thread.sleep(100);
                }

                //Mostramos las salidas y errores
                String response = responseStream.toString();
                String errors = errorStream.toString();

                System.out.println("Command> " + command);
                System.out.println(response);

                if(!errors.isEmpty()){
                    System.err.println("Error> " + command);
                    System.err.println(errors);
                }

                //Limpiamos el stream para el siguiente commando
                responseStream.reset();
                errorStream.reset();
            }
        } finally {
            if(session != null && session.isConnected()){
                session.disconnect();
            }

            if(channelExec != null && channelExec.isConnected()){
                channelExec.disconnect();
            }
        }
    }

}
