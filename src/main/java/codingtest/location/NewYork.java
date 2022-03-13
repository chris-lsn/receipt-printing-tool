package codingtest.location;

import codingtest.product_category.Clothing;
import codingtest.product_category.Food;
import codingtest.product_category.ProductCategory;

public class NewYork extends Country {
  private static final Country country = new NewYork();

  public NewYork() {
    super.name = "New York";
    super.code = "NY";
    super.taxRate = 8.875F;
    super.taxFreeProductCategories =
        new ProductCategory[] {Food.getInstance(), Clothing.getInstance()};
  }

  public static Country getInstance() {
    return country;
  }
}
