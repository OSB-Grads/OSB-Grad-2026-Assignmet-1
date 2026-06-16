package com.bank.exception;

public class ProductsNotFoundForCategoryException extends RuntimeException {
    ProductsNotFoundForCategoryException(String message)
    {
        super(message);
    }
}
