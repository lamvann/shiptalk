package shiptalk.com.shiptalk.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import org.jetbrains.annotations.NotNull;
import shiptalk.com.shiptalk.data.Message;
import shiptalk.com.shiptalk.data.ResponseError;
import shiptalk.com.shiptalk.data.source.MessagesDataSource;
import shiptalk.com.shiptalk.utils.Constants;

import java.util.ArrayList;
import java.util.List;

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
                                messages.add(jsonToMessage(pnHistoryItemResult.getEntry()));
                            }

                            callback.onMessagesLoaded(messages);
                        }
                        else{
                            callback.onMessagesNotLoaded(new ResponseError(0, "Some error retrieving the PubNub messages"));
                        }
                    }
                });
    }

    public MessageService() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBLICS_KEY);
        this.pubNub = new PubNub(pnConfiguration);
    }

    private Message jsonToMessage(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, Message.class);
    }
}