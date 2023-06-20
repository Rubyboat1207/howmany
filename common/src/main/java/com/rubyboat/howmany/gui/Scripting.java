package com.rubyboat.howmany.gui;

import net.minecraft.inventory.Inventory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.mvel2.MVEL;

import java.util.ArrayList;
import java.util.Arrays;

public class Scripting {
    public static TrackedItemEntry parseScript(String script, Inventory inv) {
        StringBuilder newStr = new StringBuilder();

        int prevEnd = 0;

        var iconEquation = script.split("!>");
        iconEquation[0] = iconEquation[0].trim();
        iconEquation[1] = iconEquation[1].trim();

        for(var ident : parseIdentifiers(iconEquation[1])) {
            newStr.append(iconEquation[1], prevEnd, ident.getLeft());

            int count = new TrackedItemEntry(new Identifier(ident.getRight())).getCount(inv);

            newStr.append(count);

            prevEnd = ident.getLeft() + ident.getRight().length();

        }

        newStr.append(iconEquation[1], prevEnd + 1, iconEquation[1].length());

        var entry = new TrackedItemEntry(new Identifier(iconEquation[0]));
        int numericalResult = -1;

        try {
            var result = MVEL.executeExpression(MVEL.compileExpression(newStr.toString()));

            if(result instanceof Integer) {
                numericalResult = (int) result;
            }else if(result instanceof Double) {
                numericalResult = (int) Math.floor((Double) result);
            }

            entry.setCount(numericalResult);
        }catch (Exception e) {
            e.printStackTrace();
        }


        return entry;
    }

    public static ArrayList<Pair<Integer, String>> parseIdentifiers(String input) {
        var words = Arrays.stream(input.split(" ")).toList();
        ArrayList<Pair<Integer, String>> identifiers = new ArrayList<>();
        int currentIndex = 0;

        for (String word : words) {
            if (!word.contains(":")) {
                currentIndex += word.length() + 1;  // Add 1 for the space
                continue;
            }

            identifiers.add(new Pair<>(currentIndex, word));
            currentIndex += word.length() + 1;  // Add 1 for the space
        }

        return identifiers;
    }

}
