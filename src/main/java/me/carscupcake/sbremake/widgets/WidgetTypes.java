package me.carscupcake.sbremake.widgets;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.widgets.impl.*;

import java.util.ArrayList;
import java.util.Arrays;

public enum WidgetTypes {
    GeneralInfo {
        @Override
        IWidget create(SkyblockPlayer player, ConfigSection config) {
            return new GeneralInfoWidget(player);
        }
    },
    Profile {
        @Override
        IWidget create(SkyblockPlayer player, ConfigSection config) {
            return new ProfileWidget(player);
        }
    },
    Pet {
        @Override
        IWidget create(SkyblockPlayer player, ConfigSection config) {
            return new PetWidget(player);
        }
    },
    Skills {
        @Override
        IWidget create(SkyblockPlayer player, ConfigSection config) {
            var skillArray = config.get("skills", ConfigSection.STRING_ARRAY, new String[]{Skill.Farming.name(), Skill.Mining.name(), Skill.Combat.name(), Skill.Foraging.name()});
            var list = new ArrayList<Skill>();
            for (var skill : skillArray) {
                list.add(Skill.valueOf(skill));
            }
            return new SkillsWidget(player, list);
        }
    },
    Stats {
        @Override
        IWidget create(SkyblockPlayer player, ConfigSection config) {
            var statArray = config.get("stats", ConfigSection.STRING_ARRAY, new String[]{Stat.Speed.name(), Stat.Strength.name(), Stat.CritChance.name(), Stat.CritDamage.name(), Stat.AttackSpeed.name(), Stat.MagicFind.name()});
            var list = new ArrayList<Stat>();
            for (var skill : statArray) {
                list.add(Stat.valueOf(skill));
            }
            return new StatsWidget(player, list);
        }
    },
    Collections {
        @Override
        IWidget create(SkyblockPlayer player, ConfigSection config) {
            var collectionArray = config.get("collections", ConfigSection.STRING_ARRAY, new String[]{"COAL", "COBBLESTONE", "OAK_WOOD"});
            return new CollectionsWidget(Arrays.stream(collectionArray).map(s -> player.getCollections().get(s)).toList());
        }
    };

    abstract IWidget create(SkyblockPlayer player, ConfigSection config);

}
