package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementFluidTankSmall;
import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementTabs;
import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class BaseScreen
    extends GuiContainerBase<BaseContainer> {

  protected final int textColor;

  public BaseScreen(BaseContainer container, PlayerInventory playerInventory, ITextComponent title, int width, int height) {

    super(container, playerInventory, title, width, height);

    // background texture
    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        new Texture(this.getBackgroundTexture(), 0, 0, 256, 256),
        0,
        0,
        this.xSize,
        this.ySize
    ));

    EnumType tableType = container.getTile().getTableType();
    this.textColor = TextColorProvider.getColorFor(tableType);

    BaseTileEntity tile = this.getContainer().getTile();

    this.guiContainerElementAdd(new GuiElementFluidTankSmall(
        this,
        tile.getTank(),
        tile.getPos(),
        this.textColor,
        8,
        17
    ));

    this.guiContainerElementAdd(new GuiElementTabs(
        this,
        tile,
        176
    ));
  }

  public <T extends TileEntity> T getTile() {

    //noinspection unchecked
    return (T) this.container.getTile();
  }

  protected ResourceLocation getBackgroundTexture() {

    BaseTileEntity tile = this.container.getTile();
    String typeName = tile.getTableType().getName();
    String tierName = tile.getTableTier().getName();
    return new ResourceLocation(ArtisanWorktablesMod.MOD_ID, "textures/gui/" + tierName + "_" + typeName + ".png");
  }

  @Override
  public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {

    super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    GuiHelper.drawStringOutlined(matrixStack, this.title, this.titleX, this.titleY, this.font, this.textColor, false);
    GuiHelper.drawStringOutlined(matrixStack, this.playerInventory.getDisplayName(), this.playerInventoryTitleX, this.playerInventoryTitleY, this.font, this.textColor, false);
  }
}