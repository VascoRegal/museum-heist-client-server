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
 *  Represent an ordinary thief, comunicates via stubs
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

    /**
     * Stub reference
     */
    private GeneralMemoryStub generalMemory;

    /**
     * Stub reference
     */
    private CollectionSiteMemoryStub collectionSiteMemory;

    /**
     * Stub reference
     */
    private PartiesMemoryStub partiesSiteMemory;

    /**
     * Stub reference
     */
    private MuseumMemoryStub museumMemoryStub;


    /**
     * identification and reference to stubs
     * @param id
     * @param generalMemory
     * @param collectionSiteMemory
     * @param partiesSiteMemory
     * @param museumMemoryStub
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
        int roomId;
        boolean hasCanvas;

        while (amINeeded())
        {
            roomId = prepareExcursion();
            crawlIn(roomId);
            hasCanvas = pickCanvas(roomId);
            crawlOut(hasCanvas);
            handACanvas(hasCanvas);
        }

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

    /**
     * Remove the canvas
     */
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

    /**
     * blocks until needed for a party
     * @return true if needed
     */
    private boolean amINeeded()
    {
        this.setThiefState(ThiefState.CONCENTRATION_SITE);
        generalMemory.setOrdinaryThiefState(id, ThiefState.CONCENTRATION_SITE);
        int party = collectionSiteMemory.amINeeded();
        this.setPartyId(party);
        return (party != -1);
    }

    /**
     * tell the server thief is ready
     * @return int, party identification
     */
    private int prepareExcursion()
    {
        this.setThiefState(ThiefState.CRAWLING_INWARDS);
        generalMemory.setOrdinaryThiefState(id, ThiefState.CRAWLING_INWARDS);
        return partiesSiteMemory.prepareExcursion(this.getPartyId());
    }

    /**
     * crawl to a room
     * @param roomId
     */
    private void crawlIn(int roomId)
    {
        int roomLocation = museumMemoryStub.getRoomLocation(roomId);
        partiesSiteMemory.crawlIn(roomLocation, this.getMaxDisplacement());
        generalMemory.setOrdinaryThiefState(this.id, ThiefState.AT_A_ROOM);
        this.setThiefState(ThiefState.AT_A_ROOM);
    }

    /**
     * pick a canvas from a room
     * @param roomId
     * @return true if canvas picked
     */
    private boolean pickCanvas(int roomId)
    {
        boolean pickedCanvas = museumMemoryStub.pickCanvas(roomId);
        return pickedCanvas;
    }

    /**
     * crawl out of roo,
     * @param withCanvas true if canvas taken
     */
    private void crawlOut(boolean withCanvas)
    {
        generalMemory.setOrdinaryThiefState(id, ThiefState.CRAWLING_OUTWARDS);
        this.setThiefState(ThiefState.CRAWLING_OUTWARDS);
        partiesSiteMemory.crawlOut(withCanvas);
    }

    /**
     * hand a canvas
     * @param hasCanvas true if canvas taken
     */
    private void handACanvas(boolean hasCanvas)
    {
        generalMemory.setOrdinaryThiefState(id, ThiefState.COLLECTION_SITE);
        this.setThiefState(ThiefState.COLLECTION_SITE);
        collectionSiteMemory.handACanvas(hasCanvas);
        this.setPartyId(-1);
        generalMemory.setOrdinaryThiefState(id, ThiefState.CONCENTRATION_SITE);
        this.setThiefState(ThiefState.CONCENTRATION_SITE);
    }
}
