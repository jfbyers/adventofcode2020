package com.jfbyers.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) {
        Program p = getProgram();

        Set<Ticket> validTickets = part1(p);
        long counter = part2(p, validTickets);
        System.out.println("Part2: " + counter);
    }

    private static Set<Ticket> part1(Program p) {
        int invalidFields = 0;
        Set<Ticket> validTickets = new HashSet<>();

        for (Ticket t : p.nearbyTickets) {
            int invalidFieldsValue = checkTicket(t, p.rules);
            if (invalidFieldsValue == 0) {
                validTickets.add(t);
            }
            invalidFields += invalidFieldsValue;
        }
        System.out.println("Part1:  " + invalidFields);
        return validTickets;
    }

    private static long part2(Program p, Set<Ticket> validTickets) {
        long counter = 1L;
        Map<Integer, Set<Rule>> positionAndInvalidRules = getPositionAndInvalidRules(p, validTickets);
        Map<Integer, Set<Rule>> positionAndValidRules = reverseMap(positionAndInvalidRules, p.rules);

        Map<Integer, Rule> positionOfeveryRule = new HashMap<>();

        RuleAndPosition r = getPositionOfSingleRule(positionAndValidRules);
        while (r != null) {
            positionOfeveryRule.put(r.key, r.rule);
            removeRule(positionAndValidRules, r);
            r = getPositionOfSingleRule(positionAndValidRules);
        }

        for (Integer value : p.myTicket.values) {
            Rule rule = positionOfeveryRule.get(p.myTicket.values.indexOf(value));

            if (rule != null && rule.label.startsWith("departure")) {
                counter *= value;
            }
        }
        return counter;
    }

    private static Map<Integer, Set<Rule>> getPositionAndInvalidRules(Program p, Set<Ticket> validTickets) {
        Map<Integer, Set<Rule>> positionAndInvalidRules = new HashMap<>();
        for (Ticket t : validTickets) {
            for (Integer value : t.values) {
                Set<Rule> inValidRules = getInValidRules(value, p.rules);
                if (inValidRules.size() != 0) {
                    int position = t.values.indexOf(value);
                    Set<Rule> invalidRuleSet = positionAndInvalidRules.get(position);
                    if (invalidRuleSet == null) {
                        invalidRuleSet = new HashSet<>();
                    }
                    invalidRuleSet.addAll(inValidRules);
                    positionAndInvalidRules.put(position, invalidRuleSet);

                }
            }
        }
        return positionAndInvalidRules;
    }

    private static void removeRule(Map<Integer, Set<Rule>> positionAndValidRules, RuleAndPosition r) {
        for (Map.Entry<Integer, Set<Rule>> entry : positionAndValidRules.entrySet()) {
            Set<Rule> validRules = entry.getValue();
            if (entry.getKey() != r.key) {
                validRules.remove(r.rule);
            }
        }
    }

    private static RuleAndPosition getPositionOfSingleRule(Map<Integer, Set<Rule>> positionAndValidRules) {
        for (Map.Entry<Integer, Set<Rule>> entry : positionAndValidRules.entrySet()) {
            Set<Rule> validRules = entry.getValue();
            if (validRules.size() == 1) {
                positionAndValidRules.remove(entry.getKey());
                return new RuleAndPosition(validRules.iterator().next(), entry.getKey());
            }
        }
        return null;
    }

    private static Map<Integer, Set<Rule>> reverseMap(Map<Integer, Set<Rule>> positionAndInvalidRules, Set<Rule> rules) {
        Map<Integer, Set<Rule>> reversedMap = new HashMap<>();
        for (Map.Entry<Integer, Set<Rule>> entry : positionAndInvalidRules.entrySet()) {
            Set<Rule> validRules = new HashSet<>(rules);
            Set<Rule> invalidRules = entry.getValue();
            validRules.removeAll(invalidRules);
            reversedMap.put(entry.getKey(), validRules);
        }
        return reversedMap;
    }

    private static int checkTicket(Ticket t, Set<Rule> rules) {
        boolean valid;
        int counter = 0;
        for (Integer value : t.values) {
            valid = anyRuleIsValid(value, rules);
            if (!valid) {
                counter += value;
            }
        }
        return counter;
    }

    private static boolean anyRuleIsValid(Integer value, Set<Rule> rules) {
        return !getValidRules(value, rules).isEmpty();
    }


    private static Set<Rule> getValidRules(Integer value, Set<Rule> rules) {
        Set<Rule> validRules = new HashSet<>();
        for (Rule r : rules) {
            boolean isValid = isValid(r, value);
            if (isValid) {
                validRules.add(r);
            }
        }
        return validRules;
    }

    private static Set<Rule> getInValidRules(Integer value, Set<Rule> rules) {
        Set<Rule> inValidRules = new HashSet<>();
        for (Rule r : rules) {
            boolean isValid = isValid(r, value);
            if (!isValid) {
                inValidRules.add(r);
            }
        }
        return inValidRules;
    }

    private static boolean isValid(Rule r, int value) {
        Range range1 = r.ranges[0];
        Range range2 = r.ranges[1];
        boolean isValid = (range1.lowerBound <= value && value <= range1.upperBound)
                || (range2.lowerBound <= value && value <= range2.upperBound);
        return isValid;
    }

    private static Program getProgram() {
        Program program = new Program();

        try {
            Scanner scanner = new Scanner(new File("inputDay16.txt"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (!line.trim().isEmpty()) {
                    Rule rule = Rule.fromLine(line);
                    program.rules.add(rule);
                    line = scanner.nextLine();
                }
                while (!line.equals("your ticket:")) {
                    line = scanner.nextLine();
                }
                line = scanner.nextLine();
                Ticket myTicket = Ticket.fromLine(line);
                program.myTicket = myTicket;

                while (!line.equals("nearby tickets:")) {
                    line = scanner.nextLine();
                }
                while (scanner.hasNext()) {
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
            return new Rule(label, new Range[]{range1, range2});

        }
    }

    private static class Ticket {
        private List<Integer> values;

        public Ticket(List<Integer> collect) {
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
            return new Range(Integer.valueOf(bounds[0].trim()), Integer.valueOf(bounds[1].trim()));
        }
    }

    private static class RuleAndPosition {
        private final Integer key;
        private final Rule rule;

        public RuleAndPosition(Rule next, Integer key) {
            this.rule = next;
            this.key = key;
        }
    }
}
