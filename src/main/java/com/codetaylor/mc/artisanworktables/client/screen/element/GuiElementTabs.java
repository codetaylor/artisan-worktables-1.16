package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.client.screen.GuiTabOffset;
import com.codetaylor.mc.artisanworktables.common.network.CSPacketWorktableTab;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementClickable;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GuiElementTabs
    extends GuiElementBase
    implements IGuiElementClickable {

  private static final int TAB_WIDTH = 24;
  private static final int TAB_SPACING = 2;
  private static final int TAB_CURRENT_OFFSET = 1;
  private static final int TAB_HEIGHT = 21;
  private static final int TAB_LEFT_OFFSET = 4;
  private static final int TAB_ITEM_HORIZONTAL_OFFSET = 4;
  private static final int TAB_ITEM_VERTICAL_OFFSET = 4;
  private static final int BUTTON_WIDTH = 8;

  private static final int MAX_TAB_COUNT = 6;

  public static boolean RECALCULATE_TAB_OFFSETS = false;
  private static final GuiTabOffset GUI_TAB_OFFSET = new GuiTabOffset(0);

  private final BaseTileEntity worktable;

  public GuiElementTabs(
      GuiContainerBase guiBase,
      BaseTileEntity worktable,
      int elementWidth
  ) {

    super(guiBase, 0, -TAB_HEIGHT, elementWidth, TAB_HEIGHT);
    this.worktable = worktable;

    if (RECALCULATE_TAB_OFFSETS) {
      RECALCULATE_TAB_OFFSETS = false;
      this.calculateInitialTabOffset(worktable, GUI_TAB_OFFSET);
    }
  }

  @Override
  public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    int x = this.elementXModifiedGet();
    int y = this.elementYModifiedGet();

    int tabX = x + TAB_LEFT_OFFSET;
    int tabY = y;

    List<BaseTileEntity> actualJoinedTables = this.worktable.getJoinedTables(
        new ArrayList<>(),
        Minecraft.getInstance().player,
        BaseTileEntity::allowTabs
    );
    List<BaseTileEntity> joinedTables = this.getJoinedTableOffsetView(
        actualJoinedTables,
        GUI_TAB_OFFSET.getOffset()
    );

    this.textureBind(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS);

    // draw arrows

    if (GUI_TAB_OFFSET.getOffset() > 0) {
      // draw left button
      int textureX = (this.worktable.getWorktableGuiTabTextureYOffset() / 12) * (TAB_WIDTH + BUTTON_WIDTH * 2);

      GuiHelper.drawModalRectWithCustomSizedTexture(
          this.elementXModifiedGet() + TAB_LEFT_OFFSET + TAB_ITEM_HORIZONTAL_OFFSET - 18,
          tabY,
          0,
          TAB_WIDTH + textureX,
          (this.worktable.getWorktableGuiTabTextureYOffset() % 12) * TAB_HEIGHT,
          BUTTON_WIDTH,
          TAB_HEIGHT,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
      );
    }

    if (GUI_TAB_OFFSET.getOffset() + MAX_TAB_COUNT < actualJoinedTables.size()) {
      // draw right button
      int textureX = (this.worktable.getWorktableGuiTabTextureYOffset() / 12) * (TAB_WIDTH + BUTTON_WIDTH * 2);

      GuiHelper.drawModalRectWithCustomSizedTexture(
          this.elementXModifiedGet() + this.elementWidthModifiedGet() - 12,
          tabY,
          0,
          TAB_WIDTH + BUTTON_WIDTH + textureX,
          (this.worktable.getWorktableGuiTabTextureYOffset() % 12) * TAB_HEIGHT,
          BUTTON_WIDTH,
          TAB_HEIGHT,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
      );
    }

    // draw tabs

    for (BaseTileEntity joinedTable : joinedTables) {
      int textureX = (joinedTable.getWorktableGuiTabTextureYOffset() / 12) * (TAB_WIDTH + BUTTON_WIDTH * 2);
      int textureY = (joinedTable.getWorktableGuiTabTextureYOffset() % 12) * TAB_HEIGHT;

      if (joinedTable == this.worktable) {
        GuiHelper.drawModalRectWithCustomSizedTexture(
            tabX,
            tabY + TAB_CURRENT_OFFSET,
            0,
            textureX,
            textureY,
            TAB_WIDTH,
            TAB_HEIGHT,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
        );

      } else {
        GuiHelper.drawModalRectWithCustomSizedTexture(
            tabX,
            tabY,
            0,
            textureX,
            textureY,
            TAB_WIDTH,
            TAB_HEIGHT,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
        );
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }

    // draw tab icons
    tabX = x + TAB_LEFT_OFFSET + TAB_ITEM_HORIZONTAL_OFFSET;
    tabY = y + TAB_ITEM_VERTICAL_OFFSET;

    RenderHelper.enableStandardItemLighting();

    for (BaseTileEntity joinedTable : joinedTables) {
      BlockState blockState = joinedTable.getWorld().getBlockState(joinedTable.getPos());
      // TODO
      //blockState = blockState.getBlock().getActualState(blockState, this.worktable.getWorld(), joinedTable.getPos());
      ItemStack itemStack = joinedTable.getItemStackForTabDisplay(blockState);

      if (joinedTable == this.worktable) {
        this.guiBase.getItemRender().renderItemAndEffectIntoGUI(itemStack, tabX, tabY + TAB_CURRENT_OFFSET);

      } else {
        this.guiBase.getItemRender().renderItemAndEffectIntoGUI(itemStack, tabX, tabY);
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }
  }

  @Override
  public void drawForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
    //
  }

  @Override
  public void mouseClicked(double mouseX, double mouseY, int mouseButton) {

    if (mouseButton != 0) {
      return;
    }

    List<BaseTileEntity> actualJoinedTables = this.worktable.getJoinedTables(
        new ArrayList<>(),
        Minecraft.getInstance().player,
        BaseTileEntity::allowTabs
    );
    List<BaseTileEntity> joinedTables = this.getJoinedTableOffsetView(
        actualJoinedTables,
        GUI_TAB_OFFSET.getOffset()
    );

    int yMin = this.elementYModifiedGet();
    int yMax = yMin + TAB_HEIGHT;

    for (int i = 0; i < joinedTables.size(); i++) {
      int xMin = this.elementXModifiedGet() + TAB_ITEM_HORIZONTAL_OFFSET + (TAB_WIDTH + TAB_SPACING) * i;
      int xMax = xMin + TAB_WIDTH;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        BaseTileEntity table = joinedTables.get(i);
        BlockPos pos = table.getPos();
        ArtisanWorktablesMod.getProxy().getPacketService()
            .sendToServer(new CSPacketWorktableTab(pos));
        Minecraft.getInstance()
            .getSoundHandler()
            .play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    int maximumDisplayedTabCount = MAX_TAB_COUNT;

    if (GUI_TAB_OFFSET.getOffset() > 0) {
      // check for left button click
      int xMin = this.elementXModifiedGet() + TAB_LEFT_OFFSET + TAB_ITEM_HORIZONTAL_OFFSET - 18;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        int tabOffset = GUI_TAB_OFFSET.getOffset() - maximumDisplayedTabCount;
        GUI_TAB_OFFSET.setOffset(Math.max(0, tabOffset));
        Minecraft.getInstance()
            .getSoundHandler()
            .play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    if (GUI_TAB_OFFSET.getOffset() + maximumDisplayedTabCount < actualJoinedTables.size()) {
      // check for right button click
      int xMin = this.elementXModifiedGet() + this.elementWidthModifiedGet() - 12;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        GUI_TAB_OFFSET.setOffset(Math.min(
            actualJoinedTables.size() - maximumDisplayedTabCount,
            GUI_TAB_OFFSET.getOffset() + maximumDisplayedTabCount
        ));
        Minecraft.getInstance()
            .getSoundHandler()
            .play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }
  }

  /**
   * Sets the initial tab offset when the gui is opened.
   *
   * @param worktableBase the worktable
   * @param guiTabOffset  the offset
   */
  private void calculateInitialTabOffset(BaseTileEntity worktableBase, GuiTabOffset guiTabOffset) {

    guiTabOffset.setOffset(0);

    List<BaseTileEntity> actualJoinedTables = worktableBase.getJoinedTables(
        new ArrayList<>(),
        Minecraft.getInstance().player,
        BaseTileEntity::allowTabs
    );

    boolean tabInView = false;

    while (!tabInView && !actualJoinedTables.isEmpty()) {
      List<BaseTileEntity> joinedTables = this.getJoinedTableOffsetView(
          actualJoinedTables,
          guiTabOffset.getOffset()
      );

      for (BaseTileEntity joinedTable : joinedTables) {

        if (joinedTable == worktableBase) {
          tabInView = true;
          break;
        }
      }

      if (!tabInView) {
        guiTabOffset.setOffset(Math.min(
            actualJoinedTables.size() - MAX_TAB_COUNT,
            guiTabOffset.getOffset() + MAX_TAB_COUNT
        ));
      }
    }
  }

  private List<BaseTileEntity> getJoinedTableOffsetView(
      List<BaseTileEntity> list,
      int offset
  ) {

    List<BaseTileEntity> result = new ArrayList<>(MAX_TAB_COUNT);

    if (offset + MAX_TAB_COUNT > list.size()) {
      offset = list.size() - MAX_TAB_COUNT;
    }

    if (offset < 0) {
      offset = 0;
    }

    int limit = Math.min(list.size(), offset + MAX_TAB_COUNT);

    for (int i = offset; i < limit; i++) {
      result.add(list.get(i));
    }

    return result;
  }

}
