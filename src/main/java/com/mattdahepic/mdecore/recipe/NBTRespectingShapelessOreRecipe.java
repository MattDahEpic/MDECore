package com.mattdahepic.mdecore.recipe;

import com.mattdahepic.mdecore.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class NBTRespectingShapelessOreRecipe implements IRecipe {
    protected ItemStack output = null;
    protected ArrayList<Object> input = new ArrayList<Object>();

    public NBTRespectingShapelessOreRecipe (Block result, Object... recipe) {this(new ItemStack(result),recipe);}
    public NBTRespectingShapelessOreRecipe (Item result, Object... recipe) {this(new ItemStack(result),recipe);}
    public NBTRespectingShapelessOreRecipe (ItemStack result, Object... recipe) {
        this.output = result.copy();
        for (Object in : recipe)
        {
            if (in instanceof ItemStack)
            {
                input.add(((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                input.add(new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                input.add(new ItemStack((Block)in));
            }
            else if (in instanceof String)
            {
                input.add(OreDictionary.getOres((String)in));
            }
            else
            {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }
    }

    @Override public int getRecipeSize(){ return input.size(); }
    @Override public ItemStack getRecipeOutput(){ return output; }
    @Override public ItemStack getCraftingResult(InventoryCrafting c){ return output.copy(); }
    public ArrayList<Object> getInput()
    {
        return this.input;
    }

    @SuppressWarnings("unchecked")
    @Override public boolean matches(InventoryCrafting c, World world) {
        ArrayList<Object> required = new ArrayList<Object>(input);

        for (int x = 0; x < c.getSizeInventory(); x++) {
            ItemStack slot = c.getStackInSlot(x);

            if (slot != null) {
                boolean inRecipe = false;
                for (Object req : required) {
                    boolean match = false;
                    if (req instanceof ItemStack) {
                        match = isItemSame((ItemStack)req,slot);
                    } else if (req instanceof List) {
                        for (ItemStack s : (List<ItemStack>)req) {
                            match = isItemSame(s,slot);
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(req);
                        break;
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    public boolean isItemSame (ItemStack stack1, ItemStack stack2) {
        return ItemHelper.isSameIgnoreStackSize(stack1,stack2,true);
    }
}
