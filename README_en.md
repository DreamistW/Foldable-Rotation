# Foldable Rotation Documentation

## [中文](https://github.com/DreamistW/Foldable-Rotation/blob/main/README.md) | English

This application is designed for foldable Android devices. It automatically enables or disables the device's auto-rotation feature based on the folding status:
- When the device is folded, the auto-rotation feature is disabled.
- When the device is unfolded, the auto-rotation feature is enabled.

This application requires the **Modify System Settings** permission to toggle auto-rotation. Without granting this permission, the app will not function properly.

To allow the app to run in the background, it is recommended to disable battery optimization and manually grant permissions for **auto-start** and **unrestricted background operation**, especially for devices from Chinese manufacturers.

### Example: Huawei Devices
- Open **Settings** → **Launch manager** → Search for **Foldable Rotation** → Toggle the switch → Turn on **Auto-launch** and **Run in background**.
- Go to the recent tasks interface → Find the **Foldable Rotation** app card → Pull down the card to lock it.

This application does not require an internet connection, and the permissions obtained will not be used for purposes other than those stated. This project is open-source under Apache License 2.0.

### User Guide
1. Open the app and toggle the **Fold State Monitoring Service** switch.
2. In the permission request dialog, click **OK** to jump to the system permissions page.
3. Enable the **Allow Modifying System Settings** toggle.
4. Return to the app and toggle the **Fold State Monitoring Service** switch again.
5. Manually grant **auto-start** and **unrestricted background operation** permissions. The settings may vary by manufacturer, please search online for specific instructions.
6. Verify that the system's auto-rotation switch changes based on the folding and unfolding states.

### Application Interface
![Screenshot_20250122_161007](https://github.com/user-attachments/assets/464fbc30-1c93-45c7-9b53-3ab8cb99b5f1)
