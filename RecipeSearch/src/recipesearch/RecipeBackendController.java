package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.ArrayList;
import java.util.List;

public class RecipeBackendController {
    String cuisineGlobal = "Sverige";
    String mainIngredientGlobal = null;
    String difficultyGlobal = null;
    int maxPriceGlobal = 0;
    int maxTimeGlobal = 0;
    private String[] cuisineList = {"Sverige","Grekland", "Indien", "Asien", "Afrika", "Frankrike"};
    private String[] mainIngredientList = {"Kött","Fisk", "Kyckling", "Vegetarisk"};
    private String[] difficultyList = {"Lätt","Mellan", "Svår"};
    private int[] maxTimeList = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};
    RecipeDatabase db = RecipeDatabase.getSharedInstance();



    public List<Recipe> getRecipes(){
        List<Recipe> recipes = db.search(new SearchFilter(difficultyGlobal,maxTimeGlobal,cuisineGlobal,maxPriceGlobal,mainIngredientGlobal));
        return recipes;
    }

    public void setCuisine(String cuisine){
        for(int i = 0; i < cuisineList.length; i++){
            if(cuisineList[i].equals(cuisine)){
                cuisineGlobal = cuisine;
            }
        }
    }

    public void setMainIngredient(String mainIngredient){
        for(int i = 0; i < mainIngredientList.length; i++){
            if(mainIngredientList[i].equals(mainIngredient)){
                mainIngredientGlobal = mainIngredient;
            }
        }
    }

    public void setDifficulty(String difficulty){
        for(int i = 0; i < difficultyList.length; i++){
            if(difficultyList[i].equals(difficulty)){
                difficultyGlobal = difficulty;
            }
        }
    }

    public void setMaxPrice(int maxPrice){
        if(maxPrice != 0){
            maxPriceGlobal = maxPrice;
        }
    }

    public void setMaxTime(int maxTime){
        for(int i = 0; i < maxTimeList.length; i++){
            if(maxTimeList[i] == maxTime){
                maxTimeGlobal = maxTime;
            }
        }
    }
}
