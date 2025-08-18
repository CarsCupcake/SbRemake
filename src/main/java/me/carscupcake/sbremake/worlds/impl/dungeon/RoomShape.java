package me.carscupcake.sbremake.worlds.impl.dungeon;

import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.Lazy;
import me.carscupcake.sbremake.util.Pos2d;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public enum RoomShape {
    ONE_BY_ONE("1x1") {
        @Override
        public List<Pos2d> children(Pos2d target, Rotation rotation) {
            return List.of();
        }

        public Point withRotationOffset(Point point, Rotation rotation) {
            return switch (rotation) {
                case NW -> point;
                case NE -> point.add(30, 0, 0);
                case SE -> point.add(30, 0, 30);
                case SW -> point.add(0, 0, 30);
            };
        }
    },
    ONE_BY_TWO("1x2") {
        @Override
        public List<Pos2d> children(Pos2d target, Rotation rotation) {
            return switch (rotation) {
                case NE, SW ->  List.of(target.add(0, 1));
                case NW, SE ->  List.of(target.add(1, 0));
            };
        }

        @Override
        public Point withRotationOffset(Point point, Rotation rotation) {
            return switch (rotation) {
                case NW -> point;
                case NE -> point.add(30, 0, 0);
                case SE -> point.add(62, 0, 30);
                case SW -> point.add(0, 0, 62);
            };
        }
    },
    ONE_BY_THREE("1x3") {
        @Override
        public List<Pos2d> children(Pos2d target, Rotation rotation) {
            return switch (rotation) {
                case NE, SW ->  List.of(target.add(0, 1), target.add(0, 2));
                case NW, SE ->  List.of(target.add(1, 0), target.add(2, 0));
            };
        }

        @Override
        public Point withRotationOffset(Point point, Rotation rotation) {
            return switch (rotation) {
                case NW -> point;
                case NE -> point.add(30, 0, 0);
                case SE -> point.add(91, 0, 30);
                case SW -> point.add(0, 0, 91);
            };
        }
    },
    ONE_BY_FOUR("1x4") {
        @Override
        public List<Pos2d> children(Pos2d target, Rotation rotation) {
            return switch (rotation) {
                case NE, SW ->  List.of(target.add(0, 1), target.add(0, 2), target.add(0, 3));
                case NW, SE ->  List.of(target.add(1, 0), target.add(2, 0), target.add(3, 0));
            };
        }

        @Override
        public Point withRotationOffset(Point point, Rotation rotation) {
            return switch (rotation) {
                case NW -> point;
                case NE -> point.add(30, 0, 0);
                case SE -> point.add(126, 0, 30);
                case SW -> point.add(0, 0, 126);
            };
        }
    },
    L_SHAPE("L-shape") {
        @Override
        public List<Pos2d> children(Pos2d target, Rotation rotation) {
            return switch (rotation) {
                case NW ->  List.of(target.add(0, 1), target.add(1, 1));
                case NE ->  List.of(target.add(-1, 0), target.add(-1, 1));
                case SE ->  List.of(target.add(0, -1), target.add(-1, -1));
                case SW -> List.of(target.add(1, 0),  target.add(1, -1));
            };
        }

        @Override
        public Point withRotationOffset(Point point, Rotation rotation) {
            return switch (rotation) {
                case NW -> point;
                case NE -> point.add(30, 0, 0);
                case SE -> point.add(30, 0, 30);
                case SW -> point.add(0, 0, 30);
            };
        }
    },
    TWO_BY_TWO("2x2") {
        @Override
        public List<Pos2d> children(Pos2d target, Rotation rotation) {
            return List.of(target.add(1, 0), target.add(1, 1), target.add(0, 1));
        }

        @Override
        public Point withRotationOffset(Point point, Rotation rotation) {
            return switch (rotation) {
                case NW -> point;
                case NE -> point.add(62, 0, 0);
                case SE -> point.add(62, 0, 62);
                case SW -> point.add(0, 0, 62);
            };
        }
    };
    private final String shape;

    @Getter
    private final Lazy<String[]> schematics;

    RoomShape(String shape) {
        this.shape = shape;
        schematics = new Lazy<>(() -> {
            var inResourcesPath = "assets/shematics/dungeon/rooms/" + shape + "/";
            URI uri = null;
            try {
                uri = Objects.requireNonNull(Main.class.getClassLoader().getResource(inResourcesPath)).toURI();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path folderRootPath = fileSystem.getPath(inResourcesPath);
                try (Stream<Path> walk = Files.walk(folderRootPath, 1)) {
                    return walk.map(Path::getFileName).map(Path::toString).filter(s -> !s.equals(shape)).toArray(String[]::new);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString() {
        return shape;
    }

    public boolean tryInsert(Room[][] rooms, Pos2d target, Rotation rotation) {
        if (rooms[target.x()][target.z()] != null) return false;
        var children = children(target, rotation);
        for (var child : children) {
            if (child.x() < 0 || child.z() < 0) return false;
            if (child.x() >= rooms.length || child.z() >= rooms[0].length) return false;
            if (rooms[child.x()][child.z()] != null) return false;
        }
        var roomBase = new Room(RoomType.Room, this, target, rotation, children, null);
        rooms[target.x()][target.z()] = roomBase;
        for (var child : children) {
            rooms[child.x()][child.z()] = new Room(RoomType.Room, this, child, rotation, List.of(), target);

        }
        return true;
    }
    public abstract List<Pos2d> children(Pos2d target, Rotation rotation);
    public abstract Point withRotationOffset(Point point, Rotation rotation);

    public Point toActual(Pos2d target, Point blockPos, Rotation rotation) {
        var pos = rotation.toActual(target, blockPos);
        return withRotationOffset(pos, rotation);
    }
    public Point toRelative(Pos2d target, Point actualPos, Rotation rotation) {
        var rotOffset = withRotationOffset(Pos.ZERO, rotation);
        var pos = actualPos.sub(rotOffset);
        return rotation.toRelative(target, pos);
    }
}
