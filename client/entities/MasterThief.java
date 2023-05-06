package client.entities;

import client.stubs.CollectionSiteMemoryStub;
import client.stubs.GeneralMemoryStub;

public class MasterThief extends Thief {

    GeneralMemoryStub generalMemory;

    CollectionSiteMemoryStub collectionSiteMemory;

    /**
     *  Collection Site memory instantiation.
     *    
     *    @param id thief identification
     *    @param generalMemory general memory reference
     *    @param collectionSiteMemory collection site memory reference
     */

    public MasterThief(
        int id,
        GeneralMemoryStub generalMemory,
        CollectionSiteMemoryStub collectionSiteMemory) 
    {
        super(id);
        this.generalMemory = generalMemory;
        this.collectionSiteMemory = collectionSiteMemory;
        
    }

    /**
     *  Main lifecycle
     * 
     */
    public void run() {

        char action;
        int partyId;


        startOperations();
        while (isHeistInProgress())
        {
            action = appraiseSit();
            switch (action)
            {
                case 'p':
                    partyId = prepareAssaultParty();
                    System.out.println("Created party " + partyId);
                    break;
                case 'r':
                    System.out.println("OPERATION REST");
                    break;
                case 's':
                    System.out.println("Operation END");
                    break;
            }
        }

        /*
        collectionSiteMemory.startOperations();                             // start the operations
        while (generalMemory.isHeistInProgres()) {                          // while heist is running
            action = collectionSiteMemory.appraiseSit();                    // decide what action to do next
            switch (action) {           
                case 'p':                                                   // 'p' - create a party
                    partyId = collectionSiteMemory.prepareAssaultParty();   // prepare it
                    collectionSiteMemory.sendAssaultParty(partyId);         // send it when ready
                    break;
                case 'r':                                                   // rest  
                    collectionSiteMemory.takeARest();                       // wait for thieves arrival
                    collectionSiteMemory.collectACanvas();                  // awake to collect canvas
                    break;
                case 's':
                    break;
            }
        }
        collectionSiteMemory.sumUpResults();                                // sum up and present the results
        */
    }

    private void startOperations()
    {
        this.setThiefState(ThiefState.PLANNING_THE_HEIST);
        generalMemory.setMasterThiefState(ThiefState.PLANNING_THE_HEIST);
        collectionSiteMemory.startOperations();
    }

    private boolean isHeistInProgress()
    {
        return collectionSiteMemory.isHeistInProgress();
    }

    private char appraiseSit()
    {
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        return collectionSiteMemory.appraiseSit();
    }

    private int prepareAssaultParty()
    {
        this.setThiefState(ThiefState.ASSEMBLING_A_GROUP);
        generalMemory.setMasterThiefState(ThiefState.ASSEMBLING_A_GROUP);
        return collectionSiteMemory.prepareAssaultParty();
    }
}
