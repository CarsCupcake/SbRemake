package me.carscupcake.sbremake.util.schematic;

import org.enginehub.linbus.stream.LinBinaryIO;

import java.io.File;

public record Schematic(File file, LinBinaryIO binaryIO) {
}
