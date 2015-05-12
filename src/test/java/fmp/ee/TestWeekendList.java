package fmp.ee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestWeekendList
{
  @Test
  public void test_toChar() throws Exception {
    assertEquals('A', Couple.toChar(1));
    assertEquals('B', Couple.toChar(2));
    assertEquals('M', Couple.toChar(13));
    assertEquals('Y', Couple.toChar(25));
    assertEquals('Z', Couple.toChar(26));
  }
  
  @Test
  public void test_toChar_fails() throws Exception {
    toChar_fails(' ');
    toChar_fails('a');
    toChar_fails('z');
    toChar_fails('4');
    toChar_fails('%');
  }
  
  private void toChar_fails(char ch) {
    try {
      Couple.toChar(ch);
      fail(String.format("expected IAE not thrown for value:%s", ch));
    }
    catch (IllegalArgumentException e) {
      // TODO: handle exception
    }
  }
  
  @Test
  public void test_assignRooms_1() throws Exception {
    List<Couple> couples = createCouples(1);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'B', true);
  }
  
  @Test
  public void test_assignRooms_2() throws Exception {
    List<Couple> couples = createCouples(2);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'B', true);
    verifyRoomAssignment(couples.get(1), 'A', 'B', false);
  }
  
  @Test
  public void test_assignRooms_3() throws Exception {
    List<Couple> couples = createCouples(3);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'C', true);
    verifyRoomAssignment(couples.get(1), 'A', 'D', false);
    verifyRoomAssignment(couples.get(2), 'B', 'C', false);
  }
  
  @Test
  public void test_assignRooms_4() throws Exception {
    List<Couple> couples = createCouples(4);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'C', true);
    verifyRoomAssignment(couples.get(1), 'A', 'D', false);
    verifyRoomAssignment(couples.get(2), 'B', 'C', false);
    verifyRoomAssignment(couples.get(3), 'B', 'D', true);
  }
  
  @Test
  public void test_assignRooms_5() throws Exception {
    List<Couple> couples = createCouples(5);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'D', true);
    verifyRoomAssignment(couples.get(1), 'A', 'E', false);
    verifyRoomAssignment(couples.get(2), 'B', 'F', true);
    verifyRoomAssignment(couples.get(3), 'B', 'D', false);
    verifyRoomAssignment(couples.get(4), 'C', 'E', true);
  }
  
  @Test
  public void test_assignRooms_17() throws Exception {
    List<Couple> couples = createCouples(17);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'J', true);
    verifyRoomAssignment(couples.get(1), 'A', 'K', false);
    verifyRoomAssignment(couples.get(2), 'B', 'L', true);
    verifyRoomAssignment(couples.get(3), 'B', 'M', false);
    verifyRoomAssignment(couples.get(4), 'C', 'N', true);
    verifyRoomAssignment(couples.get(5), 'C', 'O', false);
    verifyRoomAssignment(couples.get(6), 'D', 'P', true);
    verifyRoomAssignment(couples.get(7), 'D', 'Q', false);
    verifyRoomAssignment(couples.get(8), 'E', 'R', true);
    
    verifyRoomAssignment(couples.get(9), 'E', 'J', false);
    verifyRoomAssignment(couples.get(10), 'F', 'K', true);
    verifyRoomAssignment(couples.get(11), 'F', 'L', false);
    verifyRoomAssignment(couples.get(12), 'G', 'M', true);
    verifyRoomAssignment(couples.get(13), 'G', 'N', false);
    verifyRoomAssignment(couples.get(14), 'H', 'O', true);
    verifyRoomAssignment(couples.get(15), 'H', 'P', false);
    verifyRoomAssignment(couples.get(16), 'I', 'Q', true);
  }
  
  @Test
  public void test_assignRooms_19() throws Exception {
    List<Couple> couples = createCouples(19);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'K', true);
    verifyRoomAssignment(couples.get(1), 'A', 'L', false);
    verifyRoomAssignment(couples.get(2), 'B', 'M', true);
    verifyRoomAssignment(couples.get(3), 'B', 'N', false);
    verifyRoomAssignment(couples.get(4), 'C', 'O', true);
    verifyRoomAssignment(couples.get(5), 'C', 'P', false);
    verifyRoomAssignment(couples.get(6), 'D', 'Q', true);
    verifyRoomAssignment(couples.get(7), 'D', 'R', false);
    verifyRoomAssignment(couples.get(8), 'E', 'S', true);
    verifyRoomAssignment(couples.get(9), 'E', 'T', false);
    verifyRoomAssignment(couples.get(10), 'F', 'K', false);
    verifyRoomAssignment(couples.get(11), 'F', 'L', true);
    verifyRoomAssignment(couples.get(12), 'G', 'M', false);
    verifyRoomAssignment(couples.get(13), 'G', 'N', true);
    verifyRoomAssignment(couples.get(14), 'H', 'O', false);
    verifyRoomAssignment(couples.get(15), 'H', 'P', true);
    verifyRoomAssignment(couples.get(16), 'I', 'Q', false);
    verifyRoomAssignment(couples.get(17), 'I', 'R', true);
    verifyRoomAssignment(couples.get(18), 'J', 'S', false);
  }
  
  @Test
  public void test_assignRooms_20() throws Exception {
    List<Couple> couples = createCouples(20);
    
    RoomAssignments ra = new RoomAssignments(couples);
    ra.assignRooms();
    
    verifyRoomAssignment(couples.get(0), 'A', 'K', true);
    verifyRoomAssignment(couples.get(1), 'A', 'L', false);
    verifyRoomAssignment(couples.get(2), 'B', 'M', true);
    verifyRoomAssignment(couples.get(3), 'B', 'N', false);
    verifyRoomAssignment(couples.get(4), 'C', 'O', true);
    verifyRoomAssignment(couples.get(5), 'C', 'P', false);
    verifyRoomAssignment(couples.get(6), 'D', 'Q', true);
    verifyRoomAssignment(couples.get(7), 'D', 'R', false);
    verifyRoomAssignment(couples.get(8), 'E', 'S', true);
    verifyRoomAssignment(couples.get(9), 'E', 'T', false);
    verifyRoomAssignment(couples.get(10), 'F', 'K', false);
    verifyRoomAssignment(couples.get(11), 'F', 'L', true);
    verifyRoomAssignment(couples.get(12), 'G', 'M', false);
    verifyRoomAssignment(couples.get(13), 'G', 'N', true);
    verifyRoomAssignment(couples.get(14), 'H', 'O', false);
    verifyRoomAssignment(couples.get(15), 'H', 'P', true);
    verifyRoomAssignment(couples.get(16), 'I', 'Q', false);
    verifyRoomAssignment(couples.get(17), 'I', 'R', true);
    verifyRoomAssignment(couples.get(18), 'J', 'S', false);
    verifyRoomAssignment(couples.get(19), 'J', 'T', true);
  }
  
  private void verifyRoomAssignment(Couple couple, char hers, char his, boolean useHers) {
    assertEquals(Character.valueOf(hers), couple.herRoom);
    assertEquals(Character.valueOf(his), couple.hisRoom);
    assertEquals(useHers, couple.useHerRoom);
  }
  
  private List<Couple> createCouples(int numCouples) {
    List<Couple> couples = new ArrayList<>(numCouples);
    for (int i = 0; i < numCouples; i++) {
      Couple c = new Couple(i, 
          new Person("wName",
              "Address, City, State, Zip",
              "wEmail",
              "wPhone",
              26,
              "wReligion"),
          new Person("mName",
              "Address, City, State, Zip",
              "mEmail",
              "mPhone",
              25,
              "mReligion"),
          "Notes",
          "Church",
          "Priest",
          "Engagement",
          "Wedding",
          "DietaryRestrictions"
      );
      couples.add(c);
    }
    
    return couples;
  }

}
