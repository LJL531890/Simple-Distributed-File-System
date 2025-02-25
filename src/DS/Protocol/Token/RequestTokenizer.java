package DS.Protocol.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import DS.Protocol.Protocol;
import DS.Protocol.Token.TokenType.*;

/**
 * 标记消息字符串。用于解析消息字符串并生成相应 Token 对象
 */
public class RequestTokenizer {
    
    /**
     * Class constuctor. Private as class is static.
     */
    private RequestTokenizer(){}

    /**
     * Gathers a token from a message string.
     * @param message The message string.
     * @return The gathered token.
     * 这段代码的作用是使用 StringTokenizer 对象 sTokenizer 对消息进行分词，根据空格将消息拆分成多个单词或标记。
     * 具体步骤如下：
     * 1. 创建一个 StringTokenizer 对象 sTokenizer，并将要分词的消息 message 作为参数传入，根据空格拆分字符串。
     * 2. 检查是否存在更多的标记（token），即检查是否成功拆分了消息。
     * 3. 如果没有更多的标记（tokens），即消息为空或没有被成功拆分成单词，则返回一个表示无效请求标记的 InvalidRequestToken 对象。
     * 4. 如果存在更多的标记，通过 sTokenizer.nextToken() 方法获取并返回第一个标记（token），即消息中的第一个单词或标记，存储在 firstToken 变量中。
     * 这段代码的目的是从消息中提取第一个单词或标记，以便进一步处理或识别消息的类型或内容。
     * 要获取消息拆分后的第二个单词，可以继续使用 sTokenizer.nextToken() 方法来获取下一个标记（token）。在已经获取了第一个单词后，再次调用 sTokenizer.nextToken() 将返回消息中的第二个单词
     */
    //根据消息字符串获取相应的 Token 对象
    public static Token getToken(String message){
        StringTokenizer sTokenizer = new StringTokenizer(message); // 分词器根据空格拆分字符串

        // No tokens
        if(!(sTokenizer.hasMoreTokens())){
            return new InvalidRequestToken(message);
        }

        String firstToken = sTokenizer.nextToken();


        // JOIN_DSTORE //
        if (firstToken.equals(Protocol.JOIN_DSTORE_TOKEN)){
            return getJoinDstoreToken(message, sTokenizer);
        }

        // JOIN_CLIENT //
        else if(firstToken.equals(Protocol.JOIN_CLIENT_TOKEN)){
            return new JoinClientToken(message);
        }

        // JOIN_CLIENT_HEARTBEAT //
        else if(firstToken.equals(Protocol.JOIN_CLIENT_HEARTBEAT)){
            return getJoinClientHeartbeatToken(message, sTokenizer);
        }

        // JOIN_ACK //
        else if(firstToken.equals(Protocol.JOIN_ACK_TOKEN)){
            return new JoinAckToken(message);
        }

        // ACK //
        else if(firstToken.equals(Protocol.ACK_TOKEN)) {
            return new AckToken(message);
        }

        // STORE //
        else if(firstToken.equals(Protocol.STORE_TOKEN)){
            return getStoreToken(message, sTokenizer);
        }

        // STORE_TO //
        else if(firstToken.equals(Protocol.STORE_TO_TOKEN)){
            return getStoreToToken(message, sTokenizer);
        }

        // STORE_ACK //
        else if(firstToken.equals(Protocol.STORE_ACK_TOKEN)){
            return getStoreAckToken(message, sTokenizer);
        }

        // STORE_COMPLETE //
        else if(firstToken.equals(Protocol.STORE_COMPLETE_TOKEN)){
            return new StoreCompleteToken(message);
        }

        // LOAD //
        else if(firstToken.equals(Protocol.LOAD_TOKEN)){
            return getLoadToken(message, sTokenizer);
        }

        // LOAD_FROM //
        else if (firstToken.equals(Protocol.LOAD_FROM_TOKEN)){
            return getLoadFromToken(message, sTokenizer);
        }

        // LOAD_DATA //
        else if(firstToken.equals(Protocol.LOAD_DATA_TOKEN)){
            return getLoadDataToken(message, sTokenizer);
        }

        // RELOAD //
        else if(firstToken.equals(Protocol.RELOAD_TOKEN)){
            return getReloadToken(message, sTokenizer);
        }

        // REMOVE //
        else if (firstToken.equals(Protocol.REMOVE_TOKEN)){
            return getRemoveToken(message, sTokenizer);
        }

        // REMOVE_ACK //
        else if(firstToken.equals(Protocol.REMOVE_ACK_TOKEN)){
            return getRemoveAckToken(message, sTokenizer);
        }

        // REMOVE_COMPLETE //
        else if(firstToken.equals(Protocol.REMOVE_COMPLETE_TOKEN)){
            return new RemoveCompleteToken(message);
        }

        // LIST //
        else if(firstToken.equals(Protocol.LIST_TOKEN)){
            return getListToken(message, sTokenizer);
        }

        // REBALANCE //
        else if (firstToken.equals(Protocol.REBALANCE_TOKEN)){
            return getRebalanceToken(message, sTokenizer);
        }

        // REBALANCE_STORE //
        else if (firstToken.equals(Protocol.REBALANCE_STORE_TOKEN)){
            return getRebalanceStoreToken(message, sTokenizer);
        }

        // REBALANCE_COMPLETE //
        else if(firstToken.equals(Protocol.REBALANCE_COMPLETE_TOKEN)){
            return new RebalanceCompleteToken(message);
        }

        // ERROR_DSTORE_PORT_IN_USE //
        else if(firstToken.equals(Protocol.ERROR_DSTORE_PORT_IN_USE_TOKEN)){
            return new ErrorDstorePortInUseToken(message);
        }

        // ERROR_NOT_ENOUGH_DSTORES //
        else if(firstToken.equals(Protocol.ERROR_NOT_ENOUGH_DSTORES_TOKEN)){
            return new ErrorNotEnoughDStoresToken(message);
        }

        // ERROR_FILE_ALREADY_EXISTS //
        else if(firstToken.equals(Protocol.ERROR_FILE_ALREADY_EXISTS_TOKEN)){
            return new ErrorFileAlreadyExistsToken(message);
        }

        // ERROR_FILE_DOES_NOT_EXIST //
        else if(firstToken.equals(Protocol.ERROR_FILE_DOES_NOT_EXIST_TOKEN)){
            return getErrorFileDoesNotExistToken(message, sTokenizer);
        }

        // ERROR_LOAD //
        else if(firstToken.equals(Protocol.ERROR_LOAD_TOKEN)){
            return new ErrorLoadToken(message);
        }

        // Unrecognized //
        else{
            return new InvalidRequestToken(message);
        }
    }

    /**
     * 从消息字符串中收集JOIN_DSTORE令牌。
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getJoinDstoreToken(String message, StringTokenizer sTokenizer) {
        try{
            int port = Integer.parseInt(sTokenizer.nextToken());

            return new JoinDstoreToken(message, port);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a JOIN_CLIENT_HEARTBEAT token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getJoinClientHeartbeatToken(String message, StringTokenizer sTokenizer) {
        try{
            int port = Integer.parseInt(sTokenizer.nextToken());

            return new JoinClientHeartbeatToken(message, port);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a STORE token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getStoreToken(String message, StringTokenizer sTokenizer) {
        try{
            String filename = sTokenizer.nextToken();
            int filesize = Integer.parseInt(sTokenizer.nextToken());
            return new StoreToken(message, filename, filesize);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a STORE_TO token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getStoreToToken(String message, StringTokenizer sTokenizer) {
        ArrayList<Integer> ports = new ArrayList<Integer>();
        
        try{
            while(sTokenizer.hasMoreTokens()){
                int port = Integer.parseInt(sTokenizer.nextToken());
                ports.add(port);
            }

            return new StoreToToken(message, ports);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a STORE_ACK token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    public static Token getStoreAckToken(String message, StringTokenizer sTokenizer){
        try{
            String filename = sTokenizer.nextToken();

            return new StoreAckToken(message, filename);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a LOAD token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getLoadToken(String message, StringTokenizer sTokenizer){
        try{
            String filename = sTokenizer.nextToken();

            return new LoadToken(message, filename);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a LOAD_FROM token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getLoadFromToken(String message, StringTokenizer sTokenizer) {
        try{
            int port = Integer.parseInt(sTokenizer.nextToken());

            int filesize = Integer.parseInt(sTokenizer.nextToken());

            return new LoadFromToken(message, port, filesize);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a LOAD_DATA token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getLoadDataToken(String message, StringTokenizer sTokenizer) {
        try{
            String filename = sTokenizer.nextToken();

            return new LoadDataToken(message, filename);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a RELOAD token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getReloadToken(String message, StringTokenizer sTokenizer) {
        try{
            String filename = sTokenizer.nextToken();

        return new ReloadToken(message, filename);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a REMOVE token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getRemoveToken(String message, StringTokenizer sTokenizer) {
        try{
            String filename = sTokenizer.nextToken();

            return new RemoveToken(message, filename);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a REMOVE_ACK token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getRemoveAckToken(String message, StringTokenizer sTokenizer) {
        try{
            String filename = sTokenizer.nextToken();

            return new RemoveAckToken(message, filename);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers an ERROR_FILE_DOES_NOT_EXIST token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getErrorFileDoesNotExistToken(String message, StringTokenizer sTokenizer) {
        if(sTokenizer.hasMoreTokens()){
            String filename = sTokenizer.nextToken();

            return new ErrorFileDoesNotExistFilenameToken(message, filename);
        }
        else{
            return new ErrorFileDoesNotExistToken(message);
        }
    }

    /**
     * Gathers a LIST token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getListToken(String message, StringTokenizer sTokenizer) {
        try{
            if(sTokenizer.hasMoreTokens()){
                HashMap<String,Integer> files = new HashMap<String,Integer>();
                while(sTokenizer.hasMoreTokens()){
                    String filename = sTokenizer.nextToken();

                    int filesize = Integer.parseInt(sTokenizer.nextToken());

                    files.put(filename, filesize);
                }
    
                return new ListFilesToken(message, files);
            }
            else if(message.length() == 5){ // ERROR FIX : Case where there are no files but is still a LIST filenames token - just LIST followed by space and therefore has 5 characters
                HashMap<String,Integer> files = new HashMap<String,Integer>();
                return new ListFilesToken(message, files);
            }
            else{
                return new ListToken(message);
            }
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a REBALANC token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getRebalanceToken(String message, StringTokenizer sTokenizer) {
        try{
            
            // Files to send //

            int numberOfFilesToSend = Integer.parseInt(sTokenizer.nextToken());

            ArrayList<FileToSend> filesToSend = new ArrayList<FileToSend>();

            for(int i = 0; i < numberOfFilesToSend; i++){
                String filename = sTokenizer.nextToken();

                int filesize = Integer.parseInt(sTokenizer.nextToken());

                int numberOfDStores = Integer.parseInt(sTokenizer.nextToken());

                ArrayList<Integer> ports = new ArrayList<Integer>();

                for(int j = 0; j < numberOfDStores; j++){
                    int port = Integer.parseInt(sTokenizer.nextToken());

                    ports.add(port);
                }

                FileToSend fileToSend = new FileToSend(filename, filesize, ports);
                filesToSend.add(fileToSend);
            }

            // Files to remove //

            int numberOfFilesToRemove = Integer.parseInt(sTokenizer.nextToken());

            ArrayList<String> filesToRemove = new ArrayList<String>();

            for(int i = 0; i < numberOfFilesToRemove; i++){
                String filename = sTokenizer.nextToken();

                filesToRemove.add(filename);
            }

            return new RebalanceToken(message, filesToSend, filesToRemove);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }

    /**
     * Gathers a REBALANCE_STORE token from a message string.
     * 
     * @param message
     * @param sTokenizer
     * @return
     */
    private static Token getRebalanceStoreToken(String message, StringTokenizer sTokenizer) {
        try{
            String filename = sTokenizer.nextToken();
            int filesize = Integer.parseInt(sTokenizer.nextToken());
            return new RebalanceStoreToken(message, filename, filesize);
        }
        catch(Exception e){
            return new InvalidRequestToken(message);
        }
    }
}