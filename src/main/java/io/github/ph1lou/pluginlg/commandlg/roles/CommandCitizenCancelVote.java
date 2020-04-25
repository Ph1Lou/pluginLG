package io.github.ph1lou.pluginlg.commandlg.roles;

import io.github.ph1lou.pluginlg.MainLG;
import io.github.ph1lou.pluginlg.commandlg.Commands;
import io.github.ph1lou.pluginlg.enumlg.*;
import io.github.ph1lou.pluginlg.game.GameManager;
import io.github.ph1lou.pluginlg.game.PlayerLG;
import io.github.ph1lou.pluginlg.savelg.TextLG;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCitizenCancelVote extends Commands {


    public CommandCitizenCancelVote(MainLG main) {
        super(main);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)){
            return;
        }

        GameManager game=null;
        Player player =(Player) sender;

        for(GameManager gameManager:main.listGames.values()){
            if(gameManager.getWorld().equals(player.getWorld())){
                game=gameManager;
                break;
            }
        }

        if(game==null){
            return;
        }

        TextLG text = game.text;
        String playername = player.getName();

        if(!game.playerLG.containsKey(playername)) {
            player.sendMessage(text.getText(67));
            return;
        }

        PlayerLG plg = game.playerLG.get(playername);


        if(!game.isState(StateLG.LG)) {
            player.sendMessage(text.getText(68));
            return;
        }

        if (!plg.isRole(RoleLG.CITOYEN)){
            player.sendMessage(String.format(text.getText(189),text.translateRole.get(RoleLG.CITOYEN)));
            return;
        }

        if (args.length!=0) {
            player.sendMessage(String.format(text.getText(190),0));
            return;
        }

        if(!plg.isState(State.LIVING)){
            player.sendMessage(text.getText(97));
            return;
        }

        if(!plg.hasPower()) {
            player.sendMessage(text.getText(103));
            return;
        }

        if (game.score.getTimer() % (game.config.timerValues.get(TimerLG.DAY_DURATION) * 2) < game.config.timerValues.get(TimerLG.VOTE_DURATION)) {
            text.getText(103);
            return;
        }
        if (!game.config.configValues.get(ToolLG.VOTE) || game.config.timerValues.get(TimerLG.VOTE_DURATION) + game.config.timerValues.get(TimerLG.VOTE_BEGIN) > 0) {
            text.getText(103);
            return;
        }
        if (game.score.getTimer() % (game.config.timerValues.get(TimerLG.DAY_DURATION) * 2) > game.config.timerValues.get(TimerLG.VOTE_DURATION) + game.config.timerValues.get(TimerLG.CITIZEN_DURATION)) {
            text.getText(103);
            return;
        }

        plg.setPower(false);
        String vote=game.vote.getResult();
        sender.sendMessage(String.format(text.powerHasBeenUse.get(RoleLG.CITOYEN),vote));
        plg.addAffectedPlayer(vote);
        for(Player p:Bukkit.getOnlinePlayers()){
            if(game.playerLG.containsKey(p.getName())){
                p.sendMessage(text.getText(94));
            }
        }
        game.vote.resetVote();
    }
}
