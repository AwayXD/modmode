# ModMode Plugin for Minecraft 1.21+

A modern, comprehensive, and production-ready ModMode plugin designed for staff moderation on Minecraft servers.

## Features

- Toggleable Mod Mode (`/mod`)
- Freeze & Unfreeze players (`/freeze`, `/unfreeze`)
- Inventory inspection (`/invsee`)
- Smite rulebreakers (`/smite`)
- Check for alt accounts (`/checkalts`)
- Staff-only chat channel (`/sc`)
- Join/leave announcements
- Glow effect for staff
- Block drops/pickups/movement in frozen state
- Prevent item interactions while frozen
- Highly configurable & permission-based

## Commands

| Command             | Description                                  | Permission Node           |
|---------------------|----------------------------------------------|----------------------------|
| `/mod`              | Toggle Mod Mode                              | `modmode.toggle`           |
| `/freeze <player>`  | Freeze a player                              | `modmode.freeze`           |
| `/unfreeze <player>`| Unfreeze a player                            | `modmode.unfreeze`         |
| `/invsee <player>`  | View another player's inventory              | `modmode.invsee`           |
| `/smite <player>`   | Strike lightning and deal damage             | `modmode.smite`            |
| `/checkalts <ip>`   | Lookup alts by IP                            | `modmode.checkalts`        |
| `/sc <message>`     | Send message to staff chat                   | `modmode.staffchat`        |
| `/modmode reload`   | Reload config files                          | `modmode.reload`           |

## Listeners (Event Handling)

- `JoinListener` – triggers vanish/mod mode on join
- `PlayerConnectListener` – detect joins & alts
- `PlayerMoveListener` – block movement for frozen players
- `PlayerInteractListener` – block interaction in frozen state
- `PlayerDropPickupListener` – block item drops/pickups
- `ModModeListener` – cancel interactions while in mod mode
- `GlowEffectListener` – adds glowing effect to mod mode players
- `AnnouncerListener` – handles announcements on join/leave

## Permissions

| Node                    | Description                                    |
|-------------------------|------------------------------------------------|
| `modmode.toggle`        | Toggle mod mode                                |
| `modmode.freeze`        | Freeze players                                 |
| `modmode.unfreeze`      | Unfreeze players                               |
| `modmode.invsee`        | Inspect player inventories                     |
| `modmode.smite`         | Smite players                                  |
| `modmode.checkalts`     | Check for alt accounts                         |
| `modmode.staffchat`     | Use staff chat                                 |
| `modmode.reload`        | Reload config                                  |

## Configuration

Located in `config.yml`:
- Enable/disable features
- Customize tool names/lore
- Define glow effect colors
- Set freeze message, join announcements, etc.

## Developer Info

- Java Version: 17+
- API: Spigot / Paper 1.21
- Plugin Type: Staff Moderation Utility
- Plugin File: `ModModePlugin.java` 

## Installation

1. Download the latest `.jar` from Releases
2. Drop it into your server’s `/plugins` folder
3. Restart the server
4. Configure `config.yml` as needed
5. Use `/modmode reload` to apply changes

## Contributing

Bug reports, suggestions, and pull requests are welcome.

```bash
git clone https://github.com/AwayXD/modmode.git
cd modmode
```

## License

This project is open-sourced under the MIT License.

## Credits

Developed by [AwayXD](https://github.com/AwayXD)
Tested on Paper 1.21
Special thanks to the Minecraft moderation community.
