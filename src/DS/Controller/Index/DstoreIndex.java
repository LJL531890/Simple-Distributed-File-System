package DS.Controller.Index;
//分布式存储索引的管理。
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import DS.Controller.Index.State.OperationState;
import DS.Controller.Index.State.RebalanceState;
import Network.Connection;

/**
 * Represents the 'State' of a Dstore within the system. Used by the Controller
 * to keep track of the Dstores it is controlling, along with the files stored on them.
 */
public class DstoreIndex implements Comparable<DstoreIndex>{
    
    // member variables
    private volatile int port; // the port the Dstore is listening on
    private volatile Connection connection;
    private volatile CopyOnWriteArrayList<DstoreFile> files;
    private volatile RebalanceState rebalanceState;

    /**
     * Class constructor.
     * 
     * @param port The port the dstore is listening on.
     * @param connection The controller's connection to the Dstore.
     */
    public DstoreIndex(int port, Connection connection){
        this.port = port;
        this.connection = connection;
        this.files = new CopyOnWriteArrayList<DstoreFile>();
        this.rebalanceState = RebalanceState.IDLE;
    }

    ///////////////////////
    // CONFIGURING FILES //
    ///////////////////////

    /**
     * Adds a new file to the list of files
     * 
     * @param filename The name of the file to be added.
     * @param filesize The size of the file to be added in bytes.
     */
    public void addFile(String filename, int filesize){
        this.files.add(new DstoreFile(filename, filesize));
    }

    /**
     * Removes a given file from the index.
     * 
     * @param filename The file to be removed
     */
    public void removeFile(String filename){
        // place holder for the file object
        DstoreFile foundFile = null;

        // finding the file object
        for(DstoreFile file : this.files){
            if(file.getFilename().equals(filename)){
                foundFile = file;
            }
        }

        // removing the file if it was found
        if(foundFile != null){
            this.files.remove(foundFile);
        }
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Sets the state of a file to the given state.
     * 
     * @param filename The file having it's state changed.
     * @param state The state the file will be changed to.
     */
    public void updateFileState(String filename, OperationState state){
        for(DstoreFile file : this.files){
            if(file.getFilename().equals(filename)){
                file.setState(state);
            }
        }
    }

    /**
     * Determines if the given file is stored on the Dstore.
     * 
     * @param filename The file being checkedd for.
     * @return True if the file is stored on the Dstore, false if not.
     */
    public boolean hasFile(String filename){
        for(DstoreFile file : this.files){
            if(file.getFilename().equals(filename)){
                return true;
            }
        }

        return false;
    }

    /**
     * Comparator method. Compares to Dstore indexes based on the number of files
     * they have stored on them.
     * 
     * @param otherDstore
     * @return -1 if this DstoreIndex has less files than the Dstore index it is being 
     * comparedd to, 0 if it has the same, and 1 if it has more.
     */
    @Override
    public int compareTo(DstoreIndex otherDstore){
        if(this.files.size() < otherDstore.getFiles().size()){
            return -1;
        }
        else if(this.files.size() == otherDstore.getFiles().size()){
            return 0;
        }
        else{
            return 1;
        }
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    public int getPort(){
        return this.port;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public CopyOnWriteArrayList<DstoreFile> getFiles(){
        return this.files;
    }

    public DstoreFile getFile(String filename){
        for(DstoreFile file : this.files){
            if(file.getFilename().equals(filename)){
                return file;
            }
        }
        
        return null;
    }

    public void setFiles(HashMap<String, Integer> files){
        // clearing old files
        this.files.clear();

        // adding new files
        for(String file : files.keySet()){
            DstoreFile dstoreFile = new DstoreFile(file, files.get(file));
            dstoreFile.setState(OperationState.IDLE);

            this.files.add(dstoreFile);
        }
    }

    public RebalanceState getRebalanceState(){
        return this.rebalanceState;
    }

    public void setRebalanceState(RebalanceState rebalanceState){
        this.rebalanceState = rebalanceState;
    }

    public String toString(){
        return (this.port + " : " + this.files.toString());
    }
}