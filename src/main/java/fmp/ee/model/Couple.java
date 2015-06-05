package fmp.ee.model;

/**
 * Created by pedrozaf on 6/5/15.
 */
public class Couple
{
  public final Person man, woman;
  public final String church, priest, wedding, notes, engagement, dietaryRestrictions;
  public final int id;
  Character herRoom, hisRoom;
  Boolean useHerRoom;

  //Notes, Church, Priest, EngagementDate, WeddingDate, DietaryRestrictions
  private static final int NUM_PARAMS = 6;

  private static final String NO_DIET_RESTRICTIONS = "None";

  public Couple(int id, Person woman, Person man, String... params)
  {
    if (params.length != NUM_PARAMS) {
      throw new IllegalArgumentException(String.format(
          "unexpected number of params: expected(%d) != actual(%d)", NUM_PARAMS, params.length));
    }

    this.id = id;
    this.woman = woman;
    this.man = man;

    int i = 0;
    this.notes = params[i++];
    this.church = params[i++];
    this.priest = params[i++];

    this.engagement = prefix(params[i++], "E");
    this.wedding = prefix(params[i++], "W");

    String tempRestrictions = params[i++];
    this.dietaryRestrictions = (!NO_DIET_RESTRICTIONS.equals(tempRestrictions)) ?
        tempRestrictions : null;
  }

  public Character getHerRoom() {
    return herRoom;
  }

  public Character getHisRoom() {
    return hisRoom;
  }

  public Boolean useHerRoom() {
    return useHerRoom;
  }

  private String prefix(String value, String prefix) {
    return (value != null) ? String.format("%s: %s", prefix, value) : null;
  }

  public void setUseHerRoomaAsSharing(boolean b) {
    this.useHerRoom = b;
  }

  public void setHerRoom(int room) {
    this.herRoom = toChar(room);
  }

  public void setHisRoom(int room) {
    this.hisRoom = toChar(room);
  }

  static char toChar(int val) {
    int charVal = 'A' + val - 1;
    if (!Character.isUpperCase(charVal)) {
      throw new IllegalArgumentException(String.format(
          "value '%d' -> [%s] is not a letter value", val, charVal));
    }
    return Character.toChars(charVal)[0];
  }

  public String getRoomAssignmentString() {
    return "Couple [id:" + id +
        (herRoom != null ? " hers:" + herRoom : "") +
        (useHerRoom != null ? " sharing:" + (useHerRoom ? herRoom : hisRoom) : "") +
        (hisRoom != null ? " his:" + hisRoom : "");
  }

  @Override
  public String toString() {
    return "Couple [id: " + id +
        "\n man: " + man +
        "\n woman: " + woman +
        "\n church: " + church + ", priest: " + priest +
        "\n wedding: " + wedding  + ", engagement: " + engagement +
        "\n dietaryRestrictions: " + dietaryRestrictions +
        "\n notes: " + notes + "]";
  }
}
