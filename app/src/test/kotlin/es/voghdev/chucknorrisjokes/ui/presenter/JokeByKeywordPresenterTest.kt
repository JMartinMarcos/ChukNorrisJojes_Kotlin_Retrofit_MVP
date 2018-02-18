package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.anyCategory
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokeByKeywordPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByKeywordPresenter.Navigator

    @Mock lateinit var mockView: JokeByKeywordPresenter.MVPView

    @Mock lateinit var mockChuckNorrisRepository : ChuckNorrisRepository

    lateinit var presenter : JokeByKeywordPresenter


    val exampleJoke = Joke(id = "abc",
            iconUrl = "http://chuck.image.url",
            url = "http://example.url",
            value = "We have our fears, fear has its Chuck Norris'es")

    val anotherJoke = Joke(
            id = "GdEH64AkS9qEQCmqMwM2Rg",
            iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
            url = "http://api.chucknorris.io/jokes/GdEH64AkS9qEQCmqMwM2Rg",
            value = "Chuck Norris knows how to say souffle in the French language."
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun shouldDisplayErrorIfSearchButtonIsClickedAndTextIsEmpty(){
        runBlocking { presenter.initialize()
                      presenter.onButtonSearchClicked("")}
        verify(mockView).showError("Please enter keyword")
    }

    @Test
    fun shouldRequestAJokeByKeywordIfTextIsNotEmpty(){
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(Pair(listOf(exampleJoke,anotherJoke), null))

        runBlocking { presenter.initialize()
            presenter.onButtonSearchClicked("Bruce Lee")}
        verify(mockChuckNorrisRepository).getRandomJokeByKeyword("bruce lee")
    }

    @Test
    fun shouldShowTheJokesTextWhenTheApiReturnAJokeByKeyword(){
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(Pair(listOf(exampleJoke,anotherJoke), null))

        runBlocking { presenter.initialize()
            presenter.onButtonSearchClicked("Bruce Lee")}
        verify(mockView).showJokeText("We have our fears, fear has its Chuck Norris'es")
    }

    @Test
    fun shouldShowTheJokesImageWhenARandomJokeByKeywordIsReceived () {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(Pair(listOf(exampleJoke,anotherJoke), null))

        runBlocking { presenter.initialize()
            presenter.onButtonSearchClicked("Bruce Lee")}
        verify(mockView, times(1)).showJokeImage("http://chuck.image.url")
    }



    private fun createMockedPresenter(): JokeByKeywordPresenter {
        val presenter = JokeByKeywordPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
