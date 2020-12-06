package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 {

    private static List<GroupAnswer> groupAnswers = getGroupAnswers();

    public static void main(String[] args) {
        int count = 0;
        int count2 = 0;
        for (GroupAnswer g : groupAnswers){
            count+= g.getNumberOfYes();
            count2+= g.getNumberOfYes2();
        }
        System.out.println("Number of yeses "+count);
        System.out.println("Number of yeses 2 "+count2);

    }

    private static List<GroupAnswer> getGroupAnswers() {
        final File file = new File("inputDay6.txt");
        List<GroupAnswer> passports = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.isEmpty()) {
                    GroupAnswer p = GroupAnswer.fromString(sb.toString().trim());
                    sb = new StringBuilder();
                    passports.add(p);
                } else {
                    sb.append(line).append("\n");
                }
            }
            GroupAnswer p = GroupAnswer.fromString(sb.toString().trim());
            passports.add(p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passports;
    }

    private static class GroupAnswer {
        private final List<List<Character>> peopleAnswers;

        private GroupAnswer(List<List<Character>> personAnswer) {
            this.peopleAnswers = personAnswer;
        }

        public static GroupAnswer fromString(String grouLines) {
            final String[] split = grouLines.split("\n");
            final List<List<Character>> peopleAnswers = new ArrayList<>();

            for (String line : split){
                List<Character> personAnswers = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                peopleAnswers.add(personAnswers);

            }
            return new GroupAnswer(peopleAnswers);
        }


        public int getNumberOfYes2(){
            int length = this.peopleAnswers.size();

            List<Character> list = peopleAnswers.get(0);
            for (int i =0 ; i< length ; i++ ) {
                List<Character> compareList = peopleAnswers.get(i);
                List<Character> resultList = compareList.stream()
                        .distinct()
                        .filter(list::contains)
                        .collect(Collectors.toList());
                list = resultList;

            }
            return list.size();
        }


        public int getNumberOfYes(){
            int length = this.peopleAnswers.size();
            Set<Character> totalCharacters= new HashSet<>();


            for (int i =0 ; i< length ; i++ ) {
                List<Character> list = peopleAnswers.get(i);
                totalCharacters.addAll(list);
            }
            return totalCharacters.size();
        }
    }
}
