<div align="right">
🌐
<a href="https://github.com/wen-wen520/Minecraft.Mod-MagpieBridge">English</a>
&nbsp;|&nbsp;
<a href="https://github.com/wen-wen520/Minecraft.Mod-MagpieBridge/blob/master/README.zh.md">中文</a>
</div>

<div align="center">

<img src="assets/icon/transparent.png" alt="icon for this repo" style="width: 15%;">

<h1>Magpie Bridge</h1>
<h2>A Minecraft mod that sends in game chats as System Notifications.</h2>

</div>


## 📋 Overview

Magpie Bridge is a lightweight Minecraft mod that brings your in-game chat messages directly to your desktop as system notifications!


## ❇️ Features

- **Native Windows Notifications:**
  Seamlessly integrates with Windows 10 and 11 notification systems.
- **Dark Mode Support:**
  Notifications match your system theme, including dark mode.
- **Unified Notification Center:**
  All Minecraft chat notifications appear in your Windows Notification Center for easy management and review.


## 🖼️ Gallery

[Modrinth Gallery](https://modrinth.com/mod/magpiebridge/gallery)

![Normal Mode](assets/gallery/Introduction.jpg)

<br>

![Dark Mode](assets/gallery/Dark%20Mode.jpg)

<br>

![Notification center](assets/gallery/Notification%20Center.jpg)


## ⚙️ Requirements

Architecture: x64\
System: Windows 10 version 15063.0 or higher\
Java: Java 21\
Minecraft: 1.21 - 1.21.8\
Fabric: 0.16.10 or higher

## ✅ Installation

Modrinth [[⬇️ Download]](https://modrinth.com/mod/magpiebridge/versions) provides a modern UI to choose and download this mod

Github [[📦 Releases]](https://github.com/wen-wen520/Minecraft.Mod-MagpieBridge/releases) Page is good to get the source code and pack them by self

## ⚙️ Configuration

### File Location:
`.minecraft\config\magpiebridge`\
Not recommended to edit\
will reset to default when mod updates or downgrades, unless `keepConfigDir` is set to `true` in `magpiebridge.json`

>[!WARNING]\
>Setting `keepConfigDir` to `true` can cause serious issues when mod updates or downgrades.\
>**Please do not change the default value unless you know what you are doing.**

### Config Options:

| Key | Description | Range | Default | In-game Edit Command |
|-----|-------------|-------|---------|---------|
| modVersion | Current mod version | System | Current | Not Editable |
| keepConfigDir | Keep config directory when mod updates or downgrades | System | false | `magpiebridge config general keepConfigDir [Enable: Boolean]` |
| notificationsEnabled | Toggle all desktop notifications | All | true | `magpiebridge config general desktopNotifications [Enable: Boolean]` |
| playerNotifications | Toggle player chats | Hypixel Skyblock | true | `magpiebridge config notifications skyblock player [Enable: Boolean]` |
| bazaarNotifications | Toggle bazaar messages | Hypixel Skyblock | true | `magpiebridge config notifications skyblock bazaar [Enable: Boolean]` |
| auctionNotifications | Toggle auction messages | Hypixel Skyblock | true | `magpiebridge config notifications skyblock auctions [Enable: Boolean]` |

## 📃 Feeadbacks

Welcome to [📑Issue Page](https://github.com/wen-wen520/Minecraft.Mod-MagpieBridge/issues/new/choose) to submit any bugs or featuers.

## 📜 License

[All rights reserved.](LICENSE.txt)

## 🎉 Thanks & Related Resource

[Go Toast / Toast](https://github.com/go-toast/toast) used to send system notifications.\
Under the MIT License.
