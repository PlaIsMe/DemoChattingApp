package com.example.demo.service.interfaces;

import java.util.List;

import com.example.demo.model.Account;

public interface AccountService {
  List<Account> getAllAccounts();
  Account createAccount(Account account) throws Exception;
  boolean deleteAccount(Long accountId) throws Exception;
  Account updateAccount(Long accountId, Account account) throws Exception;
  Account getAccountById(Long accountId) throws Exception;
}
