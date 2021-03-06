package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamData;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.SidebarButton;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FTBLibClientEventHandler
{
    @SubscribeEvent
    public static void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        SharedClientData.INSTANCE.reset();
    }

    /* TODO: Close world / destroy cached data
    @SubscribeEvent
    public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
    }
    */

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        if(FTBLibClientConfig.ITEM_ORE_NAMES.getBoolean())
        {
            Collection<String> ores = ODItems.getOreNames(event.getItemStack());

            if(!ores.isEmpty())
            {
                event.getToolTip().add("Ore Dictionary names:");

                for(String or : ores)
                {
                    event.getToolTip().add("> " + or);
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public static void onDrawDebugText(RenderGameOverlayEvent.Text event)
    {
        if(!Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            if(LMUtils.DEV_ENV)
            {
                event.getLeft().add("[MC " + TextFormatting.GOLD + Loader.MC_VERSION + TextFormatting.WHITE + " DevEnv]");
            }
        }

        Minecraft mc = FTBLibClient.mc();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        double width2 = scaledResolution.getScaledWidth_double();
        double height2 = scaledResolution.getScaledHeight_double();

        for(Entity entity : mc.theWorld.loadedEntityList)
        {
            if(entity != mc.thePlayer && entity.getDistanceSqToEntity(mc.thePlayer) <= 256D)
            {
                Vector4f pos = LMFrustumUtils.worldToViewport((float) entity.posX, (float) entity.posY, (float) entity.posZ);

                //if(pos.z >= 0D)
                {
                    //GuiBase.drawBlankRect(width2 + pos.getX() * 30D - 8D, height2 + pos.getY() * 30D - 8D, 0F, 16D, 16D);
                    GuiBase.drawBlankRect(width2 + pos.getX() * width2 - 4D, height2 + pos.getY() * height2 - 4D, 0F, 8D, 8D);

                    event.getRight().add(pos.toString());

                    //System.out.println(pos);
                }
            }
        }
    }
     */

    // Add Sidebar Buttons //

    @SubscribeEvent
    public static void guiInitEvent(final GuiScreenEvent.InitGuiEvent.Post event)
    {
        if(event.getGui() instanceof InventoryEffectRenderer)
        {
            List<SidebarButton> buttons = FTBLibModClient.getSidebarButtons(false);

            if(!buttons.isEmpty())
            {
                ButtonInvLMRenderer renderer = new ButtonInvLMRenderer(495830);
                event.getButtonList().add(renderer);

                if(!LMUtils.isNEILoaded() && FTBLibClientConfig.ACTION_BUTTONS_ON_TOP.getBoolean())
                {
                    int i = 0;
                    for(SidebarButton button : buttons)
                    {
                        int x = i % 4;
                        int y = i / 4;
                        ButtonInvLM b = new ButtonInvLM(495830 + i, button, 4 + x * 18, 4 + y * 18);
                        event.getButtonList().add(b);
                        renderer.buttons.add(b);
                        i++;
                    }
                }
                else
                {
                    int xSize = 176;
                    int ySize = 166;
                    int buttonX = -17;
                    int buttonY = 8;

                    if(event.getGui() instanceof GuiContainerCreative)
                    {
                        xSize = 195;
                        ySize = 136;
                        buttonY = 6;
                    }
                    boolean hasPotions = !event.getGui().mc.player.getActivePotionEffects().isEmpty();
                    if(hasPotions)
                    {
                        buttonX -= 4;
                        buttonY -= 26;
                    }

                    int guiLeft = (event.getGui().width - xSize) / 2;
                    int guiTop = (event.getGui().height - ySize) / 2;

                    int i = 0;
                    for(SidebarButton button : buttons)
                    {
                        ButtonInvLM b;

                        if(hasPotions)
                        {
                            int x = i % 8;
                            int y = i / 8;
                            b = new ButtonInvLM(495830 + i, button, guiLeft + buttonX - 18 * x, guiTop + buttonY - y * 18);
                        }
                        else
                        {
                            int x = i / 8;
                            int y = i % 8;
                            b = new ButtonInvLM(495830 + i, button, guiLeft + buttonX - 18 * x, guiTop + buttonY + 18 * y);
                        }

                        event.getButtonList().add(b);
                        renderer.buttons.add(b);
                        i++;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if(event.getButton() instanceof ButtonInvLM)
        {
            GuiHelper.playClickSound();
            (((ButtonInvLM) event.getButton()).button).onClicked(MouseButton.LEFT);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    //public void renderGui(RenderGameOverlayEvent event)
    public static void renderGui(TickEvent.RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        //if(event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        if(event.phase == TickEvent.Phase.END && mc.world != null && !mc.gameSettings.hideGUI)
        {
            if(ClientNotifications.shouldRenderTemp())
            {
                ClientNotifications.renderTemp(new ScaledResolution(mc));
            }
        }
    }

    @SubscribeEvent
    public static void renderWorld(RenderWorldLastEvent event)
    {
        FTBLibClient.updateRenderInfo();
    }

    private static class ButtonInvLM extends GuiButton
    {
        public final SidebarButton button;
        public final String title;
        public final boolean renderMessages;

        public ButtonInvLM(int id, SidebarButton b, int x, int y)
        {
            super(id, x, y, 16, 16, "");
            button = b;
            title = StringUtils.translate("sidebar_button." + b.getName());
            renderMessages = b.getName().equals("ftbl.teams_gui");
        }

        @Override
        public void drawButton(Minecraft mc, int mx, int my)
        {
        }
    }

    private static class ButtonInvLMRenderer extends GuiButton
    {
        public final List<ButtonInvLM> buttons;

        public ButtonInvLMRenderer(int id)
        {
            super(id, -1000, -1000, 0, 0, "");
            buttons = new ArrayList<>();
        }

        @Override
        public void drawButton(Minecraft mc, int mx, int my)
        {
            //if(creativeContainer != null && creativeContainer.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex())
            //	return;

            zLevel = 0F;

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1F, 1F, 1F, 1F);

            for(ButtonInvLM b : buttons)
            {
                b.button.icon.draw(b.xPosition, b.yPosition, b.width, b.height, Color4I.NONE);

                if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
                {
                    GuiHelper.drawBlankRect(b.xPosition, b.yPosition, b.width, b.height, Color4I.WHITE_A33);
                }
            }

            for(ButtonInvLM b : buttons)
            {
                if(b.renderMessages && MyTeamData.unreadMessages > 0)
                {
                    String n = String.valueOf(MyTeamData.unreadMessages);
                    int nw = mc.fontRendererObj.getStringWidth(n);
                    int width = 16;
                    GuiHelper.drawBlankRect(b.xPosition + width - nw, b.yPosition - 4, nw + 1, 9, Color4I.LIGHT_RED);

                    mc.fontRendererObj.drawString(n, b.xPosition + width - nw + 1, b.yPosition - 3, 0xFFFFFFFF);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                }

                if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
                {
                    GlStateManager.pushMatrix();
                    double mx1 = mx - 4D;
                    double my1 = my - 12D;

                    int tw = mc.fontRendererObj.getStringWidth(b.title);

                    if(LMUtils.isNEILoaded() || !FTBLibClientConfig.ACTION_BUTTONS_ON_TOP.getBoolean())
                    {
                        mx1 -= tw + 8;
                        my1 += 4;
                    }

                    if(mx1 < 4D)
                    {
                        mx1 = 4D;
                    }
                    if(my1 < 4D)
                    {
                        my1 = 4D;
                    }

                    GlStateManager.translate(mx1, my1, zLevel);

                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GuiHelper.drawBlankRect(-3, -2, tw + 6, 12, Color4I.DARK_GRAY);
                    mc.fontRendererObj.drawString(b.title, 0, 0, 0xFFFFFFFF);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    GlStateManager.popMatrix();
                }
            }

            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }
}