package server.shared;

import consts.HeistConstants;
import server.entities.Room;
import server.entities.RoomState;



/**
 *  MuseumMemory class
 *
 *  Shared memory containing rooms and room operations
 *
 *  Public methods are controlled with an access semaphore
 */
public class MuseumMemory {
    
     /*
     *   Array of rooms
     */

    private final Room [] rooms;

    /**
     *  Museum Memory memory instantiation.
     *
     *    @param generalMemory general memory reference
     */

    public MuseumMemory() {
        rooms = new Room [HeistConstants.NUM_ROOMS];
        for (int i=0; i < HeistConstants.NUM_ROOMS; i++) {
            rooms[i] = new Room(i);
        }
    }

    /**
     *  Get a room available to send a party
     * 
     *  Called by the Master Thief when assembling a group
     *
     *    @return AVAILABLE room
     */

    public int findNonClearedRoom() {
        for (int i=0 ; i < rooms.length; i++) {
            if (rooms[i].getRoomState() == RoomState.AVAILABLE) {
                rooms[i].setRoomState(RoomState.IN_PROGRESS);
                return i;
            }
        }
        return -1;
    }


    /**
     *  Update a room's state
     *
     *    @param roomId room identification
     *    @param roomState room state
     */
    public void setRoomState(int roomId, RoomState roomState) {
        rooms[roomId].setRoomState(roomState);
    }


    public int getRoomLocation(int roomId)
    {
        return rooms[roomId].getLocation();
    }


    /**
     *  Update a room's state
     * 
     *  Roll a canvas. Called by Ordinary Thieves
     * 
     *  Nothing happens if there are no canvas on the walls
     *
     *    @param roomId room identification
     */
    public boolean rollACanvas(int roomId) {
        Room targetRoom;
        boolean picked = false;
        targetRoom = rooms[roomId];

        System.out.println(String.format("[ROOM%d] Rolling a canvas", roomId));
        if (!targetRoom.isEmpty()) {
            targetRoom.removePainting();
            System.out.println(String.format("[ROOM%d] We can roll it up", roomId));
            picked = true;
        } else {
            System.out.println(String.format("[ROOM%d] Room is empty", roomId));
            targetRoom.setRoomState(RoomState.COMPLETED);
        }
        return picked;
    }
}
