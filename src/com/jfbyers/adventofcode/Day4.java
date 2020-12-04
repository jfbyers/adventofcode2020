package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day4 {

    private final static List<Passport> passports = getPassports();

    public static void main(String[] args) {
        int countLoose = 0, countStrict = 0;
        PassportValidator validator = new LooseValidator();
        PassportValidator validatorStrict = new StrictValidator();
        for (Passport p : passports) {
            if (validator.isValid(p)) countLoose++;
            if (validatorStrict.isValid(p)) countStrict++;
        }
        System.out.print("passports valid loosely: " + countLoose);
        System.out.print("passports valid strictly: " + countStrict);
    }

    private static List<Passport> getPassports() {
        final File file = new File("inputDay4.txt");
        List<Day4.Passport> passports = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line == null || line.isEmpty()) {
                    Passport p = Passport.fromString(sb.toString().trim());
                    sb = new StringBuilder();
                    passports.add(p);
                } else {
                    sb.append(line);
                }
                sb.append(" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passports;
    }

    private static class Passport {

        private String byr;
        private String iyr;
        private String eyr;
        private String hgt;
        private String hcl;
        private String ecl;
        private String pid;
        private String cid;

        private Passport() {
        }

        public static Passport fromString(String stringValues) {
            String[] pairValues = stringValues.split(" ");
            Passport p = new Passport();
            for (String pairValue : pairValues) {
                String[] keyValue = pairValue.split(":");
                String key = keyValue[0];
                String value = keyValue[1];
                populateField(p, key, value);
            }
            return p;
        }

        private static void populateField(Passport p, String key, String value) {

            try {
                p.getClass().getDeclaredField(key).set(p,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    interface PassportValidator {

        boolean isValid(Passport p);

    }

    private static class LooseValidator implements PassportValidator {

        @Override
        public boolean isValid(Passport p) {
            return (p.byr != null && p.iyr != null && p.eyr != null && p.hgt != null && p.hcl != null && p.ecl != null
                    && p.pid != null);
        }
    }

    private static class StrictValidator implements PassportValidator {

        @Override
        public boolean isValid(Passport p) {
            boolean validByr = checkByr(p.byr);
            boolean validIyr = checkIyr(p.iyr);
            boolean validEyr = checkEyr(p.eyr);
            boolean validHgt = checkHgt(p.hgt);
            boolean validHcl = checkHcl(p.hcl);
            boolean validEcl = checkEcl(p.ecl);
            boolean validPid = checkPid(p.pid);

            return validByr && validIyr && validEyr && validHcl && validHcl && validHgt && validEcl && validPid;
        }

        private boolean checkPid(String pid) {
            if (pid == null) return false;
            Pattern pattern = Pattern.compile("\\d{9}");
            return pattern.matcher(pid).matches();
        }

        private boolean checkEcl(String ecl) {
            if (ecl == null) return false;
            switch (ecl) {
                case "amb":
                case "blu":
                case "brn":
                case "gry":
                case "grn":
                case "hzl":
                case "oth":
                    return true;
                default:
                    return false;
            }
        }

        private boolean checkHcl(String hcl) {
            if (hcl == null) return false;
            Pattern pattern = Pattern.compile("#([0-9]|[a-f]){6}");
            return pattern.matcher(hcl).matches();
        }

        private boolean checkHgt(String hgt) {
            if (hgt == null) return false;
            String unit = hgt.substring(hgt.length() - 2);
            try {
                int number = Integer.parseInt(hgt.substring(0, hgt.length() - 2));

                switch (unit) {
                    case "cm":
                        return number >= 150 && number <= 193;
                    case "in":
                        return number >= 59 && number <= 76;
                    default:
                        return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        private boolean checkEyr(String eyr) {
            if (eyr == null) return false;
            int year = Integer.parseInt(eyr);
            return year >= 2020 && year <= 2030;
        }

        private boolean checkIyr(String iyr) {
            if (iyr == null) return false;
            int year = Integer.parseInt(iyr);
            return year >= 2010 && year <= 2020;
        }

        private boolean checkByr(String byr) {
            if (byr == null) return false;
            int year = Integer.parseInt(byr);
            return year >= 1920 && year <= 2002;
        }
    }
}
