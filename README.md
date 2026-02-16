# CurseByte-RP-Jobs
<p align="center">
  <img alt="@cursebyte" style="width: 150px;" src="https://github.com/user-attachments/assets/fbfa6853-2cb5-4213-a1bd-98520f14cd22">
</p>
<div align="center">
  <h3>CurseByteRP Jobs</h3>
  <p>A Minecraft plugin that adds RolePlay jobs for Cursebyte servers, allowing players to choose careers</p>
  <img src="https://img.shields.io/badge/Java-17%2B-orange" alt="Java" />
  <img src="https://img.shields.io/badge/Platform-PaperMC-blue" alt="Platform" />
  <img src="https://img.shields.io/badge/Status-Development-green" alt="Status" />
</div>
<div align="center">
  <a href="https://dsc.gg/DrelezTM">Report Bug</a> | <a href="https://github.com/DrelezTM/CurseByte-RP-Jobs/issues">Issues</a> | <a href="https://www.cursebyte.my.id/">Website</a>
</div>

## Main Features ✨
- **🎮 RolePlay Jobs System**: Adds job options for players, allowing them to choose careers within the server's RolePlay world.
- **🧑‍💼 Career Selection**: Players can select or switch between available jobs.
- **⚙️ Easy Configuration**: All jobs and rewards can be customized through configuration files.
- **🔌 Economy Integration**: Integrates with the economy in the [CurseByte RP Core](https://github.com/fzrilsh/cursebyte-rp-core) plugins.

## Installation & Setup ⚙️
1. Download the latest `.jar` file from the Releases section or [here](https://github.com/DrelezTM/CurseByte-RP-Jobs/blob/main/target/cursebytejobs-1.0.jar).
2. Place it in the `plugins/` folder on your server.
3. Restart the server.
4. Complete the in-game setup using `/jobs`.

## User Interface
<img alt="image" src="https://github.com/user-attachments/assets/b4dd5fc1-b821-4b61-b6c8-e9c7fbd05913" />
<img alt="image" src="https://github.com/user-attachments/assets/d51d5fd2-53e9-42d9-abb2-63e206f94483" />



## Build Instructions (For Developers) 🏗️
This project uses **Maven**. However, since it directly accesses NMS (Net Minecraft Server), you need to provide the server file manually.
1. Clone this repository.
2. Obtain the `paper-1.21.4-mojang-mapped.jar` file (generated from a Paper server).
3. Edit `pom.xml` and adjust the `systemPath` in the dependency section:
    ```xml
    <dependency>
        <groupId>io.papermc.paper</groupId>
        <artifactId>paper-server-manual</artifactId>
        <version>1.21.4</version>
        <scope>system</scope>
        <systemPath>C:/Path/To/Your/paper-mojang-mapped.jar</systemPath>
    </dependency>
    ```
4. Run the build command:
    ```bash
    mvn clean package
    ```

## Configurations (`config.yml`) 📂
```yaml
jobs:
  fisherman:
    display-name: "🐟 Nelayan"
    prices:
      COD: 4.0
      SALMON: 6.0
      TROPICAL_FISH: 20.0
      PUFFERFISH: 35.0
      NAUTILUS_SHELL: 350.0

  farmer:
    display-name: "🌱 Petani"
    prices:
      WHEAT: 0.8
      POTATO: 1.5
      CARROT: 1.5
      SUGAR_CANE: 2.0
      MELON_SLICE: 1.0
      PUMPKIN: 4.0
      NETHER_WART: 12.0

  miner:
    display-name: "⛏ Penambang"
    prices:
      COBBLESTONE: 0.2
      COAL: 4.0
      RAW_IRON: 8.0
      RAW_COPPER: 4.0
      RAW_GOLD: 18.0
      REDSTONE: 6.0
      LAPIS_LAZULI: 7.0
      DIAMOND: 120.0
      EMERALD: 140.0
      ANCIENT_DEBRIS: 1200.0

  lumberjack:
    display-name: "🪓 Penebang Kayu"
    prices:
      OAK_LOG: 1.5
      SPRUCE_LOG: 1.5
      BIRCH_LOG: 1.5
      JUNGLE_LOG: 1.5
      ACACIA_LOG: 1.5
      DARK_OAK_LOG: 1.5
      MANGROVE_LOG: 1.5
      CHERRY_LOG: 1.8
      PALE_OAK_LOG: 1.8

  hunter:
    display-name: "🏹 Pemburu"
    prices:
      ROTTEN_FLESH: 0.1
      BONE: 4.0
      STRING: 4.0
      SPIDER_EYE: 4.0
      GUNPOWDER: 9.0
      ENDER_PEARL: 45.0
      BLAZE_ROD: 90.0
      WITHER_SKELETON_SKULL: 7000.0

  smelter:
    display-name: "🔥 Smelter"
    prices:
      IRON_INGOT: 14.0
      COPPER_INGOT: 7.0
      GOLD_INGOT: 28.0
      NETHERITE_SCRAP: 650.0
      GLASS: 1.5
      SMOOTH_STONE: 0.8

  breeder:
    display-name: "🐄 Peternak"
    prices:
      BEEF: 5.0
      PORKCHOP: 5.0
      MUTTON: 5.0
      CHICKEN: 4.0
      RABBIT: 7.0
      MILK_BUCKET: 12.0
      EGG: 2.0
      LEATHER: 9.0
      FEATHER: 3.0

  cook:
    display-name: "🍳 Pemasak"
    prices:
      BREAD: 3.5
      BAKED_POTATO: 4.0
      COOKED_BEEF: 9.0
      COOKED_PORKCHOP: 9.0
      COOKED_CHICKEN: 7.0
      COOKED_MUTTON: 8.0
      COOKED_RABBIT: 11.0
      PUMPKIN_PIE: 18.0
      CAKE: 45.0

leave:
  price: 1000.0
  cooldown-seconds: 0
```

## Error or Bug 🐞
* [Discord](https://dsc.gg/DrelezTM)
* [YouTube](https://www.youtube.com/p/DrelezTM)
* [Instagram](https://www.instagram.com/ziids)
* [Issues](https://github.com/DrelezTM/CurseByte-RP-Jobs/issues)

## License 📜
This project uses a private license for **Cursebyte Network**. It is not for commercial distribution without permission.