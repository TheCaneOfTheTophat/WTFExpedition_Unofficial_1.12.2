package wtf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class WTFMessageBlockCrackEvent implements IMessage {

    public WTFMessageBlockCrackEvent() {}

    private int stateId;
    private BlockPos pos;

    public WTFMessageBlockCrackEvent(int stateId, BlockPos pos) {
        this.stateId = stateId;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stateId = buf.readInt();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(stateId);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class Handler implements IMessageHandler<WTFMessageBlockCrackEvent, IMessage> {
        @Override
        public IMessage onMessage(WTFMessageBlockCrackEvent message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();

            mc.addScheduledTask(() -> mc.player.world.playEvent(2001, message.pos, message.stateId));

            return null;
        }
    }
}
