package codingtest.location;

import codingtest.product_category.ProductCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Country {
  protected float taxRate;
  protected String name;
  protected String code;
  protected ProductCategory[] taxFreeProductCategories;
}
