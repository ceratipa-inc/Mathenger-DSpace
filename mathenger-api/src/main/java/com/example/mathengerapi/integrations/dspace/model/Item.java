package com.example.mathengerapi.integrations.dspace.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Item {
    private UUID uuid;
    private String name;

    @Override
    public String toString() {
        return name + " /id:" + uuid;
    }
}
