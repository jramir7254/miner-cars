package minercars.vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import minercars.vehicle.car.Car;
import minercars.vehicle.car.Color;
import minercars.vehicle.car.Condition;
import minercars.vehicle.car.Hatchback;
import minercars.vehicle.car.Pickup;
import minercars.vehicle.car.SUV;
import minercars.vehicle.car.Sedan;
import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

public class CarManager {
    /** Header line written when no header was read from a file. */
    public static final String DEFAULT_HEADER = "ID , Car Type , Model              , Condition, Color , "
            + "Capacity, Mileage, Fuel Type, Transmission, VIN              , Price   , Cars Available";
    /** Number of columns in each row of the cars file. */
    private static final int COLUMN_COUNT = 12;
    /** Header line of the cars file, kept so the saved file matches the original. */
    private String header = DEFAULT_HEADER;
    /** ID that will be assigned to the next car added by an admin. */
    private int nextId = 1;

    private final Map<Integer, Car> cars = new LinkedHashMap<>();

    public int loadCars(String filePath) throws IOException {
        Map<Integer, Car> loaded = new LinkedHashMap<>();
        String fileHeader = null;
        int maxId = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (fileHeader == null) {
                    fileHeader = line.replace("﻿", "").trim();
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                try {
                    Car car = parseRow(line);
                    if (loaded.containsKey(car.getId())) {
                        throw new IllegalArgumentException("duplicate car ID " + car.getId());
                    }

                    loaded.put(car.getId(), car);
                    maxId = Math.max(maxId, car.getId());
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping line " + lineNumber + " of " + filePath + ": " + e.getMessage());
                }
            }
        }

        cars.clear();
        cars.putAll(loaded);
        header = (fileHeader == null || fileHeader.isEmpty()) ? DEFAULT_HEADER : fileHeader;
        nextId = maxId + 1;
        return cars.size();
    }

    /**
     * Converts one row of the cars file into a car object.
     *
     * @param line the CSV row
     * @return the car described by the row
     * @throws IllegalArgumentException if the row is malformed
     */
    private static Car parseRow(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != COLUMN_COUNT) {
            throw new IllegalArgumentException("expected " + COLUMN_COUNT + " columns but found " + fields.length);
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        return createCar(
                Integer.parseInt(fields[0]),
                fields[1],
                fields[2],
                Condition.fromString(fields[3]),
                Color.fromString(fields[4]),
                Integer.parseInt(fields[5]),
                Integer.parseInt(fields[6]),
                Fuel.fromString(fields[7]),
                Transmission.fromString(fields[8]),
                fields[9],
                Double.parseDouble(fields[10]),
                Integer.parseInt(fields[11]) //
        );

    }

    /**
    * Creates the subclass of {@link Car} that matches the given car type.
    *
    * @param id            vehicle ID
    * @param type          car type: Sedan, SUV, Hatchback or Pickup (case is ignored)
    * @param model         make and model
    * @param condition     new or used
    * @param color         exterior color
    * @param capacity      seating capacity
    * @param mileage       mileage in miles
    * @param fuelType      fuel type
    * @param transmission  transmission
    * @param vin           vehicle identification number
    * @param price         price in dollars
    * @param carsAvailable units in stock
    * @return the new car
    * @throws IllegalArgumentException if the type is unknown or any value is invalid
    */
    private static Car createCar(int id, String type, String model, Condition condition, Color color,
            int capacity, int mileage, Fuel fuelType, Transmission transmission, String vin, double price,
            int carsAvailable) {
        String key = type == null ? "" : type.trim().toLowerCase();
        switch (key) {
            case "sedan":
                return new Sedan(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            case "suv":
                return new SUV(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            case "hatchback":
                return new Hatchback(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            case "pickup":
                return new Pickup(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            default:
                throw new IllegalArgumentException("Unknown car type: \"" + (type == null ? "" : type.trim())
                        + "\". Valid types are Sedan, SUV, Hatchback, Pickup.");
        }
    }
}
