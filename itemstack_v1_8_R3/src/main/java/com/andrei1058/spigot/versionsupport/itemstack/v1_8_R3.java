package com.andrei1058.spigot.versionsupport.itemstack;

import com.andrei1058.spigot.versionsupport.ItemStackSupport;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class v1_8_R3 implements ItemStackSupport {
    @Nullable
    public ItemStack getInHand(Player player) {
        return player.getItemInHand();
    }

    @Nullable
    public ItemStack getInOffHand(Player player) {
        return null;
    }

    @Nullable
    public ItemStack createItem(String material, int amount, byte data) {
        Material m;
        try {
            m = Material.valueOf(material);
        } catch (Exception e) {
            return null;
        }
        return new ItemStack(m, amount, data);
    }

    public ItemStack createItem(Material material, int amount, byte data) {
        return new ItemStack(material, amount, data);
    }

    public ItemStack addTag(ItemStack itemStack, String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = cis.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            cis.setTag(tag);
        }
        tag.setString(key, value);
        return CraftItemStack.asBukkitCopy(cis);
    }

    public boolean hasTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        return cis.hasTag() && (cis.getTag().hasKey(key));
    }

    @Nullable
    public String getTag(ItemStack itemStack, String key) {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(itemStack).getTag();
        if (tag == null) return null;
        return tag.getString(key);
    }

    public ItemStack removeTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = cis.getTag();
        if (tag == null) return itemStack;
        if (!tag.hasKey(key)) return itemStack;
        tag.remove(key);
        return CraftItemStack.asBukkitCopy(cis);
    }

    public void setUnbreakable(ItemStack itemStack, boolean unbreakable) {
        itemStack.getItemMeta().spigot().setUnbreakable(unbreakable);
    }

    public void minusAmount(Player p, ItemStack i, int amount) {
        if (i.getAmount() - amount <= 0) {
            p.getInventory().removeItem(i);
            return;
        }
        i.setAmount(i.getAmount() - amount);
        p.updateInventory();
    }

    public double getDamage(ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsStack.hasTag()) return 0D;
        return nmsStack.getTag().getDouble("generic.attackDamage");
    }

    public boolean isArmor(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack) == null) return false;
        if (CraftItemStack.asNMSCopy(itemStack).getItem() == null) return false;
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemArmor;

    }

    public boolean isTool(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack) == null) return false;
        if (CraftItemStack.asNMSCopy(itemStack).getItem() == null) return false;
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemTool;

    }

    public boolean isSword(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack).getItem() == null) return false;
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemSword;

    }

    public boolean isAxe(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack).getItem() == null) return false;
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemAxe;

    }

    public boolean isBow(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack) == null) return false;
        if (CraftItemStack.asNMSCopy(itemStack).getItem() == null) return false;
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemBow;
    }

    public boolean isProjectile(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack) == null) return false;
        if (CraftItemStack.asNMSCopy(itemStack).getItem() == null) return false;
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof IProjectile;
    }
}
