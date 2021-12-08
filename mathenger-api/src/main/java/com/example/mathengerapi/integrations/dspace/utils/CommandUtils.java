package com.example.mathengerapi.integrations.dspace.utils;

import java.util.UUID;

public class CommandUtils {
    public static UUID extractId (String command) {
        return UUID.fromString(command.substring(command.length()-UUID.randomUUID().toString().length(), command.length()));
    }
}
