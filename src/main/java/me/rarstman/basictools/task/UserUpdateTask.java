package me.rarstman.basictools.task;


import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.task.impl.TimerTask;

public class UserUpdateTask extends TimerTask {

    private final UserManager userManager;

    public UserUpdateTask() {
        super(18000L, 18000L);

        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onDisable() {}

    @Override
    public void onExecute() {
        this.userManager.saveUsers();
    }

}