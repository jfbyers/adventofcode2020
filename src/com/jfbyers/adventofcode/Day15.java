package com.jfbyers.adventofcode;

import java.util.*;
import java.util.stream.Collectors;

public class Day15 {

    public static void main(String[] args) {

         List<Integer> startList = Arrays.stream("6,3,15,13,1,0".split(","))
                 .map(s -> Integer.valueOf(s))
                 .collect(Collectors.toList());
        Integer spokenNumberat =  getSpokenNumberAt(startList,30000000 );
        System.out.println("Number: "+spokenNumberat+ " Pos : "+30000000);
    }

    private static Integer  getSpokenNumberAt(List<Integer> startList, int position){
        List<Integer> spokenNumbers = new ArrayList<>(startList);

        Map<Integer, Index> posMap = new HashMap<>();
        for (int i =0 ; i<spokenNumbers.size();i++){
            Integer value = spokenNumbers.get(i);
            posMap.put(value, new Index(i,-1,value));
        }

        int pos = posMap.size()  ;

        Index number = posMap.get(spokenNumbers.get(spokenNumbers.size()-1));
        while (pos<position){
            if (number.lastIndexBefore == -1){
                 number = new Index(pos ,posMap.get(0).currentIndex,0);
                posMap.put(0, number);
            }else{
                int value = number.currentIndex - number.lastIndexBefore;
                Index index = posMap.get(value);
                if (index==null){
                    number = new Index(pos, -1, value);
                }else {
                    number = new Index(pos, index.currentIndex, value);
                }

                posMap.put(value,number);
            }
            pos ++;

        }
        return posMap.values().stream().filter(index ->  index.currentIndex == position - 1 ).findFirst().get().value;
    }

    private static class Index {
        int currentIndex;
        int lastIndexBefore;
        int value;
        public Index(int i, int i1,int value) {
            this.currentIndex = i;
            this.lastIndexBefore = i1;
            this.value = value;
        }
    }
}
