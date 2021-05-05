package ca.tokidex.client.model;

/**
 * Model of our tokimon class, used to hold the data from the json response of server.
 */
public class Tokimon {
    private Long tokimonId;
    private String name;
    private Double weight;
    private Double height;
    private String ability;
    private Double strength;
    private String color;
    private int health;

    @Override
    public String toString() {
        return "Tokimon{" +
                "tokimonId=" + tokimonId +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", ability='" + ability + '\'' +
                ", strength=" + strength +
                ", color='" + color + '\'' +
                ", health=" + health +
                '}';
    }

    public Long getTokimonId() {
        return tokimonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
