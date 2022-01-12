package com.example.inventoryapp.view.utils

import com.example.inventoryapp.model.Product

interface RecyclerItemClickListener {

    fun onClick(product: Product)

    fun dotClicked(product: Product)

}