package manager;

import model.Admin;
import model.Cashier;
import model.Customer;
import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoginManager {

    private final List<Person> users = new ArrayList<>();
    private final String USER_FILE = "users.txt";
    private final CashierManager cashierManager;

    public LoginManager(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
        loadUsersFromFile();              // Admins + Customers
        this.cashierManager.loadCashiersFromFile();
    }

    public Person login(String username, String password) {
        // Admins & Customers only
        for (Person user : users) {
            if (user.getUsername().equalsIgnoreCase(username)
                    && user.getPassword().equals(password)) {
                return user; // role already embedded in the concrete class type
            }
        }
        return null;
    }

    // NEW: Cashier login
    public Cashier loginAsCashier(String username, String password) {
        return cashierManager.authenticate(username, password);
    }


    // ------------------- REGISTER USER -------------------
    public synchronized boolean registerUser(Person user) {
        if (user == null) return false;
        if (isUsernameTaken(user.getUsername())) return false;

        users.add(user);
        saveUsersToFile();
        return true;
    }


    // ------------------- CHECK USERNAME -------------------
    public boolean isUsernameTaken(String username) {
        return users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    // ------------------- CUSTOMER MANAGEMENT -------------------
    public List<Customer> getUnapprovedCustomers() {
        return users.stream()
                .filter(u -> u instanceof Customer)
                .map(u -> (Customer) u)
                .filter(c -> !c.isApproved())
                .collect(Collectors.toList());
    }

    public synchronized boolean approveCustomer(String username) {
        for (Person user : users) {
            if (user instanceof Customer && user.getUsername().equalsIgnoreCase(username)) {
                ((Customer) user).setApproved(true);
                saveUsersToFile();
                return true;
            }
        }
        return false;
    }

    public List<Customer> getAllCustomers() {
        return users.stream()
                .filter(u -> u instanceof Customer)
                .map(u -> (Customer) u)
                .collect(Collectors.toList());
    }

    public synchronized boolean deleteCustomer(String username) {
        return users.removeIf(u -> u instanceof Customer && u.getUsername().equalsIgnoreCase(username));
    }

    public Customer getCustomerByUsername(String username) {
        for (Person user : users) {
            if (user instanceof Customer && user.getUsername().equalsIgnoreCase(username)) {
                return (Customer) user;
            }
        }
        return null;
    }
    public boolean deleteUserById(String id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed) {
            saveUsersToFile(); // Save updated list to users.txt
        }
        return removed;
    }
   public synchronized boolean updateUser(Cashier updated) {
        for (int i = 0; i < users.size(); i++) {
            Person u = users.get(i);
            if (u instanceof Cashier && u.getId().equals(updated.getId())) {
                users.set(i, updated);
                saveUsersToFile();
                return true;
            }
        }
        return false;
    }



    // ------------------- FILE HANDLING -------------------
    private synchronized void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Person user : users) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getId()).append("|")
                        .append(user.getName()).append("|")
                        .append(user.getUsername()).append("|")
                        .append(user.getPassword()).append("|");

                if (user instanceof Admin) {
                    sb.append("ADMIN|").append(((Admin) user).getSalary());
                } else if (user instanceof Customer) {
                    Customer c = (Customer) user;
                    sb.append("CUSTOMER|").append(c.getLoyaltyPoints()).append("|").append(c.isApproved());
                } else if (user instanceof Cashier) {
                    sb.append("CASHIER|").append(((Cashier) user).getSalary());
                }

                writer.write(sb.toString());
                writer.newLine();
            }

            // Create default admin if file is empty
            File file = new File(USER_FILE);
            if (!file.exists() || file.length() == 0) {
                try (BufferedWriter defaultWriter = new BufferedWriter(new FileWriter(file))) {
                    defaultWriter.write("1|Super Admin|admin|admin123|ADMIN|50000");
                    defaultWriter.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsersFromFile() {
        users.clear();
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 6) continue;

                String id = parts[0];
                String name = parts[1];
                String username = parts[2];
                String password = parts[3];
                String role = parts[4].toUpperCase();

                switch (role) {
                    case "ADMIN": {
                        double salary = Double.parseDouble(parts[5]);
                        users.add(new Admin(id, name, username, password, salary));
                        break;
                    }
                    case "CUSTOMER": {
                        if (parts.length < 7) continue;
                        int points = Integer.parseInt(parts[5]);
                        boolean approved = Boolean.parseBoolean(parts[6]);
                        users.add(new Customer(id, name, username, password, points, approved));
                        break;
                    }
                    // DO NOT read CASHIER here anymore. Cashiers live in cashiers.txt
                    default:
                        // unknown role, skip
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }


