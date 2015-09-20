package com.amazon.notification.batch;

;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Builds a Product from a row in the CSV file (as a set of fields)
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product>
{
    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = new Product();
        product.setId( fieldSet.readInt(0) );
        product.setAttrName(fieldSet.readString(1));
        product.setAttrValue(fieldSet.readString(2));
        return product;
    }
}
