package shiptalk.com.shiptalk.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.jetbrains.annotations.NotNull;
import shiptalk.com.shiptalk.data.Message;
import shiptalk.com.shiptalk.data.ResponseError;
import shiptalk.com.shiptalk.data.source.MessagesDataSource;
import shiptalk.com.shiptalk.utils.Constants;

import java.lang.reflect.Array;
import java.util.*;

public class MessageService implements MessagesDataSource {

    private final Gson gson = new Gson();

    private final PubNub pubNub;

    @Override
    public void getMessagesFromChannel(@NotNull String channelId, final @NotNull GetMessagesCallback callback, final @NotNull GetNewMessageCallback newMessageCallback) {
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
                                if (message != null && message.isValidated()) {
                                    messages.add(message);
                                }
                            }
                            callback.onMessagesLoaded(filterUniqueMessages(messages));
                        } else {
                            callback.onMessagesNotLoaded(new ResponseError(0, "Some error retrieving the PubNub messages"));
                        }
                    }
                });

        subscribeToChannel(channelId, newMessageCallback);
    }

    public void subscribeToChannel(@NotNull String channelId, final @NotNull GetNewMessageCallback callback) {
        pubNub.subscribe().channels(Arrays.asList(channelId)).execute();
        SubscribeCallback subscribeCallback = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // internet got lost, do some magic and call reconnect when ready
                    pubnub.reconnect();
                } else if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                    // do some magic and call reconnect when ready
                    pubnub.reconnect();
                } else {
                    callback.onMessageNotLoaded(new ResponseError(2, "Some error when subscribed"));
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Message messageObject = jsonToMessage(message.getMessage());
                if (messageObject != null && messageObject.isValidated()) {
                    callback.onMessageLoaded(messageObject);
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        };
        pubNub.addListener(subscribeCallback);
    }

    private ArrayList<Message> filterUniqueMessages(List<Message> unfilteredMessages) {
        if (unfilteredMessages.size() < 2) {
            return new ArrayList<>(unfilteredMessages);
        }

        HashMap<String, Message> uniqueMessages = new HashMap<>();

        for (Message message : unfilteredMessages) {
            //Check for concurrency
            if (uniqueMessages.containsKey(message.getMessageId())) {
                Message messageUnique = uniqueMessages.get(message.getMessageId());
                if ((messageUnique != null)
                        && (messageUnique.getTimeCreated() != null)
                        && (message.getTimeCreated() != null)
                        && messageUnique.getTimeCreated() < message.getTimeCreated()) {
                    uniqueMessages.put(message.getMessageId(), message);
                }
            } else {
                uniqueMessages.put(message.getMessageId(), message);
            }
        }

        HashMap<String, Message> uniqueMessagesNoBlockers = new HashMap<>();

        for(Message message : uniqueMessages.values()){
            if(!message.isBlocked()){
                uniqueMessagesNoBlockers.put(message.getMessageId(), message);
            }
        }

        return orderChronologically(uniqueMessagesNoBlockers);
    }

    private ArrayList<Message> orderChronologically(HashMap<String, Message> messages) {
        ArrayList<Message>  messagesList = new ArrayList<Message>(messages.values());
        if(messagesList.isEmpty()){
            return messagesList;
        }
        Collections.sort(messagesList, new ComparatorClass());
        return messagesList;
    }

    class ComparatorClass implements Comparator<Message>{

        @Override
        public int compare(Message m1, Message m2) {
            return m2.getTimeCreated().compareTo(m1.getTimeCreated());
        }
    }

    public MessageService() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBLICS_KEY);
        this.pubNub = new PubNub(pnConfiguration);
    }

    private Message jsonToMessage(JsonElement jsonElement) {
        try {
            return gson.fromJson(jsonElement, Message.class);
        } catch (JsonSyntaxException e) {
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
                        } else {
                            callback.onMessageNotSent(new ResponseError(0, "Some error sending message"));
                        }
                    }
                });
    }
}