package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

    private static List<Instruction> instructionList;

    public static void main(String[] args) {
        //Part 1
        Program p = new Program() ;
        if (!p.execute()){
            System.out.println("Infinite loop at program. Accumulator value "+p.accumulator );
        }

        //Part 2
        List<Integer> jmpIndexes = getOperandsIndexes(Operand.JMP);
        List<Integer> noopIndexes = getOperandsIndexes(Operand.NOP);

        for (Integer instructionIndexToChange: jmpIndexes){
            Program p1 = new Program();
            p1.execute(instructionIndexToChange,Operand.NOP);
        }

        for (Integer instructionIndexToChange: noopIndexes){
            Program p2 = new Program();
            p2.execute(instructionIndexToChange,Operand.JMP);
        }
    }

    private static List<Integer> getOperandsIndexes(Operand op) {
        List<Integer> result = new ArrayList<>();
        for (Instruction i : getInstructions()){
            if (i.op.equals(op)){
                result.add(i.index);
            }
        }
        return result;
    }

    private static List<Instruction> getInstructions() {
        final File file = new File("inputDay8.txt");
        final List<Instruction> instructionList = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            Integer index = 0;
            while ((line = br.readLine()) != null) {
                Instruction i = Instruction.fromString(line, index);
                instructionList.add(i);
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instructionList;
    }

    private static class Program {
        private final List<Instruction> instructions;
        private Integer accumulator = 0;
        private int nextInstruction = 0;
        private List<Integer> memory = new ArrayList<>();

        public Program() {
            this.instructions = getInstructions();
        }

        public boolean execute() {
            return execute(null,null);
        }

        public boolean execute(Integer instructionIndexToChange,Operand opToChange) {
            boolean stop = false;

            while (!stop && !(nextInstruction == this.instructions.size())) {
                Instruction ins = this.instructions.get(nextInstruction);
                if (ins.index.equals(instructionIndexToChange)){
                    ins.op = opToChange;
                }
                stop = executeInstruction(ins);
            }
            if (nextInstruction == this.instructions.size()) {
                System.out.println("Program finished at accumulator = " + accumulator + ". Instruction changed to a "+ opToChange+ " at index "+instructionIndexToChange);
                return true;
            }
            return false;
        }
        private boolean executeInstruction(Instruction i) {
            if (memory.contains(nextInstruction)) {
                return true;
            }
            memory.add(nextInstruction);

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
            String[] opAndOffeset = instructionLine.split(" ");
            Operand op = Operand.valueOf(opAndOffeset[0].toUpperCase());
            Integer offset = Integer.valueOf(opAndOffeset[1]);
            return new Instruction(op, offset, index);
        }
    }

    private enum Operand {
        ACC, JMP, NOP;
    }
}
