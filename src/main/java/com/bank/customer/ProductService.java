package com.bank.customer;

import com.bank.db.repository.AccountRepository;
import com.bank.db.repository.ProductRepository;
import com.bank.dto.AccountDTO;
import com.bank.dto.ProductDTO;
import com.bank.mapper.AccountMapper;
import com.bank.enums.log.LogType;
import com.bank.exception.ProductsNotFoundForCategoryException;
import com.bank.mapper.ProductMapper;
import com.bank.utils.*;
import com.bank.session.Session;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final LoggerService loggerService;


    public ProductService(){
        this.productRepository = new ProductRepository();
        this.accountRepository = new AccountRepository();
        this.loggerService = new LoggerService();
    }

    public List<AccountDTO> getAllAccountsForProductCategory(String category)
            throws SQLException {

        List<Long> productIds = Collections.unmodifiableList(productRepository
                .findAllByProductCategory(category)
                .stream()
                .map(row -> (Long) row.get("id"))
                .toList());

        List<AccountDTO> accounts = new ArrayList<>();

        for (Long productId : productIds) {

            List<Map<String, Object>> accountRows =
                    accountRepository.findAccountsByProductId(productId);

            accounts.addAll(
                    accountRows.stream()
                            .map(AccountMapper::toDTO)
                            .toList()
            );
        }
        return accounts;
    }

     public List<ProductDTO> listProductsByCategory(String category) throws SQLException {
         try{
             List<Map<String,Object>> allProducts = productRepository.findAllByProductCategory(category);

             List<ProductDTO> listOfProducts = allProducts.stream()
                     .map(ProductMapper::toDTO)
                     .collect(Collectors.toList());
             loggerService.log("PRODUCT_LIST_BY_CATEGORY","Accessing all products based on product category successfull", LogType.SUCCESS);
             return listOfProducts;
         }catch (ProductsNotFoundForCategoryException e)
         {
             loggerService.log("PRODUCT_LIST_BY_CATEGORY","No products found for this category",LogType.ERROR);
             throw e;
         }catch(RuntimeException e)
         {
             loggerService.log("PRODUCT_LIST_BY_CATEGORY","Accessing all products based on product category is not succesfull",LogType.FAILURE);
             throw e;
         }
     }
     public String createProduct(String productCategory, BigDecimal minOperatingBalance, BigDecimal interestRate, Long termMonths) {
         try{
             String productName = ProductNameGenerator.productIdGenerate(); //UUID productNumber generate
             ProductDTO productDto = new ProductDTO(null,productName,productCategory,interestRate,minOperatingBalance,termMonths);
             Map<String,Object> productRow = ProductMapper.toRow(productDto);
             String productId = productRepository.insert(productRow);
             loggerService.log(
                     "PRODUCT_CREATION",
                     "New Product created with Product Number: "+productName +" successfully",
                     LogType.SUCCESS
             );
             return productName;
         } catch (SQLException e) {
             loggerService.log(
                     "PRODUCT_CREATION",
                     "Failed to create Product "+e.getMessage(),
                     LogType.FAILURE
             );
             throw new RuntimeException("Failed to Create Product",e);
         } catch (Exception e) {
             loggerService.log(
                     "PRODUCT_CREATION",
                     "Product Creation operation failed "+e.getMessage(),
                     LogType.ERROR
             );
             throw new RuntimeException("Product creation operation failed ",e);
         }
     }
}
