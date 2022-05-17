package aiss.model.pokemon;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "type1",
        "type2",
        "hp",
        "attack",
        "defense",
        "generation",
        "legend"
})
@Generated("jsonschema2pojo")
public class Pokemon {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type1")
    private String type1;
    @JsonProperty("type2")
    private String type2;
    @JsonProperty("hp")
    private Integer hp;
    @JsonProperty("attack")
    private Integer attack;
    @JsonProperty("defense")
    private Integer defense;
    @JsonProperty("generation")
    private Integer generation;
    @JsonProperty("legend")
    private Boolean legend;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("type1")
    public String getType1() {
        return type1;
    }

    @JsonProperty("type1")
    public void setType1(String type1) {
        this.type1 = type1;
    }

    @JsonProperty("type2")
    public String getType2() {
        return type2;
    }

    @JsonProperty("type2")
    public void setType2(String type2) {
        this.type2 = type2;
    }

    @JsonProperty("hp")
    public Integer getHp() {
        return hp;
    }

    @JsonProperty("hp")
    public void setHp(Integer hp) {
        this.hp = hp;
    }

    @JsonProperty("attack")
    public Integer getAttack() {
        return attack;
    }

    @JsonProperty("attack")
    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    @JsonProperty("defense")
    public Integer getDefense() {
        return defense;
    }

    @JsonProperty("defense")
    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    @JsonProperty("generation")
    public Integer getGeneration() {
        return generation;
    }

    @JsonProperty("generation")
    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    @JsonProperty("legend")
    public Boolean getLegend() {
        return legend;
    }

    @JsonProperty("legend")
    public void setLegend(Boolean legend) {
        this.legend = legend;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
