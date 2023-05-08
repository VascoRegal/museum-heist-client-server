package client.entities;

/**
 * Interface for server object cloning
 */
public interface ThiefCloning {
    public int getThiefId();
    public void setThiefId(int id);
    public ThiefState getThiefState();
    public void setThiefState(ThiefState state);
    public int getPartyId();
    public void setPartyId(int id);
}
