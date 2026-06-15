package com.bank.customer;

import com.bank.db.repository.InboxRepository;

public class InboxService {
    private final InboxRepository inboxRepository;
   public InboxService(){
        this.inboxRepository=new InboxRepository();
    }

    public void deleteById(Long id){
        try {
           inboxRepository.deleteById(id);
        } catch (Exception e) {
             throw new RuntimeException("Unable to delete");
        }
    }
}
