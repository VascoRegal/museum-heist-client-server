package server.shared;

import client.entities.ThiefState;
import consts.HeistConstants;

public class GeneralMemory {
    
    private ThiefState mtState;

    private ThiefState [] otStates;

    private String logName;

    public GeneralMemory()
    {
        int i;

        this.mtState = ThiefState.PLANNING_THE_HEIST;
        this.otStates = new ThiefState[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            otStates[i] = null;
        }
    }

    public synchronized void init(String fileName)
    {
        // TODO: LOGS
        System.out.println("Initing shit ya get me");
    }

    public synchronized void setMasterThiefState(ThiefState ts)
    {
        this.mtState = ts;
    }

    public synchronized void setOrdinaryThiefState(int thiefId, ThiefState ts)
    {
        this.otStates[thiefId] = ts;
    }
}
