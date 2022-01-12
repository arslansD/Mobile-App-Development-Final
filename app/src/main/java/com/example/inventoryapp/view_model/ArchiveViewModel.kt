package com.example.inventoryapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.inventoryapp.model.Product
import com.example.inventoryapp.repository.MainRepository
import java.util.concurrent.Executors

class ArchiveViewModel(private val repository: MainRepository) : ViewModel() {

    val allArchiveProduct = repository.getAllProduct(true).asLiveData()

    fun getSearchArchiveProduct(searchProduct:String): LiveData<List<Product>> {
        return repository.getSearchArchiveProduct(searchProduct).asLiveData()
    }

    fun deleteProduct(id:Int){
        val thread = Executors.newSingleThreadExecutor()
        thread.execute { repository.deleteItem(id) }
        thread.shutdown()
    }

    fun unArchiveData(product: Product){
        val thread = Executors.newSingleThreadExecutor()
        thread.execute { repository.updateItem(Product(product.id, product.name, product.image, product.price, product.quantity, false, product.sup)) }
        thread.shutdown()
    }

}