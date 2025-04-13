package com.annalech.budgetcalendar.presentation.fragments

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.annalech.budgetcalendar.utils.UtilityFunctions
import com.annalech.budgetcalendar.utils.UtilityFunctions.dateMillisToString
import com.annalech.budgetcalendar.utils.UtilityFunctions.getEndDate
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
    private val dateRangeArray = arrayOf(
        "Select Date Range", "1 Week", "1 Month", "6 Month", "1 Year", "Show All"
    )
    private lateinit var startDate: String


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
        activity?.title = "Spending Reports"
        startDate = setStartDate()
        initializeRecyclerView()
        setSpinnerValuesDate()


        //удаление бюджета свайпом
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
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
                Snackbar.make(view, "Успешно удалено", Snackbar.LENGTH_SHORT).apply {
                    setAction("Отменить удаление") {
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

        //показ статистики в другом фрагменте - окне
        binding.statistics.setOnClickListener {
            val fragment = StaticsBottomSheetFragment()
            fragment.show(requireActivity().supportFragmentManager, "BottomSheetFragment")
        }

        //показ отчета за промежуток времени
        binding.dateRangeReportSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent?.getItemAtPosition(position)) {
                        "1 Week" -> getReportsBetweenDates(startDate, getEndDate(7) )
                        "1 Month" -> getReportsBetweenDates(startDate, getEndDate(30) )
                        "6 Month"->getReportsBetweenDates(startDate, getEndDate(180) )
                        "1 Year" -> getReportsBetweenDates(startDate, getEndDate(365))
                        "Show All" -> getAllEntries()

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
    }

    //получает значение бюджета за промежуток времени и устанавливает в адаптер этот список
    private fun getReportsBetweenDates(startDate: String, endDate: String) {
        val start = UtilityFunctions.dateStringToMillis(endDate)
        val end = UtilityFunctions.dateStringToMillis(startDate)
        viewModelBudget.getBudgetBetweenDate(start, end)
        viewModelBudget.budgetEntriesBetweenDate.observe(viewLifecycleOwner){
            it->
            adapterReports.differ.submitList(it)
        }
    }


    private fun setSpinnerValuesDate() {
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dateRangeArray)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dateRangeReportSpinner.adapter = arrayAdapter
    }

    private fun setStartDate(): String {
        val dateInMillis = Calendar.getInstance().timeInMillis
        return dateMillisToString(dateInMillis)
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