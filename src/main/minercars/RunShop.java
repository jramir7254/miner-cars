package minercars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import minercars.vehicle.car.Car;
import minercars.vehicle.car.Color;
import minercars.vehicle.car.Condition;
import minercars.vehicle.car.SUV;
import minercars.vehicle.fuel.Diesel;
import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Manual;

public class RunShop {
    public static void main(String[] args) throws Exception {
        Car shitbox1 = new SUV(1, "Ford Penis", Condition.NEW, Color.RED, 4, 300_000, new Diesel(), new Manual(),
                "IUEHWIUFH(83y98f98", 20.69);
        Car shitbox2 = new SUV(1, "Ford Penis", Condition.NEW, Color.RED, 4, 300_000, new Diesel(), new Manual(),
                "IUEHWIUFH(83y98f98", 20.69);
        System.out.println(shitbox1.hashCode());
        System.out.println(shitbox2.hashCode());

        // String filePath = "data/car_data.csv";
        // String line;

        // try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        //     while ((line = br.readLine()) != null) {
        //         String[] values = line.split(",");
        //         for (String value : values) {
        //             System.out.print(value + " | ");
        //         }
        //         System.out.println();
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

}
