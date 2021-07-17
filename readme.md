## XMU Auto Daily Health Report

本项目是基于 Selenium 的 XMU 自动每日打卡服务。

只需提交一次账号登录信息，通过注册验证后，服务器将每天持续自动打卡，无需任何后续操作 ———— 相当于直接取消打卡制度。

每天 6、12、18 时共三次定时任务会对已注册并通过验证的有效账户执行自动打卡操作。18 时后新注册的账户会从第二天开始自动打卡。

一个账号有三种状态：UNVALIDATED / AUTOREPORTING / DISABLED。

通过 register 接口提交后为 UNVALIDATED 状态。若学号、密码、真实姓名相匹配、通过验证，则转换为 AUTOREPORTING 状态。

注册流程会对提交的账号信息进行校验。学号、密码、真实姓名，其中任意一项不匹配的提交请求都会被删除。

注册后数分钟内，请使用查询接口检查此前提交的账号信息是否已从 UNVALIDATED 状态转换为 AUTOREPORTING。若是，则已正确地完成注册。若提示该账号不存在，请检查账号信息是否不匹配。仍无法注册请通过邮箱联系管理员手动添加。

DISABLE 状态表示暂时停用自动打卡功能但不删除，数据库中记录仍然保留，可以在以后重新打开自动打卡。可以通过 enable / disable 接口与 AUTOREPORTING 状态互相转换。


### 服务器地址与应用程序端口

server ip = 116.63.240.166

application port = 12345 





### 账户状态说明




### 接口URL与使用说明

所有接口使用 GET 方法，可以在浏览器地址栏直接输入 URL 访问完成操作，降低使用成本。

用户名、姓名、密码在查询时已做处理，返回结果只显示学号的首、尾各三个数字和姓。

##### 传入 accountId、password、realName 提交并创建一个新的自动打卡账号
http://116.63.240.166:12345/autoHealthReport/account/register?accountId=【学号】&password=【统一身份认证密码】&realName=【真实姓名完整汉字】
注意：realName 参数请务必正确填写简体中文全名。注册流程会进行校验，学号、密码、真实姓名任意一项不符合的提交都会被删除。

##### 查看数据库中所有已登记的账号信息（敏感字段已处理）：
http://116.63.240.166:12345/autoHealthReport/account

##### 传入 accountId，查询对应账户状态
http://116.63.240.166:12345/autoHealthReport/account?accountId=【学号】

##### 传入 accountId, 停用该账户自动打卡
http://116.63.240.166:12345/autoHealthReport/account/disable?accountId=【学号】&adminToken=【管理员Token】

注意：该接口只允许持有有效 adminToken 的管理员操作。普通用户请勿尝试暴力破解或操作他人账户。

##### 传入 accountId，取消停用并恢复自动打卡
http://116.63.240.166:12345/autoHealthReport/account/enable?accountId=【学号】






### 其他

推荐 Selenium IDE 插件用于此类自动化 Web 操作脚本的录制。可直接根据键盘与鼠标操作生成 Java / Python 代码，避免手写 CSS 选择器的繁琐过程。

本服务稳定性无法严格保证，请注册的用户关注班群通告并自行验证打卡效果。

如果你觉得这个项目确实帮你节省了时间和精力，请推荐给不限于信息学院的其他厦大同学，顺便点个 star。

联系邮箱：1812549986@qq.com