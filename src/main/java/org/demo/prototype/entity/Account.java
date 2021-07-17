package org.demo.prototype.entity;

import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Account {
    private String accountId;

    private String password;

    private String realName;

    private String status;

    public String getAccountId() {
        return accountId;
    }

    public Account() {
    }
    public Account(String accountId) {
        this.accountId = accountId;
    }
    public Account(String accountId, String password, String realName, String status) {
        this.accountId = accountId;
        this.password = password;
        this.realName = realName;
        this.status = status;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @SneakyThrows
    public void updateTodayHealthReport(){
        System.out.println("【执行今日打卡】");
        // 初始化 --------------------------------------------
        WebDriver driver = new ChromeDriver();
        // 登录 --------------------------------------------
        driver.get("https://ids.xmu.edu.cn/authserver/login?service=https://xmuxg.xmu.edu.cn/login/cas/xmu");
        Thread.sleep(3000);
        driver.manage().window().setSize(new Dimension(1280, 720));
        Thread.sleep(3000);
        driver.findElement(By.id("username")).sendKeys(accountId);
        Thread.sleep(3000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(3000);
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        // 切换到打卡程序页面 --------------------------------------------
        driver.get("https://xmuxg.xmu.edu.cn/app/214");
        Thread.sleep(3000);
        // 模拟点击到“我的表单”标签
        driver.findElement(By.cssSelector(".gm-scroll-view > .tab:nth-child(2)")).click();
        Thread.sleep(3000);

        // 判断当前是否已经打卡过
        String currentStatus = driver.findElement(By.cssSelector("#select_1582538939790 .form-control")).getText();
        currentStatus = currentStatus.substring(0, currentStatus.indexOf("\n"));

        if(currentStatus.equals("请选择")){
            System.out.println("进入自动打卡流程");
            driver.findElement(By.cssSelector("#select_1582538939790 .form-control")).click();
            Thread.sleep(3000);
            // 点击提示弹框的 “确认提交...” 按钮
            driver.findElement(By.cssSelector(".dropdown-items > .btn-block")).click();
            // 保存和提交
            driver.findElement(By.cssSelector(".form-save")).click();
            System.out.println("自动打卡信息成功保存");
            Thread.sleep(3000);
            driver.switchTo().alert().accept();     // assertThat(driver.switchTo().alert().getText(), is("是否保存数据?"));
            Thread.sleep(3000);
        } else if(currentStatus.equals("是 Yes")){
            System.out.println("已经打过卡，跳过操作");
        } else {
            System.out.println("错误：未知的显示状态");
        }


        // 注销当前帐号 --------------------------------------------
        driver.get("https://xmuxg.xmu.edu.cn/platform");
        Thread.sleep(3000);
        driver.findElement(By.cssSelector(".dropdown > .dropdown-toggle")).click();
        Thread.sleep(3000);
        driver.findElement(By.linkText("注销")).click();
        // 关闭窗口 --------------------------------------------
        Thread.sleep(3000);
        driver.close();
    }



    @SneakyThrows
    public boolean checkIfTodayReportIsDone(){
        System.out.println("【检查今天是否已经打卡过】");
        // 初始化 --------------------------------------------
        WebDriver driver = new ChromeDriver();
        // 登录 --------------------------------------------
        driver.get("https://ids.xmu.edu.cn/authserver/login?service=https://xmuxg.xmu.edu.cn/login/cas/xmu");
        Thread.sleep(3000);
        driver.manage().window().setSize(new Dimension(1280, 720));
        Thread.sleep(3000);
        driver.findElement(By.id("username")).sendKeys(accountId);
        Thread.sleep(3000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(3000);
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        // 切换到打卡程序页面 --------------------------------------------
        driver.get("https://xmuxg.xmu.edu.cn/app/214");
        Thread.sleep(3000);
        // 模拟点击到“我的表单”标签
        driver.findElement(By.cssSelector(".gm-scroll-view > .tab:nth-child(2)")).click();
        Thread.sleep(3000);

        // 判断当前是否已经打卡过
        String currentStatus = driver.findElement(By.cssSelector("#select_1582538939790 .form-control")).getText();
        currentStatus = currentStatus.substring(0, currentStatus.indexOf("\n"));

        boolean result = false;

        if(currentStatus.equals("是 Yes")){
            System.out.println("已经打过卡");
            result = true;
        } else if(currentStatus.equals("请选择")){
            System.out.println("今日未打卡");
            result = false;
        } else {
            System.out.println("错误：未知的显示状态");
            result = false;
        }


        // 注销当前帐号 --------------------------------------------
        driver.get("https://xmuxg.xmu.edu.cn/platform");
        Thread.sleep(3000);
        driver.findElement(By.cssSelector(".dropdown > .dropdown-toggle")).click();
        Thread.sleep(3000);
        driver.findElement(By.linkText("注销")).click();
        // 关闭窗口 --------------------------------------------
        Thread.sleep(3000);
        driver.close();


        return result;

    }


    @SneakyThrows
    public boolean checkIfAccountIsValid(){

        WebDriver driver = new ChromeDriver();

        try{
            System.out.println("【检查账号是否真实有效】");

            // 登录 -------------------------------------------- TODO：对于错误密码的处理
            driver.get("https://ids.xmu.edu.cn/authserver/login?service=https://xmuxg.xmu.edu.cn/login/cas/xmu");
            Thread.sleep(3000);
            driver.manage().window().setSize(new Dimension(1280, 720));
            Thread.sleep(3000);
            driver.findElement(By.id("username")).sendKeys(accountId);
            Thread.sleep(3000);
            driver.findElement(By.id("password")).sendKeys(password);
            Thread.sleep(3000);
            driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
            Thread.sleep(3000);
            // 切换到打卡程序页面 --------------------------------------------
            driver.get("https://xmuxg.xmu.edu.cn/app/214");
            Thread.sleep(3000);
            // 模拟点击到“我的表单”标签
            driver.findElement(By.cssSelector(".gm-scroll-view > .tab:nth-child(2)")).click();
            Thread.sleep(3000);
            // 解析页面显示的真实姓名
            String parsedName = driver.findElement(By.cssSelector("#input_1582537796856 > input:nth-child(2)")).getAttribute("data-str");
            // 判断是否相等
            boolean result = parsedName.equals(realName);
            // 注销当前帐号 --------------------------------------------
            driver.get("https://xmuxg.xmu.edu.cn/platform");
            Thread.sleep(3000);
            driver.findElement(By.cssSelector(".dropdown > .dropdown-toggle")).click();
            Thread.sleep(3000);
            driver.findElement(By.linkText("注销")).click();
            // 关闭窗口 --------------------------------------------
            Thread.sleep(3000);
            driver.close();
            return result;
        } catch (Exception e){
            driver.close();
            throw e;
        }


    }
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}