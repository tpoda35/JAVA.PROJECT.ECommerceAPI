package com.eCommerce.product.mapper;

import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toNormal(ProductDto productDto);

}
