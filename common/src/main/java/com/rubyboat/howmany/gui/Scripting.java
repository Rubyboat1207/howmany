package com.rubyboat.howmany.gui;

import com.rubyboat.howmany.entry.TrackedItemEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.mvel2.MVEL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scripting {
    public static int parseScript(String script, Inventory inv) {
        StringBuilder newStr = new StringBuilder();

        int prevEnd = 0;

        var iconEquation = script.split("=");
        iconEquation[1] = iconEquation[1].trim();

        var idents = parseIdentifiers(iconEquation[1]);

        for(var ident : idents) {
            newStr.append(iconEquation[1], prevEnd, ident.getLeft());

            int count = new TrackedItemEntry(new Identifier(ident.getRight())).getCount(inv);

            newStr.append(count);

            prevEnd = MathHelper.clamp(ident.getLeft() + ident.getRight().length(), 0, iconEquation[1].length() - 1);

        }

        if(idents.size() > 1) {
            var finalIdent = idents.get(idents.size() - 1);
            newStr.append(iconEquation[1].substring(finalIdent.getLeft() + finalIdent.getRight().length()));
        }

        int numericalResult = -1;

        String itemComputedString = newStr.toString();

        String minMaxComputedString= replaceMinMax(itemComputedString);

        try {
            var result = MVEL.executeExpression(MVEL.compileExpression(minMaxComputedString));

            if(result instanceof Integer) {
                numericalResult = (int) result;
            }else if(result instanceof Double) {
                numericalResult = (int) Math.floor((Double) result);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


        return numericalResult;
    }

    public static Identifier getIconIdentifier(String script) {
        var iconEquation = script.split("=");

        return new Identifier(iconEquation[0].trim());
    }

    public static ArrayList<Pair<Integer, String>> parseIdentifiers(String input) {
        // Theoretical regex for finding identifiers: ([A-z]*:*[A-z])

        var words = Arrays.stream(input.split("[ \\(\\)\\+\\-\\*\\/]")).toList();
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

    public static String replaceMinMax(String input) {
        String replacedString = input;
        String pattern = "(min|max)\\((.+?)\\)";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(replacedString);

        while (matcher.find()) {
            String operator = matcher.group(1);
            String expression = matcher.group(2);

            String[] parts = expression.split(",");

            if (parts.length == 2) {
                String firstExpression = parts[0].trim();
                String secondExpression = parts[1].trim();

                String firstResult = evaluateExpression(firstExpression);
                String secondResult = evaluateExpression(secondExpression);

                if (!firstResult.isEmpty() && !secondResult.isEmpty()) {
                    String replacement;
                    if (operator.equals("min")) {
                        replacement = String.valueOf(Math.min(Double.parseDouble(firstResult), Double.parseDouble(secondResult)));
                    } else {
                        replacement = String.valueOf(Math.max(Double.parseDouble(firstResult), Double.parseDouble(secondResult)));
                    }
                    replacedString = replacedString.replaceFirst(Pattern.quote(matcher.group()), replacement);
                }
            }
        }

        return replacedString;
    }

    public static String evaluateExpression(String expression) {
        try {
            Object result = MVEL.eval(expression);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
