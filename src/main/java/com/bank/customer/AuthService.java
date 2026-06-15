package com.bank.customer;

import com.bank.db.repository.AuthRepository;
import com.bank.db.repository.CustomerRepository;
// import com.bank.db.repository.ProductRepository;
// import com.bank.dto.ProductDTO;
// import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;
import java.util.Map;
// import java.util.stream.Collectors;

public class AuthService {
     private final AuthRepository authRepository;
     private final CustomerRepository customerRepository;

     public AuthService(){
         this.authRepository = new AuthRepository();
         this.customerRepository = new CustomerRepository();
     }

     public String LoginAuthservice(String UserName, String Password) throws SQLException {
        
        try {
            
            if(UserName==null || UserName.trim().isEmpty() ){
                return "Invalid username";
            }
            if(Password==null || Password.trim().isEmpty() ){
                return "Invalid Password";
            }

            Map<String, Object> userInfo=authRepository.findByUsername(UserName);
            Object tempPassword = userInfo.get("password_hash");
            String passwordHash = tempPassword.toString();
            if(passwordHash.equals(Password)){
                 Map<String, Object> Roleinfo = customerRepository.findByUsername(UserName);
                 Object Roleobject= Roleinfo.get("role");
                  String Role = Roleobject.toString();
                  return Role;
                 
            }else{
                return "Enter valid Password";
            }
        
            

        } catch (SQLException e) {
            System.out.println("failed to login");
            
        }

        

         return "";
     }
}
