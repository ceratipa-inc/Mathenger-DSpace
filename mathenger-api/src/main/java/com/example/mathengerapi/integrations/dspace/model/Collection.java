package com.example.mathengerapi.integrations.dspace.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Collection {
    private UUID uuid;
    private String name;
    private String introductoryText;
    private String shortDescription;
    @Override
    public String toString() {return name;}
}
