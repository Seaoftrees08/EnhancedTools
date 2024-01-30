package com.github.seaoftrees08.entools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(label.equalsIgnoreCase("enhancedtools") || label.equalsIgnoreCase("entool")){

            if(!(sender instanceof Player)){
                reply(sender, ChatColor.RED + "Only player can use this command.");
                return false;
            }

            Player p = (Player) sender;

            // /entool <radius>
            if(args.length == 1 && isNum(args[0])){
                int r = parseInt(args[0]);
                if(r%2 == 0 && r<100){
                    reply(sender, "掘削半径は1以上100未満の奇数のみ設定可能です");
                }else if (!ToolSettings.canEnTool(p.getInventory().getItemInMainHand().getType())) {
                    reply(sender, "設定できる道具は、ピッケルかシャベルか斧のみです.");
                }else{
                    ToolSettings.setRadius(r, p);
                    reply(sender, "掘削半径を " + args[0] + " にしました.");
                }

            // /entool conf
            }else if(args.length == 1 && args[0].equalsIgnoreCase("conf")) {
                reply(sender, "掘削半径は " + ToolSettings.getRadius(p) + " に設定されています.");
                if(ToolSettings.getMineall(p)){
                    reply(sender, "MineAllは有効です.");
                }

            // /entool mineall
            }else if(args.length == 1 && (args[0].equalsIgnoreCase("mineall")|| args[0].equalsIgnoreCase("m"))){

                if(!ToolSettings.canMineall(p.getInventory().getItemInMainHand().getType())){
                    reply(sender, "MineAllを設定できるのはピッケルのみです.");
                }else{
                    if(ToolSettings.getMineall(p)){
                        reply(sender, "Mineallを無効にしました");
                    }else{
                        reply(sender, "Mineallを有効にしました");
                    }
                    ToolSettings.setMineAll(p);
                }

            // /entool help
            }else if(args.length==1 && args[0].equalsIgnoreCase("help")){

                replyg(sender, "/entool <radius>");
                replyg(sender, "    右手に持っているツールに掘削半径を指定します。");
                replyg(sender, "    <radius>(1以上の奇数のみ)で半径を指定します。");
                replyg(sender, "    <radius>を1を設定することで、未設定状態に戻せます。");
                replyg(sender, "");
                replyg(sender, "/entool conf");
                replyg(sender, "    右手に持っているツールの掘削半径を表示します。");
                replyg(sender, "    インベントリでの説明欄でも確認可能。");
                replyg(sender, "");
                replyg(sender, "/entool mineall");
                replyg(sender, "/entool m");
                replyg(sender, "    右手に持っているツールのMineAll機能の有効/無効を切り替えます。");
                replyg(sender, "    インベントリでの説明欄でも確認可能。");
                replyg(sender, "");
                replyg(sender, "/entool help");
                replyg(sender, "     /entoolのコマンドの説明を表示します。");

            }else{
                reply(sender, "コマンドの構文が間違っています.");
                reply(sender, "詳しくは /entool help");
            }

            return true;
        }
        return false;
    }

    private void reply(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.BLUE + "[Enhanced Tools] " + ChatColor.WHITE + msg);
    }

    private void replyg(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.BLUE + "[Enhanced Tools] " + ChatColor.GRAY + msg);
    }

    private boolean isNum(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }
    }

    //入力は数字である前提
    private int parseInt(String s){
        try{
            int i = Integer.parseInt(s);
            return i;
        }catch (NumberFormatException e) {
            return 0;
        }
    }
}
