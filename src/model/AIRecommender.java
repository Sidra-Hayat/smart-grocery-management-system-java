package model;

import java.util.List;
import java.util.Random;

public class AIRecommender {
    private Random random = new Random();

    public Product recommendProduct(List<Product> productList) {
        if (productList.isEmpty()) return null;
        return productList.get(random.nextInt(productList.size()));
    }
}
