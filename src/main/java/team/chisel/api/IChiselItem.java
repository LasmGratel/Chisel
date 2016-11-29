package team.chisel.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.api.carving.ICarvingVariation;

/**
 * Implement this on items which can be used to chisel blocks.
 */
public interface IChiselItem {

    /**
     * Checks whether the chisel can have its GUI opened
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The player holding the chisel. It can always be assumed that the player's current item will be this.
     * @param hand
     *            The {@link EnumHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @return True if the GUI should open. False otherwise.
     */
    boolean canOpenGui(World world, EntityPlayer player, EnumHand hand);

    /**
     * The type of GUI to open. Currently the only valid options are normal and hitech.
     * <p>
     * Use {@link IChiselGuiType.ChiselGuiType} as return value.
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The player holding the chisel. It can always be assumed that the player's current item will be this.
     * @param hand
     *            The {@link EnumHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @return
     */
    IChiselGuiType getGuiType(World world, EntityPlayer player, EnumHand hand);

    /**
     * Called when an item is chiseled using this chisel
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The {@link EntityPlayer} performing the chiseling.
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link ICarvingVariation} representing the target item
     * @return True if the chisel should be damaged. False otherwise.
     */
    boolean onChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target);

    /**
     * Called to check if this {@link ItemStack} can be chiseled in this chisel. If not, there will be no possible variants displayed in the GUI.
     * <p>
     * It is not necessary to take into account whether this item <i>has</i> any variants, this method will only be called after that check.
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The {@link EntityPlayer} performing the chiseling.
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link ICarvingVariation} representing the target item
     * @return True if the current target can be chiseled into anything. False otherwise.
     */
    boolean canChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target);

    /**
     * Allows you to control if your item can chisel this block in the world.
     * 
     * @param world
     *            World object
     * @param player
     *            {@link EntityPlayer The player} holding the chisel.
     * @param hand
     *            The {@link EnumHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @param pos
     *            The {@link BlockPos position} of the block being chiseled.
     * @param state
     *            The {@link IBlockState} of the block being chiseled.
     * @return True if the chiseling should take place. False otherwise.
     */
    boolean canChiselBlock(World world, EntityPlayer player, EnumHand hand, BlockPos pos, IBlockState state);

    /**
     * Allows you to control if your item has chiseling modes.
     * 
     * @param player
     *            {@link EntityPlayer The player} holding the chisel.
     * @param hand
     *            The {@link EnumHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @return True if the chisel supports modes. False otherwise.
     */
    boolean hasModes(EntityPlayer player, EnumHand hand);
    
    default ItemStack craftItem(ItemStack chisel, ItemStack source, ICarvingVariation target, EntityPlayer player) {
        int toCraft = source.stackSize;
        if (chisel.isItemStackDamageable()) {
            int damageLeft = chisel.getMaxDamage() - chisel.getItemDamage() + 1;
            toCraft = Math.min(toCraft, damageLeft);
            chisel.damageItem(toCraft, player);
        }
        ItemStack res = target.getStack().copy();
        source.stackSize -= toCraft;
        res.stackSize = toCraft;
        return res;
    }
}