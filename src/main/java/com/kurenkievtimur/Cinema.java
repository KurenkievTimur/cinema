package com.kurenkievtimur;

import java.util.Scanner;

public class Cinema {
    private final static int TICKET_PRICE_10 = 10;
    private final static int TICKET_PRICE_8 = 8;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[] cinemaSize = enterCinemaSize(scanner);

        int row = cinemaSize[0];
        int column = cinemaSize[1];

        String[][] cinema = new String[row + 1][column + 1];

        loadCinema(cinema);
        printCinema(cinema);

        while (true) {
            switch (chooseAction(scanner)) {
                case 1 -> printCinema(cinema);
                case 2 -> buyTicket(scanner, cinema, row, column);
                case 3 -> printStatistics(cinema, row, column);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Incorrect number!");
            }
        }
    }

    public static int[] enterCinemaSize(Scanner scanner) {
        int row;
        int column;

        boolean isValid = false;
        do {
            System.out.println("Enter the number of rows:");
            row = scanner.nextInt();

            System.out.println("Enter the number of seats in each row:");
            column = scanner.nextInt();

            try {
                isValid = isValidCinema(row, column);
            } catch (IllegalArgumentException e) {
                System.out.printf("\n%s\n\n", e.getMessage());
            }
        } while (!isValid);

        return new int[]{row, column};
    }

    public static boolean isValidCinema(int row, int column) {
        if (row < 1 || column < 1) {
            throw new IllegalArgumentException("Wrong input!");
        }

        return true;
    }

    public static void loadCinema(String[][] cinema) {
        for (int i = 0; i < cinema.length; i++) {
            for (int j = 0; j < cinema[i].length; j++) {
                if (i == 0 && j == 0) {
                    cinema[i][j] = " ";
                } else if (i == 0) {
                    cinema[i][j] = String.valueOf(j);
                } else if (j == 0) {
                    cinema[i][j] = String.valueOf(i);
                } else {
                    cinema[i][j] = "S";
                }
            }
        }
    }

    public static void printCinema(String[][] cinema) {
        System.out.println("\nCinema:");
        for (String[] row : cinema) {
            for (String col : row) {
                System.out.printf("%s ", col);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void buyTicket(Scanner scanner, String[][] cinema, int row, int column) {
        int seatRow;
        int seatColumn;

        boolean isValid = false;
        do {
            System.out.println("\nEnter a row number:");
            seatRow = scanner.nextInt();

            System.out.println("Enter a seat number in that row:");
            seatColumn = scanner.nextInt();

            try {
                isValid = isValidTicket(cinema, seatRow, seatColumn, row, column);
            } catch (IllegalArgumentException e) {
                System.out.printf("\n%s\n", e.getMessage());
            }
        } while (!isValid);

        printTicketPrice(row, column, seatRow);

        cinema[seatRow][seatColumn] = "B";
    }

    public static boolean isValidTicket(String[][] cinema, int seatRow, int seatColumn, int row, int column) throws IllegalArgumentException {
        if (seatRow > row || seatColumn > column || seatRow < 1 || seatColumn < 1) {
            throw new IllegalArgumentException("Wrong input!");
        } else if (cinema[seatRow][seatColumn].equals("B")) {
            throw new IllegalArgumentException("That ticket has already been purchased!");
        }

        return true;
    }

    public static void printTicketPrice(int row, int column, int seatRow) {
        int totalSeats = row * column;
        int front = row / 2;

        if (totalSeats <= 60) {
            System.out.printf("\nTicket price: $%d\n\n", TICKET_PRICE_10);
        } else {
            System.out.printf("\nTicket price: $%d\n\n", seatRow <= front ? TICKET_PRICE_10 : TICKET_PRICE_8);
        }
    }

    public static int chooseAction(Scanner scanner) {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");

        return scanner.nextInt();
    }

    public static void printStatistics(String[][] cinema, int row, int col) {
        int totalSeats = row * col;

        int front = row / 2;
        int back = row - front;

        int[] income = countIncome(cinema, totalSeats, front);

        int purchasedTickets = income[0];
        int currentIncome = income[1];

        int totalIncome;
        if (totalSeats <= 60) {
            totalIncome = col * row * TICKET_PRICE_10;
        } else {
            totalIncome = (front * col * TICKET_PRICE_10) + (back * col * TICKET_PRICE_8);
        }

        double percentage = (double) purchasedTickets / (row * col) * 100;

        System.out.printf("\nNumber of purchased tickets: %d\n", purchasedTickets);
        System.out.printf("Percentage: %.2f%%\n", percentage);
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n\n", totalIncome);
    }

    public static int[] countIncome(String[][] cinema, int totalSeats, int front) {
        int purchasedTickets = 0;
        int income = 0;

        for (int i = 1; i < cinema.length; i++) {
            for (int j = 1; j < cinema[i].length; j++) {
                if (cinema[i][j].equals("B")) {
                    purchasedTickets++;

                    if (totalSeats <= 60)
                        income += TICKET_PRICE_10;
                    else if (i <= front)
                        income += TICKET_PRICE_10;
                    else
                        income += TICKET_PRICE_8;
                }
            }
        }

        return new int[]{purchasedTickets, income};
    }
}