package io.github.ph1lou.werewolfplugin.commands.roles;

import io.github.ph1lou.werewolfapi.Formatter;
import io.github.ph1lou.werewolfapi.ICommand;
import io.github.ph1lou.werewolfapi.IPlayerWW;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.enums.Prefix;
import io.github.ph1lou.werewolfapi.events.roles.sister.SisterSeeRoleEvent;
import io.github.ph1lou.werewolfapi.rolesattributs.IAffectedPlayers;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandSisterSeeRole implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        UUID uuid = player.getUniqueId();
        IPlayerWW playerWW = game.getPlayerWW(uuid).orElse(null);
        IPlayerWW killerWW = args[0].equals("pve") ? null : game.getPlayerWW(UUID.fromString(args[0])).orElse(null);
        if (playerWW == null) return;

        IAffectedPlayers affectedPlayers = (IAffectedPlayers) playerWW.getRole();

        if (!affectedPlayers.getAffectedPlayers().contains(killerWW)) {
            playerWW.sendMessageWithKey(Prefix.RED.getKey() , "werewolf.role.sister.already");
            return;
        }

        affectedPlayers.removeAffectedPlayer(killerWW);

        SisterSeeRoleEvent sisterSeeRoleEvent = new SisterSeeRoleEvent(playerWW, killerWW);

        if (sisterSeeRoleEvent.isCancelled()) {
            playerWW.sendMessageWithKey(Prefix.RED.getKey() , "werewolf.check.cancel");
            return;
        }


        playerWW.sendMessageWithKey(Prefix.YELLOW.getKey() , "werewolf.role.sister.reveal_killer_role",
                Formatter.role(
                killerWW != null ?
                        game.translate(killerWW.getRole().getKey()) :
                        game.translate("werewolf.utils.pve")));
    }
}
