# KeepAliveTools

### Android 服务(进程)保活/常驻/守护 (Android Service Daemon)

#### 1. 将Service设置为前台服务而不显示通知

思路一：API < 18，启动前台Service时直接传入new Notification()；

思路二：API >= 18，同时启动两个id相同的前台Service，然后再将后启动的Service做stop处理；

//启动前台服务而不显示通知的漏洞已在 API Level 25 修复，大快人心！

前台服务相对于后台服务的优势，除了优先级的提升以外，还有一点：

在最近任务列表中划掉卡片时，前台服务不会停止；

(更新：经过测试，发现只是对于AOSP/CM/国际上对Framework层改动较小的Android系统是成立的；EMUI/MIUI等未加入白名单的情况下，划掉卡片，前台服务也会停止；加入白名单后划掉卡片的行为与国际厂商的系统相似。)

而后台服务会停止，并在稍后重新启动（onStartCommand 返回 START_STICKY 时）。

前台服务和后台服务被划掉卡片时，回调的都是 onTaskRemoved 方法。

onDestroy 方法只在 设置 -> 开发者选项 -> 正在运行的服务 里停止服务时才会回调。

#### 2. 在 Service 的 onStartCommand 方法里返回 START_STICKY

#### 3. 覆盖 Service 的 onDestroy/onTaskRemoved 方法, 保存数据到磁盘, 然后重新拉起服务

#### 4. 监听 8 种系统广播 :

- CONNECTIVITY_CHANGE
- USER_PRESENT
- ACTION_POWER_CONNECTED
- ACTION_POWER_DISCONNECTED
- BOOT_COMPLETED
- PACKAGE_ADDED
- PACKAGE_REMOVED

在网络连接改变, 用户屏幕解锁, 电源连接 / 断开, 系统启动完成, 安装 / 卸载软件包时拉起 Service.

Service 内部做了判断，若 Service 已在运行，不会重复启动.

#### 5. 开启守护服务 : 定时检查服务是否在运行，如果不在运行就拉起来

#### 6. 守护 Service 组件的启用状态, 使其不被 MAT 等工具禁用

#### 7. 守护服务 : Android 5.0 及以上版本使用 JobScheduler，效果比 AlarmManager 好

使用 JobScheduler, Android 系统能自动拉起被 Force Stop 的 Package，而 AlarmManager 无法拉起.

Android 4.4 及以下版本使用 AlarmManager.

#### 8.使用定时 Observable : 避免 Android 定制系统 JobScheduler / AlarmManager 唤醒间隔不稳定的情况

#### 9.增强对国产机型的适配 : 防止华为机型按返回键回到桌面再锁屏后几秒钟进程被杀

测试机型 : 华为 荣耀6 Plus (EMUI 4.0 Android 6.0), 应用未加入白名单.

>  
观察到 :
>  
在未加入白名单的情况下，按Back键回到桌面再锁屏后几秒钟即会杀掉进程；
>  
但是按Home键返回桌面的话，即使锁屏，也不会杀掉进程。

(更新：经过测试，在EMUI系统上，『即使锁屏，也不会杀掉进程』只对App的卡片还在多任务屏幕的第一屏时有效，一旦被挤到第二页及以后，锁屏后几秒钟即会杀掉进程；加入白名单后，回到桌面再锁屏后不会杀进程。)

因此，重写了onBackPressed方法，使其只是返回到桌面，而不是将当前Activity finish/destroy掉。

测试机型 : 红米1S 4G (MIUI 8 Android 4.4.2), 应用未加入白名单.

>  
观察到 :
>  
在未加入白名单的情况下，回到桌面再锁屏后不会杀进程；
>  
但划掉卡片，进程死亡并不再启动；加入白名单后，划掉卡片，服务不会停止，与CM的行为相似。

可以看出，若不想使用Native保活，引导用户加入白名单可能是比较可行的方法。

用 Intent 跳转

- Android Doze 模式
- 华为 自启管理
- 华为 锁屏清理
- 小米 自启动管理
- 小米 神隐模式
- 三星 5.0/5.1 自启动应用程序管理
- 三星 6.0+ 未监视的应用程序管理
- 魅族 自启动管理
- 魅族 待机耗电管理
- Oppo 自启动管理
- Vivo 后台高耗电
- 金立 应用自启
- 金立 绿色后台
- 乐视 自启动管理
- 乐视 应用保护
- 酷派 自启动管理
- 联想 后台管理
- 联想 后台耗电优化
- 中兴 自启管理
- 中兴 锁屏加速受保护应用

配合 android.support.v7.AlertDialog 引导用户将 App 加入白名单.

#### 10.守护服务和BroadcastReceiver运行在:watch子进程中，与主进程分离

守护服务和BroadcastReceiver运行在:watch子进程中，与主进程分离
工作服务运行在主进程中，免去与服务通信需使用AIDL或其他IPC方式的麻烦, 启动的前台服务与 UI 运行在同一进程中。

做了防止重复启动Service的处理，可以任意调用startService(Intent i),若服务还在运行，就什么也不做；若服务不在运行就拉起来。

在子线程中运行定时任务，处理了运行前检查和销毁时保存的问题.开始任务前，先检查磁盘中是否有上次销毁时保存的数据；定期将数据保存到磁盘。

- https://github.com/xingda920813/HelloDaemon
