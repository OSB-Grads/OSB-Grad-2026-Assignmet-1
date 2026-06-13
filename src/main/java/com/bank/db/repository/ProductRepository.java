package com.bank.db.repository;

import com.bank.db.DatabaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private final DatabaseManager db;

    public ProductRepository() {
        this.db = DatabaseManager.getInstance();
    }


    public Map<String,Object> findByProductId(Long productId) throws SQLException
    {
        String sql = "SELECT * FROM products where id = ? ";
        List<Map<String,Object>> row = db.query(sql,productId);
        if(row.isEmpty()){
            return null;
        }

        return row.get(0);
    }

    public Long insert(Map<String,Object> productFields) throws SQLException
    {
        String sql = "INSERT INTO products (product_name , category, interest_rate ,min_operating_balance , term_months) "+
                "VALUES (?,?,?,?,?)";

        List<Map<String,Object>> productRow = db.query(sql,productFields.get("product_name"),productFields.get("category"),
                                                    productFields.get("interest_rate"),productFields.get("min_operating_balance"),
                                                    productFields.get("termMonths"));

        return (Long) productRow.get(0).get("id");
    }

    public List<Map<String,Object>> findAllByProductCategory(String category) throws SQLException
    {
        String sql = "SELECT * FROM products WHERE category = ?";
        List<Map<String,Object>> productNames = db.query(sql,category);
        if(productNames.isEmpty())
        {
            return null;
        }
        return productNames;
    }

    public List<Map<String,Object>> findAllProducts() throws SQLException
    {
        String sql = "SELECT * FROM products";
        List<Map<String,Object>> products = db.query(sql);

        return products;
    }
}
