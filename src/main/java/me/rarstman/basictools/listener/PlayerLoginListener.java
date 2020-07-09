package me.rarstman.basictools.listener;

import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.configuration.impl.RarstAPIMessages;
import me.rarstman.rarstapi.listener.ListenerProvider;
import me.rarstman.rarstapi.util.DateUtil;
import me.rarstman.rarstapi.util.StringUtil;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener extends ListenerProvider {

    private final BasicToolsMessages messages;
    private final RarstAPIMessages rarstAPIMessages;

    public PlayerLoginListener() {
        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.rarstAPIMessages = ConfigManager.getConfig(RarstAPIMessages.class);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(final PlayerLoginEvent event) {
        if(event.getResult() != PlayerLoginEvent.Result.KICK_BANNED) {
            return;
        }
        final Player player = event.getPlayer();
        final BanEntry banEntry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(player.getName()) == null ? Bukkit.getBanList(BanList.Type.IP).getBanEntry(event.getAddress().getHostAddress()) : Bukkit.getBanList(BanList.Type.NAME).getBanEntry(player.getName());

        event.setKickMessage(StringUtil.replace(this.messages.banFormat,
                "{BLOCKER}", banEntry.getSource(),
                "{DATE}", banEntry.getExpiration() == null ? this.rarstAPIMessages.unknown : DateUtil.formatDateFromMills(banEntry.getExpiration().getTime()),
                "{TIME}", banEntry.getExpiration() == null ? this.messages.permanently : DateUtil.stringFromMills(banEntry.getExpiration().getTime() - banEntry.getCreated().getTime() + 2),
                "{REASON}", banEntry.getReason()
        ));
    }

}
