package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.CategoryDao
import com.wiyadama.expensetracker.data.entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    fun getRootCategories(): Flow<List<Category>> = categoryDao.getRootCategories()

    fun getSubcategories(parentId: Long): Flow<List<Category>> =
        categoryDao.getSubcategories(parentId)

    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)

    fun getCategoryByIdFlow(id: Long): Flow<Category?> = categoryDao.getCategoryByIdFlow(id)

    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)

    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    suspend fun getTransactionCountForCategory(categoryId: Long): Int =
        categoryDao.getTransactionCountForCategory(categoryId)

    suspend fun reassignTransactions(oldCategoryId: Long, newCategoryId: Long) =
        categoryDao.reassignTransactions(oldCategoryId, newCategoryId)
}
