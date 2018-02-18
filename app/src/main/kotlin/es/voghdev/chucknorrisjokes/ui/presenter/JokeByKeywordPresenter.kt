package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    interface MVPView {
        fun showError(errorText: String)
        fun showJokeText(text: String)
        fun showJokeImage(img: String)

    }

    interface Navigator {

    }

    suspend fun onButtonSearchClicked(text: String) {
            if (text.isEmpty()){
                view?.showError("Please enter keyword")
            }else{
                coroutine { repository.getRandomJokeByKeyword(text.toLowerCase()) }.await().let { result ->
                    if (result.success()){
                        view?.showJokeText(result.first?.elementAt(0)?.value ?: "")
                        view?.showJokeImage(result.first?.elementAt(0)?.iconUrl ?: "")
                    }
                }
            }
    }
}
