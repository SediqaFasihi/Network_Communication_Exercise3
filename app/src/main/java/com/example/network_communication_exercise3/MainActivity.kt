package com.example.network_communication_exercise3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.network_communication_exercise3.databinding.ActivityMainBinding
import com.example.network_communication_exercise3.model.CovidData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//credentials to authenticate an user agent with server, ensure that client request access datta securely.

const val API_AUTHORISATION_HEADER = "X-RapidAPI-Key"

//interceptor can monitor, rewrite, and retry calls
class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //chain is a processing object which acquires the result of the previous interceptor through chain
        val request = chain.request()

        val newRequest = request.newBuilder().addHeader(
            API_AUTHORISATION_HEADER, "3b59e63daemsh112daf8eb7a31b1p11277djsnae421707a5ef"
        ).build()
        val value1 = chain.proceed(newRequest)
        return value1
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val logging = HttpLoggingInterceptor()
    private val authorizationInterceptor = AuthorizationInterceptor()
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authorizationInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://covid-193.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val covidApiService = retrofit.create(CovidApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        logging.level = HttpLoggingInterceptor.Level.BODY
        retrieveCovidCasesDetails()

    }

    private fun setCovidDetails(covidResult: CovidData) {
        binding.newCassesTitle.text = getString(
            R.string.new_covid_casses_in_italy_today_d,
            covidResult.response.get(0).cases.new
        )
        binding.activeCasesTitle.text = getString(
            R.string.active_covid_cases_in_italy_today_d,
            covidResult.response.get(0).cases.active
        )
        binding.criticalCasesTitle.text = getString(
            R.string.new_critical_cases_in_italy_today_d,
            covidResult.response.get(0).cases.critical
        )
        binding.recoveredCasesTitle.text = getString(
            R.string.recovered_cases_in_italy_today_d,
            covidResult.response.get(0).cases.recovered
        )
        binding.totalCasesTitle.text = getString(
            R.string.total_cases_in_italy_today_d,
            covidResult.response.get(0).cases.total
        )
    }

    private fun retrieveCovidCasesDetails() {
        lifecycleScope.launch {
            try {
                val details = covidApiService.getCovidDetails()
                setCovidDetails(details)
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(R.id.main_view),
                    "Retrieving cases unsuccesful",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") {
                    retrieveCovidCasesDetails()
                }.show()
            }
        }
    }
}