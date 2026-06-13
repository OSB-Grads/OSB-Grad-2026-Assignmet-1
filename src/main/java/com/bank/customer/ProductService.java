package com.bank.customer;

import com.bank.db.repository.ProductRepository;
import com.bank.dto.ProductDTO;
import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductService {
     private final ProductRepository repository;

     public ProductService(){
         this.repository = new ProductRepository();
     }

     public List<ProductDTO> listAllProducts() throws SQLException {
         List<Map<String,Object>> allProducts = repository.findAllProducts();
         List<ProductDTO> listOfProducts = new ArrayList<>();
         for(Map<String,Object> product : allProducts)
         {
             ProductDTO dto = ProductMapper.toDTO(product);
             listOfProducts.add(dto);
         }
         return listOfProducts;
     }
}
