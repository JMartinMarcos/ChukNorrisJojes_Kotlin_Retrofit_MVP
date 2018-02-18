package es.voghdev.chucknorrisjokes.datasource.api

import com.google.gson.JsonSyntaxException
import es.voghdev.chucknorrisjokes.BuildConfig
import es.voghdev.chucknorrisjokes.datasource.api.model.ChuckNorrisService
import es.voghdev.chucknorrisjokes.datasource.api.model.JokeByKeywordApiResponse
import es.voghdev.chucknorrisjokes.model.AbsError
import es.voghdev.chucknorrisjokes.model.CNError
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.usecase.GetRandomJokeByKeyword
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetRandomJokeByKeywordApiImpl : GetRandomJokeByKeyword, ApiRequest {
    override fun getRandomJokeByKeyword(keyword: String): Pair<List<Joke>?, AbsError?> {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            builder.addInterceptor(LogJsonInterceptor())

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(getEndPoint())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()

        val service: ChuckNorrisService = retrofit.create(ChuckNorrisService::class.java)

        val call: Call<JokeByKeywordApiResponse> = service.getRandomJokeByKeyword(keyword)

        try {
            val rsp: Response<JokeByKeywordApiResponse>? = call.execute()

            if (rsp?.body() ?: false is JokeByKeywordApiResponse) {
                return Pair(rsp?.body()?.map() ?: emptyList<Joke>(), null)
            } else if (rsp?.errorBody() != null) {
                val error = rsp.errorBody()!!.string()
                return Pair(null, CNError(error))
            }
        } catch(e: JsonSyntaxException) {
            return Pair(null, CNError(e.message ?: "Unknown error parsing JSON"))
        }

        return Pair(null, CNError("Unknown error"))
    }
}
