package com.lambdaprofessional.myvillage.utils;

import com.lambdaprofessional.myvillage.MyVillage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class Messenger {

    private static final String RAW_PREFIX = "[MyVillage] ";
    private static final TextComponent PRETTY_PREFIX = Component.text(RAW_PREFIX).color(NamedTextColor.GREEN);

    public static void send(CommandSender reciever, Boolean addPrefix, String text, NamedTextColor color) {
        if (addPrefix) {
            reciever.sendMessage(PRETTY_PREFIX.append(Component.text(text).color(color)));
        } else {
            reciever.sendMessage(Component.text(text).color(color));
        }
    }

    public static void send(CommandSender reciever, Boolean addPrefix, String text, NamedTextColor color, String hoverText) {
        Component textComponent = Component.text(text)
                .color(color)
                .hoverEvent(HoverEvent.showText(Component.text(hoverText)));

        if (addPrefix) {
            reciever.sendMessage(PRETTY_PREFIX.append(textComponent));
        } else {
            reciever.sendMessage(textComponent);
        }
    }


    public static void send(CommandSender reciever, Boolean addPrefix, String text, NamedTextColor color, String hoverText, String suggestText) {
        Component textComponent = Component.text(text)
                .color(color)
                .hoverEvent(HoverEvent.showText(Component.text(hoverText)))
                .clickEvent(ClickEvent.suggestCommand(suggestText));

        if (addPrefix) {
            reciever.sendMessage(PRETTY_PREFIX.append(textComponent));
        } else {
            reciever.sendMessage(textComponent);
        }
    }


    public static void info(String text) {
        MyVillage.getInstance().getLogger().info(RAW_PREFIX + text);
    }

    public static void severe(String text) {
        MyVillage.getInstance().getLogger().severe(text);
    }



}
