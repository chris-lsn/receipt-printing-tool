package codingtest.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemInfoData {
  private String productName;
  private int quantity;
  private float price;
}
