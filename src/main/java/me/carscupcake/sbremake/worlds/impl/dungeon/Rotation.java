package me.carscupcake.sbremake.worlds.impl.dungeon;

import lombok.Getter;
import me.carscupcake.sbremake.util.Pos2d;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

@Getter
public enum Rotation {
    NW("northwest") {
        @Override
        public Point toRelative(Pos2d mapPoint, Point point) {
            return point.sub(mapPoint.getMapX(), 0,  mapPoint.getMapZ());
        }

        @Override
        public Point toActual(Pos2d mapPoint, Point point) {
            return point.add(mapPoint.getMapX(), 0,  mapPoint.getMapZ());
        }
    }, NE("northeast") {
        @Override
        public Point toRelative(Pos2d mapPoint, Point point) {
            return new Vec(point.z() - mapPoint.getMapZ(), point.y(), -point.x() + mapPoint.getMapX());
        }

        @Override
        public Point toActual(Pos2d mapPoint, Point point) {
            return new Vec(-point.z() + mapPoint.getMapX(), point.y(), point.x() + mapPoint.getMapZ());
        }
    }, SW("southwest") {
        @Override
        public Point toRelative(Pos2d mapPoint, Point point) {
            return new Vec(-point.z() + mapPoint.getMapZ(), point.y(), point.x() - mapPoint.getMapX());
        }

        @Override
        public Point toActual(Pos2d mapPoint, Point point) {
            return new Vec(point.z() + mapPoint.getMapX(), point.y(), -point.x() + mapPoint.getMapZ());
        }
    }, SE("southeast") {
        @Override
        public Point toRelative(Pos2d mapPoint, Point point) {
            return new Vec(-point.x() + mapPoint.getMapX(), point.y(), -point.z() + mapPoint.getMapZ());
        }

        @Override
        public Point toActual(Pos2d mapPoint, Point point) {
            return new Vec(-point.x() + mapPoint.getMapX(), point.y(), -point.z() + mapPoint.getMapZ());
        }
    };
    private final String name;

    Rotation(String name) {
        this.name = name;
    }

    public abstract Point toRelative(Pos2d mapPoint, Point point);
    public abstract Point toActual(Pos2d mapPoint, Point point);
}
