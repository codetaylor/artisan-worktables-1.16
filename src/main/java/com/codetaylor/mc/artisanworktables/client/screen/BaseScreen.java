package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.tile.WorktableTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
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
  }

  public <T extends TileEntity> T getTile() {

    //noinspection unchecked
    return (T) this.container.getTile();
  }

  protected ResourceLocation getBackgroundTexture() {

    WorktableTileEntity tile = (WorktableTileEntity) this.container.getTile();
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
}
