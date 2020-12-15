package com.jfbyers.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day9 {

    private final static int ELEMENTS = 25;

    private static Integer[] numericInput = getNumericInput();


    public static void main(String[] args) {
        //First step
        final Integer value = getOddValue();

        Integer[] baseInput = Arrays.copyOfRange(numericInput, 0, 2);
        for (int start = 2, index = 0; index + start < numericInput.length; start++,index++) {
           boolean stop = check (baseInput, start,value);
            if (stop) {
                break;
            }
        }
    }


    private static boolean check(Integer[] baseInput, int start, Integer value){

        for (int index2=0; index2 + start < numericInput.length;) {
            boolean result2 = applyOperation2(baseInput,value);

            if (result2) {
                long value1 = Collections.max(Arrays.asList(baseInput));
                long value2 = Collections.min(Arrays.asList(baseInput));
                System.out.println("Number is  the sum of the array values of length  " + start + " = "+ (value1+value2));
                return true;
            }
            index2++;
            baseInput = Arrays.copyOfRange(numericInput, index2, index2 + start);

        }
       return false;
    }

    private static Integer getOddValue() {
        int index = 0;
        Integer[] baseInput = Arrays.copyOfRange(numericInput, index, ELEMENTS);
        while (index + ELEMENTS < numericInput.length) {
            Integer valueToCheck = numericInput[index + ELEMENTS];
            boolean result2 = searchSumWithOperands(baseInput, valueToCheck);
            if (!result2) {
                System.out.println("Number is not the sum of the previous 25 values " + valueToCheck + " at index " + index);
                return valueToCheck;
            }
            index++;
            baseInput = Arrays.copyOfRange(numericInput, index, index + ELEMENTS);
        }
        return -1;
    }

    private static boolean searchSumWithOperands(Integer[] baseInput, Integer value) {
        for (int nextIndex = 0; nextIndex < baseInput.length; nextIndex++) {
            boolean result = start(baseInput, nextIndex, value);
            if (result) {
                return true;
            }
        }
        return false;
    }

    private static boolean start(Integer[] baseInput, int firstElementIndex, Integer value) {
        Integer firstElement = baseInput[firstElementIndex];
        for (int secondElementIndex = firstElementIndex + 1; secondElementIndex < baseInput.length; secondElementIndex++) {
            boolean differentElements = secondElementIndex != firstElementIndex;
            if (differentElements) {
                Integer secondElement = baseInput[secondElementIndex];
                boolean result = applyOperation2(new Integer[]{firstElement, secondElement}, value);
                if (result) return true;
            }
        }
        return false;
    }


    private static boolean applyOperation2(Integer[] elements, Integer value) {

        int elementsSum = Arrays.stream(elements)
                .mapToInt(Integer::intValue)
                .sum();
        if (elementsSum == value) {
            return true;
        }

        return false;
    }


    private static Integer[] getNumericInput() {

        try {
            Scanner scanner = new Scanner(new File("inputDay9.txt"));
            List<Integer> tall = new ArrayList<>();

            while (scanner.hasNextInt()) {
                tall.add(scanner.nextInt());
            }
            return tall.toArray(new Integer[]{});
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new Integer[]{};

    }
}
