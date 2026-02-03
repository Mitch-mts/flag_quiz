package com.example.flagquiz.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.flagquiz.R
import com.example.flagquiz.databinding.FragmentResultBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ResultFragment : Fragment() {
    lateinit var resultFragmentBinding: FragmentResultBinding

    var correctNumber = 0F
    var wrongNumber = 0F
    var emptyNumber = 0F


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        resultFragmentBinding = FragmentResultBinding.inflate(inflater, container, false)

        val arguments = arguments?.let{
            ResultFragmentArgs.fromBundle(it)
        }

        arguments?.let{
            correctNumber = it.correct.toFloat()
            emptyNumber = it.empty.toFloat()
            wrongNumber = it.wrong.toFloat()
        }

        val barEntriesArrayListCorrect = ArrayList<BarEntry>()
        val barEntriesArrayListEmpty = ArrayList<BarEntry>()
        val barEntriesArrayListWrong = ArrayList<BarEntry>()

        barEntriesArrayListCorrect.add(BarEntry(0F, correctNumber))
        barEntriesArrayListEmpty.add(BarEntry(1F, emptyNumber))
        barEntriesArrayListWrong.add(BarEntry(2F, wrongNumber))

        val barDataSetCorrect = BarDataSet(barEntriesArrayListCorrect, "Correct").apply {
            color = Color.GREEN
            valueTextSize = 24F
            valueTextColor = R.color.chart_text_color
        }

        val barDataSetEmpty = BarDataSet(barEntriesArrayListEmpty, "Empty").apply {
            color = Color.BLUE
            valueTextSize = 24F
            valueTextColor = R.color.chart_text_color
        }

        val barDataSetWrong = BarDataSet(barEntriesArrayListWrong, "Wrong").apply {
            color = Color.RED
            valueTextSize = 24F
            valueTextColor = R.color.chart_text_color
        }

        val barData = BarData(barDataSetCorrect, barDataSetEmpty, barDataSetWrong)
        resultFragmentBinding.resultChart.data = barData


        resultFragmentBinding.btnNewQuiz.setOnClickListener {
            this.findNavController().popBackStack(R.id.homeFragment, false)
        }

        resultFragmentBinding.btnExitQuiz.setOnClickListener {
            requireActivity().finish()
        }

        return resultFragmentBinding.root
    }

}