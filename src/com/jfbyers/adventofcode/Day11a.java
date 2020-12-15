package com.jfbyers.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day11a{


    public static void main(String[] args) {
       /* List<Point> points = seatMap.values().stream()
                .flatMap(Collection::stream)
                        .collect(Collectors.toList());
*/
        List<Point> seatMap = getSeatMap();
        int occupiedSeats = 0;
        boolean stop = false;
        List<Point> round =seatMap;
        Set<Integer> previousOccupiedSeat =new HashSet<>();
        while(!stop){
            round = round(round);
            occupiedSeats = countOccupied(round);

            if (previousOccupiedSeat.contains(occupiedSeats)){
                break;
            }
            previousOccupiedSeat.add(occupiedSeats);
            System.out.println(""+occupiedSeats);
        }
        System.out.println("Occupied Seats "+occupiedSeats);

    }

    private static  List<Point> round(List<Point> round) {
        List<Point> newPoints = new ArrayList<>();
        int counter =0;
        for (Point p : round){
            AreaType newType = getState(p,round);

            Point newPoint = new Point(newType,p.coordinates); //p.type = newType;
            newPoints.add(newPoint);

           /* System.out.print(newPoint.toString());
            counter ++;
            if(counter % 10 == 0){
                counter = 0;
                System.out.println("");
            } */
        }

        return newPoints;
    }

    private static int countOccupied(List<Point> newPoints) {
        int occupiedSeats = 0;
        for (Point p : newPoints){
            if (p.type == AreaType.OCCUPIED) occupiedSeats ++;
        }
        return occupiedSeats;
    }

    private static List<Point> getSeatMap() {
        List<Point> seatMap = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("inputDay11.txt"))) {
            AtomicInteger lineCounter = new AtomicInteger(0);
            stream.forEach(line -> {
                char[] chars = line.toCharArray();
                for (int col = 0; col < chars.length; col++) {
                    Character character = chars[col];
                    Point p = Point.fromChar(character, lineCounter.intValue(), col);
                    seatMap.add(p);
                }
                lineCounter.incrementAndGet();
            });
            return seatMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static AreaType getState(Point p, List<Point> round) {
        AreaType type = p.type;
        int numberOdAdjacetSeatsOccupied = getAdjacentSeatsOccupied(p,round);
        switch (type) {
            case EMPTY:
                if (numberOdAdjacetSeatsOccupied == 0) return AreaType.OCCUPIED;
                break;
            case OCCUPIED:
                if (numberOdAdjacetSeatsOccupied >= 4) return AreaType.EMPTY;
                break;
            default:
                return type;
        }
        return type;
    }

    private static int getAdjacentSeatsOccupied(Point p, List<Point> round) {
        int[] coordinates = p.coordinates;
        int line = coordinates[0];
        int col = coordinates[1];
        int acum = 0;
        int previousLine = line - 1;
        int nextLine = line + 1;
        if (previousLine >= 0) {
            List<Point> points = getPointsAt(previousLine,round);  //seatMap.get(previousLine);
            acum += getAdjacentLine(col, points, false);
        }


        List<Point> nextLinePoints =getPointsAt(nextLine,round);  // seatMap.get(nextLine);
        if (nextLinePoints.size()>0) {
            acum += getAdjacentLine(col, nextLinePoints, false);
        }

        List<Point> points =  getPointsAt(line,round); //seatMap.get(line);
        acum += getAdjacentLine(col, points,true);
        return acum;
    }

    private static List<Point> getPointsAt(int line, List<Point> round) {
        final List<Point> points = new ArrayList<>();
        for (Point p : round ){
            if (p.coordinates[0] == line){
                points.add(p);
            }
        }
        return points;
    }

    private static int getAdjacentLine(int col, List<Point> points, boolean ownLine) {
        int acum = 0;
        int previousCol = col - 1;
        if (previousCol >= 0) {
            Point left = points.get(previousCol);
            if (left.type == AreaType.OCCUPIED) acum++;
        }
        int nextCol = col + 1;
        if (nextCol < points.size()) {
            Point right = points.get(nextCol);
            if (right.type == AreaType.OCCUPIED) acum++;
        }
        Point center = points.get(col);
        if (center.type == AreaType.OCCUPIED && !ownLine) acum++;

        return acum;
    }

    private enum AreaType {
        OCCUPIED, EMPTY, FLOOR;
    }

    private static class Point {
        private AreaType type;
        private final int[] coordinates;

        private Point(AreaType type, int[] coordinates) {
            this.type = type;
            this.coordinates = coordinates;
        }

        public static Point fromChar(Character character, int line, int col) {
            switch (character) {
                case 'L':
                    return new Point(AreaType.EMPTY, new int[]{line, col});
                case '#':
                    return new Point(AreaType.OCCUPIED, new int[]{line, col});
                case '.':
                    return new Point(AreaType.FLOOR, new int[]{line, col});

                default:
                    throw new AssertionError("Cannot happen");
            }
        }

        public String toString() {
            if (type==AreaType.OCCUPIED) return "#";
            if (type==AreaType.FLOOR) return ".";
            if (type==AreaType.EMPTY) return "L";
            return " ";

        }
    }


    private static class SeatMap {
    }
}
