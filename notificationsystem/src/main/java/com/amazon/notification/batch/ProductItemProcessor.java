package com.amazon.notification.batch;


import org.springframework.batch.item.ItemProcessor;

/**
 * Processor that finds existing products and updates a product quantity appropriately
 */
public class ProductItemProcessor implements ItemProcessor<Product,Product>
{
  

    @Override
    public Product process(Product product) throws Exception
    {
        
        return product;
    }
}
