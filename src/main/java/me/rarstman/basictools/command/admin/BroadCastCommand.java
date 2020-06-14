package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class BroadCastCommand extends Command {

    public BroadCastCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        ChatUtil.broadCastMessage(this.messages.getMessage("BroadCastFormat"), "{message}", StringUtils.join(args, " "));
    }
}
