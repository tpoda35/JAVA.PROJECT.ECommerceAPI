package com.eCommerce.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class CacheEvictionUtil {

    private static final Logger logger = LoggerFactory.getLogger(CacheEvictionUtil.class);

    @CacheEvict(value = "prodByCat", key = "#categoryId" )
    public void evictCacheByCategory(Long categoryId){
        logger.info("Evicting cache for category ID: {}", categoryId);
    }

}
