package com.bank.customer;
import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.exception.DatabaseOperationException;
import com.bank.mapper.InboxMapper;
import java.util.Map;
import java.sql.SQLException;

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

    public Long createInboxMessage(String correlationId,
                                   String messageType,
                                   String payload,
                                   String status,
                                   String reason) throws SQLException
    {
        InboxDTO inboxMessageDto = new InboxDTO(null,correlationId,messageType,
                                                payload,status,reason,null,null);
        Map<String,Object> inboxRow = InboxMapper.toRow(inboxMessageDto);
        Long inboxMessageId = inboxRepository.insert(inboxRow);
        return inboxMessageId;
    }
}
