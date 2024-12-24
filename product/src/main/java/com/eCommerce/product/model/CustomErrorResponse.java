package com.eCommerce.product.model;

import java.util.Date;

public record CustomErrorResponse(Date date,
                                  Integer statusCode,
                                  String message) { }