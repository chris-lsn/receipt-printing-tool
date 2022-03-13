package codingtest;

import codingtest.location.California;
import codingtest.location.Country;
import codingtest.location.NewYork;

import java.util.Scanner;

public class Application {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Country[] countryArray = new Country[] {California.getInstance(), NewYork.getInstance()};

    while (true) {
      System.out.print("Input:");
      String inputData = scanner.nextLine();

      Receipt receipt = new Receipt(countryArray, inputData);

      try {
        receipt.print();
      } catch (Exception e) {
        System.out.println("Error when printing receipt: " + e.getMessage());
      }
    }
  }
}
