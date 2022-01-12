package com.example.inventoryapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inventoryapp.application.MyApplication
import com.example.inventoryapp.databinding.FragmentHomeBinding
import com.example.inventoryapp.model.Product
import com.example.inventoryapp.view.adapters.ProductRecyclerAdapter
import com.example.inventoryapp.view.utils.RecyclerItemClickListener
import com.example.inventoryapp.view.utils.navigate
import com.example.inventoryapp.view.utils.showToast
import com.example.inventoryapp.view_model.MainViewModel
import com.example.inventoryapp.view_model.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior


class HomeFragment : Fragment(), RecyclerItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModels<MainViewModel> { MainViewModelFactory((requireActivity().application as MyApplication).repository) }
    private val recyclerAdapter by lazy { ProductRecyclerAdapter(this) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        setUpSearchView()
        viewModel.getAllProducts(false).observe(viewLifecycleOwner) {
            recyclerAdapter.setList(it)
        }
        binding.add.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentToEditorFragment(null))
        }

    }

    private fun setUpSearchView() {
        binding.search.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        observeData(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        observeData(newText)
                    }
                    return true
                }

            })
        }
    }

    private fun setUpRecycler() {
        binding.recycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
        }
    }

    override fun onClick(product: Product) {
        navigate(HomeFragmentDirections.actionHomeFragmentToEditorFragment(product))
    }

    override fun dotClicked(product: Product) {
        val bottomSheet = BottomSheetBehavior.from(binding.include.bottomSheet)
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

        binding.include.textView1.setOnClickListener {
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            showAlertDialog(product)
        }
    }

    private fun observeData(querySearch: String) {
        val searchQuery = "%$querySearch%"

        viewModel.getAllSearchProduct(searchQuery).observe(viewLifecycleOwner) {
            it.let {
                recyclerAdapter.setList(it)
            }
        }
    }

    private fun showAlertDialog(product: Product) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Архивировать ${product.name}  из каталога?")
            .setPositiveButton(
                "Да"
            ) { _, _ ->
                archiveProduct(product)
            }
            .setNegativeButton("Нет") { p0, _ ->
                p0.dismiss()
            }
            .apply {
                create()
                show()
            }
    }

    private fun archiveProduct(product: Product) {
        viewModel.updateData(Product(product.id, product.name, product.image,  product.price, product.quantity, true, product.sup))
        "Archived".showToast(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}