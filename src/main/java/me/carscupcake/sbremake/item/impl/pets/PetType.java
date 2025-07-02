package me.carscupcake.sbremake.item.impl.pets;

import me.carscupcake.sbremake.player.skill.Skill;

import java.util.function.BiFunction;

public enum PetType implements BiFunction<Double, Skill, Double> {
    AllSkills("All Skills") {
        @Override
        public Double apply(Double aDouble, Skill skill) {
            return aDouble;
        }
    },
    Combat() {
        @Override
        public Double apply(Double aDouble, Skill skill) {
            if (skill != Skill.Combat) aDouble /= 3;
            return aDouble;
        }
    },
    Mining() {
        @Override
        public Double apply(Double aDouble, Skill skill) {
            if (skill != Skill.Mining) aDouble /= 3;
            return aDouble;
        }
    },
    Farming() {
        @Override
        public Double apply(Double aDouble, Skill skill) {
            if (skill != Skill.Farming) aDouble /= 3;
            return aDouble;
        }
    },
    Fishing() {
        @Override
        public Double apply(Double aDouble, Skill skill) {
            if (skill != Skill.Fishing) aDouble /= 3;
            return aDouble;
        }
    },
    Foraging() {
        @Override
        public Double apply(Double aDouble, Skill skill) {
            if (skill != Skill.Foraging) aDouble /= 3;
            return aDouble;
        }
    };
    private final String name;

    PetType(String name) {
        this.name = name;
    }

    PetType() {
        this.name = name();
    }
}
