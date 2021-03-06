package team.chisel.client;

import java.util.Random;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class ClientUtil {

    public static final Random rand = new Random();
    public static final TRSRTransformation DEFAULT_BLOCK_THIRD_PERSON_TRANSOFRM = new TRSRTransformation(new Vector3f(0, 1.5f / 16f, -2.75f / 16f), TRSRTransformation.quatFromXYZDegrees(new Vector3f(
            10, -45, 170)), new Vector3f(0.375f, 0.375f, 0.375f), null);
    public static final Matrix4f DEFAULT_BLOCK_THIRD_PERSON_MATRIX = DEFAULT_BLOCK_THIRD_PERSON_TRANSOFRM.getMatrix();

    public static void playSound(World world, BlockPos pos, String sound) {
        playSound(world, pos, sound, SoundCategory.BLOCKS);
    }
    
    public static void playSound(World world, BlockPos pos, String sound, SoundCategory category) {
        Minecraft.getMinecraft().theWorld.playSound(pos, new SoundEvent(new ResourceLocation(sound)), category, 0.3f + 0.7f * rand.nextFloat(), 0.6f + 0.4f * rand.nextFloat(), true);
    }

    public static void addHitEffects(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        state = state.getActualState(world, pos);

        if (state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AxisAlignedBB bb = state.getBoundingBox(world, pos);

            double d0 = (double) i + rand.nextDouble() * (bb.maxX - bb.minX - (double) (f * 2.0F)) + (double) f + bb.minX;
            double d1 = (double) j + rand.nextDouble() * (bb.maxY - bb.minY - (double) (f * 2.0F)) + (double) f + bb.minY;
            double d2 = (double) k + rand.nextDouble() * (bb.maxZ - bb.minZ - (double) (f * 2.0F)) + (double) f + bb.minZ;

            if (side == EnumFacing.DOWN) {
                d1 = (double) j + bb.minY - f;
            }

            if (side == EnumFacing.UP) {
                d1 = (double) j + bb.maxY + f;
            }

            if (side == EnumFacing.NORTH) {
                d2 = (double) k + bb.minZ - f;
            }

            if (side == EnumFacing.SOUTH) {
                d2 = (double) k + bb.maxZ + f;
            }

            if (side == EnumFacing.WEST) {
                d0 = (double) i + bb.minX - f;
            }

            if (side == EnumFacing.EAST) {
                d0 = (double) i + bb.maxX + f;
            }

            ((ParticleDigging) Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), d0, d1, d2, 0.0D, 0.0D, 0.0D,
                    Block.getIdFromBlock(state.getBlock()))).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F)
                    .setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture());
            ;
        }
    }

    public static void addDestroyEffects(World world, BlockPos pos, IBlockState state) {

        state = state.getActualState(world, pos);
        int i = 4;

        for (int j = 0; j < i; ++j) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < i; ++l) {
                    double d0 = (double) pos.getX() + ((double) j + 0.5D) / (double) i;
                    double d1 = (double) pos.getY() + ((double) k + 0.5D) / (double) i;
                    double d2 = (double) pos.getZ() + ((double) l + 0.5D) / (double) i;
                    ((ParticleDigging) Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY()
                            - 0.5D, d2 - pos.getZ() - 0.5D, Block.getIdFromBlock(state.getBlock()))).setBlockPos(pos).setParticleTexture(
                            Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture());
                    ;
                }
            }
        }
    }
}
