package org.demo.prototype.etc;

import lombok.SneakyThrows;
import org.demo.prototype.dao.AccountDao;
import org.demo.prototype.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutoHealthReportScheduler {

    @Autowired
    AccountDao accountDao;

    @Scheduled(cron = "0 00 6 ? * *")
    public void startMorningAutoReport(){
        startAutoReport();
    }

    @Scheduled(cron = "0 00 12 ? * *")
    public void startAfternoonAutoReport(){
        startAutoReport();
    }

    @Scheduled(cron = "0 00 18 ? * *")
    public void startEveningAutoReport(){
        startAutoReport();
    }

    @SneakyThrows
    public void startAutoReport(){
        List<Account> accountList = accountDao.selectAutoReportingAccounts();
        System.out.println("【启动自动打卡】  // 本次处理账户总数 = "+accountList.size());
        for(Account account:accountList){
            try{
                account.updateTodayHealthReport();
            } catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(3000);
        }
    }

}
