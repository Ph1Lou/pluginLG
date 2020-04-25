package io.github.ph1lou.pluginlg.commandlg.utilities;

import io.github.ph1lou.pluginlg.MainLG;
import io.github.ph1lou.pluginlg.commandlg.Commands;
import io.github.ph1lou.pluginlg.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFind extends Commands {


    public CommandFind(MainLG main) {
        super(main);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) return;

        GameManager game=null;
        Player player =(Player) sender;

        for(GameManager gameManager:main.listGames.values()){
            if(gameManager.getWorld().equals(player.getWorld())){
                game=gameManager;
                break;
            }
        }

        if(game!=null){
            player.sendMessage(game.text.getText(275));
            return;
        }

        player.openInventory(main.hubTool);
    }
}
