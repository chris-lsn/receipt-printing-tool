package codingtest.product_category;

public class Clothing extends ProductCategory {

  private static final ProductCategory instance = new Clothing();

  public Clothing() {
    super.categoryName = "clothing";
    super.products = new String[] {"shirt"};
  }

  public static ProductCategory getInstance() {
    return instance;
  }
}
