/*
Read here
-----------------------------------------------------------------------------
The program is written in such a way that it cannot be displayed beautifully
in the terminal inside the IDE, so I suggest you run the jar or bat file.
-----------------------------------------------------------------------------
When you create a new account , the card number will be shown to you
for a few seconds and then you will be redirected to the login page.
It was possible to enter the account directly after creating an account,
but I wanted the program to be similar to creating an account for big sites.
-----------------------------------------------------------------------------
I used these a lot in the code so that it is clean and readable when displayed in the executable file :

System.out.print("\033[H\033[2J"); -----> screen cleaner
System.out.flush();
***************************************************************
try {
    Thread.sleep(2000);            -----> delay in continuation
} catch (InterruptedException e) {}
-----------------------------------------------------------------------------
*/

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;

public class BankAccountManagement {
    Scanner input = new Scanner(System.in);
    List<BankAccount> Users = new ArrayList<>();
    List<Information> Transactions = new ArrayList<>();
    BankAccount LoggedinUser, SecondAccount;
    Information LoggedinUserInfo, SecondAccountInfo;
    boolean DeleteCheck = false;
    DateTimeFormatter TimeFormatter = null;
    DateTimeFormatter Format = DateTimeFormatter.ofPattern("E, MMM dd yyyy | HH:mm:ss");
    String LoginTime, LogoutTime;

    public static void main(String[] args) {
        new BankAccountManagement();
    }

    BankAccountManagement() {
        LoadInfo();
        LoadTransactions();
        while (true) {
            System.out.print("\033[H\033[2J"); // Screen cleaner
            System.out.flush();
            System.out.println("|------------------------------|"); // Main page
            System.out.println("|    |********************|    |");
            System.out.println("|    |***** Welcome! *****|    |");
            System.out.println("|    |********************|    |");
            System.out.println("|         [0] : Login          |");
            System.out.println("|         [1] : Signup         |"); // In some places I designed the ability to exit
            System.out.println("|         [E] : Exit           |"); // and in some places the ability to return to the previous page
            System.out.println("|------------------------------|"); // [0] : EXIT or [0] : BACK
            System.out.print("Enter your choice : ");
            String Option = input.nextLine();
            switch (Option) {
                case "0":
                    Login();
                    continue;
                case "1":
                    Signup();
                    continue;
                case "E", "e":
                    System.exit(0);
                    break;
                default:
                    try {
                        System.out.println("Enter a valid number!");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
            }
        }
    }

    private void Signup() {

        String FirstName, LastName, Password;

        First:
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your first name : ( [0] : EXIT ) "); // it gets a first name without any space and (@ , ! , _ , ...)
            FirstName = input.nextLine();
            if (FirstName.equals("0"))
                return;
            boolean Check = Pattern.compile("[^a-zA-Z]").matcher(FirstName).find();
            boolean CheckEmpty = FirstName.isEmpty();
            if (Check || CheckEmpty) {
                try {
                    System.out.println("Please enter a simple name with english letters and without (SPACE , @ , ! , _ , ...)");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            break;
        }

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your Last name : ( [0] : EXIT ) "); // after it got first name and last name, they will combine and make username
            LastName = input.nextLine();
            if (LastName.equals("0"))
                return;
            boolean Check = Pattern.compile("[^a-zA-Z]").matcher(LastName).find();
            boolean CheckEmpty = LastName.isEmpty();
            if (Check || CheckEmpty) {
                try {
                    System.out.println("Please enter a simple name with english letters and without (SPACE , @ , ! , _ , ...)");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            break;
        }

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your password (Contains letters and numbers) : ( [0] : EXIT ) ");
            Password = input.nextLine();
            if (Password.equals("0"))
                return;
            boolean CheckLetter = Pattern.compile("[a-zA-Z]").matcher(Password).find(); //  Handle password limits
            boolean CheckNumber = Pattern.compile("\\d").matcher(Password).find();
            boolean CheckWhiteSpace = Pattern.compile("\\s").matcher(Password).find();
            boolean CheckEmpty = Password.isEmpty();
            boolean CheckLen = false;
            if (Password.length() < 4)
                CheckLen = true;
            if (CheckWhiteSpace) {
                try {
                    System.out.println("Do not use space!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            if (!CheckLetter || !CheckNumber || CheckEmpty) {
                try {
                    System.out.println("Contains letters and numbers!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            if (CheckLen) {
                try {
                    System.out.println("The minimum length of the password must be at least 4 characters!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            break;
        }

        Random rand = new Random(new Date().getTime());
        int RandomNumber;
        RandomNumber:
        while (true) {
            RandomNumber = rand.nextInt(1000, 10000);
            for (BankAccount i : Users) {
                if (i.getAccountNumber().equals(Integer.toString(RandomNumber))) {
                    continue RandomNumber;
                }
            }
            break;
        }
        Transactions.add(new Information(Integer.toString(RandomNumber), "", "", "", "", ""));
        Users.add(new BankAccount(FirstName, LastName, Password, Integer.toString(RandomNumber), 10000, "", ""));
        SaveInfo();
        SaveTransactions();

        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Your account has been created!");
            System.out.println(RandomNumber + " {" + FirstName + " " + LastName + "} ");
            Thread.sleep(6000);
        } catch (InterruptedException e) {
        }

    }

    private void Login() {

        String AccountNumber, Password;

        ACC:
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your account number : ( [0] : EXIT ) ");
            AccountNumber = input.nextLine();
            if (AccountNumber.equals("0"))
                return;
            if ((Pattern.compile("[^0-9]").matcher(AccountNumber).find()) || (AccountNumber.length() != 4) || (Pattern.compile("^\\s").matcher(AccountNumber).find())) {
                try {
                    System.out.println("Enter a valid account number!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            for (BankAccount i : Users) {
                if (i.getAccountNumber().equals(AccountNumber)) {
                    LoggedinUser = i;
                    break ACC;
                }
            }
            try {
                System.out.println("This account does not exist!");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
        for (Information i : Transactions) {
            if (i.getAccountNumber().equals(AccountNumber)) {
                LoggedinUserInfo = i;
                break;
            }
        }
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your password : ( [0] : EXIT ) ");
            Password = input.nextLine();
            if (Password.equals("0"))
                return;
            if (LoggedinUser.CheckPass(Password)) {
                LocalDateTime Time = LocalDateTime.now();
                LoginTime = Time.format(Format);
                LoggedinUserInfo.setLoginDate(LoginTime);
                ManageBankAccount();
                return;
            } else {
                try {
                    System.out.println("Wrong password!");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    private void ManageBankAccount() {

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("{ " + LoggedinUser.getUsername() + " , " + LoggedinUser.getAccountNumber());
            System.out.println("{ Balance : " + LoggedinUser.getBalance());
            System.out.println("|-----------------------------|"); //       manage account
            System.out.println("|      1- Deposit             |");
            System.out.println("|      2- Transfer Money      |");
            System.out.println("|      3- Charge SimCard      |");
            System.out.println("|      4- Show Transactions   |");
            System.out.println("|      5- Change password     |");
            System.out.println("|      6- Delete Account      |");
            System.out.println("|      7- Logout              |");
            System.out.println("|-----------------------------|");
            System.out.print("Enter your choice : ");
            String Option = input.nextLine();
            switch (Option) {
                case "1":
                    Deposit();
                    continue;
                case "2":
                    MoneyTransfer();
                    continue;
                case "3":
                    SimCardCharge();
                    continue;
                case "4":
                    ShowTransactions();
                    continue;
                case "5":
                    ChangePassword();
                    continue;
                case "6":
                    DeleteAccount();
                    if (DeleteCheck) {
                        DeleteCheck = false;
                        return;
                    } else
                        continue;
                case "7":
                    LocalDateTime Time = LocalDateTime.now();
                    LogoutTime = Time.format(Format);
                    LoggedinUserInfo.setLogoutDate(LogoutTime);
                    return;
                default:
                    try {
                        System.out.println("Enter a valid number!");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
            }
        }
    }

    private void Deposit() {

        First:
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("You can add one of the following default values to your account : ");
            System.out.println("1- 10000     2- 20000     3- 50000\n4- 100000    5- 200000    6- 500000");
            System.out.println("Enter 'F' to enter your favorite amount : ( [0] : Exit ) ");
            String Option = input.nextLine();
            int Balance = LoggedinUser.getBalance();
            switch (Option) {
                case "1":
                    LoggedinUser.setBalance(10000 + Balance);
                    break;
                case "2":
                    LoggedinUser.setBalance(20000 + Balance);
                    break;
                case "3":
                    LoggedinUser.setBalance(50000 + Balance);
                    break;
                case "4":
                    LoggedinUser.setBalance(100000 + Balance);
                    break;
                case "5":
                    LoggedinUser.setBalance(200000 + Balance);
                    break;
                case "6":
                    LoggedinUser.setBalance(500000 + Balance);
                    break;
                case "F", "f":
                    while (true) {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("Enter a value between 10000 and 1000000000 : ( [0] : EXIT ) ");
                        String value = input.nextLine();
                        if (value.equals("0"))
                            continue First;
                        if ((Pattern.compile("[^0-9]").matcher(value).find()) || (value.length() < 5 || value.length() > 10)) {
                            try {
                                System.out.println("Enter a valid value!");
                                Thread.sleep(2000);
                                continue;
                            } catch (InterruptedException e) {
                            }
                        }
                        LoggedinUser.setBalance(Integer.parseInt(value) + Balance);
                        break;
                    }
                    break;
                case "0":
                    break;
                default:
                    try {
                        System.out.println("Enter a valid number!");
                        Thread.sleep(2000);
                        continue;
                    } catch (InterruptedException e) {
                    }
            }
            SaveInfo();
            break;
        }
    }

    private void MoneyTransfer() {

        boolean Flag = false;
        String AccountNumber, Amount, Password;

        First:
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println(LoggedinUser.getUsername() + " | Your Balance : " + LoggedinUser.getBalance() + " T");
            System.out.println("------------------------------------------------------");
            System.out.println("Enter the account number to which you want to send money : ( [0] : EXIT ) ");
            AccountNumber = input.nextLine();
            if (AccountNumber.equals("0"))
                return;
            if ((Pattern.compile("[^0-9]").matcher(AccountNumber).find()) || (AccountNumber.length() != 4) || (Pattern.compile("^\\s").matcher(AccountNumber).find())) {
                try {
                    System.out.println("Enter a valid account number!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            if (LoggedinUser.getAccountNumber().equals(AccountNumber)) {
                try {
                    System.out.println("You cant transfer money to your account. Try Deposit! ");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            for (BankAccount i : Users) {
                if (i.getAccountNumber().equals(AccountNumber)) {
                    SecondAccount = i;
                    Flag = true;
                    break;
                }
            }
            if (!Flag) {
                try {
                    System.out.println("This account does not exist!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            for (Information i : Transactions) {
                if (i.getAccountNumber().equals(AccountNumber)) {
                    SecondAccountInfo = i;
                    break;
                }
            }
            Second:
            while (true) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println(LoggedinUser.getUsername() + " | Your Balance : " + LoggedinUser.getBalance() + " T");
                System.out.println("------------------------------------------------------");
                System.out.println("Enter the account number to which you want to send money : ");
                System.out.println("* " + AccountNumber + " | " + SecondAccount.getUsername() + " *");
                System.out.println("------------------------------------------------------");
                System.out.println("Enter the desired amount to transfer : ( [0] : BACK ) ");
                try {
                    Amount = input.nextLine();
                    if (Amount.equals("0")) {
                        continue First;
                    }
                    if ((Pattern.compile("[^0-9]").matcher(Amount).find())) {
                        try {
                            System.out.println("Enter a valid value!");
                            Thread.sleep(2000);
                            continue;
                        } catch (InterruptedException e) {
                        }
                    }
                    if (Integer.parseInt(Amount) > LoggedinUser.getBalance()) {
                        try {
                            System.out.println("You dont have enough money!");
                            Thread.sleep(2000);
                            continue;
                        } catch (InterruptedException e) {
                        }
                    }
                    while (true) {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println(LoggedinUser.getUsername() + " | Your Balance : " + LoggedinUser.getBalance() + " T");
                        System.out.println("------------------------------------------------------");
                        System.out.println("Enter the account number to which you want to send money : ");
                        System.out.println("* " + AccountNumber + " | " + SecondAccount.getUsername() + " *");
                        System.out.println("------------------------------------------------------");
                        System.out.println("Enter the desired amount to transfer : ");
                        System.out.println(Amount);
                        System.out.println("------------------------------------------------------");
                        System.out.println("Enter your password to confirm transition : ( [0] : BACK ) ");
                        Password = input.nextLine();
                        if (Password.equals("0")) {
                            continue Second;
                        }
                        if (!(LoggedinUser.CheckPass(Password))) {
                            try {
                                System.out.println("Wrong password!");
                                Thread.sleep(2000);
                                continue;
                            } catch (InterruptedException e) {
                            }
                        }
                        LoggedinUser.setBalance(LoggedinUser.getBalance() - Integer.parseInt(Amount));
                        SecondAccount.setBalance(SecondAccount.getBalance() + Integer.parseInt(Amount));
                        LoggedinUserInfo.setTransferredMoney(Amount);
                        LoggedinUserInfo.setToAccountNumber(AccountNumber);
                        SecondAccountInfo.setReceivedMoney(Amount);
                        System.out.println("Done!");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        SaveTransactions();
                        SaveInfo();
                        break First;
                    }
                } catch (Exception e2) {
                    try {
                        System.out.println("Enter a valid value!");
                        Thread.sleep(2000);
                        continue;
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private void SimCardCharge() {

        String Operator = null, SimCode;

        First:
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Choose your operator : ( [0] : EXIT )");
            System.out.println("1- HAMRAHAVAL\n2- IRANCELL\n3- RIGHTEL");

            String Option = input.nextLine();
            switch (Option) {
                case "1":
                    Operator = "HAMRAHAVAL";
                    break;
                case "2":
                    Operator = "IRANCELL";
                    break;
                case "3":
                    Operator = "RIGHTEL";
                    break;
                case "0":
                    return;
                default:
                    try {
                        System.out.println("Invalid entry!");
                        Thread.sleep(2000);
                        continue;
                    } catch (InterruptedException e) {
                    }
            }
            Second:
            while (true) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Choose one of this default amounts : ( [0] : BACK ) ");
                System.out.println("1- 10000    2- 20000\n3- 50000    4- 100000");
                String AmountOption = input.nextLine();
                switch (AmountOption) {
                    case "1":
                        if (10000 > LoggedinUser.getBalance()) {
                            System.out.println("You dont have enough money! ");
                            while (true) {
                                System.out.print("\033[H\033[2J");
                                System.out.flush();
                                System.out.println("Enter 'D' to Deposit or '0' to back to menu : ");
                                String DepositOption = input.nextLine();
                                switch (DepositOption) {
                                    case "D":
                                        Deposit();
                                        return;
                                    case "0":
                                        continue Second;
                                    default:
                                        try {
                                            System.out.println("Invalid entry!");
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                        }
                                }
                            }
                        }
                        LoggedinUser.setBalance(LoggedinUser.getBalance() - 10000);
                        SimCode = GenerateSimCardCode();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("Your Sim charge : ");
                        System.out.println("{ " + Operator + " | " + SimCode + " }");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        LoggedinUser.setSimCardChargeCode(SimCode);
                        LoggedinUser.setSimCardOperator(Operator);
                        break;
                    case "2":
                        if (20000 > LoggedinUser.getBalance()) {
                            System.out.println("You dont have enough money! ");
                            while (true) {
                                System.out.print("\033[H\033[2J");
                                System.out.flush();
                                System.out.println("Enter 'D' to Deposit or '0' to back to menu : ");
                                String DepositOption = input.nextLine();
                                switch (DepositOption) {
                                    case "D":
                                        Deposit();
                                        return;
                                    case "0":
                                        continue Second;
                                    default:
                                        try {
                                            System.out.println("Invalid entry!");
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                        }
                                }
                            }
                        }
                        LoggedinUser.setBalance(LoggedinUser.getBalance() - 20000);
                        SimCode = GenerateSimCardCode();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("Your Sim charge : ");
                        System.out.println("{ " + Operator + " | " + SimCode + " }");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        LoggedinUser.setSimCardChargeCode(SimCode);
                        LoggedinUser.setSimCardOperator(Operator);
                        break;
                    case "3":
                        if (50000 > LoggedinUser.getBalance()) {
                            System.out.println("You dont have enough money! ");
                            while (true) {
                                System.out.print("\033[H\033[2J");
                                System.out.flush();
                                System.out.println("Enter 'D' to Deposit or '0' to back to menu : ");
                                String DepositOption = input.nextLine();
                                switch (DepositOption) {
                                    case "D":
                                        Deposit();
                                        return;
                                    case "0":
                                        continue Second;
                                    default:
                                        try {
                                            System.out.println("Invalid entry!");
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                        }
                                }
                            }
                        }
                        LoggedinUser.setBalance(LoggedinUser.getBalance() - 50000);
                        SimCode = GenerateSimCardCode();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("Your Sim charge : ");
                        System.out.println("{ " + Operator + " | " + SimCode + " }");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        LoggedinUser.setSimCardChargeCode(SimCode);
                        LoggedinUser.setSimCardOperator(Operator);
                        break;
                    case "4":
                        if (100000 > LoggedinUser.getBalance()) {
                            System.out.println("You dont have enough money! ");
                            while (true) {
                                System.out.print("\033[H\033[2J");
                                System.out.flush();
                                System.out.println("Enter 'D' to Deposit or '0' to back to menu : ");
                                String DepositOption = input.nextLine();
                                switch (DepositOption) {
                                    case "D":
                                        System.out.println("OK!");
                                        Deposit();
                                        return;
                                    case "0":
                                        continue Second;
                                    default:
                                        try {
                                            System.out.println("Invalid entry!");
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                        }
                                }
                            }
                        }
                        LoggedinUser.setBalance(LoggedinUser.getBalance() - 100000);
                        SimCode = GenerateSimCardCode();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("Your Sim charge : ");
                        System.out.println("{ " + Operator + " | " + SimCode + " }");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        LoggedinUser.setSimCardChargeCode(SimCode);
                        LoggedinUser.setSimCardOperator(Operator);
                        break;
                    case "0":
                        continue First;
                    default:
                        try {
                            System.out.println("Invalid entry!");
                            Thread.sleep(2000);
                            continue;
                        } catch (InterruptedException e) {
                        }
                }
                SaveInfo();
                break First;
            }
        }
    }

    private void ChangePassword() {

        String Password;

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your previous password for confirmation : ( [0] : EXIT ) ");
            Password = input.nextLine();
            if (Password.equals("0"))
                return;
            if (!(LoggedinUser.CheckPass(Password))) {
                System.out.println("Wrong password!");
                try {
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter a new password (Contains letters and numbers) : ( [0] : EXIT ) ");
            Password = input.nextLine();
            if (Password.equals("0"))
                return;
            boolean CheckLetter = Pattern.compile("[a-zA-Z]").matcher(Password).find(); //  Handle password limits
            boolean CheckNumber = Pattern.compile("\\d").matcher(Password).find();
            boolean CheckWhiteSpace = Pattern.compile("\\s").matcher(Password).find();
            boolean CheckEmpty = Password.isEmpty();
            boolean CheckLen = false;
            if (Password.length() < 4)
                CheckLen = true;
            if (CheckWhiteSpace) {
                try {
                    System.out.println("Do not use space!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            if (!CheckLetter || !CheckNumber || CheckEmpty) {
                try {
                    System.out.println("Contains letters and numbers!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            if (CheckLen) {
                try {
                    System.out.println("The minimum length of the password must be at least 4 characters!");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            break;
        }
        LoggedinUser.setPass(Password);
        System.out.println("Your password changed");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    public void ShowTransactions() {

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("The last login time : " + LoggedinUserInfo.getLoginDate());
        if (!(LoggedinUserInfo.getLogoutDate() == null)) {
            System.out.println("The last logout time : " + LoggedinUserInfo.getLogoutDate());
        }
        if (LoggedinUserInfo.getToAccountNumber().isEmpty()) {
            System.out.println("You did not transfer any money recently! ");
        }
        if (!(LoggedinUserInfo.getToAccountNumber().isEmpty())) {
            System.out.println("You transferred " + LoggedinUserInfo.getTransferredMoney() + " T to {" + LoggedinUserInfo.getToAccountNumber() + "} recently.");
        }
        if (LoggedinUserInfo.getReceivedMoney().isEmpty()) {
            System.out.println("You did not receive any money recently! ");
        }
        if (!(LoggedinUserInfo.getReceivedMoney().isEmpty())) {
            System.out.println("You received " + LoggedinUserInfo.getReceivedMoney() + " T from {" + LoggedinUserInfo.getToAccountNumber() + "} recently.");
        }
        if (LoggedinUser.getSimCardChargeCode().isEmpty()) {
            System.out.println("You dont have SimCard charge! ");
        }
        if (!(LoggedinUser.getSimCardChargeCode().isEmpty())) {
            System.out.println("Your SimCard charge : " + "{ " + LoggedinUser.getSimCardOperator() + " | " + LoggedinUser.getSimCardChargeCode() + " }");
        }
        System.out.println("Press ENTER to continue...");
        input.nextLine();

    }

    public String GenerateSimCardCode() {
        String SimCharge = "";
        Random rand = new Random(new Date().getTime());
        for (int i = 0; i < 20; i++)
            SimCharge += rand.nextInt(0, 9);
        return SimCharge;
    }

    private void DeleteAccount() {

        String Password;

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Enter your password to confirm : ( [0] : EXIT ) ");
            Password = input.nextLine();
            if (Password.equals("0")) {
                return;
            }
            if (!(LoggedinUser.CheckPass(Password))) {
                try {
                    System.out.println("Wrong password!");
                    Thread.sleep(1500);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            System.out.println("Your account deleted!");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            Users.remove(LoggedinUser);
            Transactions.remove(LoggedinUserInfo);
            DeleteCheck = true;
            SaveTransactions();
            SaveInfo();
            break;
        }
    }

    private void SaveTransactions() {
        try {
            File Info = new File("Transactions.txt");
            FileWriter FileWriter = new FileWriter(Info);
            Gson gson = new Gson();
            if (Info.exists()) {
                for (int i = 0; i < Transactions.size(); i++) {
                    FileWriter.write(gson.toJson(Transactions.get(i)));
                    if (i != Transactions.size() - 1) {
                        FileWriter.write("\n");
                    }
                }
                FileWriter.close();
            } else {
                Info.createNewFile();
                for (int i = 0; i < Transactions.size(); i++) {
                    FileWriter.write(gson.toJson(Transactions.get(i)));
                    if (i != Transactions.size() - 1) {
                        FileWriter.write("\n");
                    }
                }
                FileWriter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void LoadTransactions() {
        try {
            File Info = new File("Transactions.txt");
            Gson gson = new Gson();
            if (Info.exists()) {
                Scanner scanner = new Scanner(Info);
                while (scanner.hasNextLine()) {
                    Transactions.add(gson.fromJson(scanner.nextLine(), Information.class));
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void SaveInfo() {
        try {
            File Info = new File("Users.txt");
            FileWriter FileWriter = new FileWriter(Info);
            Gson gson = new Gson();
            if (Info.exists()) {
                for (int i = 0; i < Users.size(); i++) {
                    FileWriter.write(gson.toJson(Users.get(i)));
                    if (i != Users.size() - 1) {
                        FileWriter.write("\n");
                    }
                }
                FileWriter.close();
            } else {
                Info.createNewFile();
                for (int i = 0; i < Users.size(); i++) {
                    FileWriter.write(gson.toJson(Users.get(i)));
                    if (i != Users.size() - 1) {
                        FileWriter.write("\n");
                    }
                }
                FileWriter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void LoadInfo() {
        try {
            File Info = new File("Users.txt");
            Gson gson = new Gson();
            if (Info.exists()) {
                Scanner FileReader = new Scanner(Info);
                Users.clear();
                while (FileReader.hasNextLine()) {
                    Users.add(gson.fromJson(FileReader.nextLine(), BankAccount.class));
                }
                FileReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}