package io.github.ph1lou.werewolfplugin.roles.villagers;

import io.github.ph1lou.werewolfapi.DescriptionBuilder;
import io.github.ph1lou.werewolfapi.Formatter;
import io.github.ph1lou.werewolfapi.IPlayerWW;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.events.game.day_cycle.DayEvent;
import io.github.ph1lou.werewolfapi.rolesattributs.IRole;
import io.github.ph1lou.werewolfapi.rolesattributs.RoleVillage;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class WiseElder extends RoleVillage {

    private int neutralCounter;
    private int darkCounter;
    private int lightCounter;
    private boolean active;

    public WiseElder(WereWolfAPI game, IPlayerWW playerWW, String key) {
        super(game, playerWW, key);
    }

    @Override
    public @NotNull String getDescription() {
        return new DescriptionBuilder(game, this)
                .setDescription(game.translate("werewolf.role.wise_elder.description"))
                .build();
    }

    @Override
    public void recoverPower() {

    }

    @EventHandler
    public void onDay(DayEvent event) {
        if (event.getNumber() == 3)
            active = true;

        if (active) {
            getPlayerWW().sendMessageWithKey("werewolf.role.wise_elder.end_of_cycle",
                    Formatter.format("&neutral&",neutralCounter),
                    Formatter.format("&dark&",darkCounter),
                    Formatter.format("&light&",lightCounter));
            resetCounters();
        }
    }

    @Override
    public void second() {
        if (!active) return;

        Location location = getPlayerWW().getLocation();
        game.getPlayersWW().stream()
                .filter(iPlayerWW -> iPlayerWW.getLocation().distance(location) < 15 && !iPlayerWW.equals(getPlayerWW()))
                .map(IPlayerWW::getRole)
                .map(IRole::getAura)
                .forEach(aura -> {
                    switch (aura) {
                        case NEUTRAL:
                            neutralCounter++;
                        case DARK:
                            darkCounter++;
                        case LIGHT:
                            lightCounter++;
                    }
                });
    }

    /**
     * Reset all the aura counters
     */
    private void resetCounters() {
        neutralCounter = 0;
        darkCounter = 0;
        lightCounter = 0;
    }
}