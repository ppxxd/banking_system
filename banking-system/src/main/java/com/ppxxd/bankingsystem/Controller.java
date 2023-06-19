package com.ppxxd.bankingsystem;

import java.util.Scanner;

public class Controller {

    public static void entryMenu() {
        System.out.println(
                "1. Create an account" +
                "\n2. Log into account" +
                "\n0. Exit");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (scanner.nextInt()) {
                case 1:
                    createAcc();
                    break;
                case 2:
                    logInAcc();
                    break;
                case 0:
                    exitOut();
                    break;
                default:
                    System.out.println("Wrong action! Try again!\n");
                    break;
            }
        }
    }

    public static void createAcc() {
        Card card1 = new Card();
        Database.databaseFill(card1.getID(), card1.getDigits(), card1.getPin(), card1.getBalance());
        System.out.println("\nYour card has been created");
        System.out.println("Your card number:\n" + card1.digits);
        System.out.println("Your card PIN:\n" + card1.pin + "\n");
    }

    public static void exitOut() {
        System.out.println("\nBye!");
        System.exit(0);
    }

    public static void logInAcc() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String carddigits = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String cardpin = scanner.nextLine();

        Card currentCard = Database.findCard(carddigits, cardpin);

        if (currentCard != null && carddigits.equals(currentCard.getDigits())
                && cardpin.equals(currentCard.getPin())) {
            System.out.println("You have successfully logged in!\n");
            loggedInMenu(currentCard);
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }

    public static void loggedInMenu(Card card) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit");

        while (true) {
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("\nBalance: " + card.getBalance() + "\n");
                    break;

                case 2:
                    System.out.println("\nEnter income:");
                    long income = scanner.nextInt();
                    card.setBalance(card.getBalance() + income);
                    System.out.println("Income was added!");
                    break;

                case 3:
                    System.out.println("\nTransfer");
                    System.out.println("Enter card number:");
                    String carddigits = scanner.next();

                    //Check luna algorithm (useless but required in the task)
                    String digitsWithoutLast = carddigits.replaceAll(".$", "");
                    String checkLuna = digitsWithoutLast + Card.sumLuhn(digitsWithoutLast);
                    if (!checkLuna.equals(carddigits)) {
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                        break;
                    }

                    Card transferToCard = Database.findCard(carddigits);
                    if (transferToCard != null && carddigits.equals(transferToCard.getDigits())) {
                        System.out.println("Enter how much money you want to transfer:");
                        int transfer = scanner.nextInt();

                        if (card.getBalance() - transfer > 0) {
                            transferToCard.setBalance(transferToCard.getBalance() + transfer);
                            card.setBalance(card.getBalance() - transfer);
                            System.out.println("Success!\n");
                        }
                        else {
                            System.out.println("Not enough money!\n");
                        }

                    } else {
                        System.out.println("Such a card does not exist.");
                    }
                    break;

                case 4:
                    System.out.println("\nThe account has been closed!\n");
                    Database.deleteRow(card);
                    break;

                case 5:
                    System.out.println("\nYou have successfully logged out!\n");
                    entryMenu();
                    break;

                case 0:
                    exitOut();
                    break;

                default:
                    System.out.println("Wrong action! Try again!\n");
                    break;

            }
        }
    }
}
