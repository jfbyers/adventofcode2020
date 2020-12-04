package com.jfbyers.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Day1 {
    private static Integer[] expenseReport = getExpenseReport();

    private static final int CONST_VALUE = 2020;

    public static void main(String[] args) {

         int result = searchSumWithOperands(3);
        System.out.println("Result 3 operands:" + result);
        int result2 = searchSumWithOperands(2);
        System.out.println("Result 2 operands:" + result2);

    }

    private static int searchSumWithOperands(int numberOfOperands) {
        for (int nextIndex = 0; nextIndex < expenseReport.length; nextIndex++) {
            int result = start(nextIndex, numberOfOperands);
            if (result != -1) {
                return result;
            }
        }
        return 0;
    }

    private static int start(int firstElementIndex, int numberOfOperands) {
        Integer firstElement = expenseReport[firstElementIndex];
        for (int secondElementIndex = firstElementIndex+1; secondElementIndex < expenseReport.length; secondElementIndex++) {
            boolean differentElements = secondElementIndex != firstElementIndex;
            if (differentElements) {
                Integer secondElement = expenseReport[secondElementIndex];
                int result = getValue(numberOfOperands, new Integer[]{firstElement, secondElement});
                if (result != -1) return result;
            }
        }
        return -1;
    }

    private static int getValue(int numberOfOperands, Integer[] elements) {
        if (numberOfOperands >= 3) {
            return findThirdElement(elements, numberOfOperands);
        } else {
            return applyOperation2(elements);
        }
    }

    private static Integer applyOperation2(Integer[] elements) {

        int elementsSum = Arrays.stream(elements)
                .mapToInt(Integer::intValue)
                .sum();
        if (elementsSum == CONST_VALUE) {
            int result = 1;
            for (int element : elements) {
                result *= element;
            }
            for (int i =0 ; i<elements.length ; i++){
                System.out.print(elements[i]+ " ");
            }
            System.out.println("");
            return result;
        }

        return -1;
    }


    private static int findThirdElement(Integer[] elements, int numberOfOperands) {
        for (int nextElementIndex = elements.length + 1 ; nextElementIndex < expenseReport.length; nextElementIndex++) {
            Integer nextElement = expenseReport[nextElementIndex];
            Integer[] elements3 = Stream.of(elements, new Integer[]{nextElement}).flatMap(Stream::of).toArray(Integer[]::new);
            int result = getValue(numberOfOperands - 1,elements3);
            if (result != -1) {
                return result;
            }
        }

        return -1;
    }

    private static Integer[] getExpenseReport() {

        try {
            Scanner scanner = new Scanner(new File("C:\\workspace\\adventofcode\\input.txt"));
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
