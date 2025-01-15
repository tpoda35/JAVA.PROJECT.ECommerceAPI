package com.eCommerce.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
public class CacheEvictionUtil {

    private static final Logger logger = LoggerFactory.getLogger(CacheEvictionUtil.class);

    @CacheEvict(value = "prodByCat", key = "#categoryId" )
    public void evictProdByCatCache(Long categoryId){
        logger.info("Evicting cache 'prodByCat' for category ID: {}.", categoryId);
    }

    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "categories",
                    allEntries = true
            ),
            @CacheEvict(
                    value = "category",
                    key = "#categoryId"
            )
    })
    public void evictCategoryCache(Long categoryId){
        logger.info("Evicting cache 'category' for category ID: {}.", categoryId);
        logger.info("Evicting cache 'categories'.");
    }

}
