package Network.Protocol.Event;

/**
 * 表示已由 NetworkProcess 处理的 NetworkEvent。
 */
public class HandeledNetworkEvent {

    // member variables
    private NetworkEvent event;
    
    /**
     * Class Constructor.
     * 
     * @param event The NetworkEvent that has been handeled.
     */
    public HandeledNetworkEvent(NetworkEvent event){
        this.event = event;
    }
    /**
     * Converts the event to a string.
     */
    public String toString(){
        // returning string
        return this.event.toString();
    }

    /**
     * Gets the event that has been handeled.
     * 
     * @return The NetworkEvent that has been handeled.
     */
    public NetworkEvent getEvent(){
        return this.event;
    }
}