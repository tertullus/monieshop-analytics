import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// This Main class is for reading sales data, creating Monieshop objects and generating an analytics report based on the text files provided
public class Main {
    private static final int DAYS = 365;  // Total days in the year

    public static void main(String[] args) {
        Monieshop[] shops = new Monieshop[DAYS];  // Array to store Monieshop objects

        // Read data for each day of 2025 and populate the Monieshop array
        for (int i = 0; i < DAYS; i++) {
            String date = LocalDateTime.of(2025, 1, 1, 0, 0).plusDays(i)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            try {
                shops[i] = readMonieshopFromFile(date + ".txt");
            } catch (IOException e) {
                System.err.println("Error reading file for date: " + date + " - " + e.getMessage());
            }
        }

        // Generating and displaying analytics report based on the data collected
        generateAnalytics(shops);
    }

    // Reading a single text file containing sales data and createing a Monieshop object
    private static Monieshop readMonieshopFromFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        String[] data = lines.get(0).split(",", 4);  // Split line into components

        String salesStaffID = data[0].trim();
        String transacTime = data[1].trim();
        HashMap<String, Integer> productsSold = parseProductsSold(data[2].trim());
        int salesAmount = (int) Double.parseDouble(data[3].trim());

        return new Monieshop(salesStaffID, transacTime, productsSold, salesAmount);
    }

    // Converting product sales data into a HashMap
    private static HashMap<String, Integer> parseProductsSold(String products) {
        HashMap<String, Integer> productsSold = new HashMap<>();
        String cleaned = products.replaceAll("[\\[\\]]", ""); // Removing square brackets

        if (!cleaned.isEmpty()) {
            String[] pairs = cleaned.split("\\|");
            for (String pair : pairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    productsSold.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        }

        return productsSold;
    }

    // The Analytics Report
    private static void generateAnalytics(Monieshop[] shops) {
        int highestSalesVolume = 0;
        int highestSalesValue = 0;
        String highestVolumeDate = "";
        String highestValueDate = "";
        HashMap<String, Integer> productTotals = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> monthlyStaffSales = new HashMap<>();
        int[] hourlyTransactionCounts = new int[24];

        // Processing sales data for each day
        for (Monieshop shop : shops) {
            String date = shop.getTransacTime().substring(0, 10);      // Extract date (yyyy-MM-dd)
            String month = date.substring(0, 7);                       // Extract month (yyyy-MM)
            int hour = Integer.parseInt(shop.getTransacTime().substring(11, 13)); // Extract hour

            int dailyVolume = shop.getProductsSold().values().stream().mapToInt(Integer::intValue).sum();
            int dailyValue = shop.getSalesAmount();

            // Tracking highest sales volume and value
            if (dailyVolume > highestSalesVolume) {
                highestSalesVolume = dailyVolume;
                highestVolumeDate = date;
            }

            if (dailyValue > highestSalesValue) {
                highestSalesValue = dailyValue;
                highestValueDate = date;
            }

            // Combining product totals for most sold product calculation
            for (Map.Entry<String, Integer> entry : shop.getProductsSold().entrySet()) {
                productTotals.put(entry.getKey(), productTotals.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }

            // Tracking sales staff volume by month
            monthlyStaffSales.putIfAbsent(month, new HashMap<>());
            HashMap<String, Integer> staffSales = monthlyStaffSales.get(month);
            staffSales.put(shop.getSalesStaffID(), staffSales.getOrDefault(shop.getSalesStaffID(), 0) + dailyVolume);

            // Tracking hourly transaction counts
            hourlyTransactionCounts[hour]++;
        }

        // Finding most sold product
        String mostSoldProduct = productTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");

        // Finding peak sales hour by transaction count
        int peakHour = 0;
        for (int i = 1; i < 24; i++) {
            if (hourlyTransactionCounts[i] > hourlyTransactionCounts[peakHour]) {
                peakHour = i;
            }
        }

        // Generating report output
        System.out.println("\nAnalytics Report:");
        System.out.println("- Highest sales volume in a day: " + highestSalesVolume + " on " + highestVolumeDate);
        System.out.println("- Highest sales value in a day: " + highestSalesValue + " on " + highestValueDate);
        System.out.println("- Most sold product ID by volume: " + mostSoldProduct);
        System.out.println("- Highest hour of the day by average transaction volume: " + peakHour + ":00");

        // Displaying top sales staff per month
        System.out.println("\nHighest sales staff ID for each month:");
        monthlyStaffSales.forEach((month, staffMap) -> {
            Map.Entry<String, Integer> topStaff = staffMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);
            if (topStaff != null) {
                System.out.println("- " + month + ": Staff ID: " + topStaff.getKey() + " (Volume: " + topStaff.getValue() + ")");
            }
        });
    }
}
