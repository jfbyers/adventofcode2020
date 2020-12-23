package com.jfbyers.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17 {

    public static void main(String[] args) {

        Map<Coordinates, Cube> initialCubes = getCubeMap();
        Map<Coordinates, Cube> finalMap = new HashMap<>();
        for (Coordinates c : initialCubes.keySet()) {
            Map<Coordinates, Cube> neighbours = getNeighbours(c, initialCubes);
            Cube cube = initialCubes.get(c);
            finalMap.put(c, cube);
            finalMap.putAll(neighbours);
        }

        int cycle = 0;
        long activeCubes2 = finalMap.values().stream()
                .filter(cube -> cube.state.equals(CubeState.ACTIVE)).count();
        System.out.println("Cube actives before cycle : " + activeCubes2);
        Map<Coordinates, Cube> cycleCubes = finalMap;
        while (cycle <= 5) {
            System.out.println("cycle " + cycle);
            cycleCubes = cycle(cycleCubes);

            cycle++;

        }
        long activeCubes = cycleCubes.values().stream()
                .filter(cube -> cube.state.equals(CubeState.ACTIVE)).count();
        System.out.println("Cube actives: " + activeCubes);
    }

    private static void printActiveNeighbours(Map<Coordinates, Cube> neighbours, Cube cube) {
        System.out.println("active neighbours for active Cube  " + cube);
        if (cube.state == CubeState.ACTIVE) {
            for (Cube n : neighbours.values()) {
                if (n.state == CubeState.ACTIVE)
                    System.out.println(n);
            }
        }
    }


    private static Map<Coordinates, Cube> cycle(Map<Coordinates, Cube> cubes) {
        Map<Coordinates, Cube> finalMap = new HashMap<>();

        for (Coordinates c : cubes.keySet()) {
            Map<Coordinates, Cube> neighbours = getNeighbours(c, cubes);
            Stream<Cube> cubeStream = neighbours.values().stream()
                    .filter(cube -> cube.state.equals(CubeState.ACTIVE));

            List<Cube> activeNeighbourSet = cubeStream.collect(Collectors.toList());
            long activeNeighbours = activeNeighbourSet.size();
            Cube cube = cubes.get(c);
            //printActiveNeighbours(neighbours, cube);
            switch (cube.state) {
                case ACTIVE:
                    if (activeNeighbours == 2 || activeNeighbours == 3) {

                        finalMap.put(c, cube);
                    } else {
                        Cube newCube = new Cube('.', c);
                        finalMap.put(c, newCube);
                    }
                    break;
                case INACTIVE:
                    if (activeNeighbours == 3) {
                        Cube newCube = new Cube('#', c);
                        finalMap.put(c, newCube);
                    } else {
                        finalMap.put(c, cube);
                    }
                    break;
            }
            //activeNeighbourList.addAll(activeNeighbourSet);
            //finalMap.putAll(neighbours);

        }

        List<Cube> activesAfterCycle = finalMap.values().stream()
                .filter(cube -> cube.state.equals(CubeState.ACTIVE)).collect(Collectors.toList());

        for (Cube c : activesAfterCycle) {
            if (c.state == CubeState.ACTIVE)

                System.out.println(c.coords);
        }

        return finalMap;
    }


    private static Map<Coordinates, Cube> getNeighbours(Coordinates c, Map<Coordinates, Cube> cubes) {

        Map<Coordinates, Cube> newCubesMap = new HashMap<>();
        for (int i = c.x - 1; i <= c.x + 1; i++) {
            for (int j = c.y - 1; j <= c.y + 1; j++) {
                for (int k = c.z - 1; k <= c.z + 1; k++) {
                    Coordinates coord = new Coordinates(i, j, k);
                    if (coord.equals(c)) {
                        continue;
                    }
                    Cube aCube = cubes.get(coord);
                    if (aCube == null) {

                        aCube = new Cube('.', coord);
                    }
                    newCubesMap.put(coord, aCube);
                }
            }
        }
        return newCubesMap;
    }


    private static Map<Coordinates, Cube> getCubeMap() {
        Map<Coordinates, Cube> cubeMap = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get("inputDay17a.txt"))) {
            final int z = 0;
            AtomicInteger lineCounter = new AtomicInteger(0);
            stream.forEach(line -> {
                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    Coordinates coords = new Coordinates(lineCounter.get(), i, z);
                    Cube cube = new Cube(chars[i], coords);
                    cubeMap.put(coords, cube);
                }
                lineCounter.incrementAndGet();
            });

            return cubeMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private enum CubeState {
        ACTIVE('#'), INACTIVE('.');

        private final char state;

        CubeState(char state) {
            this.state = state;
        }
    }

    private static class Cube {


        private final Coordinates coords;
        private CubeState state;

        @Override
        public String toString() {
            return "Cube{" +
                    "coords=" + coords +
                    ", state=" + state +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return coords.equals(cube.coords) && state == cube.state;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coords, state);
        }

        public Cube(char state, Coordinates coords) {
            this.state = state == '.' ? CubeState.INACTIVE : CubeState.ACTIVE;
            this.coords = coords;
        }
    }

    private static class Coordinates {
        int x, y, z;

        @Override
        public String toString() {
            return "Coordinates{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinates that = (Coordinates) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        public Coordinates(int x, int y, int z) {

            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
