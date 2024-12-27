package org.example;

import org.example.connections.SSH;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        List<String> commands = generateCommandList();
        SSH.initConnection(
                "dummy",
                "dummy",
                "localhost",
                22,
                commands);

    }

    public static List<String> generateCommandList(){
        return Arrays.asList(
                "./test.sh",
                "ls",
                "copy",
                "creation",
                "exit"
        );
    }
}