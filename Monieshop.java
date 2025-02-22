import java.util.HashMap;

// This Monieshop class represents a shop's daily sales record
public class Monieshop {
    private String salesStaffID;          // Sales staff ID for the transaction
    private String transacTime;           // Timestamp of the transaction (format: yyyy-MM-ddTHH:mm:ss)
    private HashMap<String, Integer> productsSold;  // Product ID mapped to quantity sold
    private int salesAmount;              // Total sales amount for the transaction

    // Constructor for initializing the Monieshop attributes
    public Monieshop(String salesStaffID, String transacTime, HashMap<String, Integer> productsSold, int salesAmount) {
        this.salesStaffID = salesStaffID;
        this.transacTime = transacTime;
        this.productsSold = productsSold;
        this.salesAmount = salesAmount;
    }

    public String getSalesStaffID() { return salesStaffID; }
    public String getTransacTime() { return transacTime; }
    public HashMap<String, Integer> getProductsSold() { return productsSold; }
    public int getSalesAmount() { return salesAmount; }
}