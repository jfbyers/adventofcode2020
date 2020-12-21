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
        part1(instructions);
        part2(instructions);
    }

    private static void part1(List<Instruction> instructions) {
        Map<Integer,Long> memory = new HashMap<>();
        String mask="000000000000000000000000000000000000";

        for (Instruction i : instructions){
            switch (i.type){

                case MASK:
                     mask = i.values[0];
                     break;
                case MEM:
                    Integer memoryPosition = Integer.valueOf(i.values[0]);
                    Long newValue = overwrite(Integer.valueOf(i.values[1]),mask);
                    memory.put(memoryPosition,newValue);
                    break;
            }
        }
        long sum = memory.values().stream().reduce(Long::sum).orElse(0L);
        System.out.println("Sum is "+sum);
    }

    private static Long overwrite(Integer value, String mask) {
        char[] maskBits = (mask.toCharArray());
        char[] valueBits = String.format("%36s",Integer.toBinaryString(value)).replace(' ', '0').toCharArray();

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

    private static void part2(List<Instruction> instructions) {
        Map<String, Long> memory = new HashMap<>();
        String mask = "000000000000000000000000000000000000";

        for (Instruction i : instructions) {
            switch (i.type) {
                case MASK:
                    mask = i.values[0];
                    break;
                case MEM:
                    String memAddrss = String.format("%36s",Integer.toBinaryString(Integer.parseInt(i.values[0]))).replace(' ', '0');
                    Set<String> memoryPositions = applyMask2(memAddrss, mask,0);
                    for (String memoryPosition : memoryPositions) {
                        memory.put(memoryPosition, Long.valueOf(i.values[1]));
                    }
                    break;
            }
        }
        long sum = memory.values().stream().reduce(Long::sum).orElse(0L);
        System.out.println("Sum is " + sum);
    }

    private static Set<String> applyMask2(String address, String mask, int index){
        if (index == address.length()){
            Set<String> solution = new HashSet<>();
            solution.add(address);
            return solution;
        }
        if (mask.charAt(index)=='1'){
            address = address.substring(0,index) + mask.charAt(index) + address.substring(index+1);
            return applyMask2(address,mask,index+1);
        } else if (mask.charAt(index)=='X'){
            address = address.substring(0,index) + "1" + address.substring(index+1);
            Set<String> one =applyMask2(address,mask,index+1);
            address = address.substring(0,index) + "0" + address.substring(index+1);
            Set<String> two =applyMask2(address,mask,index+1);
            one.addAll(two);
            return one;
        }

        return applyMask2(address,mask,index+1);
    }

    private static List<Instruction> getInstructions() {
        try (Stream<String> stream = Files.lines(Paths.get("inputDay14.txt"))) {
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

}
