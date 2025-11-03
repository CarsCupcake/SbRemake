package me.carscupcake.config;

import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings("unused")
public final class Constants {
  public static final class SLAYER {
    /**
     * The value of the config option slayerquest-costs
     */
    public static final double[] SLAYERQUEST_COSTS = new double[]{2000.,7500.,20000.,50000.,150000.};

    public static final class ZOMBIE {
      /**
       * The value of the config option health
       */
      public static final double[] HEALTH = new double[]{500.,20000.,400000.,1500000.,10000000.};

      /**
       * The value of the config option damage
       */
      public static final double[] DAMAGE = new double[]{15.,25.,120.,400.,2400.};

      /**
       * The value of the config option lifedrain
       */
      public static final String LIFEDRAIN = "§cLife Drain\n"
          + "§7Drains health every few second.";

      /**
       * The value of the config option pestilence
       */
      public static final String PESTILENCE = "§aPestilence\n"
          + "§7Deals AOE damage every second, shredding armor by 25%.";

      /**
       * The value of the config option enrage
       */
      public static final String ENRAGE = "§cEnrage\n"
          + "§7Gets real mad once in a while.";

      /**
       * The value of the config option redemption
       */
      public static final String REDEMPTION = "§aRedemption\n"
          + "§7Heals rapidly.";

      /**
       * The value of the config option explosive-assault
       */
      public static final String EXPLOSIVE_ASSAULT = "§cExplosive Assault\n"
          + "§7Throws explosive TNT.";

      /**
       * The value of the config option thermonuclear
       */
      public static final String THERMONUCLEAR = "§5Thermonuclear\n"
          + "§7Charges up and releases a massive explosion.";

      /**
       * The value of the config option slayer-names
       */
      public static final String[] SLAYER_NAMES = new String[]{"Beginner","Strong","Challenging","Deadly","Excruciating"};
    }

    public static final class ENDERMAN {
      /**
       * The value of the config option health
       */
      public static final double[] HEALTH = new double[]{300000.,12000000.,50000000.,210000000.};

      /**
       * The value of the config option damage
       */
      public static final double[] DAMAGE = new double[]{1200.,5000.,12000.,21000.};

      /**
       * The value of the config option malevolent-hitshield
       */
      public static final String MALEVOLENT_HITSHIELD = "§aMalevolent Hitshield\n"
          + "§8At 100%/66%/33% HP\n"
          + "§7Gain a shield for 15 hits, reduced on hit regardless of damage.\n"
          + "Receives §c-75% ⫽Ferocity§7 from your hits while the shield is NOT active.";

      /**
       * The value of the config option yang-glyphs
       */
      public static final String YANG_GLYPHS = "§5Yang Glyphs\n"
          + "§8Starting at 50% HP\n"
          + "§7Throws glyphs down.\n"
          + "Stand next to them or die.";

      /**
       * The value of the config option nukekubi-fixations
       */
      public static final String NUKEKUBI_FIXATIONS = "§eNukekubi Fixations\n"
          + "§8Starting at 33% HP\n"
          + "§7Spawns weard heads.\n"
          + "Clear them by looking at them or suffer damage.";

      /**
       * The value of the config option broken-heart-radiation
       */
      public static final String BROKEN_HEART_RADIATION = "§dBroken Heart Radiation\n"
          + "§8Halfway between Hitshields\n"
          + "§7Spins lasers, dont hit them!";

      /**
       * The value of the config option slayer-names
       */
      public static final String[] SLAYER_NAMES = new String[]{"Beginner","Strong","Challenging","Guaranteed-Doom"};
    }
  }
}
