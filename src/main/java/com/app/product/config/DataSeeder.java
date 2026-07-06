package com.app.product.config;

import com.app.product.entity.Product;
import com.app.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            seedProducts();
        }
    }

    private void seedProducts() {
        Product[] products = {
                new Product(null, "Laptop", "High-performance laptop with 16GB RAM and 512GB SSD", 
                        new BigDecimal("1299.99"), 15, "Electronics", null, null),
                
                new Product(null, "Wireless Mouse", "Ergonomic wireless mouse with 2.4GHz connection", 
                        new BigDecimal("29.99"), 50, "Accessories", null, null),
                
                new Product(null, "USB-C Cable", "1.5m USB-C to USB-C charging cable", 
                        new BigDecimal("12.99"), 100, "Cables", null, null),
                
                new Product(null, "Monitor 27 inch", "4K UHD display with USB-C and HDR support", 
                        new BigDecimal("499.99"), 20, "Electronics", null, null),
                
                new Product(null, "Mechanical Keyboard", "RGB mechanical keyboard with Cherry MX switches", 
                        new BigDecimal("149.99"), 30, "Accessories", null, null),
                
                new Product(null, "Webcam HD", "1080p HD webcam with auto-focus and built-in microphone", 
                        new BigDecimal("59.99"), 25, "Accessories", null, null),
                
                new Product(null, "Phone Stand", "Adjustable phone stand for desks and tables", 
                        new BigDecimal("14.99"), 60, "Accessories", null, null),
                
                new Product(null, "HDMI Cable", "2m HDMI 2.1 cable for 4K video", 
                        new BigDecimal("19.99"), 80, "Cables", null, null),
                
                new Product(null, "Power Bank", "20000mAh power bank with fast charging", 
                        new BigDecimal("39.99"), 40, "Electronics", null, null),
                
                new Product(null, "Desk Lamp", "LED desk lamp with adjustable brightness and color temperature", 
                        new BigDecimal("44.99"), 35, "Accessories", null, null),
        };

        for (Product product : products) {
            productRepository.save(product);
        }
        
        System.out.println("✅ Database seeded with " + products.length + " sample products!");
    }
}
