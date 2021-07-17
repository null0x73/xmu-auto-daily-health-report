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
        if (accountId.equals("【学号】")) {
            return "错误：请把链接中的 '【学号】、【统一身份认证密码】、【真实姓名完整汉字】' 替换成你的真实信息，例如 http://116.63.240.166:12345/autoHealthReport/account/register?accountId=35320181234567&password=1234567&realName=张三";
        }
        if (accountDao.selectAccountById(accountId) != null) {
            return String.format("注册失败：数据库中已存在该账号，请勿重复提交。可以通过 https://116.63.240.166:12345/autoHealthReport/account?accountId=%d 检查该账号状态。",accountId);
        }
        Account account = new Account(accountId, password, realName, "UNVALIDATED");
        accountDao.insert(account);
        return String.format("注册成功。请等待服务器自动完成账号验证。\n" +
                "可以通过 https://116.63.240.166:12345/autoHealthReport/account?accountId=%d 检查该账号状态。\n" +
                "通常情况下每个账号需要一分钟完成验证。请关注验证是否通过。若未通过，请检查提交的参数并重试注册。",accountId);
    }

    @GetMapping("/account/disable")
    public Object disable(@RequestParam("accountId") String accountId, @RequestParam("adminToken") String adminToken) {
        if (adminToken.hashCode() != -287814169) {
            return "Forbidden: Invalid AdminToken.";
        }
        Account account = accountDao.selectAccountById(accountId);
        if (account == null) {
            return "Account not exist: invalid AccountId.";
        }
        accountDao.updateAccountStatusToDisabled(accountId);
        return "ok";
    }

    @GetMapping("/account/enable")
    public Object enable(@RequestParam("accountId") String accountId) {
        Account account = accountDao.selectAccountById(accountId);
        if (account == null) {
            return "Account not exist: invalid AccountId.";
        }
        accountDao.updateAccountStatusToAutoReporting(accountId);
        return "ok";
    }

    @GetMapping("/account")
    public Object checkInfo(@RequestParam(value = "accountId", required = false) String accountId) {
        if(accountId!=null&&accountId.equals("【学号】")){
            return "错误：请把链接中的 '【学号】' 替换成真实的学号数字，例如 http://116.63.240.166:12345/autoHealthReport/account?accountId=35320181234567。";
        }
        if (accountId == null) {        // display all
            List<Account> accountList = accountDao.selectAllAccounts();
            List<Account> processedAccountList = new ArrayList<>();
            accountList.forEach(account -> processedAccountList.add(processPrivacyFields(account)));
            return processedAccountList;
        } else {        // display all
            Account account = accountDao.selectAccountById(accountId);
            if (account == null) {
                return "错误：传入的 accountId 无效。请检查是否输入了正确的学号。";
            } else {
                return processPrivacyFields(account);
            }
        }
    }

    public Account processPrivacyFields(Account account) {
        char[] chars = account.getRealName().toCharArray();
        chars[0] = '*';
        if(chars.length>2){
            chars[1] = '*';
        };
        account.setRealName(String.valueOf(chars));
        account.setPassword("*******");
        account.setAccountId(account.getAccountId().substring(0, 3) + "********" + account.getAccountId().substring(11, 14));
        return account;
    }

}
