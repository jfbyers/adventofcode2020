package com.jfbyers.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day5 {


    private final static List<BoardingPass> passes = getBoardingPasses();

    public static void main(String[] args) {
        List<Integer> seatIds = getSeatIds();
        int max = Collections.max(seatIds);
        System.out.println("Maximum id is : "+max);
        int mySeatId = getMySeatId(seatIds);
        System.out.println("My seat  id is : "+mySeatId);
    }

    private static int getMySeatId(final List<Integer> seatIds) {
        final List<Integer> copySeatIds = new ArrayList<>(seatIds);
        Collections.sort(copySeatIds);

        int previousId = copySeatIds.get(0) - 1 ;
        int size = copySeatIds.size();
        for (int i = 0; i< size; i++){
            int guessedNewId = previousId + 1;
            int actualId = copySeatIds.get(i);
            if (actualId != guessedNewId) {
               return guessedNewId;
            }
            previousId = actualId;
        }
        return -1;
    }

    private static List<Integer> getSeatIds() {
        List<Integer> seatIds = new ArrayList<>();
        for (BoardingPass p : passes){
            int seatId = p.row * 8 + p.seat;
            seatIds.add(seatId);
        }
        return seatIds;
    }


    private static List<BoardingPass> getBoardingPasses() {
        final File file = new File("inputDay5.txt");
        List<BoardingPass> boardingPasses = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                BoardingPass pass = BoardingPass.fromString(line);
                boardingPasses.add(pass);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardingPasses;
    }

    private static class BoardingPass {

        private final int row;
        private final int seat;

        public BoardingPass(int row, int seat) {
            this.row = row;
            this.seat = seat;
        }

        public static BoardingPass fromString(String line) {
            String binaryLine = line.substring(0,7);
            String binaryString = binaryLine.replaceAll("F","0").replaceAll("B","1");
            int row = Integer.parseInt(binaryString,2);
            String binaryEnd= line.substring(7);
            String binarySeatString = binaryEnd.replaceAll("L","0").replaceAll("R","1");
            int seat = Integer.parseInt(binarySeatString,2);
            return new BoardingPass(row,seat);
        }
    }
}
