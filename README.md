# Android LAN Scanner App

A simple **LAN Scanner** Android app built in Java that scans your home network for devices and identifies open ports. Supports **multi-threaded scanning** and displays IPs, hostnames, and open ports in UI.

---

## Features

- Scans all devices on your Wi-Fi subnet.
- Detects **open ports** for each device (default: 22, 80, 139, 443, 445, 8009, 5353).
- Resolves hostnames for detected devices.
- Multi-threaded scanning for faster results.
- Safe handling of null data and UI updates.
- Compatible with Android 8.0+.

---

## Requirements

- Android Studio Bumblebee or later
- Java 8+
- Minimum SDK: 25 (Android 658.0)
- Wi-Fi connection for scanning devices

---

## Installation

1. Clone this repository:

```bash
git clone https://github.com/yourusername/android-lan-scanner.git
```
## Notes

Testing on an emulator is limited; use a real device for full LAN scanning.

Timeout per port: 500ms by default.

Thread pool size: 20 by default (adjust for faster or safer scanning).
