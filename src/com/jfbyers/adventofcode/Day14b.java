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

public class Day14b {


    public static void main(String[] args) {
        List<Instruction> instructions = getInstructions();
        System.out.println("There are " + instructions
                .size() + " instructions");

        Map<Integer, Long> memory = new HashMap<>();
        String mask = "000000000000000000000000000000000000";

        for (Instruction i : instructions) {
            switch (i.type) {

                case MASK:
                    mask = i.values[0];
                    System.out.println("Mask updated");
                    break;
                case MEM:
                    Integer memoryPos = Integer.valueOf(i.values[0]);
                    Set<Integer> memoryPositions = new HashSet<>(overwrite2(memoryPos, mask));
                    for (Integer memoryPosition : memoryPositions) {
                        memory.put(memoryPosition, Long.valueOf(i.values[1]));
                    }
                    System.out.println("Updated "+memoryPositions.size()+ " positions");
                    break;
            }
        }
        long sum = memory.values().stream().reduce(Long::sum).orElse(0L);
        System.out.println("Sum is " + sum);

    }


    private static List<Integer> overwrite2(Integer value, String mask) {
        char[] maskBits = (mask.toCharArray());
        char[] addressBits = getValueBits(value);

        for (int i = 0; i < maskBits.length; i++) {
            if (maskBits[i] == 'X') {
                addressBits[i] = 'X';
            } else if (maskBits[i] == '1') {
                addressBits[i] = '1';
            }
        }
        List<Integer> global = new ArrayList<>();
        Set<String> set = new HashSet<>();
        set.add(new String(addressBits));
        Set<String> list = new HashSet<>();

        Set<String> g = getExpanses(set,list);

        for (String item : g){
            String cleaned = clean(item.toCharArray());
            global.add(new BigInteger(cleaned,2).intValue());
        }
        return global;

    }

    private static String clean(char[] item) {
        for (int i = 0 ; i<item.length ; i++){
            if (item[i] == 0){
                item[i] ='0';
            }
        }
        return new String (item);
    }

    private static Set<String> getExpanses(Set<String> addressList,Set<String> list) {
        for (String addressBits : addressList) {
            if (list.contains(addressBits)) continue;
            char[] result = new char[addressBits.length()];
            for (int i = 0; i < addressBits.length(); i++) {
                {
                    char c = addressBits.charAt(i);
                    if (c == 'X') {
                         list.addAll(getExpanses(finish(addressBits, i),list));
                            //return list;
                    } else {
                        result[i] = c;
                    }
                }
            }
            if (check(result)) {
                //System.out.println("Checking "+new String(result)+ " length "+result.length);

                list.add(new String(result));
            }
        }
        return list;
    }

    private static boolean check(char[] item) {

            for (int i = 0 ; i<item.length ; i++){
                if (item[i] == 0){
                    return false;
                }
            }
            return true;
        }



    private static Set<String> finish(String addressBits, int i) {
        StringBuilder stringBuilder1 = new StringBuilder(addressBits);
        StringBuilder stringBuilder2 = new StringBuilder(addressBits);
        stringBuilder1.setCharAt(i,'1');
        String one = stringBuilder1.toString();
        stringBuilder2.setCharAt(i,'0');
        String two = stringBuilder2.toString();
        Set<String> set = new HashSet<>();
        set.add(one);
        set.add(two);
        return set;
    }

    private static char[] getValueBits(Integer value) {
        char[] originalValue = Integer.toBinaryString(value).toCharArray();

        char[] valueBits = new char[36];
        int index;

        for (index = 0; index < valueBits.length - originalValue.length; index++) {
            valueBits[index] = '0';
        }
        for (int i = 0; index < valueBits.length; i++, index++) {
            valueBits[index] = originalValue[i];
        }


        return valueBits;
    }

    private static List<Instruction> getInstructions() {
        try (Stream<String> stream = Files.lines(Paths.get("inputDay14´ç´ñç_`lñ}{ñ.txt"))) {
            return stream.map(line -> Instruction.fromString(line)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public enum InstructionType {
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
            if (line.startsWith("mask")) {
                String[] values = new String[]{line.split("=")[1].trim()};
                return new Instruction(InstructionType.MASK, values);
            } else {
                String[] values = line.split("=");
                Pattern p = Pattern.compile("mem\\[(\\d+)\\]");
                Matcher m = p.matcher(values[0].trim());
                if (m.matches()) {
                    String value1 = m.group(1);
                    String value2 = values[1].trim();
                    return new Instruction(InstructionType.MEM, new String[]{value1, value2});
                }
                return null;
            }

        }
    }


}




