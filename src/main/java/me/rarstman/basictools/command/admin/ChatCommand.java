package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.cache.ChatCache;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.BooleanUtil;
import me.rarstman.rarstapi.util.ChatUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final char[] bigMessage;
    private final ChatCache chatCache;

    public ChatCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).chatCommandData, "basictools.command.chat", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.chatCache = BasicToolsPlugin.getPlugin().getChatCache();
        this.bigMessage = new char[7680];
        Arrays.fill(this.bigMessage, ' ');
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final String permission = Arrays.asList("toggle", "on", "off", "clear").stream().anyMatch(args[0]::equalsIgnoreCase) ? this.permission + "." + args[0].toLowerCase() : null;

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final String reason = args.length > 1 ? StringUtils.join(args, " ", 1, args.length) : this.messages.chatActionDefaultReason;

        switch (args[0].toLowerCase()) {
            case "clear": {
                ChatUtil.broadCast(String.valueOf(this.bigMessage));
                this.messages.chatCleared.send(commandSender, "{REASON}", reason);
                this.messages.chatClearedInfo.broadCast(
                        "{NICK}", commandSender.getName(),
                        "{REASON}", reason
                );
                break;
            }
            default: {
                if(!BooleanUtil.isStringStatus(args[0])) {
                    this.rarstAPIMessages.turnOptionNotExist.send(commandSender);
                    return;
                }
                final boolean chatStatus = BooleanUtil.stringStatusToBoolean(args[0]);
                final String variableStatus = this.chatCache.isEnabled() ? this.rarstAPIMessages.on_ : this.rarstAPIMessages.off_;

                this.chatCache.setEnabled(chatStatus);
                this.messages.chatToggled.send(commandSender,
                        "{STATUS}", variableStatus,
                        "{REASON}", reason
                );
                this.messages.chatToggledInfo.broadCast(
                        "{NICK}", commandSender.getName(),
                        "{STATUS}", variableStatus,
                        "{REASON}", reason
                );
                break;
            }
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                final List<String> list = new ArrayList<>();

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".toggle")) {
                    list.addAll(Arrays.asList("on", "off"));
                }

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".clear")) {
                    list.add("clear");
                }
                return list;
            }
        }
        return new ArrayList<>();
    }

}
