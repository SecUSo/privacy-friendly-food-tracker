/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.secuso.privacyfriendlyfoodtracker.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Java Model for the JSON Open Food Facts response. Representation of a single product.
 */
public class NetworkProduct {

    private static final long serialVersionUID = 1L;

    @SerializedName("image_small_url")
    @Expose
    private String imageSmallUrl;
    @SerializedName("image_nutrition_url")
    @Expose
    private String imageNutritionUrl;
    @SerializedName("image_front_url")
    @Expose
    private String imageFrontUrl;
    @SerializedName("image_ingredients_url")
    @Expose
    private String imageIngredientsUrl;
    @SerializedName("link")
    @Expose
    private String manufactureUrl;
    private String url;

    private String code;
    @SerializedName("traces_tags")
    @Expose
    private List<String> tracesTags = new ArrayList<>();
    @SerializedName("ingredients_that_may_be_from_palm_oil_tags")
    @Expose
    private List<String> ingredientsThatMayBeFromPalmOilTags = new ArrayList<>();
    @SerializedName("additives_tags")
    @Expose
    private List<String> additivesTags = new ArrayList<>();
    @SerializedName("allergens_hierarchy")
    @Expose
    private List<String> allergensHierarchy = new ArrayList<>();
    @SerializedName("manufacturing_places")
    @Expose
    private String manufacturingPlaces;
    @SerializedName("ingredients_from_palm_oil_tags")
    @Expose
    private List<Object> ingredientsFromPalmOilTags = new ArrayList<>();
    @SerializedName("brands_tags")
    @Expose
    private List<String> brandsTags = new ArrayList<>();
    private String traces;
    @SerializedName("categories_tags")
    @Expose
    private List<String> categoriesTags;
    @SerializedName("ingredients_text")
    @Expose
    private String ingredientsText;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("generic_name")
    @Expose
    private String genericName;
    @SerializedName("ingredients_from_or_that_may_be_from_palm_oil_n")
    @Expose
    private long ingredientsFromOrThatMayBeFromPalmOilN;
    @SerializedName("serving_size")
    @Expose
    private String servingSize;
    @SerializedName("last_modified_by")
    @Expose
    private String lastModifiedBy;
    private String allergens;
    private String origins;
    private String stores;
    @SerializedName("nutrition_grade_fr")
    @Expose
    private String nutritionGradeFr;
    private String countries;
    @SerializedName("countries_tags")
    @Expose
    private List<String> countriesTags;
    private String brands;
    private String packaging;
    @SerializedName("labels_hierarchy")
    @Expose
    private List<String> labelsHierarchy;
    @SerializedName("labels_tags")
    @Expose
    private List<String> labelsTags;
    @SerializedName("cities_tags")
    @Expose
    private List<Object> citiesTags = new ArrayList<>();
    private String quantity;
    @SerializedName("ingredients_from_palm_oil_n")
    @Expose
    private long ingredientsFromPalmOilN;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("emb_codes_tags")
    @Expose
    private List<Object> embTags = new ArrayList<>();
    @SerializedName("states_tags")
    @Expose
    private List<String> statesTags = new ArrayList<>();
    @SerializedName("vitamins_tags")
    @Expose
    private List<String> vitaminTags = new ArrayList<>();
    @SerializedName("minerals_tags")
    @Expose
    private List<String> mineralTags = new ArrayList<>();
    @SerializedName("amino_acids_tags")
    @Expose
    private List<String> aminoAcidTags = new ArrayList<>();
    @SerializedName("other_nutritional_substances_tags")
    @Expose
    private List<String> otherNutritionTags = new ArrayList<>();
    @SerializedName("created_t")
    @Expose
    private String createdDateTime;
    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("last_modified_t")
    @Expose
    private String lastModifiedTime;
    @SerializedName("editors_tags")
    @Expose
    private List<String> editorsTags = new ArrayList<>();

    private Map<String, Object> nutriments;

    /**
     * @return The statesTags
     */
    public List<String> getStatesTags() {
        return statesTags;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }


    /**
     * @return The vitaminTags
     */

    public List<String> getVitaminTags() {
        return vitaminTags;
    }

    public void setVitaminTags(List<String> vitaminTags) {
        this.vitaminTags = vitaminTags;
    }

    /**
     * @return The mineralsTags
     */

    public List<String> getMineralTags() {
        return mineralTags;
    }

    public void setMineralTags(List<String> mineralTags) {
        this.mineralTags = mineralTags;
    }

    /**
     * @return The aminoAcidTags
     */

    public List<String> getAminoAcidTags() {
        return aminoAcidTags;
    }

    public void setAminoAcidTags(List<String> aminoAcidTags) {
        this.aminoAcidTags = aminoAcidTags;
    }

    /**
     * @return The otherNutritionTags
     */

    public List<String> getOtherNutritionTags() {
        return otherNutritionTags;
    }

    public void setOtherNutritionTags(List<String> otherNutritionTags) {
        this.otherNutritionTags = otherNutritionTags;
    }

    /**
     * @return The imageSmallUrl
     */
    public String getImageSmallUrl() {
        return imageSmallUrl;
    }

    /**
     * @return The imageFrontUrl
     */
    public String getImageFrontUrl() {
        return imageFrontUrl;
    }

    /**
     * @return The imageIngredientsUrl
     */
    public String getImageIngredientsUrl() {
        return imageIngredientsUrl;
    }

    /**
     * @return The imageNutritionUrl
     */
    public String getImageNutritionUrl() {
        return imageNutritionUrl;
    }

    /**
     * @return The manufactureUrl
     */
    public String getManufactureUrl() {
        return manufactureUrl;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The tracesTags
     */
    public List<String> getTracesTags() {
        return tracesTags;
    }

    /**
     * @return The ingredientsThatMayBeFromPalmOilTags
     */
    public List<String> getIngredientsThatMayBeFromPalmOilTags() {
        return ingredientsThatMayBeFromPalmOilTags;
    }

    /**
     * @return The additivesTags
     */
    public List<String> getAdditivesTags() {
        return additivesTags;
    }


    /**
     * @return The allergensHierarchy
     */
    public List<String> getAllergensHierarchy() {
        return allergensHierarchy;
    }


    /**
     * @return The manufacturingPlaces
     */
    public String getManufacturingPlaces() {
        return manufacturingPlaces;
    }


    /**
     * @return The ingredientsFromPalmOilTags
     */
    public List<Object> getIngredientsFromPalmOilTags() {
        return ingredientsFromPalmOilTags;
    }


    /**
     * @return The brandsTags
     */
    public List<String> getBrandsTags() {
        return brandsTags;
    }


    /**
     * @return The traces
     */
    public String getTraces() {
        return traces;
    }


    /**
     * @return The categoriesTags
     */
    public List<String> getCategoriesTags() {
        return categoriesTags;
    }

    /**
     * @return The ingredientsText
     */
    public String getIngredientsText() {
        return ingredientsText;
    }


    /**
     * @return The productName
     */
    public String getProductName() {
        return productName;
    }


    /**
     * @return The genericName
     */
    public String getGenericName() {
        return genericName;
    }

    /**
     * @return The ingredientsFromOrThatMayBeFromPalmOilN
     */
    public long getIngredientsFromOrThatMayBeFromPalmOilN() {
        return ingredientsFromOrThatMayBeFromPalmOilN;
    }


    /**
     * @return The servingSize
     */


    public String getServingSize() {
        return servingSize;
    }


    /**
     * @return The allergens
     */
    public String getAllergens() {
        return allergens;
    }


    /**
     * @return The origins
     */
    public String getOrigins() {
        return origins;
    }


    /**
     * @return The stores
     */
    public String getStores() {
        if (stores == null)
            return null;
        return stores.replace(",", ", ");
    }


    /**
     * @return The nutritionGradeFr
     */
    public String getNutritionGradeFr() {
        return nutritionGradeFr;
    }


    /**
     * @return The countries
     */
    public String getCountries() {
        if (countries == null)
            return null;
        return countries.replace(",", ", ");
    }


    /**
     * @return The brands
     */
    public String getBrands() {
        if (brands == null)
            return null;
        return brands.replace(",", ", ");
    }


    /**
     * @return The packaging
     */
    public String getPackaging() {
        if (packaging == null)
            return null;
        return packaging.replace(",", ", ");
    }


    /**
     * @return The labels tags
     */
    public List<String> getLabelsTags() {
        return labelsTags;
    }

    /**
     * @return The labels hierarchy
     */
    public List<String> getLabelsHierarchy() {
        return labelsHierarchy;
    }

    /**
     * @return The citiesTags
     */
    public List<Object> getCitiesTags() {
        return citiesTags;
    }


    /**
     * @return The quantity
     */
    public String getQuantity() {
        return quantity;
    }


    /**
     * @return The ingredientsFromPalmOilN
     */
    public long getIngredientsFromPalmOilN() {
        return ingredientsFromPalmOilN;
    }


    /**
     * @return The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return The nutriments
     */
    public String getNutrimentEnergy() {
        if(nutriments.containsKey("energy_100g")){
            return nutriments.get("energy_100g").toString();
        }
        return "";
    }


    /**
     * @param key the nutriment's key in openfoodfacts, e.g. "salt"
     * @return The nutriment's amount per 100g of product in grams
     */
    public String getNutrimentInGper100gByKey(String key) {
        String off_field = String.format("%1$s_100g", key);
        if(nutriments.containsKey(off_field)){
            return nutriments.get(off_field).toString();
        }
        return "";
    }

    /**
     * @return The Emb_codes
     */
    public List<Object> getEmbTags() {
        return embTags;
    }

    public List<String> getCountriesTags() {
        return countriesTags;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public List<String> getEditors() {
        return editorsTags;
    }


    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "code" + code + "productName" + productName;
    }
}
