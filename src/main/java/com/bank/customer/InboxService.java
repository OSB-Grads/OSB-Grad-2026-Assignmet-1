package com.bank.customer;
import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;
import com.bank.mapper.InboxMapper;
import java.util.Map;
import java.sql.SQLException;
import java.util.logging.Logger;

public class InboxService {

    private final InboxRepository inboxRepository;
    private final LoggerService loggerService;

    public InboxService() {
        this.inboxRepository = new InboxRepository();
        this.loggerService = new LoggerService();
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
        try{
            InboxDTO inboxMessageDto = new InboxDTO(null,correlationId,messageType,
                    payload,status,reason,null,null);
            Map<String,Object> inboxRow = InboxMapper.toRow(inboxMessageDto);
            Long inboxMessageId = inboxRepository.insert(inboxRow);
            loggerService.log(null,"INBOX","Inbox Message for Queue created Succesfully",LogType.SUCCESS);
            return inboxMessageId;
        }catch(RuntimeException e){
            loggerService.log(null,"INBOX","Inbox Message for Queue could not be created", LogType.FAILURE);
            throw e;
        }
    }
    public void deleteById(Long id){
        try {
           inboxRepository.deleteById(id);
        } catch (Exception e) {
             throw new RuntimeException("Unable to delete");
        }
    }
}
