package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class PropertyJson extends PropertyBase
{
    public static final String ID = "json";
    public static final Color4I COLOR = new Color4I(false, 0xFFFFAA49);

    private JsonElement value;

    public PropertyJson()
    {
        this(new JsonObject());
    }

    public PropertyJson(JsonElement v)
    {
        value = v;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getJsonElement();
    }

    public void setJsonElement(JsonElement v)
    {
        value = v;
    }

    public JsonElement getJsonElement()
    {
        return value;
    }

    @Override
    public void writeData(ByteBuf data)
    {
        NetUtils.writeJsonElement(data, getJsonElement());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setJsonElement(NetUtils.readJsonElement(data));
    }

    @Override
    public String getString()
    {
        return getJsonElement().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return getJsonElement().getAsBoolean();
    }

    @Override
    public int getInt()
    {
        return getJsonElement().getAsInt();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyJson(getJsonElement());
    }

    @Override
    public Color4I getColor()
    {
        return COLOR;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setJsonElement(json);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return getJsonElement();
    }
}