package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

    private final static BaseMatrix matrix = getBaseMatrix();

    public static void main(String[] args) {

        long treesP1 = getTrees(matrix, 0, 0,new Policy(1,1));
        long treesP2 = getTrees(matrix, 0, 0,new Policy(1,3));
        long treesP3 = getTrees(matrix, 0, 0,new Policy(1,5));
        long treesP4 = getTrees(matrix, 0, 0,new Policy(1,7));
        long treesP5 = getTrees(matrix, 0, 0,new Policy(2,1));

        System.out.println("Trees: " + treesP1 * treesP2 * treesP3 * treesP4 * treesP5);
    }

    private static int getTrees(BaseMatrix matrix, int rowPosition, int columnPosition, Policy policy) {

        final List<BaseMatrix.Row> rows = matrix.rows;
        if (rowPosition >= rows.size()) return 0;

        int trees = isTree(matrix, rowPosition, columnPosition, policy) ? 1 : 0;
        int lengthColumn = getLengthColumn(rowPosition, rows);
        final BaseMatrix nextMatrix = getNextMatrix(columnPosition,lengthColumn,matrix,policy);
        int nextRow = rowPosition + policy.slopeDownShift;
        int nextColumn = columnPosition + policy.slopeRightShift;
        trees += getTrees(nextMatrix, nextRow, nextColumn, policy);

        return trees;
    }

    private static boolean isTree(BaseMatrix m, int rowPosition, int columnPosition, Policy policy) {
        final Character valueToCheck = getCharacterToCheck(m, rowPosition, columnPosition, policy);
        if (valueToCheck == '#') {
            return true;
        }
        return false;
    }

    private static BaseMatrix getNextMatrix(int columnPosition, int lengthColumn, BaseMatrix m, Policy policy) {
        if (columnPosition + policy.slopeRightShift >= lengthColumn){
            return m.clone();
        }else{
            return m;
        }

    }

    private static int getLengthColumn(int i, List<BaseMatrix.Row> rows) {
        final BaseMatrix.Row r = rows.get(i);
        final List<Character> valuesRowI = r.items;
        return valuesRowI.size();
    }

    private static Character getCharacterToCheck(BaseMatrix m, int i, int k, Policy policy ) {
        final List<Character> valuesRowI = m.rows.get(i).items;
        int lengthColumn = valuesRowI.size();
        if (k + policy.slopeRightShift >= lengthColumn){
            return m.clone().rows.get(i).items.get(k);
        }else{
            return valuesRowI.get(k);
        }
    }


    private static BaseMatrix getBaseMatrix() {
        final File file = new File("inputDay3.txt");
        List<BaseMatrix.Row> rows = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                List<Character> rowValues = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                BaseMatrix.Row row = new BaseMatrix.Row(rowValues);
                rows.add(row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BaseMatrix(rows);
    }


    private static class BaseMatrix {

        private final List<Row> rows;

        private BaseMatrix(List<Row> rows) {
            this.rows = rows;
        }

        public BaseMatrix clone() {
            List<Row> doubledRows = new ArrayList<>();
            for (Row row : this.rows) {
                List<Character> items = new ArrayList<>(row.items);
                items.addAll(row.items);
                Row clonedRow = new Row(items);
                doubledRows.add(clonedRow);
            }
            return new BaseMatrix(doubledRows);
        }


        private static class Row {
            private final List<Character> items;

            private Row(List<Character> items) {
                this.items = items;
            }
        }

    }

    private static class Policy {
        private final int slopeRightShift;
        private final int slopeDownShift;

        public Policy(int slopeDownShift, int slopeRightShift) {
            this.slopeDownShift = slopeDownShift;
            this.slopeRightShift = slopeRightShift;
        }
    }
}
