package com.bank.customer;

import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.mapper.InboxMapper;
import com.bank.mapper.ProductMapper;

import java.util.Map;

public class InboxService {
    private final InboxRepository repository;

    public InboxService(){
        this.repository = new InboxRepository();
    }

    public Long createInbox(InboxDTO inboxDto)
    {
        Map<String,Object> inboxRow = InboxMapper.toRow(inboxDto);
        Long inbox_id = repository.insert(inboxRow);
        return inbox_id;
    }
}
