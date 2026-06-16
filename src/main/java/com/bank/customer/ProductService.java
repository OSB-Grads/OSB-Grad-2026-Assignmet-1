package com.bank.customer;

import com.bank.db.repository.ProductRepository;
import com.bank.dto.ProductDTO;
import com.bank.enums.log.LogType;
import com.bank.exception.ProductsNotFoundForCategoryException;
import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {
     private final ProductRepository repository;
     private final LoggerService loggerService;

     public ProductService(){
         this.repository = new ProductRepository();
         this.loggerService = new LoggerService();
     }

     public List<ProductDTO> listProductsByCategory(String category) throws SQLException {
         try{
             List<Map<String,Object>> allProducts = repository.findAllByProductCategory(category);

             List<ProductDTO> listOfProducts = allProducts.stream()
                     .map(ProductMapper::toDTO)
                     .collect(Collectors.toList());
             loggerService.log(null,"PRODUCT_LIST_BY_CATEGORY","Accessing all products based on product category successfull", LogType.SUCCESS);
             return listOfProducts;
         }catch (ProductsNotFoundForCategoryException e)
         {
             loggerService.log(null,"PRODUCT_LIST_BY_CATEGORY","No products found for this category",LogType.ERROR);
             throw e;
         }catch(RuntimeException e)
         {
             loggerService.log(null,"PRODUCT_LIST_BY_CATEGORY","Accessing all products based on product category is not succesfull",LogType.FAILURE);
             throw e;
         }
     }
}
