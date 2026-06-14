package com.bank.customer;

import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.mapper.InboxMapper;
import com.bank.mapper.ProductMapper;

import java.sql.SQLException;
import java.util.Map;

public class InboxService {
    private final InboxRepository repository;

    public InboxService(){
        this.repository = new InboxRepository();
    }

    public Long createInboxMessage(String correlationId, String messageType, String payload , String status , String reason) throws SQLException
    {
        InboxDTO inboxMessageDto = new InboxDTO(null,correlationId,messageType,payload,status,reason,null,null);
        Map<String,Object> inboxRow = InboxMapper.toRow(inboxMessageDto);
        Long inboxMessageId = repository.insert(inboxRow);
        return inboxMessageId;
    }
}
