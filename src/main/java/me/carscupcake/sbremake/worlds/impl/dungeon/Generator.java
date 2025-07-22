package me.carscupcake.sbremake.worlds.impl.dungeon;

import lombok.Getter;
import me.carscupcake.sbremake.util.Pos2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Generator {
    private final Random random;
    private final Room[][] rooms;
    private final Room entrance;
    private final Room blood;
    private final Room fairy;
    private final Room trap;
    public Generator(Room[][] dimensions) {
        this(dimensions, System.currentTimeMillis());
    }
    public Generator(Room[][] dimensions, long seed) {
        this.rooms = dimensions;
        this.random = new Random(seed);
        System.out.println("Rooms seed it " + seed);
        var xEs = new Integer[dimensions.length];
        int zLength = dimensions[0].length;
        for (int i = 0; i < dimensions.length; i++) {
            xEs[i] = i;
            if (dimensions[i].length != zLength) throw new IllegalArgumentException("Dimensions lengths don't match");
        }
        var zEs = new Integer[zLength];
        for (int i = 0; i < zLength; i++) {
            zEs[i] = i;
        }
        shuffleArray(xEs);
        shuffleArray(zEs);

        var entrancePos = new Pos2d(0, random.nextInt(rooms[0].length));
        entrance = new Room(RoomType.Entrance, RoomShape.ONE_BY_ONE, entrancePos, Rotation.NW, List.of(), null);
        rooms[entrancePos.x()][entrancePos.z()] = entrance;

        var redZ = random.nextInt(rooms[0].length);
        var redX = rooms.length - 1;
        if (redZ == 0 || redZ == rooms[0].length - 1)
            if (random.nextBoolean())
                redX--;
        blood = new Room(RoomType.Blood, RoomShape.ONE_BY_ONE, new Pos2d(redX, redZ),  Rotation.NW, List.of(), null);
        rooms[redX][redZ] = blood;

        var fairyPossibilities = new ArrayList<Pos2d>();
        for (int x = 1; x < rooms.length - 1; x++) {
            for (int z = 1; z < rooms[x].length - 1; z++) {
                if (testForFairy(x, z))
                    fairyPossibilities.add(new Pos2d(x, z));
            }
        }
        var fairyPos = fairyPossibilities.get(random.nextInt(fairyPossibilities.size()));
        fairy = new Room(RoomType.Fairy, RoomShape.ONE_BY_ONE, fairyPos,  Rotation.NW, List.of(), null);
        rooms[fairyPos.x()][fairyPos.z()] = fairy;

        var trapSpots = new ArrayList<Pos2d>(6);
        trapSpots.add(fairyPos.add(1, 0));
        trapSpots.add(fairyPos.add(0, 1));
        trapSpots.add(fairyPos.add(-1, 0));
        trapSpots.add(fairyPos.add(0, -1));
        if (entrancePos.z() != 0) trapSpots.add(entrancePos.add(0, -1));
        if (entrancePos.z() != rooms[0].length - 1) trapSpots.add(entrancePos.add(0, 1));
        var trapPos = trapSpots.get(random.nextInt(trapSpots.size()));
        trap = new Room(RoomType.Trap, RoomShape.ONE_BY_ONE, trapPos,  Rotation.NW, List.of(), null);
        rooms[trapPos.x()][trapPos.z()] = trap;

        for (var x : xEs)
            for (var z : zEs) {
                tryPlace(new Pos2d(x, z));
            }
        for (var x : xEs)
            for (var z : zEs) {
                if (rooms[x][z] == null)
                    RoomShape.ONE_BY_ONE.tryInsert(rooms, new Pos2d(x, z), Rotation.values()[random.nextInt(4)]);
            }
    }

    private void tryPlace(Pos2d pos2d) {
        var shapes = new RoomShape[]{RoomShape.ONE_BY_TWO, RoomShape.ONE_BY_THREE,  RoomShape.ONE_BY_FOUR, RoomShape.L_SHAPE, RoomShape.TWO_BY_TWO};
        shuffleArray(shapes);
        for (RoomShape shape : shapes) {
            if (shape.tryInsert(rooms, pos2d, Rotation.values()[random.nextInt(4)])) return;
        }
    }

    private boolean testForFairy(int x , int z) {
        return rooms[x+1][z] == null &&  rooms[x][z+1] == null &&
                rooms[x-1][z] == null &&  rooms[x][z-1] == null;
    }

    private <T> void shuffleArray(T[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            var a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
