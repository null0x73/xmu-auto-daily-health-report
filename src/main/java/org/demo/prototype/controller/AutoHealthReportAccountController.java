package org.demo.prototype.controller;

import org.demo.prototype.dao.AccountDao;
import org.demo.prototype.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/autoHealthReport")
public class AutoHealthReportAccountController {

    @Autowired
    AccountDao accountDao;

    @GetMapping("/account/register")
    public Object register(@RequestParam("accountId") String accountId,
            @RequestParam("password") String password,
            @RequestParam("realName") String realName) {
        if(accountDao.selectAccountById(accountId)!=null){
            return "Failed to register: Duplicate accountId";
        }
        Account account = new Account(accountId, password, realName, "UNVALIDATED");
        accountDao.insert(account);
        return "Register Success. Waiting for server to validate your account. This process would take several minutes.";
    }

    @GetMapping("/account/disable")
    public Object disable(@RequestParam("accountId") String accountId, @RequestParam("adminToken")String adminToken) {
        if(adminToken.hashCode()!=-287814169){
            return "Forbidden: Invalid AdminToken.";
        }
        Account account = accountDao.selectAccountById(accountId);
        if(account==null){
            return "Account not exist: invalid AccountId.";
        }
        accountDao.updateAccountStatusToDisabled(accountId);
        return "ok";
    }

    @GetMapping("/account/enable")
    public Object enable(@RequestParam("accountId") String accountId) {
        Account account = accountDao.selectAccountById(accountId);
        if(account==null){
            return "Account not exist: invalid AccountId.";
        }
        accountDao.updateAccountStatusToAutoReporting(accountId);
        return "ok";
    }

    @GetMapping("/account")
    public Object checkInfo(@RequestParam(value = "accountId", required = false) String accountId) {
        if(accountId!=null){        // display single
            Account account = accountDao.selectAccountById(accountId);
            if(account==null){
                return "Account not exist: invalid AccountId.";
            } else {
                return processPrivacyFields(account);
            }
        } else {    // display all
            List<Account> accountList = accountDao.selectAllAccounts();
            List<Account> processedAccountList = new ArrayList<>();
            accountList.forEach(account -> processedAccountList.add(processPrivacyFields(account)));
            return processedAccountList;
        }
    }

    public Account processPrivacyFields(Account account){
        account.setRealName(account.getRealName().substring(0,1)+"*******");
        account.setPassword("HIDDEN");
        account.setAccountId(account.getAccountId().substring(0,3)+"********"+account.getAccountId().substring(11,13));
        return account;
    }

}
