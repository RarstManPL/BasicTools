package me.rarstman.basictools.configuration;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.rarstapi.configuration.ConfigProvider;
import me.rarstman.rarstapi.configuration.annotation.ConfigName;
import me.rarstman.rarstapi.configuration.annotation.ParseDisabler;
import me.rarstman.rarstapi.configuration.annotation.ParseValue;
import me.rarstman.rarstapi.configuration.customparser.impl.MessageParser;
import me.rarstman.rarstapi.message.Message;
import me.rarstman.rarstapi.message.impl.ChatMessage;

import java.io.File;

@ParseValue(MessageParser.class)
@MessageParser.ParseMessageData
public class BasicToolsMessages extends ConfigProvider {

    @ConfigName("WorldNotExistOrConsole")
    public Message worldNotExistOrConsole = new ChatMessage("Podany świat nie istnieje lub jesteś konsolą i próbujesz wykonać komendę bez podania nazwy świata.");

    @ConfigName("TimeIsZero")
    public Message timeIsZero = new ChatMessage("Podany czas po przetworzeniu wynosi 0. Sprawdź składnię.");

    @ConfigName("IpNotExist")
    public Message ipNotExist = new ChatMessage("Podany adres ip nie istnieje!");

    @ConfigName("Creative")
    @ParseDisabler
    public String creative = "kreatywny";

    @ConfigName("Survival")
    @ParseDisabler
    public String survival = "przetrwania";

    @ConfigName("Adventure")
    @ParseDisabler
    public String adventure = "przygodowy";

    @ConfigName("Spectator")
    @ParseDisabler
    public String spectator = "obserwacyjny";

    @ConfigName("Permanently")
    @ParseDisabler
    public String permanently = "na zawsze";

    @ConfigName("ReportSent")
    public Message reportSent = new ChatMessage("Raport został wysłany!\nTreść: {MESSAGE}");

    @ConfigName("ReportFormat")
    public Message reportFormat = new ChatMessage("[Raport] {NICK} > {MESSAGE}");

    @ConfigName("WeatherNotExist")
    public Message weatherNotExist = new ChatMessage("Podany typ pogody nie istnieje!");

    @ConfigName("WeatherChanged")
    public Message weatherChanged = new ChatMessage("Pogoda została zmieniona dla {WORLDS} na {WEATHER}.");

    @ConfigName("Thunder")
    @ParseDisabler
    public String thunder = "burza";

    @ConfigName("Storm")
    @ParseDisabler
    public String storm = "burza";

    @ConfigName("Rain")
    @ParseDisabler
    public String rain = "deszcz";

    @ConfigName("Clear")
    @ParseDisabler
    public String clear = "przejrzysta";

    @ConfigName("Sun")
    @ParseDisabler
    public String sun = "słoneczna";

    @ConfigName("VanishStatusChanged")
    public Message vanishStatusChanged = new ChatMessage("Status niewidzialności został dla {NICK} zmieniony na {STATUS}.");

    @ConfigName("VanishStatusChangedInfo")
    public Message vanishStatusChangedInfo = new ChatMessage("Twój status niewidzialności został zmieniony na {STATUS}.");

    @ConfigName("NotMuted")
    public Message notMuted = new ChatMessage("Podany gracz nie jest wyciszony!");

    @ConfigName("UnMuted")
    public Message unMuted = new ChatMessage("Odciszyłeś gracza!");

    @ConfigName("UnMutedInfo")
    public Message unMutedInfo = new ChatMessage("Zostałeś odciszony!");

    @ConfigName("NotBanned")
    public Message notBanned = new ChatMessage("Podany gracz lub IP nie jest zablokowane!");

    @ConfigName("UnBanned")
    public Message unBanned = new ChatMessage("Odblokowałeś podane IP lub podanego gracza!");

    @ConfigName("TimeNotExist")
    public Message timeNotExist = new ChatMessage("Podany czas nie istnieje!");

    @ConfigName("TimeChanged")
    public Message timeChanged = new ChatMessage("Czas został zmieniony dla {WORLDS} na {TIME}.");

    @ConfigName("LocalizationNotExist")
    public Message localizationNotExist = new ChatMessage("Podana lokalizacja nie istnieje lub nie mogła zostać pobrana!");

    @ConfigName("CoordsNotNumber")
    public Message coordsNotNumber = new ChatMessage("Podane koordynaty nie są liczbami.");

    @ConfigName("Teleported")
    public Message teleported = new ChatMessage("Przeniosłeś gracza {NICK} na lokalizację {LOCALIZATION} w świecie {WORLD}.");

    @ConfigName("TeleportedInfo")
    public Message teleportedInfo = new ChatMessage("Zostałeś przeniesiony na lokalizację {LOCALIZATION} w świecie {WORLD}.");

    @ConfigName("TeleportedAll")
    public Message teleportedAll = new ChatMessage("Przenisłeś wszystkich graczy do siebie!");

    @ConfigName("TeleportedAllInfo")
    public Message teleportedAllInfo = new ChatMessage("Zostałeś przeniesiony na lokalizację {LOCALIZATION} w świecie {WORLD}!");

    @ConfigName("SocialSpyStatusChanged")
    public Message socialSpyStatusChanged = new ChatMessage("Statuś śledzenia prywatnych wiadomości został dla {NICK} zmieniony na {STATUS}.");

    @ConfigName("SocialSpyStatusChangedInfo")
    public Message socialSpyStatusChangedInfo = new ChatMessage("Twój status śledzenia prywatnych wiadomości został zmieniony na {STATUS}.");

    @ConfigName("GodStatusChanged")
    public Message godStatusChanged = new ChatMessage("Status boskości dla {NICK} został zmieniony na {STATUS}.");

    @ConfigName("GodStatusChangedInfo")
    public Message godStatusChangedInfo = new ChatMessage("Twój status boskości został zmieniony na {STATUS}.");

    @ConfigName("FlyStatusChanged")
    public Message flyStatusChanged = new ChatMessage("Status latania został dla {NICK} zmieniony na {STATUS}.");

    @ConfigName("FlyStatusChangedInfo")
    public Message flyStatusChangedInfo = new ChatMessage("Twój status latania został zmieniony na {STATUS}.");

    @ConfigName("IsMuted")
    public Message isMuted = new ChatMessage("Podany gracz jest już wyciszony.");

    @ConfigName("Muted")
    public Message muted = new ChatMessage("Wyciszyłeś gracza {MUTED} na {TIME} (do {DATE}) z powodem {REASON}.");

    @ConfigName("MutedInfo")
    public Message mutedInfo = new ChatMessage("Zostałeś wyciszony na {TIME} (do {DATE}) z powodem {REASON} przez {MUTER}.");

    @ConfigName("MutedBroadCastInfo")
    public Message mutedBroadCastInfo = new ChatMessage("Gracz {MUTED} został wyciszony przez {MUTER} z powodem {REASON} na {TIME} (do {DATE}).");

    @ConfigName("DefaultMuteReason")
    @ParseDisabler
    public String defaultMuteReason = "Admin ma zawsze rację!";

    @ConfigName("Kicked")
    public Message kicked = new ChatMessage("Wyrzuciłeś {KICKED} z serwera z powodem {REASON}.");

    @ConfigName("KickedBroadCastInfo")
    public Message kickedBroadCastInfo = new ChatMessage("Administrator {KICKER} wyrzucił {KICKED} z serwera z powodem {REASON}.");

    @ConfigName("KickedAll")
    public Message kickedAll = new ChatMessage("Wyrzuciłeś wszystkich graczy z serwera z powodem {REASON}.");

    @ConfigName("KickedAllBroadCastInfo")
    public Message kickedAllBroadCastInfo = new ChatMessage("{KICKER} wyrzucił wszystkich graczy z powodem {REASON}.");

    @ConfigName("KickFormat")
    @ParseDisabler
    public String kickFormat = "{REASON} - {KICKER}";

    @ConfigName("DefaultKickReason")
    @ParseDisabler
    public String defaultKickReason = "Admin ma zawsze rację!";

    @ConfigName("InventoryNotExist")
    public Message inventoryNotExist = new ChatMessage("Podany typ ekwipunku nie istnieje.");

    @ConfigName("InventoryOpened")
    public Message inventoryOpened = new ChatMessage("Otwarłeś {INVENTORY} gracza {NICK}.");

    @ConfigName("EnderChest")
    @ParseDisabler
    public String enderChest = "skrzynię kresu";

    @ConfigName("Inventory")
    @ParseDisabler
    public String inventory = "ekwipunek";

    @ConfigName("IpInfo")
    public Message ipInfo = new ChatMessage("Użytkownicy: {USERS}\nZablokowane: {ISBANNED}\nPowód bana: {BANREASON}\nData końca bana: {BANREMAINING}");

    @ConfigName("PlayerInfo")
    public Message playerInfo = new ChatMessage("Nick: {NICK}\nUUID: {UUID}\nPierwsze IP: {FIRSTIP}\nOstatnie IP: {LASTIP}\nPierwsza gra: {FIRSTPLAY}\nOstatnia gra: {LASTPLAY}\nZablokowany: {ISBANNED}\nPowód bana: {BANREASON}\nData końca bana: {BANREMAINING}\nWyciszony: {ISMUTED}\nPowód wyciszenia: {MUTEREASON}\nData końca wyciszenia: {MUTEREMAINING}\nŻycie: {HEALTHLEVEL}\nNajedzenie: {FOODLEVEL}\nLevel: {EXPLEVEL}\nLata: {ISFLYING}\nSpi: {ISLEEPING}\nOnline: {ISONLINE}\nNiewidzialny: {ISVANISHED}\nBoski: {ISGODED}\nSledzi: {ISSOCIALSPYING}\nSkrada się: {ISSNEAKING}\nSwiat: {WORLD}\nLokalizacja: {LOCALIZATION}\nTryb gry: {GAMEMODE}\nBiega: {ISSPRINTING}\nPływa: {ISSWIMMING}\nOp: {ISOP}\nDodany do białej listy: {ISWHITELISTED}");

    @ConfigName("Gave")
    public Message gave = new ChatMessage("Dałeś {AMOUNT}x {ITEM} do ekwipunku gracza {NICK}.");

    @ConfigName("GaveInfo")
    public Message gaveInfo = new ChatMessage("Otrzymałeś {AMOUNT}x {ITEM} do ekwipunku.");

    @ConfigName("GameModeNotExist")
    public Message gameModeNotExist = new ChatMessage("Podany tryb gry nie istnieje!");

    @ConfigName("GameModeChanged")
    public Message gameModeChanged = new ChatMessage("Zmieniłeś tryb gracza {NICK} na {GAMEMODE}.");

    @ConfigName("GameModeChangedInfo")
    public Message gameModeChangedInfo = new ChatMessage("Twój tryb gry ozstał zmieniony na {GAMEMODE}.");

    @ConfigName("InventoryCleared")
    public Message inventoryCleared = new ChatMessage("Wyczyściłeś {INVENTORY} gracza {NICK}.");

    @ConfigName("InventoryClearedInfo")
    public Message inventoryClearedInfo = new ChatMessage("Twój {INVENTORY} został wyczyszczony!");

    @ConfigName("BroadCastFormat")
    public Message broadCastFormat = new ChatMessage("[Ogłoszenie] {MESSAGE}");

    @ConfigName("AdminChatFormat")
    public Message adminChatFormat = new ChatMessage("[AdminChat] {NICK} > {MESSAGE}");

    @ConfigName("IsBanned")
    public Message isBanned = new ChatMessage("Ten gracz lub to ip jest już zablokowane.");

    @ConfigName("NoUsers")
    public Message noUsers = new ChatMessage("Brak użytkowników do zablokowania.");

    @ConfigName("BanFormat")
    @ParseDisabler
    public String banFormat = "{REASON} - {DATE} - {TIME} - {BLOCKER}";

    @ConfigName("Banned")
    public Message banned = new ChatMessage("Zablokowałeś {BLOCKED} na serwerze na {TIME} (do {DATE}) z powodem {REASON}.");

    @ConfigName("BannedBroadCastInfo")
    public Message bannedBroadCastInfo = new ChatMessage("Administrator {BLOCKER} zablokował gracza/ip {BLOCKED} na serwerze z powodem {REASON} do {DATE} (na {TIME}).");

    @ConfigName("DefaultBanReason")
    @ParseDisabler
    public String defaultBanReason = "Admin ma zawsze rację!";

    @ConfigName("ChatToggled")
    public Message chatToggled = new ChatMessage("Zmieniłeś status czatu na {STATUS} z powodem {REASON}.");

    @ConfigName("ChatCleared")
    public Message chatCleared = new ChatMessage("Wyczyściłeś czat z powodem {REASON}.");

    @ConfigName("ChatToggledInfo")
    public Message chatToggledInfo = new ChatMessage("Status czatu został zmieniony na {STATUS} z powodem {REASON} przez {NICK}.");

    @ConfigName("ChatClearedInfo")
    public Message chatClearedInfo = new ChatMessage("Czat zosatał wyczyszczony przez {NICK} z powodem {REASON}.");

    @ConfigName("ChatActionDefaultReason")
    @ParseDisabler
    public String chatActionDefaultReason = "Admin ma zawsze rację!";

    @ConfigName("BypassPermission")
    public Message bypassPermission = new ChatMessage("Ten gracz jest nie do ruszenia!");

    @ConfigName("ChatIsDisabled")
    public Message chatIsDisabled = new ChatMessage("Czat jest aktualnie wyłączony. Nie możesz na nim pisać!");

    @ConfigName("YouAreMuted")
    public Message youAreMuted = new ChatMessage("Jesteś wyciszony i nie możesz pisać na czacie (powód: {REASON}, do: {DATE}).");

    @ConfigName("HelpMessage")
    public Message helpMessage = new ChatMessage("Przykładowy tekst pomocy\nMożesz go zmienić w pliku messages.yml");

    @ConfigName("MessageFormat")
    public Message messageFormat = new ChatMessage("[{NICK1} {ARROW} {NICK2}] {MESSAGE}");

    @ConfigName("ArrowRight")
    @ParseDisabler
    public String arrowRight = ">";

    @ConfigName("ArrowLeft")
    @ParseDisabler
    public String arrowLeft = "<";

    @ConfigName("PlayerDontReciveMessages")
    public Message playerDontReciveMessages = new ChatMessage("Ten gracz nie odbiera wiadomości prywatnych.");

    @ConfigName("ReplyUserNotExist")
    public Message replyUserNotExist = new ChatMessage("Nie masz do kogo odpisać...");

    @ConfigName("BlockMessagesStatusChanged")
    public Message blockMessagesStatusChanged = new ChatMessage("Status odbieania wiadomości dla {NICK} zmieniony na {STATUS}.");

    @ConfigName("BlockMessagesStatusChangedInfo")
    public Message blockMessagesStatusChangedInfo = new ChatMessage("Twój status odbierania wiadomości został zmieniony na {STATUS}.");

    @ConfigName("CantOpenPanelInventory")
    public Message cantOpenPanelInventory = new ChatMessage("Panel nie mógł zostać otworzony...");

    @ConfigName("ListFormat")
    public Message listFormat = new ChatMessage("Online: {ONLINE}\nGrupy:\n{GROUPSONLINE}");

    @ConfigName("GroupsOnlineFormat")
    @ParseDisabler
    public String groupsOnlineFormat = "{GROUP}: {PLAYERS}";

    @ConfigName("SocialSpyFormat")
    public Message socialSpyFormat = new ChatMessage("&7{SENDER} > {RECIVER}: {MESSAGE}");

    public BasicToolsMessages() {
        super(new File(BasicToolsPlugin.getPlugin().getDataFolder(), "messages.yml"), BasicToolsPlugin.getPlugin().getResource("messages.yml"));
    }

}
