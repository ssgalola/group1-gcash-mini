package ph.apper.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ph.apper.account.domain.Transfer;
import ph.apper.account.exception.BalanceInsufficientException;
import ph.apper.account.exceptions.InsufficientBalanceException;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.TransferData;
import ph.apper.account.payload.TransferMoneyRequest;
import ph.apper.account.payload.response.TransferMoneyResponse;
import ph.apper.account.util.IdService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private final List<Transfer> transfers = new ArrayList<>();

    private final IdService idService;
    private final AccountService accountService;

    public TransferService(IdService idService, AccountService accountService) {
        this.idService = idService;
        this.accountService = accountService;
    }

    public TransferMoneyResponse transfer(TransferMoneyRequest request) throws InsufficientBalanceException, InvalidAccountRequestException {
        double senderBalance = accountService.getAccountDetails(request.getFromAccountId()).getBalance();
        double recipientBalance = accountService.getAccountDetails(request.getToAccountId()).getBalance();

        if (senderBalance >= request.getAmount()) {

            String transferId = IdService.generateCode(6);
            LOGGER.info("Generated transfer ID: {}", transferId);

            Transfer transfer = new Transfer(transferId);
            transfer.setTransferId(transferId);
            transfer.setFromAccountId(request.getFromAccountId());
            transfer.setToAccountId(request.getToAccountId());
            transfer.setAmount(request.getAmount());
            transfers.add(transfer);

            accountService.updateBalance(request.getFromAccountId(),
                    (senderBalance - request.getAmount()));
            accountService.updateBalance(request.getToAccountId(),
                    (recipientBalance + request.getAmount()));

            LOGGER.info("Transfer: {}", transfer);

            return new TransferMoneyResponse(transferId);
        }
        return null;
    }

    public List<TransferData> getAllTransfers() {
        List<TransferData> transferDataList = new ArrayList<>();
        Stream<Transfer> transferStream = transfers.stream();

        transferStream.forEach(transfer -> transferDataList.add(toTransferData(transfer)));

        return transferDataList;
    }

    private TransferData toTransferData (Transfer t) {
        TransferData transferData = new TransferData();
        transferData.setTransferId(t.getTransferId());
        transferData.setFromAccountId(t.getFromAccountId());
        transferData.setToAccountId(t.getToAccountId());
        transferData.setAmount(t.getAmount());

        return transferData;
    }
}