package cn.iqianye.MinecraftPlugins.ZMusic.Command;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Main;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.PlayList;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.PlayMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.HelpUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MessageUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MusicUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.OtherUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandExec implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //指令输出
        if (cmd.getName().equalsIgnoreCase("zm")) {
            if (sender.hasPermission("zmusic.use") || sender.isOp()) {
                if (args.length == 0) {
                    MessageUtils.sendNull(cmd.getName(), sender);
                    return true;
                } else if (args.length >= 1) {
                    switch (args[0]) {
                        case "music":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "music", null);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "music", sender);
                                    return true;
                                }
                            }
                        case "play":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "self", null);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "play", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "search":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        SearchMusic.sendList(OtherUtils.argsXin1(args), args[1], (Player) sender);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "search", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "stop":
                            MusicUtils.stopSelf((Player) sender);
                            OtherUtils.resetPlayerStatus((Player) sender);
                            MessageUtils.sendNormalMessage("停止播放音乐成功!", sender);
                            return true;
                        case "loop":
                            if (PlayerStatus.getPlayerLoopPlay((Player) sender) != null && PlayerStatus.getPlayerLoopPlay((Player) sender)) {
                                PlayerStatus.setPlayerLoopPlay((Player) sender, false);
                                MessageUtils.sendNormalMessage("循环播放已关闭!", sender);
                            } else {
                                PlayerStatus.setPlayerLoopPlay((Player) sender, true);
                                MessageUtils.sendNormalMessage("循环播放已开启!", sender);
                            }
                            return true;
                        case "playlist":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    if (args[1].equalsIgnoreCase("import")) {
                                        new Thread(() -> {
                                            PlayList.importPlayList(args[2], (Player) sender);
                                        }).start();
                                    } else if (args[1].equalsIgnoreCase("play")) {
                                        new Thread(() -> {
                                            PlayList.playPlayList(args[2], (Player) sender);
                                        }).start();
                                    }
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "play", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "url":
                            if (sender instanceof Player) {
                                if (args.length == 2) {
                                    MusicUtils.stopSelf((Player) sender);
                                    MusicUtils.playSelf(args[1], (Player) sender);
                                    MessageUtils.sendNormalMessage("播放成功!", sender);
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "url", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "playAll":
                            if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "all", players);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "admin", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        case "stopAll":
                            if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                MusicUtils.stopAll(players);
                                OtherUtils.resetPlayerStatusAll(players);
                                MessageUtils.sendNormalMessage("强制全部玩家停止播放音乐成功!", sender);
                                return true;
                            } else {
                                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        case "help":
                            if (args.length == 2) {
                                HelpUtils.sendHelp(cmd.getName(), args[1], sender);
                                return true;
                            } else {
                                HelpUtils.sendHelp(cmd.getName(), "main", sender);
                                return true;
                            }
                        case "reload":
                            if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                JavaPlugin.getPlugin(Main.class).reloadConfig();
                                Config.load(JavaPlugin.getPlugin(Main.class).getConfig());
                                MessageUtils.sendNormalMessage("配置文件重载完毕!", sender);
                                return true;
                            }
                        default:
                            MessageUtils.sendNull(cmd.getName(), sender);
                            return true;
                    }
                } else {
                    MessageUtils.sendNull(cmd.getName(), sender);
                    return true;
                }
            } else {
                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.use 权限此使用命令.", sender);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String[] commandList = new String[0];
        if (args.length == 0) {
            //如果不是能够补全的长度，则返回空列表
            return new ArrayList<>();
        } else if (args.length >= 1) {
            if (args.length == 1) {
                if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
                    commandList = new String[]{"help",
                            "play",
                            "music",
                            "stop",
                            "loop",
                            "search",
                            "url",
                            "admin",
                            "playAll",
                            "stopAll",
                            "reload"};
                } else {
                    commandList = new String[]{"help",
                            "play",
                            "music",
                            "stop",
                            "loop",
                            "search",
                            "url"};
                }
                return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("play")
                    ||
                    args[0].equalsIgnoreCase("music")
                    ||
                    args[0].equalsIgnoreCase("search")
                    ||
                    args[0].equalsIgnoreCase("playAll")) {
                if (args.length == 2) {
                    commandList = new String[]{"qq",
                            "163",
                            "netease",
                            "kugou",
                            "kuwo"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 2) {
                    if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
                        commandList = new String[]{"play", "music", "search", "url", "admin"};
                    } else {
                        commandList = new String[]{"play", "music", "search", "url"};
                    }
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else {
                return new ArrayList<>();
            }
        }
        return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }
}
