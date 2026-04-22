package com.example.lr26.data.api
import com.example.lr26.data.model.Note
import retrofit2.http.*

interface NoteApi {
    @GET("notes") suspend fun getNotes(): List<Note>
    @POST("notes") suspend fun createNote(@Body note: Note): Note
    @PUT("notes/{id}") suspend fun updateNote(@Path("id") id: String, @Body note: Note): Note
    @DELETE("notes/{id}") suspend fun deleteNote(@Path("id") id: String)
}