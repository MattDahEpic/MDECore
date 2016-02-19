package com.mattdahepic.mdecore.command.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerInvChest extends InventoryBasic { //thanks ForgeEssentials
    public EntityPlayer viewer;
    public EntityPlayer owner;
    public boolean allowUpdate;

    public PlayerInvChest(EntityPlayer owner, EntityPlayer viewer) {
        super(owner.getName() + "'s Inventory", false, owner.inventory.mainInventory.length);
        this.owner = owner;
        this.viewer = viewer;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        MinecraftForge.EVENT_BUS.register(this);
        allowUpdate = false;
        for (int id = 0; id < owner.inventory.mainInventory.length; ++id)
        {
            setInventorySlotContents(id, owner.inventory.mainInventory[id]);
        }
        allowUpdate = true;
        super.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        MinecraftForge.EVENT_BUS.unregister(this);
        if (allowUpdate)
        {
            for (int id = 0; id < owner.inventory.mainInventory.length; ++id)
            {
                owner.inventory.mainInventory[id] = getStackInSlot(id);
            }
        }
        markDirty();
        super.closeInventory(player);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (allowUpdate)
        {
            for (int id = 0; id < owner.inventory.mainInventory.length; ++id)
            {
                owner.inventory.mainInventory[id] = getStackInSlot(id);
            }
        }
    }

    public void update() {
        allowUpdate = false;
        for (int id = 0; id < owner.inventory.mainInventory.length; ++id)
        {
            setInventorySlotContents(id, owner.inventory.mainInventory[id]);
        }
        allowUpdate = true;
        markDirty();
    }

    @SubscribeEvent
    public void playerTick (TickEvent.PlayerTickEvent e) {
        this.update();
    }
}