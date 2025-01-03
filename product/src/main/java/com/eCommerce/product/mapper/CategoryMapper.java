package com.eCommerce.product.mapper;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toNormal(CategoryDto categoryDto);

}
