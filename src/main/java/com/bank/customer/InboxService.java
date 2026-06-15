package com.bank.customer;

import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.exception.DatabaseOperationException;
import com.bank.mapper.InboxMapper;

import java.util.HashMap;
import java.util.Map;

public class InboxService {

    private final InboxRepository inboxRepository;

    public InboxService() {
        this.inboxRepository = new InboxRepository();
    }

    public InboxDTO getTopMessage() {
        try{
            Map<String, Object> messageList = inboxRepository.findFirst();
            return InboxMapper.toDTO(messageList);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to get the message",e);
        }
    }
}
