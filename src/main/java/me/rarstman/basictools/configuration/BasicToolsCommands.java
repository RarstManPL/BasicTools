package me.rarstman.basictools.configuration;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.command.CommandIcon;
import me.rarstman.basictools.configuration.customparserimpl.CommandIconParser;
import me.rarstman.rarstapi.command.CommandData;
import me.rarstman.rarstapi.configuration.ConfigProvider;
import me.rarstman.rarstapi.configuration.annotation.ConfigName;
import me.rarstman.rarstapi.configuration.annotation.ParseValue;
import me.rarstman.rarstapi.configuration.customparser.impl.CommandDataParser;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@ParseValue(CommandDataParser.class)
public class BasicToolsCommands extends ConfigProvider {

    @ConfigName("AdminChatCommand")
    public CommandData adminChatCommandData = new CommandData("adminchat", Arrays.asList("ac"), "Czat dla administracji", "/adminchat <wiadomość>", true);

    @ConfigName("AdminChatCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon adminChatCommandIcon = new CommandIcon(Material.BOOK, "a", true);

    @ConfigName("BanCommand")
    public CommandData banCommandData = new CommandData("ban", new ArrayList<>(), "Blokowanie graczy/ip", "/ban <nick/ip> <czas> [powód] [-s]", true);

    @ConfigName("BanCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon banCommandIcon = new CommandIcon(Material.DIAMOND_SWORD, "b", true);

    @ConfigName("BroadCastCommand")
    public CommandData broadCastCommandData = new CommandData("broadcast", Arrays.asList("bc"), "Tekstowe ogłoszenia na czacie", "/broadcast <wiadomość>", true);

    @ConfigName("BroadCastCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon broadCastCommandIcon = new CommandIcon(Material.OAK_SIGN, "c", true);

    @ConfigName("ChatCommand")
    public CommandData chatCommandData = new CommandData("chat", new ArrayList<>(), "Zarządzanie czatem", "/chat <on/off/clear> [powód]", true);

    @ConfigName("ChatCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon chatCommandIcon = new CommandIcon(Material.BONE, "d", true);

    @ConfigName("ClearInventoryCommand")
    public CommandData clearInventoryCommandData = new CommandData("clearinventory", Arrays.asList("ci", "clear"), "Czyszczenie ekwipunku", "/clearinventory <inventory/enderchest> [nick]", true);

    @ConfigName("ClearInventoryCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon clearInventoryCommandIcon = new CommandIcon(Material.LAVA_BUCKET, "e", true);

    @ConfigName("FlyCommand")
    public CommandData flyCommandData = new CommandData("fly", new ArrayList<>(), "Zmiana trybu latania", "/fly [on/off] [nick]", true);

    @ConfigName("FlyCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon flyCommandIcon = new CommandIcon(Material.ENDER_PEARL, "f", true);

    @ConfigName("GameModeCommand")
    public CommandData gameModeCommandData = new CommandData("gamemode", Arrays.asList("gm"), "Zmiana trybu gry", "/gamemode <survival/creative/adventure/spectator> [nick]", true);

    @ConfigName("GameModeCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon gameModeCommandIcon = new CommandIcon(Material.REDSTONE, "g", true);

    @ConfigName("GiveCommand")
    public CommandData giveCommandData = new CommandData("give", new ArrayList<>(), "Dawanie przedmiotów", "/give <nick> <przedmiot[:data]> [ilość] [data...]", true);

    @ConfigName("GiveCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon giveCommandIcon = new CommandIcon(Material.EMERALD_BLOCK, "h", true);

    @ConfigName("GodCommand")
    public CommandData godCommandData = new CommandData("god", new ArrayList<>(), "Zmiana trybu boskości", "/god [on/off] [nick]", true);

    @ConfigName("GodCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon godCommandIcon = new CommandIcon(Material.TOTEM_OF_UNDYING, "i", true);

    @ConfigName("InfoCommand")
    public CommandData infoCommandData = new CommandData("info", Arrays.asList("seen", "whois"), "Informacje nt. gracza/ip", "/info <nick/ip>", true);

    @ConfigName("InfoCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon infoCommandIcon = new CommandIcon(Material.PAPER, "j", true);

    @ConfigName("InventorySeeCommand")
    public CommandData inventorySeeCommandData = new CommandData("inventorysee", Arrays.asList("invsee"), "Podglądanie ekwipunku", "/inventorysee <inventory/enderchest> [nick]", true);

    @ConfigName("InventorySeeCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon inventorySeeCommandIcon = new CommandIcon(Material.ENDER_CHEST, "k", true);

    @ConfigName("KickCommand")
    public CommandData kickCommandData = new CommandData("kick", new ArrayList<>(), "Wyrzucanie graczy z serwera", "/kick <nick/*> [powód] [-s]", true);

    @ConfigName("KickCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon kickCommandIcon = new CommandIcon(Material.ARROW, "l", true);

    @ConfigName("MuteCommand")
    public CommandData muteCommandData = new CommandData("mute", new ArrayList<>(), "Wyciszanie graczy na serwerze", "/mute <nick> <czas> [powód] [-s]", true);

    @ConfigName("MuteCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon muteCommandIcon = new CommandIcon(Material.POTION, "m", true);

    @ConfigName("SocialSpyCommand")
    public CommandData socialSpyCommandData = new CommandData("socialspy", new ArrayList<>(), "Zmiana trybu szpiega prywatnych wiadomości", "/socialspy [on/off] [nick]", true);

    @ConfigName("SocialSpyCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon socialSpyCommandIcon = new CommandIcon(Material.GLASS, "n", true);

    @ConfigName("TeleportAllCommand")
    public CommandData teleportAllCommandData = new CommandData("teleportall", Arrays.asList("tpall"), "Teleportowanie wszystkich graczy do siebie", "/teleportall", true);

    @ConfigName("TeleportAllCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon teleportAllCommandIcon = new CommandIcon(Material.END_ROD, "o", true);

    @ConfigName("TeleportCommand")
    public CommandData teleportCommandData = new CommandData("teleport", Arrays.asList("tp"), "Teleportacja na serwerze", "/tp <nick/x> [nick/y] [z] [nick]", true);

    @ConfigName("TeleportCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon teleportCommandIcon = new CommandIcon(Material.END_PORTAL_FRAME, "p", true);

    @ConfigName("TimeCommand")
    public CommandData timeCommandData = new CommandData("time", new ArrayList<>(), "Ustawianie czasu na serwerze", "/time <czas> [świat/*]", true);

    @ConfigName("TimeCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon timeCommandIcon = new CommandIcon(Material.CLOCK, "r", true);

    @ConfigName("UnBanCommand")
    public CommandData unBanCommandData = new CommandData("unban", new ArrayList<>(), "Odblokowywanie gracza/ip na serwerze", "/unban <nick/ip>", true);

    @ConfigName("UnBanCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon unBanCommandIcon = new CommandIcon(Material.WOODEN_AXE, "s", true);

    @ConfigName("UnMuteCommand")
    public CommandData unMuteCommandData = new CommandData("unmute", new ArrayList<>(), "Odciszanie gracza na serwerze", "/unmute <nick>", true);

    @ConfigName("UnMuteCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon unMuteCommandIcon = new CommandIcon(Material.STRING, "t", true);

    @ConfigName("VanishCommand")
    public CommandData vanishCommandData = new CommandData("vanish", Arrays.asList("v"), "Zmiana trybu niewidzialności", "/vanish [on/off] [nick]", true);

    @ConfigName("VanishCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon vanishCommandIcon = new CommandIcon(Material.SPLASH_POTION, "u", true);

    @ConfigName("WeatherCommand")
    public CommandData weatherCommandData = new CommandData("weather", new ArrayList<>(), "Zmiana pogody na serwerze", "/weather <pogoda> [świat/*]", true);

    @ConfigName("WeatherCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon weatherCommandIcon = new CommandIcon(Material.WATER_BUCKET, "w", true);

    @ConfigName("ListCommand")
    public CommandData listCommandData = new CommandData("list", new ArrayList<>(), "Lista graczy online na serwerze", "/list", true);

    @ConfigName("ListCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon listCommandIcon = new CommandIcon(Material.PLAYER_HEAD, "1", true);

    @ConfigName("MessageCommand")
    public CommandData messageCommandData = new CommandData("message", Arrays.asList("msg"), "Prywatne wiadomości", "/message <nick> <wiadomość>", true);

    @ConfigName("MessageCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon messageCommandIcon = new CommandIcon(Material.FEATHER, "2", true);

    @ConfigName("ReplyCommand")
    public CommandData replyCommandData = new CommandData("reply", Arrays.asList("r"), "Odpowiadanie ostatniemu piszącemu", "/reply <wiadomość>", true);

    @ConfigName("ReplyCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon replyCommandIcon = new CommandIcon(Material.STICK, "3", true);

    @ConfigName("ReportCommand")
    public CommandData reportCommandData = new CommandData("report", Arrays.asList("helpop"), "Wysyłanie zgłoszenia administracji online", "/report <wiadomość>", true);

    @ConfigName("ReportCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon reportCommandIcon = new CommandIcon(Material.BOW, "4", true);

    @ConfigName("HelpCommand")
    public CommandData helpCommandData = new CommandData("help", Arrays.asList("pomoc"), "Pomoc serwerowa", "/help", true);

    @ConfigName("HelpCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon helpCommandIcon = new CommandIcon(Material.ENCHANTED_BOOK, "5", true);

    @ConfigName("MessageToggleCommand")
    public CommandData messageToggleCommandData = new CommandData("messagetoggle", Arrays.asList("msgtoggle"), "Zmiana statusu przyjmowania wiadomości prywatnych", "/messagetoggle [on/off] [nick]", true);

    @ConfigName("MessageToggleCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon messageToggleCommandIcon = new CommandIcon(Material.BARRIER, "6", true);

    @ConfigName("PanelCommand")
    public CommandData panelCommandData = new CommandData("panel", Arrays.asList("admin"), "Panel poleceń", "/panel", true);

    @ConfigName("PanelCommand")
    @ParseValue(CommandIconParser.class)
    public CommandIcon panelCommandIcon = new CommandIcon(Material.COMMAND_BLOCK, "7", false);

    @ConfigName("BasicToolsCommand")
    @ParseValue(CommandDataParser.class)
    public CommandData basicToolsCommandData = new CommandData("basictools", Arrays.asList("bt"), "Przeładowuje konfiguracje", "/basictools", true);

    public BasicToolsCommands() {
        super(new File(BasicToolsPlugin.getPlugin().getDataFolder(), "commands.yml"), BasicToolsPlugin.getPlugin().getResource("commands.yml"));
    }

}
