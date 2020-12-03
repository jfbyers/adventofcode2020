package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Day2 {


    public static void main(String[] args) throws FileNotFoundException {

        final Password[] pwds = getPasswords();
        int numberOfValids = checkValid(pwds, Day2::policy1);
        System.out.println("Number of valid passwords Policy 1: " + numberOfValids);
        int numberOfValidNewPolicy = checkValid(pwds, Day2::policy2);
        System.out.println("Number of valid passwords Policy 2: " + numberOfValidNewPolicy);

    }


    private static int checkValid(Password[] pwds, PolicyChecker checker) {
        int counter = 0;
        for (Password p : pwds) {
            counter += checker.checkPolicy(p) ? 1 : 0;
        }
        return counter;
    }

    private static boolean policy2(Password p) {
        String password = p.data;
        Password.Policy pol = p.policy;
        char charToCheck = pol.character;
        char char1 = password.charAt(pol.lowerBound - 1);
        char char2 = password.charAt(pol.upperBound - 1);
        if (char1 == charToCheck && char2 != char1) {
            return true;
        } else if (char2 == charToCheck && char2 != char1) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean policy1(Password p) {
        String password = p.data;
        Password.Policy pol = p.policy;
        char charToCheck = pol.character;
        long numberOfChars = password.codePoints().filter(ch -> ch == charToCheck).count();
        if (numberOfChars >= pol.lowerBound && numberOfChars <= pol.upperBound) {
            return true;
        }
        return false;
    }


    private static Password[] getPasswords() {
        final File file = new File("inputDay2.txt");
        final List<Password> tall = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                Password p = Password.fromString(line);
                tall.add(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tall.toArray(new Password[]{});

    }

    @FunctionalInterface
    interface PolicyChecker {
        boolean checkPolicy(Password p);
    }

    private static class Password {
        private final Policy policy;
        private final String data;

        private Password(String data, Policy policy) {
            this.data = data.trim();
            this.policy = policy;
        }

        @Override
        public String toString() {
            return "Password{" +
                    "policy=" + policy +
                    ", data='" + data + '\'' +
                    '}';
        }

        public static Password fromString(String entry) {
            String[] policyAndCode = entry.split(":");
            Policy policy = Policy.fromString(policyAndCode[0]);
            return new Password(policyAndCode[1], policy);

        }
        private static class Policy {
            private final int lowerBound;
            private final int upperBound;
            private final char character;

            @Override
            public String toString() {
                return "Policy{" +
                        "lowerBound=" + lowerBound +
                        ", upperBound=" + upperBound +
                        ", character=" + character +
                        '}';
            }

            private Policy(int lowerBound, int upperBound, char character) {
                this.lowerBound = lowerBound;
                this.upperBound = upperBound;
                this.character = character;
            }

            public static Policy fromString(String particle) {
                String[] boundsAndChar = particle.split(" ");
                String bounds = boundsAndChar[0];
                String[] limits = bounds.split("-");
                int lowerBound = Integer.valueOf(limits[0]);
                int upperBound = Integer.valueOf(limits[1]);
                char character = boundsAndChar[1].charAt(0);
                return new Policy(lowerBound, upperBound, character);
            }
        }
    }
}
