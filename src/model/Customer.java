package model;

public class Customer extends Person {
    private int loyaltyPoints;
    private boolean approved;

    public Customer(String id, String name, String username, String password, int loyaltyPoints, boolean approved) {
        super(id, name, username, password,"Customer");
        this.loyaltyPoints = loyaltyPoints;
        this.approved = false;  // default to false
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    @Override
    public String getRole() {
        return "Customer";
    }
}
