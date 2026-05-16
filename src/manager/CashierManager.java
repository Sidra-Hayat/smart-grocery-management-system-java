package manager;

import model.Cashier;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CashierManager {

    private final List<Cashier> cashiers = new ArrayList<>();
    private static final String CASHIER_FILE = "cashiers.txt";

    // CRUD
    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
        saveCashiersToFile();
    }

    public void updateCashier(Cashier updated) {
        for (int i = 0; i < cashiers.size(); i++) {
            if (cashiers.get(i).getId().equals(updated.getId())) {
                cashiers.set(i, updated);
                saveCashiersToFile();
                return;
            }
        }
    }

    public boolean removeCashierById(String id) {
        boolean removed = cashiers.removeIf(c -> c.getId().equals(id));
        if (removed) saveCashiersToFile();
        return removed;
    }

    public List<Cashier> getAllCashiers() {
        return cashiers;
    }

    public Cashier getCashierByUsername(String username) {
        for (Cashier c : cashiers) {
            if (c.getUsername().equalsIgnoreCase(username)) return c;
        }
        return null;
    }

    public Cashier getCashierById(String id) {
        for (Cashier c : cashiers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    // Authentication for cashier
    public Cashier authenticate(String username, String password) {
        Cashier c = getCashierByUsername(username);
        if (c != null && c.getPassword().equals(password)) return c;
        return null;
    }

    // FILE I/O  (cashiers.txt: id|name|username|password|salary)
    public void saveCashiersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CASHIER_FILE))) {
            for (Cashier c : cashiers) {
                writer.write(c.getId() + "|" + c.getName() + "|" + c.getUsername() + "|" +
                        c.getPassword() + "|" + c.getSalary());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCashiersFromFile() {
        cashiers.clear();
        File file = new File(CASHIER_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length != 5) continue; // skip malformed
                double salary;
                try {
                    salary = Double.parseDouble(p[4]);
                } catch (NumberFormatException e) {
                    continue;
                }
                cashiers.add(new Cashier(p[0], p[1], p[2], p[3], salary));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
