package com.example.lr26.ui.list

import com.example.lr26.data.model.Note
import com.example.lr26.data.network.Resource
import com.example.lr26.data.repository.NotesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<NotesRepository>()
    private lateinit var viewModel: NotesListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NotesListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadNotes success updates state`() = runTest {
        val fakeNotes = listOf(
            Note("1", "Test Title", "Test Content", 123L)
        )
        coEvery { repository.getNotes() } returns Resource.Success(fakeNotes)

        viewModel.loadNotes()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(fakeNotes, viewModel.uiState.value.notes)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadNotes error updates state`() = runTest {
        coEvery { repository.getNotes() } returns Resource.Error("Network error")

        viewModel.loadNotes()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Network error", viewModel.uiState.value.error)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }
}