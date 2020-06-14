package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.cache.ChatCache;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.BooleanUtil;
import me.rarstman.basictools.util.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatCommand extends Command {

    private final char[] bigMessage;
    private final ChatCache chatCache;

    public ChatCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);

        bigMessage = new char[7680];
        Arrays.fill(bigMessage, ' ');
        this.chatCache = BasicToolsPlugin.getPlugin().getChatCache();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final String permission = Arrays.asList("toggle", "on", "off", "clear").stream().filter(args[0]::equalsIgnoreCase).findAny().isPresent() ? this.permission + "." + args[0].toLowerCase() : null;

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final String reason = args.length > 1 ? StringUtils.join(args, " ", 1, args.length) : this.messages.getMessage("Lack");

        switch (args[0].toLowerCase()) {
            case "clear": {
                ChatUtil.broadCastMessage(this.bigMessage.toString());
                ChatUtil.broadCastMessage(this.messages.getMessage("ChatClearedInfo"),
                        "{nick}", commandSender.getName(),
                        "{reason}", reason
                );
                break;
            }
            default: {
                final Boolean chatStatus = BooleanUtil.stringStatusToBoolean(args[0]);

                if (chatStatus == null) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("TurnOptionNotExist"));
                    return;
                }
                this.chatCache.setEnabled(chatStatus);
                ChatUtil.broadCastMessage(this.messages.getMessage("ChatToggledInfo"),
                        "{nick}", commandSender.getName(),
                        "{status}", this.chatCache.isEnabled() ? this.messages.getMessage("On") : this.messages.getMessage("Off"),
                        "{reason}", reason
                );
                break;
            }
        }
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".toggle") ? Arrays.asList("toggle", "on", "off").stream().collect(Collectors.joining()) : null, this.vaultHook.hasPermission(commandSender, this.permission + ".clear") ? "clear" : null);
            }
        }
        return ImmutableList.of();
    }
}
