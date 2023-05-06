package client.entities;

import client.stubs.CollectionSiteMemoryStub;
import client.stubs.PartiesMemoryStub;
import client.stubs.GeneralMemoryStub;
import client.stubs.MuseumMemoryStub;
import consts.HeistConstants;
import structs.Utils;


/**
 *  Ordinary Thief class
 * 
 *  Represent an ordinary thief, containing information
 *  to handle states, canvas and movement
 * 
 */
public class OrdinaryThief extends Thief
{
    /**
     *  Position of the thief 
     */
    
    private int position;
    
    /**
     *  Thief maximum displacement
     */

    private int md;

    /**
     *  Thief canvas holding status
     */

    private boolean hasCanvas;

    /**
     *  Thief party identification
     */

    private int partyId;


    private GeneralMemoryStub generalMemory;


    private CollectionSiteMemoryStub collectionSiteMemory;

    private PartiesMemoryStub partiesSiteMemory;

    private MuseumMemoryStub museumMemoryStub;

    /**
     *  Collection Site memory instantiation.
     *
     *    @param partiesSiteMemory concentration memory reference
     *    @param museumMemory museum memory reference
     *    @param partiesMemory parties memory reference
     *    @param collectionSiteMemory collection site memory reference
     */

    public OrdinaryThief(
        int id,
        GeneralMemoryStub generalMemory,
        CollectionSiteMemoryStub collectionSiteMemory,
        PartiesMemoryStub partiesSiteMemory,
        MuseumMemoryStub museumMemoryStub
    )
    {
        super(id);
        this.position = 0;      // initial position
        this.md = Utils.randIntInRange(HeistConstants.MIN_THIEF_MD, HeistConstants.MAX_THIEF_MD);   // random number
        this.state = ThiefState.CONCENTRATION_SITE;     //initial state
        this.partyId = -1;
        this.hasCanvas = false;
        this.generalMemory = generalMemory;
        this.collectionSiteMemory = collectionSiteMemory;
        this.partiesSiteMemory = partiesSiteMemory;
        this.museumMemoryStub = museumMemoryStub;
    }

    /**
     * 
     *  Main lifecylce
     */
    public void run() {
        int partyId, roomId;
        boolean hasCanvas;

        while (amINeeded())
        {
            roomId = prepareExcursion();
            crawlIn(roomId);
            hasCanvas = pickCanvas(roomId);
            System.out.println("[OT" + this.id + "] has canvas = " + hasCanvas);
            crawlOut();

            System.out.println("redy for za ganaza " + id);

        }

        /*
        while (partiesSiteMemory.amINeeded()) {               // while thief is needed
            int room = -1;                                          // reference to target room

            partyId = partiesSiteMemory.prepareExcursion();   // if he's needed, prepare the excursion
            room = partiesMemory.crawlingIn();                      // begin crawling when ready                    
            museumMemory.rollACanvas(room);                         // at the room, steal canvas
            partiesMemory.crawlingOut();                            // crawl back to site
            collectionSiteMemory.handACanvas();                     // hand the canvas
        }
        */
    }

    /**
     *  Set Thief party identification
     * 
     *      @param id party id
     */

    public void setPartyId(int id) {
        this.partyId = id;
    }

    /**
     *  Get Thief party identification
     * 
     *      @return party id
     */

    public int getPartyId() {
        return this.partyId;
    }

    /**
     *  Get Thief max displace
     * 
     *      @return thief md
     */

    public int getMaxDisplacement() {
        return this.md;
    }

    /**
     *  Get Thief position
     * 
     *      @return thief position
     */

    public int getPosition() {
        return this.position;
    }

    /**
     *  Set Thief postion
     * 
     *      @param thief position
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    /**
     *  Toggle the canvas flag
     */

    public void pickCanvas() {
        this.hasCanvas = true;
    }

    public void removeCanvas() {
        this.hasCanvas = false;
    }

    /**
     *  Get canvas state
     *  
     *      @return true if thief holding canvas
     *              false if not
     */

    public boolean hasCanvas() {
        return this.hasCanvas;
    }

    /**
     *  Move thief by an increment and return
     *  new position
     *      
     *      @param increment
     *      @return final position
     */

    public int move(int increment) {
        this.position += increment;
        return this.position;
    }


    private boolean amINeeded()
    {
        this.setThiefState(ThiefState.CONCENTRATION_SITE);
        generalMemory.setOrdinaryThiefState(id, ThiefState.CONCENTRATION_SITE);
        int party = collectionSiteMemory.amINeeded();
        this.setPartyId(party);
        return (party != -1);
    }

    private int prepareExcursion()
    {
        this.setThiefState(ThiefState.CRAWLING_INWARDS);
        generalMemory.setOrdinaryThiefState(id, ThiefState.CRAWLING_INWARDS);
        return partiesSiteMemory.prepareExcursion(this.getPartyId());
    }

    private void crawlIn(int roomId)
    {
        int roomLocation = museumMemoryStub.getRoomLocation(roomId);
        partiesSiteMemory.crawlIn(roomLocation, this.getMaxDisplacement());
        generalMemory.setOrdinaryThiefState(this.id, ThiefState.AT_A_ROOM);
        this.setThiefState(ThiefState.AT_A_ROOM);
    }

    private boolean pickCanvas(int roomId)
    {
        return museumMemoryStub.pickCanvas(roomId);
    }

    private void crawlOut()
    {
        generalMemory.setOrdinaryThiefState(id, ThiefState.CRAWLING_OUTWARDS);
        this.setThiefState(ThiefState.CRAWLING_OUTWARDS);
        partiesSiteMemory.crawlOut();
    }
}
