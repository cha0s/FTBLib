package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;

/**
 * @author LatvianModder
 */
public class ForgeUniverseClosedEvent extends ForgeUniverseEvent
{
    public ForgeUniverseClosedEvent(IUniverse universe)
    {
        super(universe);
    }
}