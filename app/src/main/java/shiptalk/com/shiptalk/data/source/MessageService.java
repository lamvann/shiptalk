package shiptalk.com.shiptalk.data.source;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import shiptalk.com.shiptalk.data.Message;
import shiptalk.com.shiptalk.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private final Gson gson = new Gson();

    private final PubNub pubNub;

    public interface MessageServiceCallBack<Payload> {

        void onResponse(Payload result);
    }

    public MessageService() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBLICS_KEY);
        this.pubNub = new PubNub(pnConfiguration);
    }

    public void getChannelMessages(String channel, final MessageServiceCallBack<List<Message>> callBack) {
        pubNub.history()
                .channel(channel)
                .count(Constants.MESSAGE_HISTORY_COUNT)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if (!status.isError()) {

                            List<Message> messages = new ArrayList<>();
                            for (PNHistoryItemResult pnHistoryItemResult : result.getMessages()) {
                                messages.add(jsonToMessage(pnHistoryItemResult.getEntry()));
                            }

                            callBack.onResponse(messages);
                        }
                    }
                });
    }

    private Message jsonToMessage(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, Message.class);
    }
}