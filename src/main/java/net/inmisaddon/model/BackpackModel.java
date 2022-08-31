package net.inmisaddon.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class BackpackModel extends Model {

    private final ModelPart base;

    public BackpackModel(ModelPart base) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.base = base.getChild("base");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        base.addChild("cube_r1", ModelPartBuilder.create().uv(50, 0).mirrored().cuboid(-5.0F, -5.0F, -4.0F, 5.0F, 10.0F, 2.0F, new Dilation(0.0F)).mirrored(false),
                ModelTransform.of(-1.0F, -5.0F, -1.0F, -3.1416F, -1.3963F, 3.1416F));
        base.addChild("cube_r2", ModelPartBuilder.create().uv(50, 0).mirrored().cuboid(-7.0F, -5.0F, 0.0F, 5.0F, 10.0F, 2.0F, new Dilation(0.0F)).mirrored(false),
                ModelTransform.of(-1.0F, -5.0F, 1.0F, 0.0F, -1.3963F, 0.0F));
        base.addChild("cube_r3",
                ModelPartBuilder.create().uv(38, 0).cuboid(0.0F, -5.0F, 3.0F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F)).uv(38, 0).cuboid(0.0F, -5.0F, -2.0F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.of(1.0F, -11.0F, -2.5F, -1.5708F, -0.7854F, 1.5708F));
        base.addChild("cube_r4",
                ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)).uv(0, 0).mirrored()
                        .cuboid(-1.0F, -4.0F, 4.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false).uv(54, 12).cuboid(3.0F, -9.0F, -2.0F, 1.0F, 3.0F, 4.0F, new Dilation(0.0F)).uv(0, 14)
                        .cuboid(3.0F, -5.0F, -3.0F, 2.0F, 5.0F, 6.0F, new Dilation(0.0F)).uv(25, 6).mirrored().cuboid(-1.0F, -10.0F, -4.0F, 4.0F, 10.0F, 8.0F, new Dilation(0.01F)).mirrored(false),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        base.addChild("cube_r5", ModelPartBuilder.create().uv(1, 0).cuboid(-3.75F, -0.25F, -4.0F, 4.0F, 4.0F, 10.0F, new Dilation(0.0F)),
                ModelTransform.of(1.0F, -11.0F, 3.5F, -1.5708F, -0.7854F, 1.5708F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
