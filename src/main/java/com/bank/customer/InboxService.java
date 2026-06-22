package com.bank.customer;
import com.bank.db.repository.InboxRepository;
import com.bank.dto.InboxDTO;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;
import com.bank.mapper.InboxMapper;
import com.bank.session.Session;

import java.util.List;
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
            loggerService.log(
                    "FETCH TOP MESSAGE",
                    "Fetched top message",
                    LogType.SUCCESS
            );
            return InboxMapper.toDTO(messageList);
        } catch (Exception e) {
            loggerService.log(
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
        try{
            InboxDTO inboxMessageDto = new InboxDTO(null,correlationId,messageType,
                    payload,status,reason,null,null);
            Map<String,Object> inboxRow = InboxMapper.toRow(inboxMessageDto);
            Long inboxMessageId = inboxRepository.insert(inboxRow);
            loggerService.log("INBOX","Inbox Message for Queue created Succesfully",LogType.SUCCESS);
            return inboxMessageId;
        }catch(RuntimeException e){
            loggerService.log("INBOX","Inbox Message for Queue could not be created", LogType.FAILURE);
            throw e;
        }
    }
   public void deleteById(String id) {
    try {
        inboxRepository.deleteById(id);

        loggerService.log(
                "DELETE INBOX MESSAGE",
                "Deleted inbox message with ID: " + id,
                LogType.SUCCESS
        );

    } catch (Exception e) {

        loggerService.log(
                "DELETE INBOX MESSAGE",
                "Failed to delete inbox message with ID: " + id + ". Error: " + e.getMessage(),
                LogType.ERROR
        );

        throw new RuntimeException("Unable to delete");
    }
}
    public List<InboxDTO> getAllDepositsMessages() {
        try {
            List<InboxDTO> messages = inboxRepository.findAllDepositsMessages()
                    .stream()
                    .map(InboxMapper::toDTO)
                    .toList();
            loggerService.log(
                    "FETCH DEPOSIT MESSAGES",
                    "Fetched all deposit messages successfully",
                    LogType.SUCCESS
            );
            return messages;
        } catch (Exception e) {
            loggerService.log(
                    "FETCH DEPOSIT MESSAGES",
                    "Failed to fetch all deposit messages. " + e.getMessage(),
                    LogType.ERROR
            );
            throw e;
        }
    }

}