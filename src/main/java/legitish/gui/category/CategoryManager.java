package legitish.gui.category;

import legitish.gui.category.impl.ColorCategory;
import legitish.gui.category.impl.ConfigCategory;
import legitish.gui.category.impl.ModulesCategory;
import legitish.gui.category.impl.SettingsCategory;

import java.util.ArrayList;

public class CategoryManager {
    private final ArrayList<Category> categories = new ArrayList<>();

    public CategoryManager() {
        categories.add(new ModulesCategory());
        categories.add(new SettingsCategory());
        categories.add(new ConfigCategory());
        categories.add(new ColorCategory());
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Category getCategoryByClass(Class<?> categoryClass) {
        return categories.stream().filter(category -> category.getClass().equals(categoryClass)).findFirst().orElse(null);
    }
}
