package org.demo.prototype.etc;

import lombok.SneakyThrows;
import org.demo.prototype.entity.Account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RandomTimeAutoReportTask implements Runnable {

    private Account account;

    public RandomTimeAutoReportTask(Account account) {
            this.account = account;
    }

    @SneakyThrows
    @Override
    public void run() {
        int delaySecondNum = (new Random().nextInt(4*3600));
        System.out.printf("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"] 账户 %s 计划在 %d:%d 开始打卡，当前开始睡眠等待\n",account.getAccountId().toString(), 8+delaySecondNum/3600, delaySecondNum/60%60);
        Thread.sleep(delaySecondNum*1000);     // 8:00 ~ 12:00
        account.updateTodayHealthReport();
        System.out.printf("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"] 账户 %s 本日打卡流程已完成\n",account.getAccountId().toString(), 8+delaySecondNum/3600, delaySecondNum/60%6);
    }

}
