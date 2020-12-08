package com.jfbyers.adventofcode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 {

    public static void main(String[] args) {
        //Part 1
        Program p = new Program();
        if (!p.execute()) {
            System.out.println("Infinite loop at program. Accumulator value " + p.accumulator);
        }

        //Part 2
        List<Integer> jmpIndexes = getOperandsIndexes(Operand.JMP);
        List<Integer> noopIndexes = getOperandsIndexes(Operand.NOP);

        for (Integer instructionIndexToChange : jmpIndexes) {
            Program p1 = new Program();
            p1.execute(instructionIndexToChange, Operand.NOP);
        }

        for (Integer instructionIndexToChange : noopIndexes) {
            Program p2 = new Program();
            p2.execute(instructionIndexToChange, Operand.JMP);
        }
    }

    private static List<Integer> getOperandsIndexes(Operand op) {
        return getInstructions().stream()
                .filter(i -> i.op.equals(op))
                .map(instruction -> instruction.index)
                .collect(Collectors.toList());
    }

    private static List<Instruction> getInstructions() {
        try (Stream<String> stream = Files.lines(Paths.get("inputDay8.txt"))) {
            final AtomicInteger index = new AtomicInteger(0);
            return stream.map(line -> Instruction.fromString(line, index.incrementAndGet())).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static class Program {
        private final List<Instruction> instructions;
        private final List<Integer> executedInstructions = new ArrayList<>();

        private Integer accumulator = 0;
        private int nextInstruction = 0;

        public Program() {
            this.instructions = getInstructions();
        }

        public boolean execute() {
            return execute(null, null);
        }

        public boolean execute(Integer instructionIndexToChange, Operand opToChange) {
            boolean stop = false;

            int instructionsLength = this.instructions.size();
            while (!stop && !(nextInstruction == instructionsLength)) {
                Instruction ins = this.instructions.get(nextInstruction);
                if (ins.index.equals(instructionIndexToChange)) {
                    ins.op = opToChange;
                }
                stop = executeInstruction(ins);
            }
            if (nextInstruction == instructionsLength) {
                System.out.println("Program finished at accumulator = " + accumulator + ". Instruction changed to a " + opToChange + " at index " + instructionIndexToChange);
                return true;
            }
            return false;
        }

        private boolean executeInstruction(Instruction i) {
            if (executedInstructions.contains(nextInstruction)) {
                return true;
            }
            executedInstructions.add(nextInstruction);

            switch (i.op) {
                case ACC:
                    accumulator += i.offset;
                    nextInstruction++;
                    break;
                case JMP:
                    nextInstruction += i.offset;
                    break;
                case NOP:
                    nextInstruction++;
                    break;
            }
            return false;
        }
    }

    private static class Instruction {
        private Operand op;
        private final Integer offset;
        private final Integer index;

        private Instruction(Operand op, Integer offset, Integer index) {
            this.op = op;
            this.offset = offset;
            this.index = index;
        }

        public static Instruction fromString(String instructionLine, Integer index) {
            String[] opAndOffset = instructionLine.split(" ");
            Operand op = Operand.valueOf(opAndOffset[0].toUpperCase());
            Integer offset = Integer.valueOf(opAndOffset[1]);
            return new Instruction(op, offset, index);
        }
    }

    private enum Operand {
        ACC, JMP, NOP;
    }
}
