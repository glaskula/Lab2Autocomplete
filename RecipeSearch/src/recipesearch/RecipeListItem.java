package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private Text titel;
    @FXML private ImageView imagePreview;
    @FXML private Label labelPreview;
    @FXML private ImageView cuisine;
    @FXML private ImageView ingridient;
    @FXML private ImageView difficulty;
    @FXML private Label time;
    @FXML private Label price;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        this.recipe = recipe;
        this.parentController = recipeSearchController;

        imagePreview.setImage(recipe.getFXImage());
        titel.setText(recipe.getName());
        cuisine.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        ingridient.setImage(parentController.getMainIngredientImage(recipe.getMainIngredient()));
        difficulty.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
        time.setText(String.valueOf(recipe.getTime()) + " minuter");
        price.setText(String.valueOf(recipe.getPrice()) + " kr");
        labelPreview.setText(recipe.getDescription());
    }
    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }
}

