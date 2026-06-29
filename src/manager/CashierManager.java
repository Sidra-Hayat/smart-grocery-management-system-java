package manager;

import model.Cashier;
import java.io.*;
import java.util.*;

public class CashierManager {

    private final List<Cashier> cashiers = new ArrayList<>();
    private static final String CASHIER_FILE = "cashiers.txt";

    public void addCashier(Cashier cashier)    { cashiers.add(cashier); saveCashiersToFile(); }
    public List<Cashier> getAllCashiers()       { return cashiers; }

    public void updateCashier(Cashier updated) {
        for (int i = 0; i < cashiers.size(); i++) {
            if (cashiers.get(i).getId().equals(updated.getId())) {
                cashiers.set(i, updated); saveCashiersToFile(); return;
            }
        }
    }

    public boolean removeCashierById(String id) {
        boolean removed = cashiers.removeIf(c -> c.getId().equals(id));
        if (removed) saveCashiersToFile();
        return removed;
    }

    public Cashier getCashierByUsername(String username) {
        for (Cashier c : cashiers)
            if (c.getUsername().equalsIgnoreCase(username)) return c;
        return null;
    }

    public Cashier getCashierById(String id) {
        for (Cashier c : cashiers)
            if (c.getId().equals(id)) return c;
        return null;
    }

    public Cashier authenticate(String username, String password) {
        Cashier c = getCashierByUsername(username);
        return (c != null && c.getPassword().equals(password)) ? c : null;
    }

    // FILE FORMAT per line:
    // id|name|username|password|salary|totalSales|productId:qty,productId:qty,...
    public void saveCashiersToFile() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CASHIER_FILE))) {
            for (Cashier c : cashiers) {
                // Build productSales string: "P001:5,P002:3"
                StringBuilder sales = new StringBuilder();
                for (Map.Entry<String, Integer> e : c.getProductSales().entrySet()) {
                    if (sales.length() > 0) sales.append(",");
                    sales.append(e.getKey()).append(":").append(e.getValue());
                }
                w.write(c.getId() + "|" + c.getName() + "|" + c.getUsername() + "|"
                        + c.getPassword() + "|" + c.getSalary() + "|"
                        + c.getTotalSales() + "|"           // ← now saved
                        + sales);                           // ← now saved
                w.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void loadCashiersFromFile() {
        cashiers.clear();
        File file = new File(CASHIER_FILE);
        if (!file.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 5) continue;

                double salary = 0, totalSales = 0;
                try { salary     = Double.parseDouble(p[4]); } catch (NumberFormatException ignored) {}
                try { totalSales = p.length > 5 ? Double.parseDouble(p[5]) : 0; } catch (NumberFormatException ignored) {}

                Cashier c = new Cashier(p[0], p[1], p[2], p[3], salary);
                c.addSale(totalSales); // restore total sales

                // Restore per-product sales: "P001:5,P002:3"
                if (p.length > 6 && !p[6].isEmpty()) {
                    for (String entry : p[6].split(",")) {
                        String[] parts = entry.split(":");
                        if (parts.length == 2) {
                            try {
                                c.recordProductSale(parts[0], Integer.parseInt(parts[1]));
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
                cashiers.add(c);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}