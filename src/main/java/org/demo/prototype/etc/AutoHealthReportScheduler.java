package org.demo.prototype.etc;

import lombok.SneakyThrows;
import org.demo.prototype.dao.AccountDao;
import org.demo.prototype.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AutoHealthReportScheduler {

    @Autowired
    AccountDao accountDao;

    @Scheduled(cron = "0 00 8 ? * *")
    public void startRandomDelayAutoReport(){
        List<Account> accountList = accountDao.selectAutoReportingAccounts();
        System.out.printf("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"] 启动每日随机时间自动打卡，本次共提取 %d 条账户\n", accountList.size());
        accountList.forEach(account -> new Thread((new RandomTimeAutoReportTask(account))).start());
    }

    @SneakyThrows
    @Scheduled(cron = "0 30 13 ? * *")
    public void startInstantAutoReport(){
        List<Account> accountList = accountDao.selectAutoReportingAccounts();
        System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"] 启动每日最终检查自动打卡");
        for(Account account:accountList){
            try{
                account.updateTodayHealthReport();
            } catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(3000);
        }
        System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"] 每日最终检查自动打卡已完成");
    }

}
