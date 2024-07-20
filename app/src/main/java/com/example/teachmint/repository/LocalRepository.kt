package com.example.teachmint.repository

import com.example.teachmint.data.LocalDao
import com.example.teachmint.data.model.LocalModel

class LocalRepository(private val localDao: LocalDao) {
    suspend fun getSize(): Int{
        return localDao.getCount()
    }

    suspend fun insertData(repo: LocalModel){
        return localDao.insert(repo)
    }
    suspend fun getAllData(): List<LocalModel>{
        return localDao.getAllData()
    }
}
