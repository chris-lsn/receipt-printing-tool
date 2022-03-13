package codingtest;

import codingtest.data.OrderItemInfoData;
import codingtest.location.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Receipt {

  private final Country[] countries;

  private Country country;

  private String input;

  private List<OrderItemInfoData> orderItemInfoDataList;

  public Receipt() {
    this.countries = null;
  }

  public Receipt(Country[] countries, String input) {
    this.countries = countries;
    this.input = input;
  }

  public void validateAndPrepare(Country[] countries, String input) throws Exception {
    boolean isValidPattern;

    // Validate the input string pattern - Location: XX,
    isValidPattern = input.matches("Location:\\s[A-Z]{2},+.*");
    if (!isValidPattern) {
      throw new Exception("The input data pattern doesn't match; Location: {country code},");
    }

    String[] inputDataArray = input.split(", ");
    String[] orderItemInfoStringArray =
        Arrays.copyOfRange(inputDataArray, 1, inputDataArray.length);

    // Validate the country code
    String countryCode = inputDataArray[0].replace("Location:", "").trim();
    Country country =
        Arrays.stream(countries)
            .filter(c -> c.getCode().equals(countryCode))
            .findFirst()
            .orElse(null);

    if (country == null) {
      throw new Exception("The country code " + countryCode + " doesn't exist");
    }
    setCountry(country);

    List<OrderItemInfoData> orderItemInfoDataList = new ArrayList<>();
    // Validate the order item info string pattern
    for (String orderItemInfoString : orderItemInfoStringArray) {
      isValidPattern = Pattern.matches("\\d+\\s.*\\sat\\s\\d+\\.?\\d+", orderItemInfoString);
      if (!isValidPattern) {
        throw new Exception(
            "The order item info data pattern doesn't match; {quantity} {product name} at {price}");
      } else {
        String[] orderItemInfoArray1 = orderItemInfoString.split(" ", 2);
        String[] orderItemInfoArray2 = orderItemInfoArray1[1].split(" at ", 2);
        orderItemInfoDataList.add(
            new OrderItemInfoData(
                orderItemInfoArray2[0],
                Integer.parseInt(orderItemInfoArray1[0]),
                Float.parseFloat(orderItemInfoArray2[1])));
      }
    }
    setOrderItemInfoDataList(orderItemInfoDataList);
  }

  public String generate(Country country, List<OrderItemInfoData> orderItemInfoDataList) {
    StringBuilder output = new StringBuilder();
    addReceiptLine(output, "item", "price", "qty");

    float subtotal = 0, tax = 0f;
    for (OrderItemInfoData orderItemInfoData : orderItemInfoDataList) {
      int quantity = orderItemInfoData.getQuantity();
      String productName = orderItemInfoData.getProductName();
      float productPrice = orderItemInfoData.getPrice();
      subtotal += productPrice * quantity;

      addReceiptLine(
          output, productName, "$" + String.format("%.2f", productPrice), String.valueOf(quantity));

      boolean isTaxable =
          Arrays.stream(country.getTaxFreeProductCategories())
              .noneMatch(t -> Arrays.asList(t.getProducts()).contains(productName));
      if (isTaxable) tax += productPrice * quantity * country.getTaxRate() / 100;
    }

    tax = ((float) Math.ceil(tax / 0.05F)) * 0.05F;

    addReceiptLine(output, "subtotal:", "", "$" + String.format("%.2f", subtotal));
    addReceiptLine(output, "tax:", "", "$" + String.format("%.2f", tax));
    addReceiptLine(output, "total:", "", "$" + String.format("%.2f", subtotal + tax));

    return output.toString();
  }

  private void addReceiptLine(StringBuilder output, String... args) {
    String RECEIPT_PRINT_FORMAT = "%-15s %10s %10s %n";
    output.append(String.format(RECEIPT_PRINT_FORMAT, args));
  }

  public void print() throws Exception {
    validateAndPrepare(countries, input);
    String output = generate(country, orderItemInfoDataList);
    System.out.println(output);
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public List<OrderItemInfoData> getOrderItemInfoDataList() {
    return orderItemInfoDataList;
  }

  public void setOrderItemInfoDataList(List<OrderItemInfoData> orderItemInfoDataList) {
    this.orderItemInfoDataList = orderItemInfoDataList;
  }
}
