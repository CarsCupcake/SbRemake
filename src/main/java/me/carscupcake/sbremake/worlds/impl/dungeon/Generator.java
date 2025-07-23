package me.carscupcake.sbremake.worlds.impl.dungeon;

import lombok.Getter;
import me.carscupcake.sbremake.util.Pos2d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Getter
public class Generator {
    private final Random random;
    private final Room[][] rooms;
    private final DoorType[][] doorsVertical; // The doors with the vertical rotation
    private final DoorType[][] doorsHorizontal; // The doors with the horizontal rotation
    private final Room entrance;
    private final Room blood;
    private final Room fairy;
    private final Room trap;
    public Generator(Room[][] dimensions) {
        this(dimensions, System.currentTimeMillis());
    }
    public Generator(Room[][] dimensions, long seed) {
        this.rooms = dimensions;
        this.doorsVertical = new DoorType[rooms.length][rooms[0].length - 1];
        this.doorsHorizontal = new DoorType[rooms.length - 1][rooms[0].length];
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

        var puzzlesAmount = 2 + random.nextInt(3);
        for  (int i = 0; i < puzzlesAmount; i++) {
            int x;
            int z;
            do {
                x = random.nextInt(rooms.length);
                z = random.nextInt(rooms[0].length);
            } while (!canPlacePuzzle(x, z));
            rooms[x][z] =
                    new Room(RoomType.Puzzle, RoomShape.ONE_BY_ONE, new Pos2d(x, z),  Rotation.NW, List.of(), null); //TODO RoomData with puzzle data
        }

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

    private boolean canPlacePuzzle(int x , int z) {
        if (rooms[x][z] != null) return false;
        return (x + 1 == rooms.length || rooms[x+1][z] == null) && (x == 0 || rooms[x-1][z] == null);
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

    private Room getFromPos(Pos2d pos2d) {
        return rooms[pos2d.x()][pos2d.z()];
    }

    public void generateDoors(Pos2d start) {
        int count = 0;
        boolean[][] discovered = new boolean[rooms.length][rooms[0].length];
        var posebilities = new LinkedList<Pos2d>();
       posebilities.add(start);
       while (!posebilities.isEmpty()) {
           var current = getFromPos(posebilities.pop());
           if (current.type() == RoomType.Entrance) {
               doorsHorizontal[0][current.pos().z()] = DoorType.Start;
               boolean[] booleans = discovered[current.pos().x() + 1];
               if (!booleans[current.pos().z()] && testPos(current.pos(), 1, 0)) {
                   var targetPos = new Pos2d(current.pos().x() + 1, current.pos().z());
                   var target = getFromPos(targetPos);
                   if (target.parent() != null) {
                       target = getFromPos(target.parent());
                   }
                   for (var poses : target.children()) {
                       posebilities.add(poses);
                       discovered[poses.x()][poses.z()] = true;
                       count++;
                   }
                   posebilities.add(target.pos());
                   discovered[target.pos().x()][current.pos().z()] = true;
                   count++;

               }
               continue;
           }

       }
    }

    private boolean testPos(Pos2d pos2d, int x, int z) {
        return true;
    }
}
