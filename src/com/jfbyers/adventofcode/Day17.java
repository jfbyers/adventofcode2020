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

        Set<Coordinates> initialCubes = getActiveCubes();
        Set<Coordinates> cubes = new HashSet<>();
        for (Coordinates c : initialCubes) {
            Set<Coordinates> neighbours = getNeighbours(c,initialCubes);
            cubes.addAll(neighbours);
            cubes.add(c);
        }

        int cycle = 0;
        long activeCubes2 = cubes.stream().count();
        System.out.println("Cube actives before cycle : " + activeCubes2);
        Set<Coordinates> cycleCubes = cubes;
        while (cycle <= 5) {
            System.out.println("cycle " + cycle);
            cycleCubes = cycle(cycleCubes);

            cycle++;

        }
        long activeCubes = cycleCubes.stream().count();
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


    private static Set<Coordinates> cycle(Set<Coordinates> cubes) {
       Set<Coordinates> finalMap = new HashSet<>();

        for (Coordinates c : cubes) {
            Set<Coordinates> neighbours = getNeighbours(c,cubes);
            Stream<Coordinates> cubeStream = neighbours.stream();

            List<Coordinates> activeNeighbourSet = cubeStream.collect(Collectors.toList());
            long activeNeighbours = activeNeighbourSet.size();
            switch (c.state) {
                case ACTIVE:
                    if (activeNeighbours == 2 || activeNeighbours == 3) {
                        finalMap.add(c);
                    } else {
                        Cube newCube = new Cube('.', c.coords);
                        finalMap.add(newCube);
                    }
                    break;
                case INACTIVE:
                    if (activeNeighbours == 3) {
                        Cube newCube = new Cube('#', c.coords);
                        finalMap.add(newCube);
                    } else {
                        finalMap.add(c);
                    }
                    break;
            }
        }

        List<Cube> activesAfterCycle = finalMap.stream()
                .filter(cube -> cube.state.equals(CubeState.ACTIVE)).collect(Collectors.toList());

        for (Cube c : activesAfterCycle) {
            if (c.state == CubeState.ACTIVE)

                System.out.println(c.coords);
        }

        return finalMap;
    }


    private static Set<Coordinates> getNeighbours(Coordinates c) {

        Set<Coordinates> newCubesMap = new HashSet<>();
        for (int i = c.x - 1; i <= c.x + 1; i++) {
            for (int j = c.y - 1; j <= c.y + 1; j++) {
                for (int k = c.z - 1; k <= c.z + 1; k++) {
                    Coordinates coord = new Coordinates(i, j, k);
                    if (coord.equals(c)) {
                        continue;
                    }
                    newCubesMap.add(coord);
                }
            }
        }
        return newCubesMap;
    }


    private static Set<Coordinates> getActiveCubes() {
        final Set<Coordinates> cubes = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get("inputDay17a.txt"))) {

            AtomicInteger lineCounter = new AtomicInteger(0);
            stream.forEach(line -> {
                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    Coordinates coords = new Coordinates(lineCounter.get(), i, 0);
                    if (chars[i]=='#') {
                        cubes.add(coords);
                    }
                }
                lineCounter.incrementAndGet();
            });

            return cubes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
