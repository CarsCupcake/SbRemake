package me.carscupcake.sbremake.player;

import lombok.Getter;

@Getter
public enum BankAccountType {
    Starter("§aStarter", 50_000_000, 250_000),
    Gold("§6Gold", 100_000_000, 300_000),
    Deluxe("§dDeluxe", 250_000_000, 350_000),
    SuperDeluxe("§5Super Deluxe", 500_000_000, 390_000),
    Premier("§cPremier", 1_000_000_000, 500_000),
    Luxurious("§3Luxurious", 6_000_000_000d, 1_000_000),
    Palatial("§4Palatial", 60_000_000_000d, 1_500_000);
    private final String name;
    private final double maxBalance;
    private final double maxInterest;
    BankAccountType(String name, double maxBalance, double maxInterest) {
        this.name = name;
        this.maxBalance = maxBalance;
        this.maxInterest = maxInterest;
    }
}
