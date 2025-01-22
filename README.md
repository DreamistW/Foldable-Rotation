# Foldable Rotation 文档

## 中文 | [English](https://github.com/DreamistW/Foldable-Rotation/blob/main/README_en.md)

本应用适用于折叠屏Android设备，通过监测设备折叠与否自动开启或关闭设备的自动旋转功能：
- 当设备处于折叠状态时，关闭自动旋转功能
- 当设备处于展开状态时，开启自动旋转功能

本应用需要通过**修改系统设置**权限来开启或关闭自动旋转，如果没有授权应用将无法正常运行。
为了让应用能在后台运行，建议禁用电池优化，并手动允许**应用开机自启**和**完全后台运行**，尤其是中国手机厂商的设备。

以华为设备为例：
- 打开【设置】---【应用启动管理】---搜索【Foldable Rotation】---点击开关---勾选【允许自启动】、【允许后台活动】开关
- 进入最近任务界面---找到【Foldable Rotation】的应用卡片---向下拉卡片上锁

本应用不需要联网，获取的权限不会用于声明以外的其它目的。本项目遵循Apache License 2.0协议开源。

使用教程：
1. 打开应用，点击【折叠状态监测服务】开关
2. 在权限申请弹框中点击【确定】，跳转到系统权限页面
3. 打开【允许修改系统设置】开关
4. 返回应用，再次点击【折叠状态监测服务】开关
5. 手动允许**应用开机自启**和**完全后台运行**，每家厂商的设置方式不一样，请自行在网络上搜索设置方法
6. 观察折叠和展开状态下，系统的自动旋转开关是否有变化

应用界面：
![Screenshot_20250122_161405](https://github.com/user-attachments/assets/6fa23685-bea2-4e0f-8a36-06430cecc70a)
