package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.AlertScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.SelectorTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SCPacketIncompatible
    implements IMessage<SCPacketIncompatible>,
    IMessageHandler<SCPacketIncompatible, SCPacketIncompatible> {

  private List<String> modIdList;

  @SuppressWarnings("unused")
  public SCPacketIncompatible() {
    // serialization
  }

  public SCPacketIncompatible(List<String> modIdList) {

    this.modIdList = modIdList;
  }

  @Override
  public void encode(SCPacketIncompatible message, PacketBuffer packetBuffer) {

    packetBuffer.writeInt(message.modIdList.size());

    for (String modId : message.modIdList) {
      packetBuffer.writeString(modId);
    }
  }

  @Override
  public SCPacketIncompatible decode(SCPacketIncompatible message, PacketBuffer packetBuffer) {

    int size = packetBuffer.readInt();
    message.modIdList = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {
      message.modIdList.add(packetBuffer.readString());
    }

    return message;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public SCPacketIncompatible onMessage(
      SCPacketIncompatible message,
      Supplier<NetworkEvent.Context> supplier
  ) {

    Minecraft minecraft = Minecraft.getInstance();

    String modList = String.join(", ", message.modIdList.toArray(new String[0]));

    String warningString = I18n.format("gui.artisanworktables.warning");

    minecraft.displayGuiScreen(
        new AlertScreen(
            () -> minecraft.displayGuiScreen(null),
            new StringTextComponent(TextFormatting.RED + warningString),
            new TranslationTextComponent("gui.artisanworktables.incompatibility", modList),
            DialogTexts.GUI_PROCEED
        )
    );

    supplier.get().setPacketHandled(true);

    return null;
  }
}
