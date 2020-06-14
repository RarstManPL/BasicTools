package me.rarstman.basictools.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(final Material material) {
        this(material, 1, 0);
    }

    public ItemBuilder(final Material material, final int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(final Material material, final int amount, final int durability) {
        this.itemStack = new ItemStack(material, amount, (short) durability);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(final String... item) {
        Material material = Material.AIR;

        if (item.length < 1) {
            this.itemStack = new ItemStack(material);
            this.itemMeta = this.itemStack.getItemMeta();
            return;
        }
        String materialName;
        short durability = 0;

        if (item[0].split(":").length == 0) {
            materialName = item[0].toUpperCase();
        } else {
            final String[] materialData = item[0].split(":");
            materialName = materialData[0];

            if (materialData.length >= 2 && StringUtils.isNumeric(materialData[1])) {
                durability = Integer.valueOf(materialData[1]) < 0 ? 0 : Short.valueOf(materialData[1]);
            }
        }

        if (Material.valueOf(materialName) == null) {
            this.itemStack = new ItemStack(material);
            this.itemMeta = this.itemStack.getItemMeta();
            return;
        }
        material = Material.valueOf(materialName);
        int amount = 1;

        if (item.length > 1 && StringUtils.isNumeric(item[1])) {
            amount = Integer.valueOf(item[1]) < 0 ? 1 : Integer.valueOf(item[1]);
        }
        this.itemStack = new ItemStack(material, amount, durability);
        this.itemMeta = this.itemStack.getItemMeta();

        if (item.length < 3) {
            return;
        }

        Arrays.stream(Arrays.copyOfRange(item, 2, item.length))
                .filter(option -> option.split(":").length > 1)
                .map(option -> option.split(":"))
                .forEach(optionSplitted -> {
                    switch (optionSplitted[0].toLowerCase()) {
                        case "name": {
                            this.setName(optionSplitted[1]);
                        }
                        case "lore": {
                            this.setLore(
                                    Arrays.asList(optionSplitted[1].split("|"))
                                            .stream()
                                            .map(string -> StringUtils.join(string.split("_"), " "))
                                            .collect(Collectors.toList())
                            );
                            break;
                        }
                        case "owner": {
                            this.setOwner(optionSplitted[1]);
                            break;
                        }
                        case "flags": {
                            this.addFlags(
                                    Arrays.asList(optionSplitted[1].split(","))
                                            .stream()
                                            .filter(itemFlag -> ItemFlag.valueOf(itemFlag.toUpperCase()) != null)
                                            .map(itemFlag -> ItemFlag.valueOf(itemFlag.toUpperCase()))
                                            .toArray(ItemFlag[]::new)
                            );
                            break;
                        }
                        case "unbreakable": {
                            this.setUnbreakable(optionSplitted[1].equals("true"));
                            break;
                        }
                        case "glowing": {
                            this.setGlowing(optionSplitted[1].equalsIgnoreCase("true"));
                            break;
                        }
                        case "potions": {
                            this.addPotionEffects(
                                    Arrays.stream(optionSplitted[1].split(","))
                                            .map(potionData -> potionData.split(":"))
                                            .filter(potionData -> PotionEffectType.getByName(potionData[0]) != null && StringUtils.isNumeric(potionData[2]))
                                            .map(potionData -> new PotionEffect(PotionEffectType.getByName(potionData[0].toUpperCase()), ((Long) DateUtil.stringToMills(potionData[1])).intValue(), Integer.valueOf(potionData[2])))
                                            .toArray(PotionEffect[]::new)
                            );
                            break;
                        }
                        case "enchants": {
                            this.addEnchantments(
                                    Arrays.stream(optionSplitted[1].split(","))
                                            .map(enchantData -> enchantData.split(":"))
                                            .filter(enchantData -> Enchantment.getByKey(NamespacedKey.minecraft(enchantData[0].toUpperCase())) != null && StringUtils.isNumeric(enchantData[1]))
                                            .collect(Collectors.toMap(enchantData -> Enchantment.getByKey(NamespacedKey.minecraft(enchantData[0].toUpperCase())), enchantData -> Integer.valueOf(enchantData[1])))
                            );
                            break;
                        }
                    }
                });
    }

    public void updateItemMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
    }

    public void setName(final String name) {
        this.itemMeta.setDisplayName(ChatUtil.fixColors(name));
        this.updateItemMeta();
    }

    public void setLore(final List<String> lore) {
        this.itemMeta.setLore(ChatUtil.fixColors(lore));
        this.updateItemMeta();
    }

    public void setOwner(final String owner) {
        if (this.itemStack.getType() != Material.PLAYER_HEAD) {
            return;
        }
        ((SkullMeta) this.itemMeta).setOwner(owner);
        this.updateItemMeta();
    }

    public void addPotionEffects(final PotionEffect... potionEffects) {
        if (this.itemStack.getType() != Material.POTION) {
            return;
        }
        Arrays.stream(potionEffects)
                .forEach(potionEffect -> ((PotionMeta) this.itemMeta).addCustomEffect(potionEffect, true));
        this.updateItemMeta();
    }

    public void addFlags(final ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        this.updateItemMeta();
    }

    public void setUnbreakable(final boolean isUnbreakable) {
        this.itemMeta.setUnbreakable(isUnbreakable);
        this.updateItemMeta();
    }

    public void setGlowing(final boolean isGlowing) {
        if (!isGlowing) {
            return;
        }
        this.itemStack.addEnchantment(Enchantment.DURABILITY, 1);
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.updateItemMeta();
    }

    public void addEnchantments(final Map<Enchantment, Integer> enchantments) {
        enchantments
                .entrySet()
                .forEach(entrySet -> this.itemMeta.addEnchant(entrySet.getKey(), entrySet.getValue(), true));
        this.updateItemMeta();
    }

    public Material getMaterial() {
        return this.itemStack.getType();
    }

    public int getAmount() {
        return this.itemStack.getAmount();
    }

    public ItemStack build() {
        return this.itemStack;
    }
}
