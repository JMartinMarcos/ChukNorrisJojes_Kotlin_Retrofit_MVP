package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class RandomJokePresenter(val resLocator: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<RandomJokePresenter.MVPView, RandomJokePresenter.Navigator>() {

    override suspend fun initialize() {

        coroutine { repository.getRandomJoke() }.await().let { result ->

            if (result.success()) {
                view?.showJokeText(result.first?.value ?: "")

                val url = result.first?.iconUrl ?: ""
                if (url.isNotEmpty())
                    view?.showJokeImage(url)
            }
        }

    }

    interface MVPView {
        fun showJokeText(s: String)
        fun showJokeImage(img: String)
    }

    interface Navigator {

    }
}
