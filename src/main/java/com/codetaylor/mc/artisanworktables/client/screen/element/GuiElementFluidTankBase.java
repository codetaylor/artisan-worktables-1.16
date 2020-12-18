package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.network.CSPacketWorktableTankDestroyFluid;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementFluidTankVertical;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class GuiElementFluidTankBase
    extends GuiElementFluidTankVertical {

  protected final BlockPos blockPos;
  protected final int overlayColor;

  public GuiElementFluidTankBase(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight,
      int overlayColor,
      BlockPos blockPos
  ) {

    super(guiBase, fluidTank, elementX, elementY, elementWidth, elementHeight);
    this.overlayColor = overlayColor;
    this.blockPos = blockPos;
  }

  public void elementClicked(double mouseX, double mouseY, int mouseButton) {

    if (mouseButton == 0 && Screen.hasShiftDown()) {
      ArtisanWorktablesMod.getProxy().getPacketService()
          .sendToServer(new CSPacketWorktableTankDestroyFluid(this.blockPos));
    }
  }

  public List<ITextComponent> tooltipTextGet(List<ITextComponent> list) {

    if (this.fluidTank.getFluid() == FluidStack.EMPTY || this.fluidTank.getFluidAmount() == 0) {
      list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.fluid.empty"));
      list.add(new TranslationTextComponent(this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB")
          .mergeStyle(TextFormatting.GRAY));

    } else {
      Fluid fluid = this.fluidTank.getFluid().getFluid();
      list.add(fluid.getAttributes().getDisplayName(this.fluidTank.getFluid()));
      list.add(new TranslationTextComponent(this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB")
          .mergeStyle(TextFormatting.GRAY));
      list.add(new TranslationTextComponent(
          "gui.artisanworktables.tooltip.fluid.destroy"
      ).mergeStyle(TextFormatting.DARK_GRAY));
    }

    return list;
  }
}
