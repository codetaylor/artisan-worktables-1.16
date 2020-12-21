package com.codetaylor.mc.artisanworktables.common.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class MageParticle
    extends PortalParticle {

  private final double x;
  private final double y;
  private final double z;

  protected MageParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {

    super(world, x, y, z, motionX, motionY, motionZ);

    this.prevPosX = x + motionX;
    this.prevPosY = y + motionY;
    this.prevPosZ = z + motionZ;

    this.posX = this.prevPosX;
    this.posY = this.prevPosY;
    this.posZ = this.prevPosZ;

    this.x = this.posX;
    this.y = this.posY;
    this.z = this.posZ;

    this.particleScale = 0.1F * (this.rand.nextFloat() * 0.5f + 0.4f);

    float f = this.rand.nextFloat() * 0.25f + 0.75f;
    this.particleRed = 145 / 255f * f;
    this.particleGreen = 82 / 255f * f;
    this.particleBlue = 198 / 255f * f;

    this.maxAge = (int) (Math.random() * 10.0D) + 30;

    this.canCollide = false;
  }

  @Nonnull
  @Override
  public IParticleRenderType getRenderType() {

    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public float getScale(float scaleFactor) {

    return this.particleScale;
  }

  @Override
  public void tick() {

    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    if (this.age++ >= this.maxAge) {
      this.setExpired();

    } else {
      float f = (float) this.age / (float) this.maxAge;
      f = 1.0F - f;
      float f1 = 1.0F - f;
      f1 = f1 * f1;
      f1 = f1 * f1;
      this.posX = this.x + this.motionX * (double) f;
      this.posY = this.y + this.motionY * (double) f - (double) (f1 * 1.2F);
      this.posZ = this.z + this.motionZ * (double) f;
      this.particleAlpha = f;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class Factory
      implements IParticleFactory<BasicParticleType> {

    private final IAnimatedSprite spriteSet;

    public Factory(IAnimatedSprite spriteSet) {

      this.spriteSet = spriteSet;
    }

    public Particle makeParticle(@Nonnull BasicParticleType type, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

      MageParticle particle = new MageParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
      particle.selectSpriteRandomly(this.spriteSet);
      return particle;
    }
  }
}
