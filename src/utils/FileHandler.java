package utils;
import model.Cashier;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class FileHandler {
    private static final String PRODUCT_FILE = "products.txt";
    private static final String CASHIER_FILE = "users.txt.txt";


        public static Map<String, Cashier> loadCashiers() {
            Map<String, Cashier> cashiers = new HashMap<>();

            File file = new File("users.txt"); // Path to the file
            if (!file.exists()) {
                System.out.println("users.txt not found.");
                return cashiers;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String username = parts[2].trim();
                        String password = parts[3].trim();

                        Cashier cashier = new Cashier(id, name, username, password);
                        cashiers.put(id, cashier);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return cashiers;
        }



    // --- Save Products with Type Tag ---
    public static void saveProducts(List<Product> products) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product p : products) {
                if (p instanceof PerishableProduct perishable) {
                    pw.println("Perishable," + p.getId() + "," + p.getName() + "," + p.getPrice() + "," +
                            p.getQuantity() + "," + perishable.getExpiryDate() + "," + p.getBarcode());
                } else if (p instanceof NonPerishableProduct nonPerishable) {
                    pw.println("NonPerishable," + p.getId() + "," + p.getName() + "," + p.getPrice() + "," +
                            p.getQuantity() + "," + nonPerishable.getShelfLifeInDays() + "," + p.getBarcode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Load Products with Type Parsing ---
    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();

        File file = new File(PRODUCT_FILE);
        if (!file.exists()) return products;

        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String type = parts[0];
                    String id = parts[1];
                    String name = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int qty = Integer.parseInt(parts[4]);

                    if (type.equalsIgnoreCase("Perishable")) {
                        LocalDate expiry = LocalDate.parse(parts[5]);
                        String barcode = parts[6];
                        products.add(new PerishableProduct(id, name, price, qty, expiry,barcode));
                    } else if (type.equalsIgnoreCase("NonPerishable")) {
                        int shelfLife = Integer.parseInt(parts[5]);
                        String barcode = parts[6];
                        products.add(new NonPerishableProduct(id, name, price, qty, shelfLife, barcode));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    // --- Save Cashiers ---
    public static void saveCashiers(Collection<Cashier> cashiers) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CASHIER_FILE))) {
            Map<String, Cashier> map = new HashMap<>();
            for (Cashier c : cashiers) {
                map.put(c.getId(), c);
            }
            out.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
