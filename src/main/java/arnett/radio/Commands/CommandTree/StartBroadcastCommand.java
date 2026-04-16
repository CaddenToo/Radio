package arnett.radio.Commands.CommandTree;

import arnett.radio.Commands.SubCommand;
import arnett.radio.Frequencies.FrequencyBroadcaster;
import arnett.radio.Frequencies.FrequencyManager;
import arnett.radio.Items.Microphone.Microphone;
import arnett.radio.Items.Radio.FieldRadioVoiceChat;
import arnett.radio.Items.Speaker.Speaker;
import arnett.radio.Radio;
import arnett.radio.RadioConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// todo THIS COMMAND WILL BE REWORKED LATER

@SuppressWarnings("UnstableApiUsage")
public class StartBroadcastCommand implements SubCommand {

    @Override
    public String getName() {
        return "startbroadcast";
    }

    @Override
    public String getDescription() {
        return "Starts a broadcast for the given file";
    }

    @Override
    public String getSyntax() {
        return "/radio startbroadcast <filepath> <frequency>";
    }

    @Override
    public boolean execute(Player player, String[] args, int level) {

        //this is just a permission check
        if (SubCommand.super.execute(player, args, level))
            return true;

        String filePath = args[level];

        StringBuilder frequency = new StringBuilder();

        for(int i = level + 1; i < args.length; i++)
        {
            frequency.append(args[i]).append(RadioConfig.frequencySplitString);
        }

        frequency.setLength(frequency.length() - RadioConfig.frequencySplitString.length());

        try {
            FrequencyBroadcaster.startBroadcast(frequency.toString(), filePath, false, 0);
        }
        catch (Exception e)
        {
            player.sendMessage(Component.text("Unable to start broadcast", NamedTextColor.RED, TextDecoration.BOLD));
            return true;
        }

        player.sendMessage(Component.text("Starting Broadcast", NamedTextColor.GREEN, TextDecoration.BOLD));

        return true;
    }

    @Override
    public boolean canUse(Player player)
    {
        return player.hasPermission("radio.broadcast");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args, int level) {
        // /DisplayRadioListeners <here>...
        if (args.length == level) {
            //returns list of files in data folder
            return getAudioFiles(Radio.singleton.getDataFolder());
        }
        else
        {
            return FrequencyManager.dyeMap.keySet().stream().toList();
        }
    }

    public List<String> getAudioFiles(File directory) {

        System.out.println(directory.toPath());

        //go though the file tree and find audio files
        try (Stream<Path> walk = Files.walk(directory.toPath())) {
            return walk
                    // Ignore folders
                    .filter(Files::isRegularFile)

                    //only get wav and mp3 files
                    .filter(path -> {
                        String name = path.toString().toLowerCase();
                        return name.endsWith(".wav") || name.endsWith(".mp3");
                    })

                    //convert it to strings
                    .map(path -> {
                        String[] split = path.toString().split("/");
                        return split[split.length-1];
                    })
                    .toList();
        }
        //file not found ect.
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}