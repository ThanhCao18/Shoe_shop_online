package com.example.ananas.mapper;

import com.example.ananas.dto.response.CategoryResponse;
import com.example.ananas.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19 (Oracle Corporation)"
)
@Component
public class ICategoryMapperImpl implements ICategoryMapper {

    @Override
    public CategoryResponse toCategoryResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.categoryName( category.getCategoryName() );
        categoryResponse.description( category.getDescription() );
        categoryResponse.updateAt( category.getUpdateAt() );

        return categoryResponse.build();
    }
}
