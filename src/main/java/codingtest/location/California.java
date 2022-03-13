package codingtest.location;

import codingtest.product_category.Food;
import codingtest.product_category.ProductCategory;

public class California extends Country {
  private static final Country instance = new California();

  public California() {
    super.name = "California";
    super.code = "CA";
    super.taxRate = 9.75F;
    super.taxFreeProductCategories = new ProductCategory[] {Food.getInstance()};
  }

  public static Country getInstance() {
    return instance;
  }
}
