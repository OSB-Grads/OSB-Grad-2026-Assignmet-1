package com.bank.customer;

import com.bank.db.repository.AccountRepository;
import com.bank.db.repository.ProductRepository;
import com.bank.dto.AccountDTO;
import com.bank.dto.ProductDTO;
import com.bank.mapper.AccountMapper;
import com.bank.enums.log.LogType;
import com.bank.exception.ProductsNotFoundForCategoryException;
import com.bank.mapper.ProductMapper;

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
             List<Map<String,Object>> allProducts = this.productRepository.findAllByProductCategory(category);

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
