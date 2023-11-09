package pronink.main;

import org.bukkit.plugin.java.JavaPlugin;
import pronink.main.utils.Util;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        Util.registerEventsListeners(new MinecartEventListener());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}