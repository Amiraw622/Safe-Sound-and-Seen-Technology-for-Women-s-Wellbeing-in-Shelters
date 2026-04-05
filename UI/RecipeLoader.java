package UI;

import java.io.*;
import java.util.*;

public class RecipeLoader {

    private final List<String[]> allRecipes = new ArrayList<>();
    private final Random rand = new Random();

    public RecipeLoader(String csvPath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(csvPath), "UTF-8"))) {

            String header = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] cols = parseCsvLine(line);
                    if (cols.length < 13) continue;

                    String name = cols[0].trim();
                    String description = cols[3].trim();
                    if (name.isEmpty() || description.isEmpty()) continue;
                    if (cols[9].trim().equalsIgnoreCase("N/A")) continue;

                    String prepTime = cols[5].trim();
                    String cookTime = cols[6].trim();
                    String totalTime = cols[7].trim();
                    String servings = cols[8].trim();
                    String calories = cols[9].trim();
                    String fat = cols[10].trim();
                    String carbs = cols[11].trim();
                    String protein = cols[12].trim();

                    String subtitle = buildSubtitle(totalTime, servings, calories);
                    String body = buildBody(description, prepTime, cookTime,
                            calories, fat, carbs, protein);

                    // searchText = lowercase name + description for ingredient matching
                    String searchText = (name + " " + description).toLowerCase();

                    allRecipes.add(new String[]{ name, subtitle, body, searchText });
                } catch (Exception e) {
                    // skip malformed
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load recipes from: " + csvPath);
            e.printStackTrace();
        }

        System.out.println("[RecipeLoader] Loaded " + allRecipes.size() + " recipes.");

        if (allRecipes.isEmpty()) {
            allRecipes.add(new String[]{
                "Honey Lemon Tea",
                "Warm, soothing, and easy to make",
                "Ingredients:\n  - 1 cup hot water\n  - 1 tbsp honey\n  - Juice of half a lemon\n\nStir honey into hot water.\nAdd lemon juice. Sip slowly.",
                "honey lemon tea hot water"
            });
        }
    }

    /**
     * Find recipes matching ANY of the given ingredients.
     * Scores by how many ingredients match, returns top matches shuffled.
     */
    public List<String[]> findByIngredients(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> lowerIngredients = new ArrayList<>();
        for (String ing : ingredients) {
            lowerIngredients.add(ing.toLowerCase());
        }

        // Score each recipe
        List<int[]> scored = new ArrayList<>(); // [index, matchCount]
        for (int i = 0; i < allRecipes.size(); i++) {
            String searchText = allRecipes.get(i)[3];
            int matchCount = 0;
            for (String ing : lowerIngredients) {
                if (searchText.contains(ing)) {
                    matchCount++;
                }
            }
            if (matchCount > 0) {
                scored.add(new int[]{ i, matchCount });
            }
        }

        // Sort by match count descending
        scored.sort((a, b) -> b[1] - a[1]);

        // Take top 200, then shuffle so user gets variety
        int limit = Math.min(200, scored.size());
        List<String[]> results = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            String[] recipe = allRecipes.get(scored.get(i)[0]);
            // Return [name, subtitle, body] (without searchText)
            results.add(new String[]{ recipe[0], recipe[1], recipe[2] });
        }
        Collections.shuffle(results, rand);

        return results;
    }

    /** Get a completely random recipe */
    public String[] getRandom() {
        String[] r = allRecipes.get(rand.nextInt(allRecipes.size()));
        return new String[]{ r[0], r[1], r[2] };
    }

    public int size() {
        return allRecipes.size();
    }

    private String buildSubtitle(String totalTime, String servings, String calories) {
        List<String> parts = new ArrayList<>();
        if (!totalTime.isEmpty()) parts.add(totalTime);
        if (!servings.isEmpty()) parts.add(servings);
        if (!calories.isEmpty()) parts.add(calories + " cal");
        if (parts.isEmpty()) return "A recipe for you";
        return String.join("  \u00B7  ", parts);
    }

    private String buildBody(String description, String prepTime, String cookTime,
                             String calories, String fat, String carbs, String protein) {
        StringBuilder sb = new StringBuilder();

        String shortDesc = description;
        if (shortDesc.length() > 120) {
            int cut = shortDesc.lastIndexOf(' ', 120);
            if (cut < 0) cut = 120;
            shortDesc = shortDesc.substring(0, cut) + "...";
        }
        sb.append(shortDesc).append("\n\n");

        if (!prepTime.isEmpty()) sb.append("Prep: ").append(prepTime).append("\n");
        if (!cookTime.isEmpty()) sb.append("Cook: ").append(cookTime).append("\n");

        if (!calories.isEmpty() || !fat.isEmpty() || !carbs.isEmpty() || !protein.isEmpty()) {
            List<String> nutri = new ArrayList<>();
            if (!calories.isEmpty()) nutri.add(calories + " cal");
            if (!protein.isEmpty()) nutri.add("protein " + protein);
            if (!fat.isEmpty()) nutri.add("fat " + fat);
            if (!carbs.isEmpty()) nutri.add("carbs " + carbs);
            sb.append(String.join(" | ", nutri));
        }

        return sb.toString();
    }

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else if (c == '\r') {
                // skip
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
}