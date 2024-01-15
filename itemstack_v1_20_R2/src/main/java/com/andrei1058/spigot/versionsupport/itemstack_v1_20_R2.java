package com.tomkeuper.spigot.versionsupport;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.projectile.IProjectile;
import net.minecraft.world.item.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

@SuppressWarnings("unused")
public class itemstack_v1_20_R2 implements ItemStackSupport {
    @Nullable
    public ItemStack getInHand(@NotNull Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Nullable
    public ItemStack getInOffHand(@NotNull Player player) {
        return player.getInventory().getItemInOffHand();
    }

    @Nullable
    public ItemStack createItem(String material, int amount, byte data) {
        Material m;
        try {
            m = Material.valueOf(material);
        } catch (Exception e) {
            return null;
        }
        return new ItemStack(m, amount);
    }

    public ItemStack createItem(Material material, int amount, byte data) {
        return new ItemStack(material, amount);
    }

    public ItemStack addTag(ItemStack itemStack, String key, String value) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        var tag = nmsItem.v();
        if (tag == null) {
            tag = new NBTTagCompound();
            nmsItem.c(tag);
        }
        tag.a(key, value);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public boolean hasTag(ItemStack itemStack, String key) {
        var tag = CraftItemStack.asNMSCopy(itemStack).v();
        if (null == tag) {
            return false;
        }
        return tag.e(key);
    }

    @Nullable
    public String getTag(ItemStack itemStack, String key) {
        var tag = CraftItemStack.asNMSCopy(itemStack).v();
        if (tag == null) {
            return null;
        }
        return tag.l(key);
    }

    public ItemStack removeTag(ItemStack itemStack, String key) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        var tag = nmsItem.v();
        if (tag == null || !tag.e(key)) {
            return itemStack;
        }
        tag.r(key);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public void setUnbreakable(@NotNull ItemStack itemStack, boolean unbreakable) {
        if (itemStack.getItemMeta() == null) {
            return;
        }
        itemStack.getItemMeta().setUnbreakable(true);
    }

    public void minusAmount(Player p, @NotNull ItemStack i, int amount) {
        if (i.getAmount() - amount <= 0) {
            p.getInventory().removeItem(i);
            return;
        }
        i.setAmount(i.getAmount() - amount);
        p.updateInventory();
    }

    public double getDamage(ItemStack itemStack) {
        var tag = CraftItemStack.asNMSCopy(itemStack).v();
        if (tag == null) return 0D;
        return tag.k("generic.attackDamage");
    }

    public boolean isArmor(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack) == null) return false;
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem == null) {
            return false;
        }
        if (nmsItem.d() == null) {
            return false;
        }
        return nmsItem.d() instanceof ItemArmor;

    }

    public boolean isTool(ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem == null) {
            return false;
        }
        if (nmsItem.d() == null) {
            return false;
        }
        return nmsItem.d() instanceof ItemTool;

    }

    public boolean isSword(ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem == null) {
            return false;
        }
        if (nmsItem.d() == null) {
            return false;
        }
        return nmsItem.d() instanceof ItemSword;

    }

    public boolean isAxe(ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem == null) {
            return false;
        }
        if (nmsItem.d() == null) {
            return false;
        }
        return nmsItem.d() instanceof ItemAxe;

    }

    public boolean isBow(ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem == null) {
            return false;
        }
        if (nmsItem.d() == null) {
            return false;
        }
        return nmsItem.d() instanceof ItemBow;
    }

    public boolean isProjectile(ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem == null) {
            return false;
        }
        if (nmsItem.H() == null) {
            return false;
        }
        return nmsItem.H() instanceof IProjectile;
    }

    @Override
    public boolean isPlayerHead(@NotNull ItemStack itemStack) {
        return itemStack.getType() == Material.PLAYER_HEAD;
    }

    @Override
    public org.bukkit.inventory.ItemStack applyPlayerSkinOnHead(Player player, org.bukkit.inventory.ItemStack copyTagFrom) {
        var bukkitHead = new org.bukkit.inventory.ItemStack(Material.PLAYER_HEAD, 1);

        if (copyTagFrom != null) {
            var nmsItem = CraftItemStack.asNMSCopy(bukkitHead);
            nmsItem.c(CraftItemStack.asNMSCopy(copyTagFrom).v());
            bukkitHead = CraftItemStack.asBukkitCopy(nmsItem);
        }

        var headMeta = (SkullMeta) bukkitHead.getItemMeta();
        Field profileField;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, ((CraftPlayer) player).getProfile());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        bukkitHead.setItemMeta(headMeta);
        return bukkitHead;
    }

    @Override
    public ItemStack applySkinTextureOnHead(String texture, ItemStack copyTagFrom) {

        var bukkitHead = new org.bukkit.inventory.ItemStack(Material.PLAYER_HEAD, 1);

        if (copyTagFrom != null) {
            net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy(bukkitHead);
            i.c(CraftItemStack.asNMSCopy(copyTagFrom).v());
            bukkitHead = CraftItemStack.asBukkitCopy(i);
        }

        var profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        var headMeta = (SkullMeta) bukkitHead.getItemMeta();
        Field profileField;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        bukkitHead.setItemMeta(headMeta);
        return bukkitHead;
    }
}