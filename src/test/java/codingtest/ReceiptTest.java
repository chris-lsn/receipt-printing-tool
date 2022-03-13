package codingtest;

import codingtest.data.OrderItemInfoData;
import codingtest.location.California;
import codingtest.location.Country;
import codingtest.location.NewYork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReceiptTest {
  @InjectMocks @Spy private Receipt receipt;

  private Country[] countryArray;

  private static Stream<Arguments> test_validateAndPrepare_parameters() {
    Receipt[] receipts = getExceptedReceipts();

    return Stream.of(
        Arguments.of("Location: CA, 1 book at 17.99, 1 potato chips at 3.99", receipts[0]),
        Arguments.of("Location: NY, 1 book at 17.99, 3 pencils at 2.99", receipts[1]),
        Arguments.of("Location: NY, 2 pencils at 2.99, 1 shirt at 29.99", receipts[2]));
  }

  private static Stream<Arguments> test_generate_parameters() {
    Receipt[] receipts = getExceptedReceipts();
    return Stream.of(
        Arguments.of(
            California.getInstance(),
            receipts[0].getOrderItemInfoDataList(),
            "item                 price        qty \n"
                + "book                $17.99          1 \n"
                + "potato chips         $3.99          1 \n"
                + "subtotal:                      $21.98 \n"
                + "tax:                            $1.80 \n"
                + "total:                         $23.78 \n"),
        Arguments.of(
            NewYork.getInstance(),
            receipts[1].getOrderItemInfoDataList(),
            "item                 price        qty \n"
                + "book                $17.99          1 \n"
                + "pencils              $2.99          3 \n"
                + "subtotal:                      $26.96 \n"
                + "tax:                            $2.40 \n"
                + "total:                         $29.36 \n"),
        Arguments.of(
            NewYork.getInstance(),
            receipts[2].getOrderItemInfoDataList(),
            "item                 price        qty \n"
                + "pencils              $2.99          2 \n"
                + "shirt               $29.99          1 \n"
                + "subtotal:                      $35.97 \n"
                + "tax:                            $0.55 \n"
                + "total:                         $36.52 \n"));
  }

  private static Receipt[] getExceptedReceipts() {
    Receipt receipt1 = new Receipt();
    receipt1.setCountry(California.getInstance());
    receipt1.setOrderItemInfoDataList(
        Arrays.asList(
            new OrderItemInfoData("book", 1, 17.99F),
            new OrderItemInfoData("potato chips", 1, 3.99F)));

    Receipt receipt2 = new Receipt();
    receipt2.setCountry(NewYork.getInstance());
    receipt2.setOrderItemInfoDataList(
        Arrays.asList(
            new OrderItemInfoData("book", 1, 17.99F), new OrderItemInfoData("pencils", 3, 2.99F)));

    Receipt receipt3 = new Receipt();
    receipt3.setCountry(NewYork.getInstance());
    receipt3.setOrderItemInfoDataList(
        Arrays.asList(
            new OrderItemInfoData("pencils", 2, 2.99F), new OrderItemInfoData("shirt", 1, 29.99F)));

    return new Receipt[] {receipt1, receipt2, receipt3};
  }

  @BeforeEach
  public void setup() {
    countryArray = new Country[] {California.getInstance(), NewYork.getInstance()};
  }

  @ParameterizedTest
  @MethodSource("test_validateAndPrepare_parameters")
  public void test_validateAndPrepare(String input, Receipt expectedReceipt) throws Exception {
    ArgumentCaptor<Country> countryArgumentCaptor =
        ArgumentCaptor.forClass(Country.class);
    ArgumentCaptor<List<OrderItemInfoData>> orderItemInfoDataListCaptor =
        ArgumentCaptor.forClass(List.class);

    receipt.validateAndPrepare(countryArray, input);
    verify(receipt).setCountry(countryArgumentCaptor.capture());
    verify(receipt).setOrderItemInfoDataList(orderItemInfoDataListCaptor.capture());

    assertEquals(expectedReceipt.getCountry(), countryArgumentCaptor.getValue());
    assertEquals(
        expectedReceipt.getOrderItemInfoDataList(), orderItemInfoDataListCaptor.getValue());
  }

  @ParameterizedTest
  @MethodSource("test_generate_parameters")
  public void test_generate(
      Country country,
      List<OrderItemInfoData> orderItemInfoDataList,
      String expectedOutput) {
    String output = receipt.generate(country, orderItemInfoDataList);
    assertEquals(expectedOutput, output);
  }
}
