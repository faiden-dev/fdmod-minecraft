# FD Mod (Minecraft mod)

### Description

FD Mod is a personal Minecraft Forge mod for Minecraft 1.21.1 focused on experimenting with new ideas, items, and gameplay features. It serves as a simple sandbox project for testing and learning mod development in Minecraft.
### Requirements

* Minecraft 1.21.1
* Forge 52.1.0
* For /bTalk command (AI socket server): https://github.com/faiden-dev/NAI-neural_network/tree/fdmod

### Author

faiden-dev  
GitHub: https://github.com/faiden-dev  
TikTok: https://www.tiktok.com/@faiden_dev



# Mod items

### 67 Sword (sword_67)

- Strong and fast custom sword.

### Bot Spawn (bot_spawn)

- Spawns a bot (replacing /bSpawn command).



# Mod commands

### /bSpawn

- "class" - Spawns a bot at the player's position with the selected skin and behavior class.

### /bMode

- "class" - Selects bot group.
- "mode" - Sets bot behavior:
  - follow - Bot starts chasing the nearest player and follows them.
  - stop - Bot stops all movement and sends a chat message.
  - attack - Bot attacks the nearest mob.

### /bTalk

- "class" - Selects bot group.
- "message" - Sends a message to the chosen bot class, which communicates with the AI socket server and displays the response.

### "сlass" list

- oguzok, pony1, pony2, pony3.