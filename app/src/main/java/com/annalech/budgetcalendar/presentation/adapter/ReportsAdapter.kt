package com.annalech.budgetcalendar.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.data.entiity.Budget
import com.annalech.budgetcalendar.databinding.ItemBudgetBinding
import com.annalech.budgetcalendar.utils.UtilityFunctions.dateMillisToString

class ReportsAdapter(
  val  listner : onClickListnerChancgeBudget
) :RecyclerView.Adapter<ReportsAdapter.MyViewHolder> (){
        private val CREDIT = "0"


     inner class MyViewHolder(val itemBudgetBinding:ItemBudgetBinding):RecyclerView.ViewHolder(
        itemBudgetBinding.root
    ){
        init {
            itemBudgetBinding.root.setOnLongClickListener{
                val position = adapterPosition
                listner.onClick(position)
                true
            }
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<Budget>(){
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
         return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return  oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return  MyViewHolder(
           ItemBudgetBinding.inflate(
               LayoutInflater.from(parent.context),
                parent,
               false
           )
       )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        with(holder){
            with(differ.currentList[position]){

                itemBudgetBinding.budgetItemDate.text =  dateMillisToString(date.toLong())
                    itemBudgetBinding.budgetItemAmount.text =  amount.toString()
                itemBudgetBinding.budgetItemPerpose.text = purpose.toString()

                if (creditOrDebit.equals(CREDIT)){
                    itemBudgetBinding.budgetItemType.setImageResource(R.drawable.ic_credit)
                } else{
                    itemBudgetBinding.budgetItemType.setImageResource(R.drawable.ic_debit)
                }
            }
        }
    }


    interface onClickListnerChancgeBudget{
        fun onClick(position: Int)
    }

}