# Payment Processing System - In-Class Task

**Time:** 140 minutes total (across two sessions)  
**Package name:** `payments`

---

## Scenario

You are building a payment processing system for an online store. The system needs to handle different types of payments: cash payments, credit card payments, and bank transfer payments.

---

## Requirements

1. **The system must process three types of payments:**
   - Cash payments (no processing fee)
   - Credit card payments (3% processing fee)
   - Bank transfers (fixed $2 fee)

2. **Each payment should:**
   - Store the payment amount
   - Calculate the total cost including any fees
   - Process the payment and return whether it was successful
   - Provide payment details as a string

3. **Credit card payments** need to store:
   - Card number (last 4 digits)
   - Cardholder name

4. **Bank transfer payments** need to store:
   - Bank name
   - Account number (last 4 digits)

5. **Cash payments** only need to store the amount

6. All payments should be able to be **processed through a common approach**

7. The system should **validate** that payment amounts are positive

---

## What to Submit

- All your `.java` files in a folder with your student ID
- Your code must work with the provided `PaymentSystemTest.java` file
- Use package name: `payments`

---

## Constraints

- You may create as many classes as you need
- Think carefully about what should be shared vs. what is specific
- Consider which class should be responsible for what behavior

---

## Test File (Provided)

Your solution will be tested against this file:

```java
import payments.*;

public class PaymentSystemTest {
    public static void main(String[] args) {
        System.out.println("=== Payment System Test ===\n");
        
        // Test 1: Cash Payment
        System.out.println("Test 1: Cash Payment");
        CashPayment cash = new CashPayment(100.0);
        System.out.println("Total: $" + cash.calculateTotal());
        System.out.println("Processed: " + cash.process());
        System.out.println("Details: " + cash.getDetails());
        System.out.println();
        
        // Test 2: Credit Card Payment
        System.out.println("Test 2: Credit Card Payment");
        CreditCardPayment card = new CreditCardPayment(100.0, "1234", "John Doe");
        System.out.println("Total: $" + card.calculateTotal());
        System.out.println("Processed: " + card.process());
        System.out.println("Details: " + card.getDetails());
        System.out.println();
        
        // Test 3: Bank Transfer Payment
        System.out.println("Test 3: Bank Transfer Payment");
        BankTransferPayment transfer = new BankTransferPayment(100.0, "Chase Bank", "5678");
        System.out.println("Total: $" + transfer.calculateTotal());
        System.out.println("Processed: " + transfer.process());
        System.out.println("Details: " + transfer.getDetails());
        System.out.println();
        
        // Test 4: Multiple payments processed together
        System.out.println("Test 4: Processing Multiple Payments");
        CashPayment cash2 = new CashPayment(50.0);
        CreditCardPayment card2 = new CreditCardPayment(200.0, "9999", "Jane Smith");
        BankTransferPayment transfer2 = new BankTransferPayment(150.0, "Bank of America", "1111");
        
        double totalRevenue = cash2.calculateTotal() + 
                             card2.calculateTotal() + 
                             transfer2.calculateTotal();
        
        System.out.println("Total Revenue: $" + totalRevenue);
        System.out.println();
        
        // Test 5: Invalid payment amount
        System.out.println("Test 5: Invalid Payment");
        try {
            CashPayment invalid = new CashPayment(-10.0);
            System.out.println("ERROR: Should have thrown exception");
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected negative amount: " + e.getMessage());
        }
    }
}
```

---

## Expected Output

```
=== Payment System Test ===

Test 1: Cash Payment
Total: $100.0
Processed: true
Details: Cash Payment: $100.0

Test 2: Credit Card Payment
Total: $103.0
Processed: true
Details: Credit Card Payment: $100.0 (Card ending in 1234, Cardholder: John Doe)

Test 3: Bank Transfer Payment
Total: $102.0
Processed: true
Details: Bank Transfer: $100.0 (Chase Bank, Account ending in 5678)

Test 4: Processing Multiple Payments
Total Revenue: $361.0

Test 5: Invalid Payment
Correctly rejected negative amount: Payment amount must be positive
```