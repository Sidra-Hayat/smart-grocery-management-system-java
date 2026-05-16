package model;

public class Employee extends Person {
    private double salary;
    private String role;


    public Employee(String id, String name, String username, String password, double salary, String role) {
        super(id, name, username, password,role);
        this.salary = salary;
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }
    public String getRole() {
        return role;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
