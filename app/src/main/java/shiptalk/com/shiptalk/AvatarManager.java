package shiptalk.com.shiptalk;

import android.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvatarManager {

    private static AvatarManager INSTANCE;

    private final List<Integer> avatars;

    private final Map<Pair<String,String>, Integer> avatarFromUserAndChannel;

    private final Map<String, Integer> avatarSequenceByChannel;

    private AvatarManager() {
        this.avatars = new ArrayList<>();
        this.avatarFromUserAndChannel = new HashMap<>();
        this.avatarSequenceByChannel = new HashMap<>();

        Field[] fields = shiptalk.com.shiptalk.R.drawable.class.getFields();

        for (Field field : fields) {
            if (field.getName().startsWith("icons8")) {
                try {
                    int avatarId = field.getInt(shiptalk.com.shiptalk.R.drawable.class);
                    System.out.println("AvatarId" + avatarId);
                    avatars.add(avatarId);
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    public int getNextAvailableAvatarForUserAndChannel(String userId, String channel) {
        Pair<String,String> userAndChannel = new Pair<>(userId,channel);

        Integer avatarId = avatarFromUserAndChannel.get(userAndChannel);

        if(avatarId !=  null){
            return avatarId;
        }

        int avatarSequence = avatarSequenceByChannel.get(channel) != null ? avatarSequenceByChannel.get(channel) : 0;

        avatarId = avatars.get(avatarSequence);
        avatarFromUserAndChannel.put(userAndChannel, avatarId);

        if(avatarSequence < avatars.size()-1){
            avatarSequence++;
            avatarSequenceByChannel.put(channel,avatarSequence);
        }

        return avatarId;
    }

    public static AvatarManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AvatarManager();
        }

        return INSTANCE;
    }
}