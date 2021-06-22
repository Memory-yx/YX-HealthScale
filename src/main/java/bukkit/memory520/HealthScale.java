package bukkit.memory520;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;


public class HealthScale extends JavaPlugin implements Listener {

    public void onEnable() {
        System.out.println("");
        System.out.println("# +----------------------------+");
        System.out.println("# |");
        System.out.println("# |   §bYX-HealthScale");
        System.out.println("# |   #载入成功");
        System.out.println("# |");
        System.out.println("# |   | §eby.Memory520");
        System.out.println("# |   | §eQQ:3332397782");
        System.out.println("# |");
        System.out.println("# +-------");
        System.out.println("");

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        super.onEnable();
        // 保存config.yml至插件文件夹
        saveDefaultConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent p) {
        FileConfiguration config = getConfig();
        new BukkitRunnable(){
            @Override
            public void run(){
                if(Bukkit.getServer().getPlayer(p.getPlayer().getName()) != null){
                    //getLogger().info("循环 玩家存在");
                    Player player = p.getPlayer();
                    check(player);
                }else {
                    //getLogger().info("循环 玩家退出");
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(this,0,Integer.parseInt(config.getString("HeartTime")));
    }

    //计算"心"的数量并赋值
    private void check(Player player) {
        //读取配置文件
        FileConfiguration config = getConfig();
        //获取玩家最大血量
        double MaxHP = player.getMaxHealth();
        //循环检查血量所在的区间
        for (int i=1,HS,MAXHS;i<=Integer.parseInt(config.getString("HeartLength"));i++)
        {
            HS = Integer.parseInt(config.getString("HealthScale"+i));
            MAXHS = Integer.parseInt(config.getString("HealthScale"+config.getString("HeartLength")));
            //当血量小于或等于区间数值的话就设置玩家"心"的数量
            if(MaxHP < HS)
            {
                //算法: ((区间值-1)+初始"心"的数量)*2
                player.setHealthScale(((i-1)+Integer.parseInt(config.getString("HeartValue")))*2);
                break;
            }
            else if (MaxHP >= MAXHS)
            {
                //算法: (最大"心"的数量+初始"心的数量")*2
                player.setHealthScale((Integer.parseInt(config.getString("HeartLength"))+Integer.parseInt(config.getString("HeartValue")))*2);
                break;
            }
        }
        return;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reload")) {
            sender.hasPermission("HealthScale.admin");
            reloadConfig();
            sender.sendMessage("§aYX-HealthScale 重载完成!");
        }
        return false;
    }

    public void onDisable() {
        //取消调度的所有任务
        Bukkit.getScheduler().cancelTasks(this);
        super.onDisable();
    }
}
