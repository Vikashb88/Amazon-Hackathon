package com.amazon.notification.batch;


import com.amazon.notification.utils.DataManager;
import org.springframework.batch.item.ItemWriter;
import java.util.List;

/**
 * Writes products to a database
 */
public class ProductItemWriter implements ItemWriter<Product>
{
        

    @Override
    public void write(List<? extends Product> products) throws Exception
    {
        DataManager.getInstance().pushToJedis(products);
        for( Product product : products )
        {
            System.out.println("JEDIS Writer   "+product.getAttrValue());
        }
    }
}
