package com.example.co5225_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.co5225_22_23.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class  MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    var cocktailChosen = ""
    val cocktails = arrayOf(
        "Negroni", "Mojito", "Bramble", "Greyhound", "Margarita",
        "Manhattan", "151 Florida Bushwacker", "Blue Hurricane", "Zombie"
    )
    val baseURLString: String = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s="



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        var spinner= binding.CocktailSpinner
        val arrayAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, cocktails
            )
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    Toast.makeText(
                        applicationContext,
                        "selected Cocktail is = " + cocktails[position],
                        Toast.LENGTH_SHORT
                    ).show()
                    cocktailChosen = cocktails[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        binding.searchBtn.setOnClickListener(this)

    }

    fun updateInstructionsTextView(text: String) {

            binding.Instructions.text = text

    }

    fun updateIngredientsTextView(items: List<String>){
        val ingredients = ArrayList<String>()

        for(element in items){
            if (element != null){
                ingredients.add(element)
            }
            binding.Ingredients.text= ingredients.toString()
        }

    }
    private fun concat(s1: String, s2: String): String {
        return s1 + s2
    }

    private fun fetchCocktail(urlString: String) {

        val thread = Thread {

            try {
                val request = Request.Builder().url(urlString).build()

                val client = OkHttpClient()
                client.newCall(request).enqueue(object: Callback{
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()

                        val gson = GsonBuilder().create()

                        val cocktailFeed = gson.fromJson(body,CocktailFeed::class.java)

                        val cocktailInstructions = cocktailFeed.drinks.get(0).strInstructions
                        val cocktailIngredients = listOf(cocktailFeed.drinks.get(0).strIngredient1,cocktailFeed.drinks.get(0).strMeasure1,
                            cocktailFeed.drinks.get(0).strIngredient2,cocktailFeed.drinks.get(0).strMeasure2, cocktailFeed.drinks.get(0).strIngredient3,
                            cocktailFeed.drinks.get(0).strMeasure3, cocktailFeed.drinks.get(0).strIngredient4,cocktailFeed.drinks.get(0).strMeasure4,
                            cocktailFeed.drinks.get(0).strIngredient5,cocktailFeed.drinks.get(0).strMeasure5, cocktailFeed.drinks.get(0).strIngredient6,
                            cocktailFeed.drinks.get(0).strMeasure6,cocktailFeed.drinks.get(0).strIngredient7,
                            cocktailFeed.drinks.get(0).strMeasure7, cocktailFeed.drinks.get(0).strIngredient8, cocktailFeed.drinks.get(0).strMeasure8)


                        runOnUiThread {
                             updateInstructionsTextView(cocktailInstructions)
                            updateIngredientsTextView(cocktailIngredients)
                        }
                    }


                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Failure to execute code")
                    }
                })
            } catch(e: IOException){
                updateInstructionsTextView("IO Exception")

            }
        }
        thread.start()
    }

    override fun onClick(v: View?) {

       val urlChosen = concat(baseURLString,cocktailChosen)
        fetchCocktail(urlChosen)
    }

    class CocktailFeed(val drinks: List<Cocktail>)

    class Cocktail(val idDrink: Int, val strDrink: String, val strInstructions: String,val strIngredient1: String,
                   val strIngredient2: String, val strIngredient3: String, val strIngredient4: String,
                   val strIngredient5: String, val strIngredient6: String, val strIngredient7: String,
                   val strIngredient8: String, val strMeasure1: String,
                   val strMeasure2: String, val strMeasure3: String, val strMeasure4: String,
                   val strMeasure5: String, val strMeasure6: String, val strMeasure7: String,
                   val strMeasure8: String)


}




