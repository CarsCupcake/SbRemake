package carscupcake;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoorwaysModel {
    public boolean north;
    public boolean east;
    public boolean south;
    public boolean west;
}
