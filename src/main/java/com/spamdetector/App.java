package com.spamdetector;

import com.spamdetector.cli.CLI;

public class App {
    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run(args);
    }
}
