package com.bank.customer;

import com.bank.db.repository.AuthRepository;
import com.bank.db.repository.ProductRepository;
import com.bank.dto.AuthUserDTO;
import com.bank.dto.ProductDTO;
import com.bank.mapper.AuthMapper;
import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthService {
     private final AuthRepository repository;

     public AuthService(){
         this.repository = new AuthRepository();
     }

   public Map<String, Object> login(String username, String password)
        throws SQLException {

    if (username == null || username.trim().isEmpty()) {
        throw new RuntimeException("username is empty");
    }

    if (password == null || password.trim().isEmpty()) {
        throw new RuntimeException("Passwor is  empty");
    }

    Map<String, Object> userInfo = repository.findByUsername(username);

    if (userInfo == null || userInfo.isEmpty()) {
        throw new RuntimeException("Invalid");
    }
      
       AuthUserDTO dto = AuthMapper.toDTO(userInfo);

    if (password.equals(dto.getPasswordHash())) {
        Map<String, Object> result = new HashMap<>();
        result.put("authId", dto.getId());
        result.put("role",dto.getRole() );
        return result;
    }
    else{
        throw new RuntimeException("Invalid username or password");
    }
}
 }

