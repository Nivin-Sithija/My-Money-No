package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManagementViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categoryList ->
                _categories.value = categoryList
            }
        }
    }

    fun addCategory(name: String, color: Int, parentId: Long?) {
        viewModelScope.launch {
            val category = Category(
                name = name,
                color = color,
                parentId = parentId
            )
            categoryRepository.insertCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }

    fun deleteCategory(categoryId: Long, reassignToCategoryId: Long?) {
        viewModelScope.launch {
            val category = _categories.value.find { it.id == categoryId } ?: return@launch
            
            if (reassignToCategoryId != null) {
                categoryRepository.reassignTransactions(categoryId, reassignToCategoryId)
            }
            categoryRepository.deleteCategory(category)
        }
    }
    
    suspend fun getTransactionCount(categoryId: Long): Int {
        return categoryRepository.getTransactionCountForCategory(categoryId)
    }
}
