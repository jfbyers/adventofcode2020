package com.jfbyers.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 {

    public static void main(String[] args) {
        Map<Integer, List<BusId>> buses = getBuses();
        int timestamp = buses.keySet().iterator().next().intValue();

        List<BusId> busList = buses.get(timestamp);

        //part 1
        List<Integer> busesIds = getWorkingBuses(busList);
        Map<Integer, Integer> busModule = new TreeMap<>();
        for (Integer i : busesIds) {
            int upper = timestamp / i + 1;
            busModule.put((upper * i - timestamp), i);
        }
        Integer upper = busModule.keySet().iterator().next();
        System.out.println(busModule.get(upper) * (upper));

        //part 2
        long time = Long.valueOf(busList.get(0).id);
        long increment = time;
        for (int i = 1; i < busList.size(); i++) {
            BusId indexed = busList.get(i);
            if (indexed.id.equals("x")) continue;
            Long busId = Long.valueOf(indexed.id);

            while (true) {
                if (!((time + indexed.position) % busId != 0)) break;
                time += increment;
            }
            increment *= busId;
        }

        System.out.println("timestampPart2 " + time);

    }

    private static List<Integer> getWorkingBuses(List<BusId> buses) {
        List<Integer> ids = new ArrayList<>();
        for (BusId id : buses) {
            if (!id.id.equals("x")) {
                ids.add(Integer.valueOf(id.id));
            }
        }

        return ids;
    }

    private static Map<Integer, List<BusId>> getBuses() {
        try {
            Scanner scanner = new Scanner(new File("inputDay13.txt"));
            List<BusId> buses = null;
            int timestamp = 0;
            for (int i = 0; scanner.hasNextLine(); i++) {
                String line = scanner.nextLine();
                if (i == 0) {
                    timestamp = Integer.valueOf(line);
                } else {
                    buses = BusId.fromLine(line);
                }
            }
            return Stream.of(
                    new AbstractMap.SimpleEntry<>(timestamp, buses))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class BusId {
        private final String id;
        private final int position;

        public BusId(String busId, int position) {
            this.id = busId;
            this.position = position;
        }


        public static List<BusId> fromLine(String line) {
            final List<BusId> buses = new ArrayList<>();
            String[] busesIds = line.split(",");
            int pos = 0;
            for (String busId : busesIds) {
                BusId id = new BusId(busId, pos);
                pos++;
                buses.add(id);
            }
            return buses;
        }
    }
}
