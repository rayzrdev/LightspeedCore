package me.rayzr522.lightspeedcore.modules.chestsorter;


import me.rayzr522.lightspeedcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.Optional;

public enum ChestSortMethod implements Comparator<ItemStack> {
    ID((a, b) -> {
        String idA = a.map(ItemStack::getType).map(Material::getKey).map(NamespacedKey::toString).orElse("");
        String idB = b.map(ItemStack::getType).map(Material::getKey).map(NamespacedKey::toString).orElse("");

        boolean emptyA = idA.isEmpty();
        boolean emptyB = idB.isEmpty();

        if (emptyA || emptyB) {
            return Boolean.compare(emptyA, emptyB);
        }

        return idA.compareToIgnoreCase(idB);
    }),
    NAME((a, b) -> {
        String nameA = a.map(ItemStack::getType).map(Material::toString).orElse("");
        String nameB = b.map(ItemStack::getType).map(Material::toString).orElse("");

        if (nameA.equals(nameB)) {
            return ID.comparator.compare(a, b);
        }

        boolean emptyA = nameA.isEmpty();
        boolean emptyB = nameB.isEmpty();

        if (emptyA || emptyB) {
            return Boolean.compare(emptyA, emptyB);
        }

        return nameA.compareToIgnoreCase(nameB);
    });

    private final Comparator<Optional<ItemStack>> comparator;

    ChestSortMethod(Comparator<Optional<ItemStack>> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(ItemStack a, ItemStack b) {
        return comparator.compare(Utils.optionalItem(a), Utils.optionalItem(b));
    }
}
