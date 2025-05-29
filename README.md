# HeadUtil

> This is a java library that makes retrieving a player's skin, cape, or head texture effortless while being fast and efficient.

### Features
- [x] Caffeine In-Memory Caching 
- [x] Redis Caching
- [x] Fully Asynchronous
- [x] Stores all player textures
- [x] Resource Efficient

### Installation
<details>
<summary>Gradle (Kotlin)</summary>

```kts
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Summiner:HeadUtil:1.0.0")
}
```
</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Summiner:HeadUtil:1.0.0'
}
```
</details>

<details>
<summary>Maven</summary>

```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.Summiner</groupId>
  <artifactId>HeadUtil</artifactId>
  <version>1.0.0</version>
</dependency>
```
</details>

### Usage

<details>
<summary>Java Example</summary>

```java
HeadUtil headUtil = new HeadUtil.Builder()
        .enableCapes(true)
        .setCacheType(HeadUtil.SkinCacheType.CAFFEINE)
        .build();

UUID uuid = UUID.fromString("0bfea1b6-8ed9-4eb9-93da-c4273bfa0a09");

headUtil.getPlayerTextures(uuid).thenAcceptAsync((profile) -> {
        System.out.println("Skin: "+profile.skin());
        System.out.println("Head: "+profile.head());
        System.out.println("Cape: "+profile.cape());
}).join();
```
</details>