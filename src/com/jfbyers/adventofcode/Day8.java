package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

    public static void main(String[] args) {
        //Part 1
        Program p = getProgram() ;
        p.execute();

        //Part 2
        List<Integer> jmpIndexes = getOperandsIndexes(p.instructions,Operand.JMP);
        List<Integer> noopIndexes = getOperandsIndexes(p.instructions,Operand.NOP);

        for (Integer jmpToChange: jmpIndexes){
            Program p1 = getProgram() ;
            p1.execute(jmpToChange,Operand.NOP);
        }

        for (Integer noOpToChange: noopIndexes){
            Program p2 = getProgram() ;
            p2.execute(noOpToChange,Operand.JMP);
        }
    }

    private static List<Integer> getOperandsIndexes(List<Instruction> instructions, Operand jmp) {
        List<Integer> result = new ArrayList<>();
        for (Instruction i : instructions){
            if (i.op.equals(jmp)){
                result.add(i.index);
            }
        }
        return result;
    }

    private static Program getProgram() {
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
        return new Program(instructionList);
    }

    private static class Program {
        private final List<Instruction> instructions;
        private Integer accumulator = 0;
        private int nextInstruction = 0;
        private List<Integer> memory = new ArrayList<>();

        public Program(List<Instruction> instructionList) {
            this.instructions = instructionList;
        }

        public void execute() {
            execute(null,null);
        }

        public void execute(Integer instructionIndexToChange,Operand opToChange) {
            boolean stop = false;

            while (!stop && !(nextInstruction >= this.instructions.size())) {
                Instruction ins = this.instructions.get(nextInstruction);
                if (ins.index.equals(instructionIndexToChange)){
                    ins.op = opToChange;
                }
                stop = executeInstruction(ins);
            }
            if (nextInstruction >= this.instructions.size()) {
                System.out.println("Program finished at accumulator " + accumulator + " index "+instructionIndexToChange + " "+opToChange);
            }
        }
        private boolean executeInstruction(Instruction i) {
            if (memory.contains(nextInstruction)) {
                //System.out.println("Infinite loop at instruction number " + i.index + " (" + i.op + " " + i.offset + ")" );
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
