package legitish.gui.category;

import java.util.ArrayList;

public class CategoryManager {
    private final ArrayList<Category> categories = new ArrayList<>();

    public CategoryManager() {
        categories.add(new ModulesCategory());
        categories.add(new ClientCategory());
        categories.add(new ConfigCategory());
        categories.add(new SettingsCategory());
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Category getCategoryByClass(Class<?> categoryClass) {
        return categories.stream().filter(category -> category.getClass().equals(categoryClass)).findFirst().orElse(null);
    }
}
