package shiptalk.com.shiptalk.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import org.jetbrains.annotations.NotNull;
import shiptalk.com.shiptalk.data.Message;
import shiptalk.com.shiptalk.data.ResponseError;
import shiptalk.com.shiptalk.data.source.MessagesDataSource;
import shiptalk.com.shiptalk.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageService implements MessagesDataSource {

    private final Gson gson = new Gson();

    private final PubNub pubNub;

    @Override
    public void getMessagesFromChannel(@NotNull String channelId, final @NotNull GetMessagesCallback callback) {
        pubNub.history()
                .channel(channelId)
                .count(Constants.MESSAGE_HISTORY_COUNT)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if (!status.isError()) {

                            List<Message> messages = new ArrayList<>();
                            for (PNHistoryItemResult pnHistoryItemResult : result.getMessages()) {
                                Message message = jsonToMessage(pnHistoryItemResult.getEntry());
                                if(message != null && message.isValidated()){
                                    messages.add(message);
                                }
                            }
                            callback.onMessagesLoaded(filterUniqueMessages(messages));
                        }
                        else{
                            callback.onMessagesNotLoaded(new ResponseError(0, "Some error retrieving the PubNub messages"));
                        }
                    }
                });
    }

    private List<Message> filterUniqueMessages(List<Message> unfilteredMessages){
        if(unfilteredMessages.size() < 2){
            return unfilteredMessages;
        }

        HashMap<String, Message> uniqueMessages= new HashMap<>();

        for(Message message : unfilteredMessages){
            //Check for concurrency
            if(uniqueMessages.containsKey(message.getMessageId())){
                Message messageUnique = uniqueMessages.get(message.getMessageId());
                if((messageUnique != null)
                        && (messageUnique.getTimeCreated() != null)
                        && (message.getTimeCreated() != null)
                        && messageUnique.getTimeCreated() < message.getTimeCreated()){
                    uniqueMessages.put(message.getMessageId(), message);
                }
            }
            else{
                uniqueMessages.put(message.getMessageId(), message);
            }
        }

        return new ArrayList<>(uniqueMessages.values());
    }

    public MessageService() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBLICS_KEY);
        this.pubNub = new PubNub(pnConfiguration);
    }

    private Message jsonToMessage(JsonElement jsonElement) {
        try{
            return gson.fromJson(jsonElement, Message.class);
        }
        catch(JsonSyntaxException e){
            return null;
        }
    }

    @Override
    public void sendMessageForChannel(@NotNull Map message, @NotNull String channelId, final @NotNull GetSentMessageCallback callback) {
        pubNub.publish()
                .channel(channelId)
                .message(message)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (!status.isError()) {
                            callback.onMessageSent();
                        }
                        else{
                            callback.onMessageNotSent(new ResponseError(0, "Some error sending message"));
                        }
                    }
                });
    }
}