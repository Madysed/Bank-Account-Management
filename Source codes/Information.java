public class Information {
    private String AccountNumber;
    private String ToAccountNumber;
    private String TransferredMoney;
    private String ReceivedMoney;
    private String LoginDate;
    private String LogoutDate;
    public Information(String AccountNumber, String ToAccountNumber, String TransferredMoney, String ReceivedMoney, String LoginDate, String LogoutDate) {
        this.AccountNumber = AccountNumber;
        this.ToAccountNumber = ToAccountNumber;
        this.TransferredMoney = TransferredMoney;
        this.ReceivedMoney = ReceivedMoney;
    }
    public String getAccountNumber() {
        return AccountNumber;
    }
    public void setToAccountNumber(String ToAccountNumber) {
        this.ToAccountNumber = ToAccountNumber;
    }
    public String getToAccountNumber() {
        return ToAccountNumber;
    }
    public void setTransferredMoney(String TransferredMoney) {
        this.TransferredMoney = TransferredMoney;
    }
    public String getTransferredMoney() {
        return TransferredMoney;
    }
    public void setReceivedMoney(String ReceivedMoney) {
        this.ReceivedMoney = ReceivedMoney;
    }
    public String getReceivedMoney() {
        return ReceivedMoney;
    }
    public void setLoginDate (String LoginDate) {
        this.LoginDate = LoginDate;
    }
    public String getLoginDate () {
        return LoginDate;
    }
    public void setLogoutDate (String LogoutDate) {
        this.LogoutDate = LogoutDate;
    }
    public String getLogoutDate () {
        return LogoutDate;
    }
}
