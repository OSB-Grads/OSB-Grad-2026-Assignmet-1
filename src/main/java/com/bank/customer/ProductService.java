package com.bank.customer;

import com.bank.db.repository.ProductRepository;
import com.bank.dto.ProductDTO;
import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {
     private final ProductRepository repository;

     public ProductService(){
         this.repository = new ProductRepository();
     }

     public List<ProductDTO> listProductsByCategory(String category) throws SQLException {
         List<Map<String,Object>> allProducts = repository.findAllByProductCategory(category);

         List<ProductDTO> listOfProducts = allProducts.stream()
                 .map(ProductMapper::toDTO)
                 .collect(Collectors.toList());

         return listOfProducts;
     }
}
