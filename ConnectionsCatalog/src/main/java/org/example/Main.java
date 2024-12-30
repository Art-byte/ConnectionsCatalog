package org.example;

import org.example.connections.SSH_Parameters;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        List<String> commands = generateCommandList();
        SSH_Parameters.initConnection(
                "netcas",
                "abcd",
                "192.168.1.155",
                22,
                commands);

    }

    public static List<String> generateCommandList(){
        return Arrays.asList(
                "./test.sh ls",
                "./test.sh creation",
                "./test.sh copy",
                "./test.sh exit"
        );
    }
}