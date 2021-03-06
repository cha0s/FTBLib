package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class PropertyItemStack extends PropertyBase
{
    public static final String ID = "item_stack";

    private ItemStack value;

    public PropertyItemStack()
    {
    }

    public PropertyItemStack(@Nullable ItemStack is)
    {
        value = is;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    public ItemStack getItem()
    {
        return value;
    }

    /**
     * @param sizeMode 0 - ignore stack size, 1 - itemStack size must be equal, 2 - itemStack size must be equal or larger
     * @return stack that is equal to itemStack. null if none match
     */
    public boolean matchesItem(ItemStack itemStack, int sizeMode)
    {
        ItemStack is = getItem();
        boolean isempty = ItemStackTools.isEmpty(is);
        boolean stackempty = ItemStackTools.isEmpty(itemStack);

        if(isempty || stackempty)
        {
            return isempty == stackempty;
        }

        int issize = ItemStackTools.getStackSize(is);
        int stackSize = ItemStackTools.getStackSize(itemStack);

        Item item0 = itemStack.getItem();
        int meta = itemStack.getMetadata();

        if(is.getItem() == item0 && is.getMetadata() == meta)
        {
            switch(sizeMode)
            {
                case 1:
                    if(issize == stackSize)
                    {
                        return true;
                    }
                    break;
                case 2:
                    if(issize <= stackSize)
                    {
                        return true;
                    }
                    break;
                default:
                    return true;
            }
        }

        return false;
    }

    public void setItem(@Nullable ItemStack is)
    {
        value = is;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getItem();
    }

    @Override
    public String getString()
    {
        ItemStack is = getItem();
        return ItemStackTools.getStackSize(is) + "x " + (ItemStackTools.isEmpty(is) ? "Air" : is.getDisplayName());
    }

    @Override
    public boolean getBoolean()
    {
        return getInt() > 0;
    }

    @Override
    public int getInt()
    {
        return ItemStackTools.getStackSize(getItem());
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyItemStack(getItem());
    }

    @Override
    public void fromJson(JsonElement o)
    {
        setItem(ItemStackSerializer.deserialize(o));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return ItemStackSerializer.serialize(getItem());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        ByteBufUtils.writeItemStack(data, getItem());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setItem(ByteBufUtils.readItemStack(data));
    }
}