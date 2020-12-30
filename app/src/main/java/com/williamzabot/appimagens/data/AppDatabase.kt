package com.williamzabot.appimagens.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.williamzabot.appimagens.data.dao.ImagemDAO
import com.williamzabot.appimagens.data.entity.Imagem

@Database(entities = [Imagem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val imagemDAO : ImagemDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .build()
                }
                return instance
            }
        }
    }
}