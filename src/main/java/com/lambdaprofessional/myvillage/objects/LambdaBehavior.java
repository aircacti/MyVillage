package com.lambdaprofessional.myvillage.objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaprofessional.myvillage.enums.LambdaBehaviorType;


public class LambdaBehavior {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String id;
    private LambdaBehaviorType type;
    private String instruction;

    public LambdaBehavior(String id, LambdaBehaviorType type, String instruction) {
        this.id = id;
        this.type = type;
        this.instruction = instruction;
    }

    public LambdaBehavior() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LambdaBehaviorType getType() {
        return type;
    }

    public void setType(LambdaBehaviorType type) {
        this.type = type;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String serialize() throws JsonProcessingException {
        return MAPPER.writeValueAsString(this);
    }

    public static LambdaBehavior deserialize(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, LambdaBehavior.class);
    }


}
