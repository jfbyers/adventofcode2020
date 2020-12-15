package com.jfbyers.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day11b {


    private static int numberOfLines=0;
    private static int numberOfCols=0;

    public static void main(String[] args) {

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
            System.out.println(occupiedSeats);
        }
        System.out.println("Occupied Seats "+occupiedSeats);

    }

    private static  List<Point> round(List<Point> round) {
        List<Point> newPoints = new ArrayList<>();
        for (Point p : round){
            AreaType newType = getState(p,round);
            Point newPoint = new Point(newType,p.coordinates);
            newPoints.add(newPoint);
        }

        return newPoints;
    }

    private static int countOccupied(List<Point> newPoints) {
        int occupiedSeats = 0;
        for (Point p : newPoints){
           if (isOccupied(p, AreaType.OCCUPIED)) occupiedSeats ++;
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
                    numberOfCols = chars.length;
                    Character character = chars[col];
                    Point p = Point.fromChar(character, lineCounter.intValue(), col);
                    seatMap.add(p);
                }
                lineCounter.incrementAndGet();
            });
            numberOfLines = lineCounter.intValue();

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
                if (numberOdAdjacetSeatsOccupied >= 5) return AreaType.EMPTY;
                break;
            default:
                return type;
        }
        return type;
    }

    private static int getAdjacentSeatsOccupied(Point p, List<Point> round) {
        int acum = 0;
        acum += getPointsAtDiagonal(p,round);

        List<Point> columnPoints = getPointsAtVertical(p,round);
        acum += getAdjacentLine(p,columnPoints);

        List<Point> linePoints =  getPointsAtHorizontal(p,round);
        acum += getAdjacentLine(p, linePoints);
        return acum;
    }

    private static List<Point> getPointsAtHorizontal(Point point, List<Point> round) {
        final List<Point> points = new ArrayList<>();
        for (Point p : round ){
            if ( p.coordinates[0] == point.coordinates[0]){
                points.add(p);
            }
        }
        return points;
    }
    private static List<Point> getPointsAtVertical(Point point, List<Point> round) {
        final List<Point> points = new ArrayList<>();
        for (Point p : round ){
            if ( p.coordinates[1] == point.coordinates[1]){
                points.add(p);
            }
        }
        return points;
    }
    private static int getPointsAtDiagonal(Point point, List<Point> round) {
        int acum = 0;
        int line = point.coordinates[0];
        int col =  point.coordinates[1];


        for (int i=line-1, k= col-1; i>=0 && k>=0;i--,k--){
            Point p = getPoint(i,k,round);
            if (p.type == AreaType.EMPTY) break;
            if (isOccupied(p, AreaType.OCCUPIED)) {
                acum++;
                break;
            }

        }
        for (int i=line-1, k= col+1; i>=0 && k<numberOfCols;i--,k++){
            Point p = getPoint(i,k,round);
            if (p.type == AreaType.EMPTY) break;
            if (isOccupied(p, AreaType.OCCUPIED)) {
                acum++;
                break;
            }

        }

        for (int i=line+1, k= col-1; i<numberOfLines && k>=0;i++,k--){
            Point p = getPoint(i,k,round);
            if (p.type == AreaType.EMPTY) break;
            if (isOccupied(p, AreaType.OCCUPIED)) {
                acum++;
                break;
            }
        }

        for (int i=line+1, k= col+1; i<numberOfLines && k<numberOfCols;i++,k++){
            Point p = getPoint(i,k,round);
            if (p.type == AreaType.EMPTY) break;
            if (isOccupied(p, AreaType.OCCUPIED)) {
                acum++;
                break;
            }
        }

        return acum;
    }

    private static Point getPoint(int line, int col, List<Point> round) {
        for (Point p : round ){
            if (p.coordinates[0] == line && p.coordinates[1]==col){
                return p;
            }
        }
        return null;
    }

    private static int getAdjacentLine(Point p,  List<Point> points) {
        int index = points.indexOf(p);
        int previous = index - 1;
        int acum = 0;
        while (previous>=0) {
            Point left = points.get(previous);
            if (left.type == AreaType.EMPTY) break;
            if (isOccupied(left, AreaType.OCCUPIED)) {
                acum++;
                break;
            }
            previous--;
        }

        int next = index + 1;
        while (next < points.size()) {
            Point right = points.get(next);
            if (right.type == AreaType.EMPTY) break;
            if (isOccupied(right, AreaType.OCCUPIED)) {
                acum++;
                break;
            }
            next++;
        }
        return acum;
    }

    private static boolean isOccupied(Point right, AreaType occupied) {
        return right.type == occupied;
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
            if (isOccupied(Point.this, AreaType.OCCUPIED)) return "#";
            if (isOccupied(Point.this, AreaType.FLOOR)) return ".";
            if (isOccupied(Point.this, AreaType.EMPTY)) return "L";
            return " ";

        }
    }

}
