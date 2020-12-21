package com.jfbyers.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day15 {

    public static void main(String[] args) {

        /* List<Integer> startList = Arrays.stream("6,3,15,13,1,0".split(","))
                 .map(s -> Integer.valueOf(s))
                 .collect(Collectors.toList());
*/
        List<Integer> startList = Arrays.stream("0,3,6".split(","))
                .map(s -> Integer.valueOf(s))
                .collect(Collectors.toList());

        Integer spokenNumberat =  getSpokenNumberAt(startList,30000000 );
        System.out.println("Number: "+spokenNumberat+ " Pos : "+30000000);

    }

    private static Integer  getSpokenNumberAt(List<Integer> startList, int position){
        List<Integer> spokenNumbers = new ArrayList<>(startList);
        spokenNumbers.add(0);

        int pos = spokenNumbers.size();
        while (pos<position){
            Integer number = spokenNumbers.get(pos - 1); // last spokenNumber
            int indexLastSpoken =  spokenNumbers.lastIndexOf(number);
            int indexLastSpokenBeforeThen = spokenNumbers.subList(0,indexLastSpoken).lastIndexOf(number);
            int result;
            if (indexLastSpokenBeforeThen != -1) {
                result = indexLastSpoken - indexLastSpokenBeforeThen;
            }else{
                result = 0;
            }
            spokenNumbers.add(result);
            pos ++;

        }
        return spokenNumbers.get(position - 1);
    }



}
