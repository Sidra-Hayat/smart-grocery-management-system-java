package model;

public abstract class Person {
    private String id;
    private String name;
    private String username;
    private String password;
    protected String role;
    public Person(String id, String name, String username, String password,String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role=role;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
