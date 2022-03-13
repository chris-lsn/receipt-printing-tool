package codingtest.product_category;

public class Food extends ProductCategory {

  private static final ProductCategory instance = new Food();

  public Food() {
    super.categoryName = "food";
    super.products = new String[] {"potato chips"};
  }

  public static ProductCategory getInstance() {
    return instance;
  }
}
