public class BankAccount {
    private int Balance;
    private String Username;
    private String Password;
    private String AccountNumber;
    private String SimCardChargeCode;
    private String SimCardOperator;
    public BankAccount(String FirstName, String LastName, String Password, String AccountNumber, int Balance, String SimCardChargeCode, String SimCardOperator) {
        this.Username = FirstName + " " + LastName;
        this.AccountNumber = AccountNumber;
        this.Password = Password;
        this.Balance = Balance;
        this.SimCardChargeCode = SimCardChargeCode;
        this.SimCardOperator = SimCardOperator;
    }
    public String getUsername() {
        return Username;
    }
    public void setPass(String Password) {
        this.Password = Password;
    }
    public boolean CheckPass(String Password) {
        return Password.equals(this.Password);
    }
    public void setBalance(int Balance) {
        this.Balance = Balance;
    }
    public int getBalance() {
        return Balance;
    }
    public String getAccountNumber() {
        return AccountNumber;
    }
    public void setSimCardChargeCode(String SimCardChargeCode) { this.SimCardChargeCode = SimCardChargeCode; }
    public String getSimCardChargeCode() {
        return SimCardChargeCode;
    }
    public void setSimCardOperator(String SimCardOperator) { this.SimCardOperator = SimCardOperator; }
    public String getSimCardOperator() {
        return SimCardOperator;
    }
}