package com.jfbyers.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) {
        Program p = getProgram();
        int invalidFields = 0;
        for (Ticket t : p.nearbyTickets){
            invalidFields += checkTicket(t, p.rules);

        }
        System.out.println("Code "+invalidFields);
    }

    private static int checkTicket(Ticket t, Set<Rule> rules) {
        Map<Integer, Rule>  validValues= new HashMap<>();
        boolean valid=false ;
        int counter = 0;
        for (Integer value : t.values) {

            for (Rule r : rules) {
                Range range1 = r.ranges[0];
                Range range2 = r.ranges[1];
                boolean isValid = (range1.lowerBound <= value && value <= range1.upperBound) || (range2.lowerBound <= value && value <= range2.upperBound);
                if (isValid){
                    valid = true;
                }

            }
            if (!valid){
                counter+= value;
            }
            valid = false;
        }

        return counter;
    }

    private static  Program getProgram() {
        Program program = new Program();

        try {
            Scanner scanner = new Scanner(new File( "inputDay16.txt"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (!line.trim().isBlank()){
                    Rule rule = Rule.fromLine(line);
                    program.rules.add(rule);
                    line = scanner.nextLine();
                }
                while (!line.equals("nearby tickets:")){
                     line = scanner.nextLine();
                }
                while (scanner.hasNext()){
                    line = scanner.nextLine();
                    Ticket ticket = Ticket.fromLine(line);
                    program.nearbyTickets.add(ticket);

                }

            }
            return program;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class Program {

        private Ticket myTicket;
        private Set<Ticket> nearbyTickets = new HashSet<>();
        private Set<Rule> rules = new HashSet<>();


    }
    private static class Rule {
        private String label;
        private Range[] ranges = new Range[2];

        public Rule(String label, Range[] ranges) {
            this.label = label;
            this.ranges = ranges;
        }

        public static Rule fromLine(String line) {
            String[] pieces = line.split(":");
            String label = pieces[0];
            String[] ranks = pieces[1].split(" or ");
            Range range1 = Range.fromLine(ranks[0]);
            Range range2 = Range.fromLine(ranks[1]);
            return new Rule(label, new Range[]{range1,range2});

        }
    }
    private static class Ticket {
        private List<Integer> values = new ArrayList<>();

        public  Ticket(List<Integer> collect) {
            this.values = collect;
        }

        public static Ticket fromLine(String line) {
            return new Ticket(Arrays.stream(line.split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        }
    }

    public static class Range {
        private int lowerBound;
        private int upperBound;

        public Range(Integer valueOf, Integer valueOf1) {
            this.lowerBound = valueOf;
            this.upperBound = valueOf1;
        }

        public static Range fromLine(String rank) {
            String[] bounds = rank.split("-");
            return new Range (Integer.valueOf(bounds[0].trim()),Integer.valueOf(bounds[1].trim()));
        }
    }

}
