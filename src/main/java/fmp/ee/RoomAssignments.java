package fmp.ee;

import fmp.ee.model.Couple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pedrozaf on 6/5/15.
 */
class RoomAssignments {

  private Set<Character> assignedRooms = new HashSet<>();
  final List<Couple> couples;

  RoomAssignments(final List<Couple> couples) {
    this.couples = couples;
  }

  public void assignRooms()
  {
//    System.out.println(String.format("assignRooms(%d)", couples.size()));

    final int switchPoint = ((couples.size() + 1) / 2);
    int herRoom = 1;
    int hisRoom = herRoom + switchPoint;
    final int firstHisRoom = hisRoom;

//    System.out.println(String.format("switchPt:%d", switchPoint));

    int i = 0;
    boolean preferFirst = true;

    for (Couple couple : couples) {
      if (i > 0 && i % 2 == 0) {
        herRoom++;  // increment after even iterations
      }
      if (i == switchPoint) {
        hisRoom = firstHisRoom;
      }
      if (i >= switchPoint) {
        preferFirst = false;  // toggle preference half way through
      }

      couple.setHerRoom(herRoom);
      couple.setHisRoom(hisRoom);

      char sharingRoom = chooseRoom(couple.getHerRoom(), couple.getHisRoom(), preferFirst);
      boolean useHerRoom = (couple.getHerRoom() == sharingRoom);
      couple.setUseHerRoomaAsSharing(useHerRoom);

//      System.out.println(couple.getRoomAssignmentString());

      hisRoom++;  // increment each iteration
      i++;
    }
  }

  protected char chooseRoom(char roomOne, char roomTwo, boolean preferFirst) {
    char roomChoice;
    char firstChoice = roomOne;
    char secondChoice = roomTwo;

    if (!preferFirst) {
      char swap = firstChoice;
      firstChoice = secondChoice;
      secondChoice = swap;
    }

    if (!assignedRooms.contains(firstChoice)) {
      roomChoice = firstChoice;
    }
    else if (!assignedRooms.contains(secondChoice)) {
      roomChoice = secondChoice;
    }
    else {
      throw new IllegalStateException(String.format(
          "both rooms (%s & %s) are already assigned", firstChoice, secondChoice));
    }
    assignedRooms.add(roomChoice);
    return roomChoice;
  }
}
