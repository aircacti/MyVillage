package com.lambdaprofessional.myvillage.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lambdaprofessional.myvillage.utils.LocationUtils;
import com.lambdaprofessional.myvillage.utils.VillagerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Villager.Type;
import org.bukkit.entity.Villager.Profession;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.util.ArrayList;
import java.util.List;

public class LambdaVillager {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String id;
    private String displayName;
    private Boolean displayVisible;
    private String professionString;
    private Integer level;
    private String typeString;
    private String locationString;
    private List<String> behaviors;
    private Boolean silent;

    public LambdaVillager(String id) {
        this.id = id;
    }

    public LambdaVillager() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getDisplayVisible() {
        return displayVisible;
    }

    public void setDisplayVisible(Boolean displayVisible) {
        this.displayVisible = displayVisible;
    }

    public Integer getLevel() {
        return level;
    }

    public Boolean getSilent() {
        return silent;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<String> getBehaviors() {
        if (behaviors == null) {
            behaviors = new ArrayList<>();
        }
        return behaviors;
    }

    public void setBehaviors(List<String> behaviors) {
        this.behaviors = behaviors;
    }

    public void setSilent(Boolean silent) {
        this.silent = silent;
    }

    public String getProfessionString() {
        return professionString;
    }

    public void setProfessionString(String professionString) {
        this.professionString = professionString;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    @JsonIgnore
    public Profession getProfession() {
        return VillagerUtils.getProfessionByName(professionString);
    }

    @JsonIgnore
    public void setProfession(Profession profession) {
        this.professionString = VillagerUtils.getProfessionName(profession);
    }

    @JsonIgnore
    public Type getType() {
        return VillagerUtils.getTypeByName(typeString);
    }

    @JsonIgnore
    public void setType(Type type) {
        this.typeString = VillagerUtils.getTypeName(type);
    }

    @JsonIgnore
    public Location getLocation() {
        return LocationUtils.stringToLocation(locationString);
    }

    @JsonIgnore
    public void setLocation(Location location) {
        this.locationString = LocationUtils.locationToString(location);
    }

    public String toJson() throws JsonProcessingException {
        return MAPPER.writeValueAsString(this);
    }

    public static LambdaVillager fromJson(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, LambdaVillager.class);
    }


}
