package com.bank.customer;
import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;
import com.bank.mapper.InboxMapper;
import java.util.Map;
import java.sql.SQLException;

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
            loggerService.log(
                    null,
                    "FETCH TOP MESSAGE",
                    "Fetched top message",
                    LogType.SUCCESS
            );
            return InboxMapper.toDTO(messageList);
        } catch (Exception e) {
            loggerService.log(
                    null,
                    "FETCH TOP MESSAGE",
                    "Failed to fetch top message",
                    LogType.ERROR
            );
            throw e;
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
    public void deleteById(Long id){
        try {
           inboxRepository.deleteById(id);
        } catch (Exception e) {
             throw new RuntimeException("Unable to delete");
        }
    }
}
