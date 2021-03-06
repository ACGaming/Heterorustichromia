package rustichromia.cart.control;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import rustichromia.Rustichromia;
import rustichromia.cart.CartData;
import rustichromia.cart.Control;
import rustichromia.cart.ControlSupplier;
import rustichromia.tile.TileEntityCartControl;

import javax.annotation.Nonnull;
import java.util.Random;

public class Path extends Control {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Rustichromia.MODID, "path");
    public static final ModelResourceLocation ITEM_MODEL = new ModelResourceLocation(new ResourceLocation(Rustichromia.MODID, "cart_control/path"), "inventory");
    public static final ControlSupplier SUPPLIER = new ControlSupplier(RESOURCE_LOCATION, "path", ITEM_MODEL) {
        @Override
        public Control get() {
            return NORTH_SOUTH;
        }
    };

    public static final Path NORTH_SOUTH = new Path(EnumFacing.NORTH);
    public static final Path EAST_WEST = new Path(EnumFacing.EAST);

    Random random = new Random();
    EnumFacing facing;

    public Path(EnumFacing facing) {
        super(RESOURCE_LOCATION);
        this.facing = facing;
    }

    @Override
    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public Control setFacing(EnumFacing facing) {
        switch (facing) {
            case NORTH:
            case SOUTH:
                return NORTH_SOUTH;
            case WEST:
            case EAST:
                return EAST_WEST;
            default:
                throw new IllegalArgumentException("Control orientation can't be " + facing);
        }
    }

    @Override
    public ResourceLocation getTexture(TileEntityCartControl tile, EnumFacing facing) {
        return new ResourceLocation(Rustichromia.MODID,"blocks/cart_control/path_straight");
    }

    @Override
    public boolean isActiveSide(TileEntityCartControl tile, EnumFacing facing) {
        EnumFacing forward = getTrueFacing(tile.getFacing());
        return forward.getAxis() != facing.getAxis();
    }

    @Override
    public void controlCart(TileEntityCartControl tile, CartData cart) {
        EnumFacing forward = getTrueFacing(tile.getFacing());
        EnumFacing backward = getTrueFacing(tile.getFacing()).getOpposite();

        EnumFacing direction = cart.getForward();

        if(direction == forward || direction == backward)
            cart.moveForward();
        else {
            if (random.nextDouble() > 0.5) {
                cart.orient(forward);
            } else {
                cart.orient(backward);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        nbt.setString("facing", getFacing().getName());
        return nbt;
    }

    @Override
    public Control readFromNBT(@Nonnull NBTTagCompound nbt) {
        switch (EnumFacing.byName(nbt.getString("facing"))) {
            case NORTH:
            case SOUTH:
                return NORTH_SOUTH;
            case WEST:
            case EAST:
                return EAST_WEST;
            default:
                return null;
        }
    }
}
