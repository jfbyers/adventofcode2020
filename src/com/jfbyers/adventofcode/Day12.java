package com.jfbyers.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {


    public static void main(String[] args) {
        List<Instruction> instructions = getNavigationInstructions();
        Ship ship = new Ship(Direction.EAST, new Grid(Direction.EAST, 0));
        for (Instruction i : instructions) {
            ship.applyInstruction(i);
        }
        int manhattanDistance = getDistance(ship.grid.position);
        System.out.println("ManhattanDistance "+manhattanDistance);
    }

    private static int getDistance(Map<Direction, Integer> position) {
        int north = position.get(Direction.NORTH);
        int south = position.get(Direction.SOUTH);
        int east = position.get(Direction.EAST);
        int west = position.get(Direction.WEST);

        return Math.abs(north - south) + Math.abs(east -west);

    }

    private static List<Instruction> getNavigationInstructions() {
        try (Stream<String> stream = Files.lines(Paths.get("inputDay12.txt"))) {
            return stream.map(line -> Instruction.fromString(line)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Instruction {
        private final int value;
        private final InstructionType type;
        public final Direction direction;

        public Instruction(Character action, int degreeOrValue) {
            this.type = getType(action);
            this.direction = getDirection(action);
            this.value = degreeOrValue;
        }

        private InstructionType getType(Character action) {
            switch (action) {
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    return InstructionType.MOVE;
                case 'L':
                case 'R':
                    return InstructionType.TURN;
                default:   // F
                    return InstructionType.FORWARD;
            }
        }

        private Direction getDirection(Character action) {
            switch (action) {
                case 'N':
                    return Direction.NORTH;
                case 'S':
                    return Direction.SOUTH;
                case 'E':
                    return Direction.EAST;
                case 'W':
                    return Direction.WEST;
                case 'L':
                    return Direction.LEFT;
                case 'R':
                    return Direction.RIGHT;
                default:
                    return null;
            }
        }

        public static Instruction fromString(String line) {
            Pattern pattern = Pattern.compile("(\\D)(\\d+)");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            Character action = matcher.group(1).charAt(0);
            int degreeOrValue = Integer.valueOf(matcher.group(2));

            return new Instruction(action, degreeOrValue);
        }

        private enum InstructionType {
            TURN, FORWARD, MOVE;
        }
    }

    private static class Ship {
        private Direction currentDirection;
        private Grid grid;


        public Ship(Direction direction, Grid grid) {
            this.currentDirection = direction;
            this.grid = grid;
        }

        public void applyInstruction(Instruction i) {
            int units = i.value;
            switch (i.type) {
                case FORWARD:
                    grid.add(this.currentDirection, units);
                    break;
                case MOVE:
                    grid.add(i.direction, units);
                    break;
                case TURN:
                    this.currentDirection = getNewDirection(i, units);


            }
        }

        private Direction getNewDirection(Instruction i, int units) {
            int numberOfRotations = units / 90;
            if (i.direction == Direction.LEFT ){
                numberOfRotations = 4 - numberOfRotations;
            }
            int rotate = (this.currentDirection.pos + numberOfRotations) % 4;

            return Direction.fromValue(rotate);
        }
    }

    private enum Direction {
        NORTH(0), EAST(1), SOUTH(2), WEST(3), RIGHT(-1), LEFT(-1);


        private final int pos;

        Direction(int pos) {
            this.pos = pos;
        }

        public static Direction fromValue(int dirValue) {
            for (Direction d : Direction.values()) {
                if (d.pos == dirValue) return d;
            }
            return null;
        }
    }

    private static class Grid {
        private static Map<Direction, Integer> position = new HashMap<>();

        static {
            position.put(Direction.NORTH, 0);
            position.put(Direction.EAST, 0);
            position.put(Direction.SOUTH, 0);
            position.put(Direction.WEST, 0);
        }

        public Grid(Direction d, Integer value) {
            position.put(d, value);
        }

        public void add(Direction direction, int units) {
            int values = position.get(direction);
            position.put(direction, values + units);
        }
    }
}
