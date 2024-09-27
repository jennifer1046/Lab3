package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // private List<String> languagesNames;
    // private List<String> languagesCodes;
    // private List<String> countryNames;
    // private List<String> countryCodes2;
    // private List<String> countryCodes3;
    // private List<String> countryCodeNumeric;
    private Map<String, JSONObject> countryMap;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            this.countryMap = new HashMap<>();

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename)
                    .toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (!countryMap.containsKey(jsonObj.getString("alpha3"))) {
                    this.countryMap.put(jsonObj.getString("alpha3"), jsonObj);
                }
            }

            // for (String code: this.countryMap.keySet()) {
            // System.out.println(code);
            // }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        JSONObject jsonObj = countryMap.get(country);
        List<String> languageCodes = new ArrayList<>(jsonObj.keySet());
        // return languageCodes.subList(2 + 1, languageCodes.size());
        languageCodes.remove("id");
        languageCodes.remove("alpha3");
        languageCodes.remove("alpha2");
        return languageCodes;
    }

    @Override
    public List<String> getCountries() {
        Set<String> keyset = this.countryMap.keySet();
        List<String> temp = new ArrayList<>(keyset);
        for (String key : temp) {
            if (countryMap.get(key).length() <= (2 + 1)) {
                keyset.remove(key);
            }
        }
        return new ArrayList<>(keyset);
    }

    @Override
    public String translate(String country, String language) {
        if (!countryMap.containsKey(country) && !countryMap.get(country).has(language)) {
            return null;
        }
        return countryMap.get(country).getString(language);
    }

    /**
     * Wow, Fantastic CHECKSTYLE XD 禰牖驔迚綆魞.
     * @return this.countryMap
     */
    public Map<String, JSONObject> getCountryMap() {
        return this.countryMap;
    }
}
