package ph.apper.account.service;

import ph.apper.account.domain.Account;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.exceptions.InvalidLoginException;
import ph.apper.account.exceptions.InvalidUserRegistrationException;
import ph.apper.account.exceptions.InvalidVerificationRequestException;
import ph.apper.account.payload.AccountRequest;
import ph.apper.account.payload.response.GetAccountResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;

public interface AccountServiceInterface {

    NewAccountResponse addAccount(AccountRequest request) throws InvalidUserRegistrationException;

    boolean verifyAccount(String email, String verificationCode) throws InvalidVerificationRequestException;

    //    public boolean verifyAccount(String email, String verificationCode) throws InvalidAccountRequestException {
    //        Optional<Account>
    //        if(Objects.nonNull(verificationService.getVerificationCode(email))){
    //            if(verificationService.getVerificationCode(email).equals(verificationCode)){
    //                getAccount(email).setVerified(true);
    //                verificationService.invalidateVerificationCode(email);
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    Account getAccount(String email) throws InvalidAccountRequestException;

    Account getAccountById(String accountId) throws InvalidAccountRequestException;

    GetAccountResponse getAccountDetails(String accountId) throws InvalidAccountRequestException;

    Account authenticateAccount(String email, String password) throws InvalidLoginException;

    UpdateBalanceResponse updateBalance(String accountId, Double newBalance) throws InvalidAccountRequestException;
}
