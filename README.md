# Quarks Browser refactored ***org.lineageos.jelly*** for AOSP compilation
Jelly browser with ads blocker, a few ui changes and some bug fixes.
Ads blocker and favicon in search bar based on this: https://github.com/CarbonROM/android_packages_apps_Quarks

### Ads blocker:
 * https://pgl.yoyo.org/as/serverlist.php?hostformat=nohtml&showintro=0

### Offline reading:
 * .mht (chromium compatible)
 * /Android/data/__org.lineageos.jelly__/files/*.mht
 * ✇Favorites
 * screen Shortcuts
 
### external launches:
 * local xml/mht/html, for both \<content\> (X-plore) & \<file\> (aosp/Files or GhostCommander)
 * ShareLink

### More search engines:
 * Gibiru
 * SearX
 * StartPage

## AOSP compilation: ***packages/apps/Quarks/***
```
use branch -b jQuarksMore (org.lineageos.jelly)
```

```
etc/sysconfig/?.xml 
```
>__\<hidden-api-whitelisted-app package="org.lineageos.jelly"/\>__

prim-origin: https://github.com/LineageOS/android_packages_apps_Jelly
