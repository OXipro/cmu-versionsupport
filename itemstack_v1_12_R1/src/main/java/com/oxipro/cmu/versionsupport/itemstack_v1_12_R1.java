package com.oxipro.cmu.versionsupport;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
class itemstack_v1_12_R1 implements ItemStackSupport {
    @Nullable
    public ItemStack getInHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Nullable
    public ItemStack getInOffHand(Player player) {
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
        return new ItemStack(m, amount, data);
    }

    public ItemStack createItem(Material material, int amount, byte data) {
        return new ItemStack(material, amount, data);
    }

    public ItemStack addTag(ItemStack itemStack, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = cis.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            cis.setTag(tag);
        }
        tag.setString(key, value);
        return CraftItemStack.asBukkitCopy(cis);
    }

    public boolean hasTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_12_R1.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        return cis.getTag() != null && cis.hasTag() && (cis.getTag().hasKey(key));
    }

    @Nullable
    public String getTag(ItemStack itemStack, String key) {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(itemStack).getTag();
        if (tag == null) return null;
        return tag.getString(key);
    }

    public ItemStack removeTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_12_R1.ItemStack cis = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = cis.getTag();
        if (tag == null) return itemStack;
        if (!tag.hasKey(key)) return itemStack;
        tag.remove(key);
        return CraftItemStack.asBukkitCopy(cis);
    }

    public void setUnbreakable(ItemStack itemStack, boolean unbreakable) {
        itemStack.getItemMeta().setUnbreakable(true);
    }

    public void minusAmount(Player p, ItemStack i, int amount) {
        if (p.getInventory().getItemInOffHand().equals(i)){
            p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            p.updateInventory();
            return;
        }
        if (i.getAmount() - amount <= 0) {
            p.getInventory().removeItem(i);
            return;
        }
        i.setAmount(i.getAmount() - amount);
        p.updateInventory();
    }

    public double getDamage(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsStack.getTag() == null) return 0D;
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

    @Override
    public boolean isPlayerHead(ItemStack itemStack) {
        //noinspection deprecation
        return itemStack.getType() == Material.SKULL_ITEM && itemStack.getData().getData() == 3;
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte getItemData(ItemStack itemStack){
        return itemStack.getData().getData();
    }

    @Override
    public org.bukkit.inventory.ItemStack applyPlayerSkinOnHead(Player player, org.bukkit.inventory.ItemStack copyTagFrom) {
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(org.bukkit.Material.SKULL_ITEM, 1, (short) 3);

        if (copyTagFrom != null) {
            net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy(head);
            i.setTag(CraftItemStack.asNMSCopy(copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy(i);
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
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

        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(org.bukkit.Material.SKULL_ITEM, 1, (short) 3);

        if (copyTagFrom != null) {
            net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy(head);
            i.setTag(CraftItemStack.asNMSCopy(copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy(i);
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
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
        return Item.getById(mat.getId()) != null;
    }

    private static final Random RANDOM = new Random();

    @Override
    public void giveRandomItem(Player player, int slot, List<String> blacklist) {
        if (player == null) return;

        Set<String> blacklistSet = blacklist.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        List<Material> validMaterials = Arrays.stream(Material.values())
                .filter(this::isItem)
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
