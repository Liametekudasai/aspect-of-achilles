package com.liametekudasai.aspectofachilles.client.render;

import com.liametekudasai.aspectofachilles.entity.TetherEntity;
import com.liametekudasai.aspectofachilles.util.ModResourceLocations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

/**
 * NeoForge 1.21.1-friendly tether renderer.
 *
 * <p>The rope is rendered as a lightweight curved strip using the vanilla leash render type.
 * The owner anchor is shifted slightly toward the player's main hand so the tether reads more
 * like a hand-thrown weapon and less like a chest-mounted beam.</p>
 */
public class TetherEntityRenderer extends EntityRenderer<TetherEntity> {

    private static final ResourceLocation TEXTURE = ModResourceLocations.id("textures/entity/tether.png");
    private static final int SEGMENTS = 24;
    private static final float WIDTH = 0.028F;

    public TetherEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(TetherEntity tetherEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Entity owner = tetherEntity.resolveOwner();
        Entity trident = tetherEntity.resolveTrident();
        if (owner != null && trident != null) {
            renderRope(tetherEntity, owner, trident, partialTick, poseStack, bufferSource);
        }

        super.render(tetherEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void renderRope(TetherEntity tetherEntity, Entity owner, Entity trident, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        Vec3 ownerAnchor = getOwnerAnchor(owner, partialTick);
        Vec3 tridentAnchor = getTridentAnchor(trident, partialTick);
        Vec3 ropeVector = tridentAnchor.subtract(ownerAnchor);
        double ropeLength = ropeVector.length();
        if (ropeLength < 1.0E-4D) {
            return;
        }

        Vec3 tetherPos = getLerpedPosition(tetherEntity, partialTick);
        Vec3 side = new Vec3(-ropeVector.z, 0.0D, ropeVector.x);
        if (side.lengthSqr() < 1.0E-6D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        }
        side = side.normalize().scale(WIDTH);

        int ownerBlockLight = owner.level().getBrightness(LightLayer.BLOCK, BlockPos.containing(ownerAnchor));
        int tridentBlockLight = trident.level().getBrightness(LightLayer.BLOCK, BlockPos.containing(tridentAnchor));
        int ownerSkyLight = owner.level().getBrightness(LightLayer.SKY, BlockPos.containing(ownerAnchor));
        int tridentSkyLight = trident.level().getBrightness(LightLayer.SKY, BlockPos.containing(tridentAnchor));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.leash());
        poseStack.pushPose();
        poseStack.translate(ownerAnchor.x - tetherPos.x, ownerAnchor.y - tetherPos.y, ownerAnchor.z - tetherPos.z);
        Matrix4f matrix = poseStack.last().pose();

        for (int i = 0; i <= SEGMENTS; i++) {
            float progress = i / (float) SEGMENTS;
            Vec3 curvePoint = buildCurvePoint(ropeVector, ropeLength, progress);
            Vec3 offset = side.scale(0.55D + 0.45D * (1.0D - Math.abs(progress - 0.5D) * 2.0D));
            int localPackedLight = LightTexture.pack(
                    Mth.lerpInt(progress, ownerBlockLight, tridentBlockLight),
                    Mth.lerpInt(progress, ownerSkyLight, tridentSkyLight)
            );

            int color = (i & 1) == 0 ? 0xFFB59A6A : 0xFF8D744B;
            addVertex(consumer, matrix, curvePoint.add(offset), localPackedLight, color);
            addVertex(consumer, matrix, curvePoint.subtract(offset), localPackedLight, color);
        }

        poseStack.popPose();
    }

    private static Vec3 buildCurvePoint(Vec3 ropeVector, double ropeLength, float progress) {
        double x = ropeVector.x * progress;
        double z = ropeVector.z * progress;
        double y = ropeVector.y * progress;

        double sagStrength = Math.min(0.6D, 0.04D * ropeLength);
        double sag = Math.sin(progress * Math.PI) * sagStrength;
        return new Vec3(x, y - sag, z);
    }

    private static void addVertex(VertexConsumer consumer, Matrix4f matrix, Vec3 position, int packedLight, int argb) {
        int a = (argb >>> 24) & 0xFF;
        int r = (argb >>> 16) & 0xFF;
        int g = (argb >>> 8) & 0xFF;
        int b = argb & 0xFF;
        consumer.addVertex(matrix, (float) position.x, (float) position.y, (float) position.z)
                .setColor(r, g, b, a)
                .setLight(packedLight);
    }

    private static Vec3 getLerpedPosition(Entity entity, float partialTick) {
        float clampedPartialTick = Mth.clamp(partialTick, 0.0F, 1.0F);
        double x = Mth.lerp(clampedPartialTick, entity.xo, entity.getX());
        double y = Mth.lerp(clampedPartialTick, entity.yo, entity.getY());
        double z = Mth.lerp(clampedPartialTick, entity.zo, entity.getZ());
        return new Vec3(x, y, z);
    }

    private static Vec3 getOwnerAnchor(Entity owner, float partialTick) {
        Vec3 base = getLerpedPosition(owner, partialTick);
        double y = owner.getBbHeight() * 0.72D;
        Vec3 anchor = base.add(0.0D, y, 0.0D);

        if (owner instanceof Player player) {
            float bodyRot = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot) * Mth.DEG_TO_RAD;
            double handedness = player.getMainArm() == HumanoidArm.RIGHT ? -1.0D : 1.0D;
            double sideOffset = 0.24D * handedness;
            double forwardOffset = 0.10D;
            anchor = anchor.add(
                    Math.cos(bodyRot) * sideOffset - Math.sin(bodyRot) * forwardOffset,
                    -0.08D,
                    Math.sin(bodyRot) * sideOffset + Math.cos(bodyRot) * forwardOffset
            );
        } else if (owner instanceof LivingEntity livingEntity) {
            anchor = anchor.add(0.0D, -livingEntity.getEyeHeight() * 0.12D, 0.0D);
        }

        return anchor;
    }

    private static Vec3 getTridentAnchor(Entity trident, float partialTick) {
        Vec3 base = getLerpedPosition(trident, partialTick);
        return base.add(0.0D, 0.1D, 0.0D);
    }

    @Override
    public ResourceLocation getTextureLocation(TetherEntity entity) {
        return TEXTURE;
    }
}
