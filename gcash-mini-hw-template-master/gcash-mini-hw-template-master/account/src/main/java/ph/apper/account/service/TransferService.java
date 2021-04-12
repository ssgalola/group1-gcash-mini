package ph.apper.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ph.apper.account.domain.Transfer;
import ph.apper.account.payload.TransferRequest;
import ph.apper.account.payload.response.TransferResponse;
import ph.apper.account.util.IdService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private final List<Transfer> transfers = new ArrayList<>();

    private final IdService idService;

    public TransferService(IdService idService) {
        this.idService = idService;
    }

    public TransferResponse transfer(TransferRequest request) {
        String transferId = IdService.generateCode(6);
        LOGGER.info("Generated transfer ID: {}", transferId);

        Transfer transfer = new Transfer(transferId);
        transfer.setTransferId(transferId);
        transfer.setFromAccountId(request.getFromAccountId());
        transfer.setToAccountId(request.getToAccountId());
        transfer.setAmount(request.getAmount());
        transfers.add(transfer);

        LOGGER.info("Transfer: {}", transfer);

        return new TransferResponse(transferId);
    }
}