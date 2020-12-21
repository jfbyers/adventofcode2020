package com.jfbyers.adventofcode;

import java.io.*;
import java.util.Scanner;
import java.time.Instant;
import java.time.Duration;


public abstract class Day {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public String inputFile;
    public String getInput() {
        try {
            File f = new File(this.inputFile);
            if (!f.exists()) {
                System.out.println(String.format("%s not found", this.inputFile));
                return null;
            }
            Scanner scanner = new Scanner(f);
            return scanner.useDelimiter("\\Z").next();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public abstract void part1();
    public abstract void part2();
    public void run() {
        System.out.println(String.format("%sRunning %s%s", ANSI_GREEN, this.getClass().getName(), ANSI_RESET));

        Instant start1 = Instant.now();
        this.part1();
        Instant end1 = Instant.now();
        long runtimePart1 = Duration.between(start1, end1).toMillis();
        System.out.println(String.format("Part 1 took %d ms", runtimePart1));

        Instant start2 = Instant.now();
        this.part2();
        Instant end2 = Instant.now();
        long runtimePart2 = Duration.between(start2, end2).toMillis();
        System.out.println(String.format("Part 2 took %d ms", runtimePart2));
        System.out.println(String.format("\n%s", ANSI_RESET));
    }
}
