package org.demo.prototype.dao;

import org.demo.prototype.entity.Account;
import org.demo.prototype.entity.AccountExample;
import org.demo.prototype.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDao {

    @Autowired
    AccountMapper accountMapper;

    public int insert(Account account){
        return accountMapper.insert(account);
    }

    public List<Account> selectAutoReportingAccounts(){
        AccountExample example = new AccountExample();
        example.createCriteria().andStatusEqualTo("AUTOREPORTING");
        List<Account> validAccountList = accountMapper.selectByExample(example);
        return validAccountList;
    }

    public List<Account> selectAllAccounts(){
        AccountExample example = new AccountExample();
        List<Account> validAccountList = accountMapper.selectByExample(example);
        return validAccountList;
    }

    public List<Account> selectUnvalidatedAccounts(){
        AccountExample example = new AccountExample();
        example.createCriteria().andStatusEqualTo("UNVALIDATED");
        List<Account> validAccountList = accountMapper.selectByExample(example);
        return validAccountList;
    }

    public Account selectAccountById(String id){
        return accountMapper.selectByPrimaryKey(id);
    }

    public int updateAccountStatusToDisabled(String id){
        Account account = new Account();
        account.setAccountId(id);
        account.setStatus("DISABLED");
        int result = accountMapper.updateByPrimaryKeySelective(account);
        return result;
    }

    public int updateAccountStatusToAutoReporting(String id){
        Account account = new Account();
        account.setAccountId(id);
        account.setStatus("AUTOREPORTING");
        int result = accountMapper.updateByPrimaryKeySelective(account);
        return result;
    }

    public void deleteAccountById(String accountId) {
        accountMapper.deleteByPrimaryKey(accountId);
    }
}
