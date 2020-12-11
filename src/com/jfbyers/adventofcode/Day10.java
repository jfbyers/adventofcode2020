package com.jfbyers.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 {


    public static void main(String[] args) {
        List<Integer> joltageList = getJoltageList();
        Collections.sort(joltageList);

        List<Integer> joltDifferences = new ArrayList<>();
        Integer firstAdapterDifference = getNextValidJoltDifference(0, joltageList);
        joltDifferences.add(firstAdapterDifference);

        List<Integer> intermediateDifference = getJoltDifferenceList(joltageList);
        joltDifferences.addAll(intermediateDifference);
        //device builtInAdapter
        Integer lastAdapter = 3;
        joltDifferences.add(lastAdapter);

        long oneJoltDifference = joltDifferences.stream().filter(adapter -> adapter == 1).count();
        long threeJoltDifference = joltDifferences.stream().filter(adapter -> adapter == 3).count();

        long step1 = oneJoltDifference * threeJoltDifference;
        System.out.println("Number of 1 jolt differences times 3 jolt differences = " + step1);

        //step 2
        System.out.println("Number of dispositions " + part2(joltageList.toArray(new Integer[0])));


    }

    private static long part2(Integer[] joltageList) {
        int numberOfAdapters = joltageList.length;
        Integer lastAdapter = joltageList[numberOfAdapters - 1];
        final long[] sums = new long[lastAdapter + 1];
        sums[0] = 1;
        for (int i = 0; i < numberOfAdapters; i++) {
            Integer adapterJolts = joltageList[i];
            final long x = adapterJolts >= 3 ? sums[adapterJolts - 3] : 0;
            final long y = adapterJolts >= 2 ? sums[adapterJolts - 2] : 0;
            final long z = adapterJolts >= 1 ? sums[adapterJolts - 1] : 0;
            sums[adapterJolts] = x + y + z;
        }
        return sums[sums.length - 1];
    }


    private static List<Integer> getJoltDifferenceList(List<Integer> adapterList) {
        final List<Integer> differences = new ArrayList<>();
        for (int jolt : adapterList) {
            Integer nextValidDifference = getNextValidJoltDifference(jolt, adapterList);
            if (nextValidDifference != null) {
                differences.add(nextValidDifference);
            }
        }
        return differences;
    }

    private static Integer getNextValidJoltDifference(Integer joltage, List<Integer> list) {
        for (int jolt : list) {
            int difference = jolt - joltage;
            switch (difference) {
                case 1:
                case 2:
                case 3:
                    return  difference;
                default:
                    continue;
            }
        }
        return null;

    }

    private static List<Integer> getJoltageList() {
        try (Stream<String> stream = Files.lines(Paths.get("inputDay10.txt"))) {
            return stream.map(line -> Integer.valueOf(line)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}
