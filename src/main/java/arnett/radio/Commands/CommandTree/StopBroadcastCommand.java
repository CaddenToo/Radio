package arnett.radio.Commands.CommandTree;

import arnett.radio.Commands.SubCommand;
import arnett.radio.Frequencies.FrequencyBroadcaster;
import arnett.radio.Frequencies.FrequencyManager;
import arnett.radio.Radio;
import arnett.radio.RadioConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// todo THIS COMMAND WILL BE REWORKED LATER

@SuppressWarnings("UnstableApiUsage")
public class StopBroadcastCommand implements SubCommand {

    @Override
    public String getName() {
        return "stopbroadcast";
    }

    @Override
    public String getDescription() {
        return "Stops a broadcast for the given file";
    }

    @Override
    public String getSyntax() {
        return "/radio stopbroadcast <frequency>";
    }

    @Override
    public boolean execute(Player player, String[] args, int level) {

        //this is just a permission check
        if (SubCommand.super.execute(player, args, level))
            return true;

        StringBuilder frequency = new StringBuilder();

        for(int i = level; i < args.length; i++)
        {
            frequency.append(args[i]).append(RadioConfig.frequencySplitString);
        }

        frequency.setLength(frequency.length() - RadioConfig.frequencySplitString.length());

        FrequencyManager.stopBroadcast(frequency.toString());

        player.sendMessage(Component.text("Stopping Broadcast", NamedTextColor.GREEN, TextDecoration.BOLD));

        return true;
    }

    @Override
    public boolean canUse(Player player)
    {
        return player.hasPermission("radio.broadcast");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args, int level) {
            return FrequencyManager.dyeMap.keySet().stream().toList();
    }
}