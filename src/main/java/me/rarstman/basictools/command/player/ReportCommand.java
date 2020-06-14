package me.rarstman.basictools.command.player;

import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class ReportCommand extends Command {

    public ReportCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }

        if (!this.vaultHook.hasPermission(commandSender, this.permission + ".send")) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", this.permission + ".send");
            return;
        }
        final String message = StringUtils.join(args, " ");

        ChatUtil.sendMessage(commandSender, this.messages.getMessage("RaportSent"), "{message}", message);
        ChatUtil.broadCastPermissionMessage(this.permission + ".recive", this.messages.getMessage("ReportFormat"), "{message}", message, "{nick}", commandSender.getName()
        );
    }
}
