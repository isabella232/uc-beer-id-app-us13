package edu.uc.beeridapp.dao;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import edu.uc.beeridapp.dto.Beer;
import edu.uc.beeridapp.dto.BeerSearch;
import edu.uc.beeridapp.dto.BeerStyle;

/**
 * Beer DAO to perform data access via online data sources
 * 
 * @author Tim Guibord
 * 
 */
public class OnlineBeerDAO implements IBeerDAO {

	private static final String BEER_STYLES_URL = "http://beerid-api.herokuapp.com/beer_styles.json";
	private static final String BEER_SEARCH_URL_BASE = "http://beerid-api.herokuapp.com/search/beer.json";

	private NetworkDAO networkDAO;

	// initialize a NetworkDAO object for API calls
	public OnlineBeerDAO() {
		networkDAO = new NetworkDAO();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<BeerStyle> fetchStyles() throws Exception {
		// initialize a BeerStyle ArrayList
		ArrayList<BeerStyle> allStyles = new ArrayList<BeerStyle>();

		// get JSON string from the API
		String result = networkDAO.request(BEER_STYLES_URL);

		// pull JSONArray from returned JSON string
		JSONArray stylesJSON = new JSONArray(result);

		// iterate through the JSONArray
		for (int i = 0; i < stylesJSON.length(); i++) {
			// Pull JSONObject from array
			JSONObject jo = (JSONObject) stylesJSON.get(i);

			// create a BeerStyle object from the JSONObject and add it to the
			// ArrayList
			BeerStyle bs = new BeerStyle();
			bs.setGuid(jo.getString("guid"));
			bs.setStyle(jo.getString("style"));
			allStyles.add(bs);
		}

		return allStyles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Beer> searchBeers(BeerSearch beerSearch) throws Exception {

		// initialize a Beers ArrayList
		ArrayList<Beer> beers = new ArrayList<Beer>();

		// ArrayList to hold search params
		ArrayList<String> paramsArray = new ArrayList<String>();

		// initialize the search url container
		String searchURL = "";

		// if a name search criteria was entered, add the param to the array
		if (!TextUtils.isEmpty(beerSearch.getName())) {
			paramsArray.add("name=" + beerSearch.getName());
		}

		// if a min ABV search criteria was entered, add the param to the array
		if (!TextUtils.isEmpty(beerSearch.getLessThanABV())) {
			paramsArray.add("abv=" + beerSearch.getLessThanABV());
		}

		// if a max ABV search criteria was entered, add the param to the array
		if (!TextUtils.isEmpty(beerSearch.getLessThanCalories())) {
			paramsArray.add("cal=" + beerSearch.getLessThanCalories());
		}

		// if a beer style search criteria was entered, add the param to the
		// array
		if (!TextUtils.isEmpty(beerSearch.getStyleGUID())) {
			paramsArray.add("type=" + beerSearch.getStyleGUID());
		}

		// if params exist, build the search url
		if (paramsArray.size() > 0) {
			// join the params into a valid URL query string
			String params = TextUtils.join("&", paramsArray);

			// build the search URL
			searchURL = BEER_SEARCH_URL_BASE + "?" + params;
		} else {
			searchURL = BEER_SEARCH_URL_BASE;
		}

		// get the JSON string from the API
		String result = networkDAO.request(searchURL);

		// pull a JSONArray from the results string
		JSONArray beersJSON = new JSONArray(result);

		// iterate through the JSONArray
		for (int i = 0; i < beersJSON.length(); i++) {
			// Pull JSONObject from array
			JSONObject jo = (JSONObject) beersJSON.get(i);

			// create a Beer object from the JSONObject and add it to the
			// ArrayList
			Beer b = new Beer();
			b.setGuid(jo.getInt("guid"));
			b.setName(jo.getString("name"));
			b.setAbv(jo.getString("abv"));
			b.setCalories(jo.getString("calories"));
			b.setStyle(jo.getString("style"));
			beers.add(b);
		}

		return beers;
	}

}
