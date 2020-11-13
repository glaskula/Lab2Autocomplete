
package recipesearch;

import java.net.URL;
import java.security.cert.PolicyNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Ingredient;
import se.chalmers.ait.dat215.lab2.Recipe;
import static java.lang.System.out;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    private RecipeBackendController rbc = new RecipeBackendController();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    @FXML
    private FlowPane flowPane;
    @FXML
    private ComboBox mainIngredientButton;
    @FXML
    private ComboBox cuisineButton;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;
    @FXML
    private RadioButton radioButton4;
    @FXML
    private Spinner maxPrice;
    @FXML
    private Slider maxTime;
    @FXML
    private Label maxTimeLabel;

    @FXML
    private AnchorPane searchPane;
    @FXML
    private AnchorPane recipeDetailPane;

    @FXML
    private Button detailedButton;
    @FXML
    private Label detailedTitel;
    @FXML
    private ImageView detailedImage;
    @FXML
    private ImageView cuisineImage;
    @FXML
    private AnchorPane dark;

    @FXML private ImageView ingridient;
    @FXML private ImageView difficulty;
    @FXML private Label time;
    @FXML private Label price;
    @FXML private Label detailedLabelPreview;
    @FXML private TextArea textArea;
    @FXML private Label antalPort;
    @FXML private Label ingredientsLabel;
    @FXML private ImageView closeButtonImage;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Recipe recipe : rbc.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }


        mainIngredientButton.getItems().addAll("Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredientButton.getSelectionModel().select("Visa alla");
        cuisineButton.getItems().addAll("Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisineButton.getSelectionModel().select("Visa alla");

        mainIngredientButton.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rbc.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
        cuisineButton.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rbc.setCuisine(newValue);
                updateRecipeList();
            }
        });

        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(difficultyToggleGroup);
        radioButton2.setToggleGroup(difficultyToggleGroup);
        radioButton3.setToggleGroup(difficultyToggleGroup);
        radioButton4.setToggleGroup(difficultyToggleGroup);
        radioButton1.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    rbc.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 200, 10);
        maxPrice.setValueFactory(valueFactory);

        maxPrice.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue observable, Integer oldValue, Integer newValue) {
                rbc.setMaxPrice(newValue);
                updateRecipeList();
            }
        });

        maxPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) {
                    //focusgained - do nothing
                } else {
                    Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                    rbc.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });

        maxTime.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                if (newValue != null && !newValue.equals(oldValue) && !maxTime.isValueChanging()) {
                    rbc.setMaxTime(newValue.intValue());
                    updateRecipeList();
                }
                int newValueInt = newValue.intValue();
                maxTimeLabel.setText(toString().valueOf(newValueInt) + " minuter");
            }
        });
        populateMainIngredientComboBox();
        populateCuisineComboBox();
        Platform.runLater(()->mainIngredientButton.requestFocus());
        updateRecipeList();
    }

    private void populateRecipeDetailView(Recipe recipe) {
        detailedTitel.setText(recipe.getName());
        detailedImage.setImage(recipe.getFXImage());
        cuisineImage.setImage(getCuisineImage(recipe.getCuisine()));
        ingridient.setImage(getMainIngredientImage(recipe.getMainIngredient()));
        difficulty.setImage(getDifficultyImage(recipe.getDifficulty()));
        time.setText(String.valueOf(recipe.getTime()) + " minuter");
        price.setText(String.valueOf(recipe.getPrice()) + " kr");
        detailedLabelPreview.setText(recipe.getDescription());
        textArea.setText(recipe.getInstruction());
        antalPort.setText(String.valueOf(recipe.getServings()) + " portioner");

        List<Ingredient> ingredientsList = recipe.getIngredients();
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < ingredientsList.size(); i++){
            stringBuilder.append(ingredientsList.get(i));
            stringBuilder.append(System.getProperty("line.separator"));
        }

        ingredientsLabel.setText(stringBuilder.toString());
    }

    @FXML
    public void closeRecipeView() {
        recipeDetailPane.toBack();
        dark.toBack();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        dark.toFront();
        recipeDetailPane.toFront();

    }



    private void updateRecipeList() {
        flowPane.getChildren().clear();
        List<Recipe> recipesList = rbc.getRecipes();

        for (Recipe recipe : rbc.getRecipes()) {

            flowPane.getChildren().add(recipeListItemMap.get(recipe.getName()));

        }
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;

                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientButton.setButtonCell(cellFactory.call(null));
        mainIngredientButton.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineButton.setButtonCell(cellFactory.call(null));
        cuisineButton.setCellFactory(cellFactory);
    }
    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null;
    }
    public javafx.scene.image.Image getMainIngredientImage(String ingredient) {
        String iconPath;
        switch (ingredient) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetariskt":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null;
    }
    public javafx.scene.image.Image getDifficultyImage(String difficulty) {
        String iconPath;
        switch (difficulty) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null;
    }
    @FXML
    public void closeButtonMouseEntered(){
        closeButtonImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        closeButtonImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        closeButtonImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }
}