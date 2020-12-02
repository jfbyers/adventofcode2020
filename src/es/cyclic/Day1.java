package es.cyclic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1 {
    private static Integer[] expenseReport = getExpenseReport();

    private static final int CONST_VALUE = 2020;

    public static void main(String[] args)  {

        int result= checkForThreeOperands();
        if (result!=-1){
            System.out.println("Result:" + result);
        }
    }

    private static int checkForThreeOperands() {
        for (int nextIndex = 0; nextIndex< expenseReport.length; nextIndex++){
            int result= start(nextIndex);
            if (result!=-1){
                return result;
            }
        }
        return -1;
    }

    private static int start(int index) {
        Integer element = expenseReport[index];
        for (int nextIndex = 0; nextIndex< expenseReport.length; nextIndex++) {
            if (nextIndex != index) {
                Integer acum = element + expenseReport[nextIndex];
                int result = findThirdElement(index, nextIndex,acum);
                if (result!=-1){
                    return result;
                }
            }
        }
        return -1;
    }

    private static int findThirdElement(int firstElementIndex, int secondElementIndex, Integer acum) {
        Integer secondElement = expenseReport[secondElementIndex];
        Integer firstElement = expenseReport[firstElementIndex];

        for (int thirdElementIndex = 0; thirdElementIndex< expenseReport.length ; thirdElementIndex++){
                Integer thirdElement= expenseReport[thirdElementIndex];
                boolean differentElements = thirdElementIndex != firstElementIndex && thirdElementIndex != secondElementIndex;
                if (acum + thirdElement == CONST_VALUE && differentElements){
                    System.out.println(firstElement+" "+ secondElement + " "+ thirdElement);
                    return firstElement * secondElement * thirdElement;
                }

        }
        return -1;
    }

    private static Integer[] getExpenseReport() {

        try {
            Scanner   scanner = new Scanner(new File("C:\\workspace\\adventofcode\\input.txt"));
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
