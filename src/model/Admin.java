package model;

public class Admin extends Person {
    private double salary;

    public Admin(String id, String name, String username, String password, double salary) {
        super(id, name, username, password,"Admin");
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    @Override
    public String getRole() {
        return "Admin";
    }
}
