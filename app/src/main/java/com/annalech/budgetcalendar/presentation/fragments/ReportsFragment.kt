package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentBudgetEntryBinding
import com.annalech.budgetcalendar.databinding.FragmentCalendarViewBinding
import com.annalech.budgetcalendar.databinding.FragmentReportsBinding
import com.annalech.budgetcalendar.presentation.adapter.ReportsAdapter
import com.annalech.budgetcalendar.presentation.viewmodels.ViewModelBudget
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports),
    ReportsAdapter.onClickListnerChancgeBudget {

    private var _binding: FragmentReportsBinding? = null
    val binding: FragmentReportsBinding
        get() = _binding ?: throw RuntimeException(" FragmentReportsBinding is null")

    private val viewModelBudget: ViewModelBudget by viewModels()
    private lateinit var adapterReports: ReportsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()

        //удаление бюджета свайпом
        val itemTouchCallback= object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition
                val budget = adapterReports.differ.currentList[itemPosition]
                viewModelBudget.deleteBudgetEntry(budget)

                //вывод окна об успехе операции с вариантом отмены
                Snackbar.make(view,"Успешно удалено",Snackbar.LENGTH_SHORT ).apply {
                    setAction("Отменить удаление"){
                        viewModelBudget.insertBudget(budget)
                    }
                    show()
                }
            }

        }

        //подключение поддержки свайпов в Recycler
        ItemTouchHelper(itemTouchCallback).apply {
            attachToRecyclerView(binding.rcvReports)
        }

        getAllEntries()
    }

    private fun getAllEntries() {
        viewModelBudget.allBudgetEntriesLD.observe(viewLifecycleOwner) { it ->
            adapterReports.differ.submitList(it)

        }
    }

    private fun initializeRecyclerView() {
        adapterReports = ReportsAdapter(this)
        binding.rcvReports.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterReports
        }
    }

    override fun onClick(position: Int) {
        val currentBudget = adapterReports.differ.currentList[position]
        val bottomSheet = UpdateBudgetBottomSheetFragment(currentBudgetItem = currentBudget)
        bottomSheet.show(requireActivity().supportFragmentManager, "UPDATE_BUDGET")
    }


}