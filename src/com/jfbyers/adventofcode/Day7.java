package com.jfbyers.adventofcode;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {

    private static final Map<String, Rule> bagRules = getBagRules();
    private static Set<String> counter = new HashSet<>();

    private static Map<String, Rule> getBagRules() {
        final File file = new File("inputDay7.txt");
        Map<String, Rule> rules = new HashMap<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                Rule r = Rule.fromString(line);
                rules.put(r.bag, r);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rules;
    }

    public static void main(String[] args) {
        List<Rule> values = getvalues(new ArrayList<>(bagRules.values()), "shiny gold");
        recurse(values);
        System.out.println("Value 1" + counter.size());
        Rule startRule = bagRules.get("shiny gold");
        System.out.println("Value 2 " + (getBagNumber(startRule) - 1));
    }

    private static int getBagNumber(Rule startRule) {
        final Map<String, Integer> map = startRule.containment;
        int bags = 1;
        for (String color : map.keySet()) {
            int numberOfBags = map.get(color);
            Rule bagRule = bagRules.get(color);
            bags += (numberOfBags * getBagNumber(bagRule));
        }
        return bags;
    }

    private static void recurse(List<Rule> values) {
        for (Rule rule : values) {
            List<Rule> newValues = getvalues(bagRules.values(), rule.bag);
            recurse(newValues);
        }
    }

    private static List<Rule> getvalues(final Collection<Rule> values, final String color) {
        final List<Rule> counter = new ArrayList<>();

        for (Rule r : values) {
            boolean hasColor = getValue(color, r);
            if (hasColor) {
                counter.add(r);
            }
        }
        return counter;
    }

    private static boolean getValue(String color, Rule r) {
        final Map<String, Integer> containment = r.containment;
        
        if (containment.get(color) != null) {
            counter.add(r.bag);
            return true;
        }
        return false;
    }

    private static class Rule {

        private final String bag;
        private final Map<String, Integer> containment;

        private Rule(String bag, Map<String, Integer> containment) {
            this.bag = bag;
            this.containment = containment;
        }

        public static Rule fromString(String line) {
            String[] colorAndContainements = line.split("contain");
            final String color = colorAndContainements[0].replace("bags", "").trim();
            String[] containments = colorAndContainements[1].split(",");
            final Map<String, Integer> colorBags = new HashMap<>();
            for (String containment : containments) {
                final String numberAndBagColor = containment.replaceAll("bag(s)?(.)?", "").trim();
                if (numberAndBagColor.equals("no other")) {
                    continue;
                }
                String pattern = "(\\d+) (\\w+ \\w+)";
                Matcher m = Pattern.compile(pattern).matcher(numberAndBagColor);
                m.find();
                int colorValue = Integer.valueOf(m.group(1));
                String bagKey = m.group(2);
                colorBags.put(bagKey, colorValue);
            }
            return new Rule(color, colorBags);
        }
    }
}
