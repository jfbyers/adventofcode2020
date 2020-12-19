package com.jfbyers.adventofcode;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 {


    public static void main(String[] args) {
        List<Instruction> instructions = getInstructions();
        System.out.println("There are "+ instructions
                .size()+ " instructions");

        Map<Integer,Long> memory = new HashMap<>();


        String mask="000000000000000000000000000000000000";

        for (Instruction i : instructions){
            switch (i.type){

                case MASK:
                     mask = i.values[0];
                     break;
                case MEM:
                    Integer memoryPosition = Integer.valueOf(i.values[0]);
                    Long newValue = overwrite(Long.valueOf(i.values[1]),mask);
                    memory.put(memoryPosition,newValue);
                    break;
            }

        }
         long sum = memory.values().stream().reduce(Long::sum).orElse(0L);
         System.out.println("Sum is "+sum);

    }

    private static Long overwrite(Long value, String mask) {
        char[] maskBits = (mask.toCharArray());

        char[] valueBits = getValueBits(value);


        for (int i =0 ; i<maskBits.length;i++){
            if (maskBits[i]=='0'){
                valueBits[i] = '0';
            }else if(maskBits[i]=='1'){
                valueBits[i] = '1';
            }
        }
        String val = (new String(valueBits));
        return new BigInteger(val,2).longValue();

    }

    private static Long overwrite2(Long value, String mask) {
        char[] maskBits = (mask.toCharArray());

        char[] valueBits = getValueBits(value);


        for (int i =0 ; i<maskBits.length;i++){
            if (maskBits[i]=='0'){
                valueBits[i] = '0';
            }else if(maskBits[i]=='1'){
                valueBits[i] = '1';
            }
        }
        String val = (new String(valueBits));
        return new BigInteger(val,2).longValue();

    }


    private static char[] getValueBits(Long value) {
        char[] originalValue = Long.toBinaryString(value).toCharArray();

        char[] valueBits = new char[36];
        int index;


        for ( index = 0 ; index< valueBits.length - originalValue.length;index++){
            valueBits[index]='0';
        }


        for  (int i=0; index< valueBits.length;i++,index++){
            valueBits[index]=originalValue[i];
        }


        return valueBits;
    }

    private static List<Instruction> getInstructions() {
        try (Stream<String> stream = Files.lines(Paths.get("inputDay14a.txt"))) {
            return stream.map(line ->  Instruction.fromString(line)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public enum InstructionType{
        MEM, MASK;
    }
    public static class Instruction {
        private final InstructionType type;
        private final String[] values;


        public Instruction(InstructionType type, String[] values) {
            this.type = type;
            this.values = values;
        }

        public static Instruction fromString(String line) {
            if (line.startsWith("mask")){
                String[] values = new String[]{line.split("=")[1].trim()};
                return new Instruction(InstructionType.MASK,values);
            }else{
                String[] values = line.split("=");
                Pattern p = Pattern.compile("mem\\[(\\d+)\\]");
                Matcher m = p.matcher(values[0].trim());
                if (m.matches()){
                    String value1= m.group(1);
                    String value2 = values[1].trim();
                    return new Instruction(InstructionType.MEM,new String[]{value1,value2});
                }
            return null;
            }

        }
    }

    private static class MemoryPosition {
        private final int position;
        private int value;

        private MemoryPosition(int position, int value) {
            this.position = position;
            this.value = value;
        }
    }
}
