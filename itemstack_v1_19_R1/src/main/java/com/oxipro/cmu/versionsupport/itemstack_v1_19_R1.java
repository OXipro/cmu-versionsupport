package com.oxipro.cmu.versionsupport;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.projectile.IProjectile;
import net.minecraft.world.item.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class itemstack_v1_19_R1 implements ItemStackSupport {
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
        return new ItemStack(material, amount, data);
    }

    public ItemStack addTag(ItemStack itemStack, String key, String value) {
        net.minecraft.world.item.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = cis.u();
        if (tag == null) {
            tag = new NBTTagCompound();
            cis.c(tag);
        }
        tag.a(key, value);
        return CraftItemStack.asBukkitCopy(cis);
    }

    public boolean hasTag(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        return cis.u() != null && (cis.u().e(key));
    }

    @Nullable
    public String getTag(ItemStack itemStack, String key) {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(itemStack).u();
        if (tag == null) return null;
        return tag.l(key);
    }

    public ItemStack removeTag(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = cis.u();
        if (tag == null) return itemStack;
        if (!tag.e(key)) return itemStack;
        tag.r(key);
        return CraftItemStack.asBukkitCopy(cis);
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
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsStack.u() == null) return 0D;
        return nmsStack.u().k("generic.attackDamage");
    }

    public boolean isArmor(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy(itemStack) == null) return false;
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (item == null) {
            return false;
        }
        if (item.c() == null) {
            return false;
        }
        return item.c() instanceof ItemArmor;

    }

    public boolean isTool(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (item == null) {
            return false;
        }
        if (item.c() == null) {
            return false;
        }
        return item.c() instanceof ItemTool;

    }

    public boolean isSword(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (item == null) {
            return false;
        }
        if (item.c() == null) {
            return false;
        }
        return item.c() instanceof ItemSword;

    }

    public boolean isAxe(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (item == null) {
            return false;
        }
        if (item.c() == null) {
            return false;
        }
        return item.c() instanceof ItemAxe;

    }

    public boolean isBow(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (item == null) {
            return false;
        }
        if (item.c() == null) {
            return false;
        }
        return item.c() instanceof ItemBow;
    }

    public boolean isProjectile(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (item == null) {
            return false;
        }
        if (item.G() == null) {
            return false;
        }
        return item.G() instanceof IProjectile;
    }

    @Override
    public boolean isPlayerHead(@NotNull ItemStack itemStack) {
        return itemStack.getType() == Material.PLAYER_HEAD;
    }

    @Override
    public org.bukkit.inventory.ItemStack applyPlayerSkinOnHead(Player player, org.bukkit.inventory.ItemStack copyTagFrom) {
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(Material.PLAYER_HEAD, 1);

        if (copyTagFrom != null) {
            net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy(head);
            i.c(CraftItemStack.asNMSCopy(copyTagFrom).u());
            head = CraftItemStack.asBukkitCopy(i);
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, ((CraftPlayer) player).getProfile());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    @Override
    public ItemStack applySkinTextureOnHead(String texture, ItemStack copyTagFrom) {

        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(Material.PLAYER_HEAD, 1);

        if (copyTagFrom != null) {
            net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy(head);
            i.c(CraftItemStack.asNMSCopy(copyTagFrom).u());
            head = CraftItemStack.asBukkitCopy(i);
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public boolean isItem(Material mat) {
        return mat.isItem();
    }

    private static final Random RANDOM = new Random();

    @Override
    public void giveRandomItem(Player player, int slot, List<String> blacklist) {
        if (player == null) return;
        Set<String> blacklistSet = blacklist.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        List<Material> validMaterials = Arrays.stream(Material.values())
                .filter(material -> material != Material.AIR)
                .filter(this::isItem)
                .filter(material -> !blacklistSet.contains(material.name()))
                .collect(Collectors.toList());

        if (validMaterials.isEmpty()) return;

        Material randomMaterial = validMaterials.get(RANDOM.nextInt(validMaterials.size()));

        ItemStack item = new ItemStack(randomMaterial);
        if (slot < 0) {
            player.getInventory().addItem(item);
        } else if (slot < player.getInventory().getSize()) {
            player.getInventory().setItem(slot, item);
        } else {
            player.getInventory().addItem(item);
        }
    }
}