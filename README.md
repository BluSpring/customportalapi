# Custom Portal Api
Library mod allowing developers to easily create portals to their custom dimensions. These custom portals will function exactly like nether portals except being fully customizable. You can control the frame block, portal block or tinting of the default, ignition source, and destination and more!

|![Some example of portals](https://raw.githubusercontent.com/kyrptonaught/customportalapi/main/images/2020-10-05_04.02.08.png)| ![](https://raw.githubusercontent.com/kyrptonaught/customportalapi/main/images/2020-11-11_15.01.14.png) |
|----------------------------|--|
|     ![p](https://raw.githubusercontent.com/kyrptonaught/customportalapi/main/images/2020-11-15_17.06.44.png)                       |![p](https://raw.githubusercontent.com/kyrptonaught/customportalapi/main/images/2020-11-15_17.07.38.png)  |

# Usage: 
## Versions: 
|        **1.16**        | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.16')%2B1)%3D'1.16'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5)     |
|:----------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|        **1.17**        | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.17')%2B1)%3D'1.17'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5)     |
|        **1.18**        | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.18')%2B1)%3D'1.18'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5)     |
| **1.19/1.19.1/1.19.2** | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.19.X')%2B1)%3D'1.19.X'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|       **1.19.3**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.19.3')%2B1)%3D'1.19.3'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|       **1.19.4**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.19.4')%2B1)%3D'1.19.4'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|    **1.20/1.20.1**     | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.20')%2B1)%3D'1.20'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5)     |
|       **1.20.2**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.20.2')%2B1)%3D'1.20.2'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|       **1.20.4**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.20.4')%2B1)%3D'1.20.4'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|       **1.20.5**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.20.5')%2B1)%3D'1.20.5'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|       **1.20.6**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.20.6')%2B1)%3D'1.20.6'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |
|    **1.21/1.21.1**     | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.21')%2B1)%3D'1.21'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5)     |
|       **1.21.4**       | ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fs3.us-east-2.amazonaws.com%2Fmaven.kyrptonaught.dev%2Fnet%2Fkyrptonaught%2Fcustomportalapi%2Fmaven-metadata.xml&query=%2F%2Fversion%5Bsubstring(node()%2Cstring-length(node())-string-length('1.21.4')%2B1)%3D'1.21.4'%5D%5Blast()%5D&style=flat-square&label=%20&color=4287f5) |

Add the repository to your build.gradle.
```java
repositories {
  // ... any other repositories in this block, as needed
  maven {url = "https://maven.kyrptonaught.dev"}
}
  ```
Add the dependency to your build.gradle.
```java
dependencies {
  // ... your other dependencies in this block, as needed
  modImplementation 'net.kyrptonaught:customportalapi:<version>'
  include 'net.kyrptonaught:customportalapi:<version>'
}
  ```

Now onto creating and registering the portal itself, this is very simple thanks to the [CustomPortalBuilder](https://github.com/kyrptonaught/customportalapi/blob/1.17/src/main/java/net/kyrptonaught/customportalapi/api/CustomPortalBuilder.java) class. We will make use of this in your mod initializer.

The following is a very simple portal that will take us to the end, and is lit by right clicking the frame with an Eye of Ender.
```java
CustomPortalBuilder.beginPortal()  
        .frameBlock(Blocks.DIAMOND_BLOCK)  
        .lightWithItem(Items.ENDER_EYE)  
        .destDimID(Identifier.of("the_end"))  
        .tintColor(45,65,101)  
        .registerPortal();
  ```

A nether portal would be registered as follows: 
```java
CustomPortalBuilder.beginPortal()  
        .frameBlock(Blocks.OBSIDIAN)  
        .destDimID(Identifier.of("the_nether"))  
        .tintColor(131, 66, 184)  
        .registerPortal();
  ```

CustomPortalBuilder is filled with plenty of methods to customize the functionality of your portal, all of which are documented in the class.

Some noteworthy methods to mention:

 - lightWithWater/Item/Fluid - These allow you to control how the portal is lit. 
 - onlyLightInOverworld - Only allow the portal to be used in the overworld to your destination of choice
 - flatPortal - Flat Portal similar to the End or the Twilight Forest portal.
