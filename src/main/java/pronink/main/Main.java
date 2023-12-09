package pronink.main;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import pronink.main.utils.Util;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        Util.registerEventsListeners(new MinecartEventListener());
        new Metrics(this, 20475);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}