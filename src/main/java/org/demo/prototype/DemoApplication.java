package org.demo.prototype;

import lombok.SneakyThrows;
import org.demo.prototype.dao.AccountDao;
import org.demo.prototype.entity.Account;
import org.demo.prototype.util.EnvironmentUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@EnableScheduling
@MapperScan("org.demo.prototype.mapper")
@SpringBootApplication(scanBasePackages = "org.demo.prototype")
public class DemoApplication implements ApplicationRunner {

    @Autowired
    AccountDao accountDao;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("FuckHealthReport".hashCode());
        Thread.sleep(10000);
        System.setProperty("webdriver.chrome.driver", "C:\\dev\\software\\chromedriver\\chromedriver.exe");
        ((new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while(true){
                    Thread.sleep(10000);
                    System.out.printf("=========== [ %s ] ============\n", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    // 每 10s 检查一次是否有新注册的未验证账户
                    List<Account> unvalidatedAccountList = accountDao.selectUnvalidatedAccounts();
                    if(unvalidatedAccountList.size()==0){
                        System.out.println("待验证列表为空，等待 10s 后继续检查");
                    }
                    for(Account unvalidatedAccount:unvalidatedAccountList){
                        boolean validateResult = false;
                        try{
                            validateResult = unvalidatedAccount.checkIfAccountIsValid();
                        } catch (Exception e){
                            validateResult = false;
                        }
                        if(validateResult==true){
                            accountDao.updateAccountStatusToAutoReporting(unvalidatedAccount.getAccountId());
                            System.out.printf("已验证有效账号 %s 并加入自动报告队列\n", unvalidatedAccount.getAccountId());
                        } else {
                            accountDao.deleteAccountById(unvalidatedAccount.getAccountId());
                            System.out.printf("发现无效账号 %s 并删除\n", unvalidatedAccount.getAccountId());
                        }
                    }
                }
            }
        }))).start();
        System.out.println("HASHCODE = "+"Huaji2333".hashCode());
    }

}
