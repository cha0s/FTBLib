package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * @author LatvianModder
 */
public interface FTBLibAPI
{
    Collection<IFTBLibPlugin> getAllPlugins();

    Collection<ITickable> ticking();

    IPackModes getPackModes();

    ISharedServerData getServerData();

    ISharedClientData getClientData();

    default ISharedData getSidedData(Side side)
    {
        return side.isServer() ? getServerData() : getClientData();
    }

    @Nullable
    IUniverse getUniverse();

    void addServerCallback(int timer, Runnable runnable);

    void loadWorldData(MinecraftServer server);

    void reload(Side side, ICommandSender sender, EnumReloadType type);

    void openGui(ResourceLocation guiID, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data);

    void sendNotification(@Nullable EntityPlayer player, INotification n);

    void editServerConfig(EntityPlayerMP player, @Nullable NBTTagCompound nbt, IConfigContainer configContainer);

    void displayGuide(EntityPlayer player, GuidePage page);

    IConfigValue getConfigValueFromID(String id);

    Map<String, IRankConfig> getRankConfigRegistry();

    void handleMessage(MessageBase<?> message, MessageContext context, Side side);
}