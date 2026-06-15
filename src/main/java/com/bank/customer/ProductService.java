package com.bank.customer;

import com.bank.db.repository.AccountRepository;
import com.bank.db.repository.ProductRepository;
import com.bank.dto.AccountDTO;
import com.bank.dto.ProductDTO;
import com.bank.mapper.AccountMapper;
import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductService {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public ProductService(ProductRepository productRepository,
                          AccountRepository accountRepository) {
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    public List<ProductDTO> listProductsByCategory(String category) throws SQLException {

        List<Map<String, Object>> allProducts =
                productRepository.findAllByProductCategory(category);

        return allProducts.stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    public List<AccountDTO> getAllAccountsForProductCategory(String category)
            throws SQLException {

        List<Long> productIds = productRepository
                .findAllByProductCategory(category)
                .stream()
                .map(row -> (Long) row.get("id"))
                .toList();

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
}