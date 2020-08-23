package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;

import java.io.IOException;

/**
 * @author joserobjr
 */
@PowerNukkitOnly
@Since("1.4.0.0-PN")
public class PositionTrackingDBServerBroadcastPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.POS_TRACKING_SERVER_BROADCAST_PACKET;
    private static final Action[] ACTIONS = Action.values();

    private Action action;
    private int trackingId;
    private CompoundTag tag;

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setAction(Action action) {
        this.action = action;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setTrackingId(int trackingId) {
        this.trackingId = trackingId;
    }

    @Override
    public void encode() {
        putByte((byte) action.ordinal());
        putVarInt(trackingId);
        try {
            putByteArray(NBTIO.writeNetwork(tag));
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    @Override
    public void decode() {
        action = ACTIONS[getByte()];
        trackingId = getVarInt();
        try(FastByteArrayInputStream inputStream = new FastByteArrayInputStream(get())) {
            tag = NBTIO.readNetworkCompressed(inputStream);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public PositionTrackingDBServerBroadcastPacket clone() {
        return (PositionTrackingDBServerBroadcastPacket) super.clone();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public enum Action {
        UPDATE,
        DESTROY,
        NOT_FOUND
    }
}
