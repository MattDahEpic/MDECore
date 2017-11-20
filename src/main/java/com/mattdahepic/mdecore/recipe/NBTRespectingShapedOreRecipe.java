package com.mattdahepic.mdecore.recipe;

import com.mattdahepic.mdecore.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class NBTRespectingShapedOreRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    protected ItemStack output = null;
    protected List<ItemStack> input = null;
    protected int width = 0;
    protected int height = 0;

    public NBTRespectingShapedOreRecipe (Block result, Object... recipe) {this(new ItemStack(result),recipe);}
    public NBTRespectingShapedOreRecipe (Item result, Object... recipe) {this(new ItemStack(result),recipe);}
    public NBTRespectingShapedOreRecipe (ItemStack result, Object... recipe) {
        this.output = result.copy();
        String shape = "";
        int i = 0;

        if (recipe[i] instanceof String[]) {
            String[] parts = (String[])recipe[i++];
            for (String s : parts) {
                width = s.length();
                shape += s;
            }
            height = parts.length;
        } else {
            while (recipe[i] instanceof String) {
                String s = (String)recipe[i++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length()) {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp :  recipe)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, ItemStack> itemMap = new HashMap<>();

        for (; i < recipe.length; i++) {
            Character chr = (Character)recipe[i];
            i++;
            Object in = recipe[i];

            if (in instanceof ItemStack) {
                itemMap.put(chr,((ItemStack)in).copy());
            } else if (in instanceof Item) {
                itemMap.put(chr,new ItemStack((Item)in));
            } else if (in instanceof Block) {
                itemMap.put(chr,new ItemStack((Block)in));
            } else if (in instanceof String) {
                OreDictionary.getOres((String)in).forEach(new Consumer<ItemStack>() {
                    @Override
                    public void accept(ItemStack s) {
                        itemMap.put(chr,s);
                    }
                });
            } else {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }

        input = new ArrayList<>(itemMap.size());
        for (char chr : shape.toCharArray()) {
            input.add(itemMap.get(chr));
        }
    }

    @Override public ItemStack getCraftingResult(InventoryCrafting c){ return output.copy(); }
    @Override public boolean canFit(int width, int height){ return input.size() <= width * height; }
    @Override public ItemStack getRecipeOutput(){ return output; }
    public List<ItemStack> getInput()
    {
        return this.input;
    }
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int matches = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; ++y) {
                Object target = input.get(x+y*width);
                ItemStack slot = inv.getStackInRowAndColumn(x,y);

                if (target instanceof ItemStack) {
                    if (isItemSame((ItemStack)target,slot)) matches++;
                } else if (target instanceof List) {
                    for (ItemStack s : (List<ItemStack>)target) {
                        if (isItemSame(s,slot)) {
                            matches++;
                            break;
                        }
                    }
                }
            }
        }
        return matches == input.size();
    }

    @Override public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    public boolean isItemSame (ItemStack stack1, ItemStack stack2) {
        return ItemHelper.isSameIgnoreStackSize(stack1,stack2,true);
    }
}
