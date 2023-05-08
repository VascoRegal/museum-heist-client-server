package client.entities;

import client.stubs.CollectionSiteMemoryStub;
import client.stubs.PartiesMemoryStub;
import client.stubs.GeneralMemoryStub;
import client.stubs.MuseumMemoryStub;

/**
 * MasterThief class
 * 
 * Communicates with the servers via stubs
 */
public class MasterThief extends Thief {

    /*
     * General stub
     */
    GeneralMemoryStub generalMemory;

    /**
     * ColSite stub
     */
    CollectionSiteMemoryStub collectionSiteMemory;

    /**
     * Parties stub
     */
    PartiesMemoryStub partiesMemory;

    /**
     * Museum stub
     */
    MuseumMemoryStub museumMemory;

    /**
     * 
     * @param id 
     * @param generalMemory
     * @param collectionSiteMemory
     * @param partiesMemory
     * @param museumMemory
     */
    public MasterThief(
        int id,
        GeneralMemoryStub generalMemory,
        CollectionSiteMemoryStub collectionSiteMemory,
        PartiesMemoryStub partiesMemory,
        MuseumMemoryStub museumMemory) 
    {
        super(id);
        this.generalMemory = generalMemory;
        this.collectionSiteMemory = collectionSiteMemory;
        this.partiesMemory = partiesMemory;
        this.museumMemory = museumMemory;
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
                    sendAssaultParty(partyId);
                    break;
                case 'r':
                    takeARest();
                    collectACanvas();
                    break;
                case 's':
                    break;
            }
        }
    }

    /**
     *  start operations
     */
    private void startOperations()
    {
        this.setThiefState(ThiefState.PLANNING_THE_HEIST);
        generalMemory.setMasterThiefState(ThiefState.PLANNING_THE_HEIST);
        collectionSiteMemory.startOperations();
    }

    /**
     * 
     * @return bool wether or not heist is running
     */
    private boolean isHeistInProgress()
    {
        return collectionSiteMemory.isHeistInProgress();
    }

    /**
     * decide what to do next with current resources
     * @return char, operation to do next
     */
    private char appraiseSit()
    {
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        return collectionSiteMemory.appraiseSit();
    }

    /**
     * prepare an assault party
     * @return
     */
    private int prepareAssaultParty()
    {
        this.setThiefState(ThiefState.ASSEMBLING_A_GROUP);
        generalMemory.setMasterThiefState(ThiefState.ASSEMBLING_A_GROUP);
        return collectionSiteMemory.prepareAssaultParty();
    }

    /**
     * send the party
     * @param partyId id of the party
     */
    private void sendAssaultParty(int partyId)
    {
        //int targetRoom = museumMemory.getAvailableRoom();
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        partiesMemory.sendAssaultParty(partyId, -1);
        generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
    }

    /**
     * wait for groups to arrive
     */
    private void takeARest()
    {
        this.setThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
        generalMemory.setMasterThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
        collectionSiteMemory.takeARest();
    }

    /**
     * collect a canvas
     */
    private void collectACanvas()
    {
        collectionSiteMemory.collectCanvas();
        generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
    }
}
