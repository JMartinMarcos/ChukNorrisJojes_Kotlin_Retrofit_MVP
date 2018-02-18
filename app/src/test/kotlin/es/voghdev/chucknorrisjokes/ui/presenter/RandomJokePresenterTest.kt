package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RandomJokePresenterTest {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: RandomJokePresenter.Navigator

    @Mock lateinit var mockView: RandomJokePresenter.MVPView

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

    lateinit var presenter: RandomJokePresenter

    val exampleJoke = Joke(
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

    private fun createMockedPresenter(): RandomJokePresenter {
        val presenter = RandomJokePresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }

    @Test
    fun shouldRequestARandomJokeToTheApiOnStart() {
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(exampleJoke, null))

        runBlocking { presenter.initialize() }
        verify(mockChuckNorrisRepository).getRandomJoke()
    }

    @Test
    fun shouldShowTheJokesTextWhenARandomJokeIsReceived (){

        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(exampleJoke, null))

        runBlocking { presenter.initialize() }
        verify(mockView, times(1)).showJokeText("Chuck Norris knows how to say souffle in the French language.")
    }

    @Test
    fun shouldShowTheJokesImageWhenARandomJokeIsReceived () {
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(exampleJoke, null))

        runBlocking { presenter.initialize() }
        verify(mockView, times(1)).showJokeImage("https://assets.chucknorris.host/img/avatar/chuck-norris.png")
    }

    @Test
    fun shouldNotDownloadAnyImageIfUrlIsEmpty(){

        val jokeWithEmptyImage = exampleJoke.copy(iconUrl = "")
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(jokeWithEmptyImage, null))

        runBlocking { presenter.initialize() }
        verify(mockView, times(0)).showJokeImage(anyString())
    }
}
